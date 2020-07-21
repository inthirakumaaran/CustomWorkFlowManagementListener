Please follow the following instructions to deploy the extension.

This extension is implemented to send emails to the users in the roles assigned or selected users in a workflow to approve user account creations, including the self-registered users in the identity dashboard. 

1. To enable self registration feature follow steps mentioned in the following documentation.
    `https://is.docs.wso2.com/en/latest/learn/self-registration/`

2. Once this is done, perform the steps outlined in the following document to create a workflow as required.

	`https://is.docs.wso2.com/en/latest/learn/adding-a-new-workflow-definition/`

3. Next perform the steps outlined in the following document to engage a workflow in an operation.

	`https://is.docs.wso2.com/en/latest/learn/engaging-a-workflow-in-an-operation/`

4. The directory that this README file contains is the CustomWorkFlowManagementListener and it contains the service component source for a workflow execution manage listener. You can build it by issuing the following command while on this directory. (There's an already built jar in the target directory)

	`$ mvn clean install`

5. Once successfully built, copy the org.wso2.carbon.identity.custom.workflow.mgt.listener-1.0.0.jar file into [IS_HOME]/repository/components/dropins/ directory. 

6. You also need to create an email template to send an email to relevant users. Please follow the following document create an email template type and an email template.
   `https://is.docs.wso2.com/en/latest/learn/customizing-automated-emails`
   
   **Note:** email template type name should be **AccountApproval** as this is what the extension looks for. 
   A sample template is as shown below.
    
     * Select Email Template Type: **"AccountApproval"**
     * Select the Template Language: "English (United States)"
     * Subject: "WSO2 Carbon - PENDING account approval"
     * Email Body: 
             <table align="center" cellpadding="0" cellspacing="0" border="0" width="100%"bgcolor="#f0f0f0">
                <tr>
                   <td style="padding: 30px 30px 20px 30px;">
                      <table cellpadding="0" cellspacing="0" border="0" width="100%" bgcolor="#ffffff" style="max-width: 650px; margin: auto;">
                         <tr>
                            <td colspan="2" align="center" style="background-color: #333; padding: 40px;">
                               <a href="http://wso2.com/" target="_blank"><img src="http://cdn.wso2.com/wso2/newsletter/images/nl-2017/wso2-logo-transparent.png" border="0" /></a>
                            </td>
                         </tr>
                         <tr>
                            <td colspan="2" align="center" style="padding: 50px 50px 0px 50px;">
                               <h1 style="padding-right: 0em; margin: 0; line-height: 40px; font-weight:300; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 1em;">
                                  Approve Pending Account
                               </h1>
                            </td>
                         </tr>
                         <tr>
                            <td style="text-align: left; padding: 0px 50px 20px 50px;" valign="top">
                               <p style="font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 3%;">
                                  Hi {{user-name}},
                               </p>
                               <p style="font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 3%;">
                                  A user account with following user name is registered <br>
                                  <br>
                                  User Name:  <b> {{user-account}}</b> <br>
                                  Tenant Domain: </b> {{tenant-domain}}</b> <br>
                                  <br>
                                  Go to the below link to confirm account.
                               </p>
                               <p style="font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #505050; text-align: left;">
                                  <a style="word-break: break-all; color: #ff5000; font-size: 14px" target="_blank"
                                     href="{{carbon.product-url}}/user-portal/operations">
                                  {{carbon.product-url}}/user-portal/operations
                                  </a>
                               </p>
                               <p style="font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #666; text-align: left; padding-bottom: 3%;">
                               <p>
                            </td>
                         </tr>
                         <tr>
                            <td style="text-align: left; padding: 30px 50px 50px 50px;" valign="top">
                               <p style="font-size: 18px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #505050; text-align: left;">
                                  Thanks,<br/>WSO2 Identity Server Team
                               </p>
                            </td>
                         </tr>
                         <tr>
                            <td colspan="2" align="center" style="padding: 20px 40px 40px 40px;" bgcolor="#f0f0f0">
                               <p style="font-size: 12px; margin: 0; line-height: 24px; font-family: 'Nunito Sans', Arial, Verdana, Helvetica, sans-serif; color: #777;">
                                  &copy;2020
                                  <a href="http://wso2.com/" target="_blank" style="color: #777; text-decoration: none">WSO2</a>
                                  <br>
                                  787 Castro Street, Mountain View, CA 94041.
                               </p>
                            </td>
                         </tr>
                      </table>
                   </td>
                </tr>
             </table>

            
