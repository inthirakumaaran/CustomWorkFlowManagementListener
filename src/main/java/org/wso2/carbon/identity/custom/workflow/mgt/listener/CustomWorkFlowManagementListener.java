package org.wso2.carbon.identity.custom.workflow.mgt.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.workflow.mgt.WorkflowExecutorResult;
import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.RequestParameter;
import org.wso2.carbon.identity.workflow.mgt.bean.WorkflowRequestAssociation;
import org.wso2.carbon.identity.workflow.mgt.dao.WorkflowDAO;
import org.wso2.carbon.identity.workflow.mgt.dao.WorkflowRequestAssociationDAO;
import org.wso2.carbon.identity.workflow.mgt.dto.WorkflowRequest;
import org.wso2.carbon.identity.workflow.mgt.exception.InternalWorkflowException;
import org.wso2.carbon.identity.workflow.mgt.exception.WorkflowException;
import org.wso2.carbon.identity.workflow.mgt.listener.AbstractWorkflowExecutorManagerListener;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.UserStoreManager;

import java.util.*;

public class CustomWorkFlowManagementListener extends AbstractWorkflowExecutorManagerListener {

    private static Log log = LogFactory.getLog(CustomWorkFlowManagementListener.class);

    private static final String USER_ROLE_PARAM = "UserAndRole";
    private static final String FILTERED_EVENT_TYPE = "ADD_USER";
    private static final String TEMPLATE_TYPE = "TEMPLATE_TYPE";
    private static final String USER_ACCOUNT = "user-account";
    private static final String ACCOUNT_APPROVAL_TEMPLATE_TYPE = "AccountApproval";
    private static final String ROLES = "roles";
    private static final String USERS = "users";
    private static final String USER_NAME = "Username";

    /**
     * Trigger after executing a workflow request
     *
     * @param workFlowRequest       Details of request to execute
     * @param result                Result of the original operation
     * @throws WorkflowException    Throws WorkflowException
     */
    @Override
    public void doPostExecuteWorkflow(WorkflowRequest workFlowRequest, WorkflowExecutorResult result) throws
            WorkflowException {
        if(workFlowRequest.getEventType().equals(FILTERED_EVENT_TYPE)) {
            List<RequestParameter> requestParameters = workFlowRequest.getRequestParameters();

            String userAccount = "" ;
            for(RequestParameter requestParameter : requestParameters) {
                if(requestParameter.getName().equals(USER_NAME)) {
                    userAccount = (String) requestParameter.getValue();
                }
            }

            WorkflowRequestAssociationDAO workflowRequestAssociationDAO = new WorkflowRequestAssociationDAO();
            WorkflowDAO workflowDAO = new WorkflowDAO();
            WorkflowRequestAssociation[] workflowsOfRequest = workflowRequestAssociationDAO.getWorkflowsOfRequest(workFlowRequest.getUuid());

            List<String> userNames = new ArrayList<String>();

            try {
                UserRealm userRealm = CarbonContext.getThreadLocalCarbonContext().getUserRealm();
                UserStoreManager userStoreManager = (UserStoreManager) userRealm.getUserStoreManager();
                String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
                String domainName = userStoreManager.getRealmConfiguration().getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_DOMAIN_NAME);

                for(WorkflowRequestAssociation workflowOfRequest : workflowsOfRequest) {
                    List<Parameter> parameters = workflowDAO.getWorkflowParams(workflowOfRequest.getWorkflowId());

                    for(Parameter parameter : parameters) {
                        if(USER_ROLE_PARAM.equals(parameter.getParamName())) {
                            if (parameter.getqName().endsWith(ROLES)) {
                                String allRoles = parameter.getParamValue();

                                if(allRoles != null && !"".equals(allRoles)) {
                                    String[] roles = allRoles.split(",");

                                    for (String role : roles) {
                                        String[] users = userStoreManager.getUserListOfRole(role);
                                        userNames.addAll(Arrays.asList(users));
                                    }
                                }
                            } else if (parameter.getqName().endsWith(USERS)) {
                                String allUsers = parameter.getParamValue();
                                if(allUsers != null && !"".equals(allUsers)) {
                                    String[] users = allUsers.split(",");
                                    userNames.addAll(Arrays.asList(users));
                                }
                            }
                        }
                    }
                }

                Set<String> uniqueUserNameSet = new HashSet<String>();
                uniqueUserNameSet.addAll(userNames);
                List<String> uniqueUserNameList = new ArrayList<String>(uniqueUserNameSet);

                for(String userName : uniqueUserNameList) {
                    HashMap<String, Object> properties = new HashMap<String, Object>();
                    properties.put(IdentityEventConstants.EventProperty.USER_NAME, userName);
                    properties.put(IdentityEventConstants.EventProperty.TENANT_DOMAIN, tenantDomain);
                    properties.put(IdentityEventConstants.EventProperty.USER_STORE_DOMAIN, domainName);
                    properties.put(IdentityEventConstants.EventProperty.USER_STORE_MANAGER, userStoreManager);
                    properties.put(USER_ACCOUNT, userAccount);

                    sendEmail(properties);
                }

            } catch (UserStoreException e) {
                throw new InternalWorkflowException("Error when notifying the approvers", e);
            }
        }
    }

    private void sendEmail(HashMap<String, Object> properties) throws WorkflowException {
        String eventName = IdentityEventConstants.Event.TRIGGER_NOTIFICATION;
        properties.put(TEMPLATE_TYPE, ACCOUNT_APPROVAL_TEMPLATE_TYPE);

        Event identityMgtEvent = new Event(eventName, properties);

        try {
            DataHolder.getInstance().getIdentityEventService().handleEvent(identityMgtEvent);
        } catch (IdentityEventException e) {
            log.warn("Couldn't send the email to " + properties.get(IdentityEventConstants.EventProperty.USER_NAME) +
                        " check if users email address is configured");
        } catch (IdentityRuntimeException e) {
            log.warn("Couldn't send the email to " + properties.get(IdentityEventConstants.EventProperty.USER_NAME) +
                    " check if users email address is configured");
        }
    }
}
