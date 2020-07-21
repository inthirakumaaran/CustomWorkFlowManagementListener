package org.wso2.carbon.identity.custom.workflow.mgt.listener.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.event.services.IdentityEventService;
import org.wso2.carbon.identity.workflow.mgt.listener.WorkflowExecutorManagerListener;
import org.wso2.carbon.identity.custom.workflow.mgt.listener.DataHolder;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.Properties;

/**
 * @scr.component name="custom.workflow.management.listener.dscomponent" immediate=true
 * @scr.reference name="realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"cardinality="1..1"
 * policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 * @scr.reference name="IdentityEventService"
 * interface="org.wso2.carbon.identity.event.services.IdentityEventService" cardinality="1..1"
 * policy="dynamic" bind="setIdentityEventService" unbind="unsetIdentityEventService"
 */
public class CustomWorkFlowManagementListenerDSComponent {
    private static Log log = LogFactory.getLog(CustomWorkFlowManagementListenerDSComponent.class);

    protected void activate(ComponentContext context) {
        //register the custom listener as an OSGI service.
        context.getBundleContext().registerService(
                WorkflowExecutorManagerListener.class.getName(), DataHolder.getInstance().getCustomWorkflowManagementListener(), new Properties());

        log.info("CustomWorkFlowManagementListenerDSComponent bundle activated successfully");
    }

    protected void deactivate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("CustomWorkFlowManagementListenerDSComponent is deactivated ");
        }
    }

    protected void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting the Realm Service");
        }
        DataHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("UnSetting the Realm Service");
        }
        DataHolder.getInstance().setRealmService(null);
    }

    protected void unsetIdentityEventService(IdentityEventService identityEventService) {
        if (log.isDebugEnabled()) {
            log.debug("UnSetting the Identity Event Service");
        }
        DataHolder.getInstance().setIdentityEventService(null);
    }

    protected void setIdentityEventService(IdentityEventService identityEventService) {
        if (log.isDebugEnabled()) {
            log.debug("UnSetting the Identity Event Service");
        }
        DataHolder.getInstance().setIdentityEventService(identityEventService);
    }
}
