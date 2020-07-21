package org.wso2.carbon.identity.custom.workflow.mgt.listener;

import org.wso2.carbon.identity.event.services.IdentityEventService;
import org.wso2.carbon.user.core.service.RealmService;

public class DataHolder {
    private static RealmService realmService;
    private static volatile DataHolder dataHolder;
    private IdentityEventService identityEventService;
    private static CustomWorkFlowManagementListener customWorkFlowManagementListener;

    private DataHolder() {
    }

    public static DataHolder getInstance() {
        if (dataHolder == null) {
            synchronized (DataHolder.class) {
                if (dataHolder == null) {
                    dataHolder = new DataHolder();
                    customWorkFlowManagementListener = new CustomWorkFlowManagementListener();
                }
            }
        }

        return dataHolder;
    }

    public void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

    public RealmService getRealmService() {
        return realmService;
    }

    public CustomWorkFlowManagementListener getCustomWorkflowManagementListener() {
        return customWorkFlowManagementListener;
    }

    public IdentityEventService getIdentityEventService() {
        return identityEventService;
    }

    public void setIdentityEventService(IdentityEventService identityEventService) {
        this.identityEventService = identityEventService;
    }
}
