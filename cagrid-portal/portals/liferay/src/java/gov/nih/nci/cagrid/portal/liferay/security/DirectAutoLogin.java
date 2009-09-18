/**
 *
 */
package gov.nih.nci.cagrid.portal.liferay.security;

import com.liferay.portal.DuplicateUserEmailAddressException;
 import com.liferay.portal.ReservedUserEmailAddressException;
 import com.liferay.portal.UserEmailAddressException;
 import com.liferay.portal.model.User;
 import com.liferay.portal.security.auth.AutoLoginException;
 import com.liferay.portal.service.UserLocalServiceUtil;
 import gov.nih.nci.cagrid.portal.dao.AuthnTicketDao;
 import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
 import gov.nih.nci.cagrid.portal.domain.AuthnTicket;
 import gov.nih.nci.cagrid.portal.domain.PortalUser;
 import gov.nih.nci.cagrid.portal.security.EncryptionService;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 import org.springframework.beans.factory.annotation.Required;

 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import java.util.Date;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
  * @author kherm manav.kher@semanticbits.com
  */

 public class DirectAutoLogin extends AbstractAutoLogin {

     private static final Log logger = LogFactory.getLog(DirectAutoLogin.class);

     private EncryptionService encryptionService;
     private String ticketParameterName;
     private PortalUserDao portalUserDao;
     private AuthnTicketDao authnTicketDao;

     /**
      *
      */
     public DirectAutoLogin() {

     }

     public String[] login(HttpServletRequest request,
                           HttpServletResponse response) throws AutoLoginException {

         String[] credentials = null;
         String ticket = request.getParameter(getTicketParameterName());
         if (ticket == null) {
             return null;
         }
         AuthnTicket authnTicket = null;
         try {
             ticket = getEncryptionService().decrypt(ticket);
             authnTicket = new AuthnTicket();
             authnTicket.setTicket(ticket);
             authnTicket = getAuthnTicketDao().getByExample(authnTicket);
             if (authnTicket == null) {
                 throw new Exception("Didn't find AuthnTicket for ticket '"
                         + ticket + "'");
             }
             if (new Date().after(authnTicket.getNotAfter())) {
                 throw new Exception("Authentication ticket is expired.");
             }
             PortalUser portalUser = authnTicket.getPortalUser();
             if (portalUser == null) {
                 throw new Exception(
                         "No PortalUser associated with AuthnTicket: "
                                 + authnTicket.getId());
             }
             try {

                 User user = null;
                 if (portalUser.getPortalId() == null) {
                     logger.debug("creating new User for "
                             + portalUser.getGridIdentity());

                     user = addLiferayUser(portalUser);

                     portalUser.setPortalId(String.valueOf(user.getCompanyId())
                             + ":" + String.valueOf(user.getUserId()));
                     getPortalUserDao().save(portalUser);
                 } else {
                     String[] portalId = portalUser.getPortalId().split(":");
                     user = UserLocalServiceUtil.getUserById(Integer
                             .parseInt(portalId[0]), Integer
                             .parseInt(portalId[1]));
                     if (user == null) {
                         throw new AutoLoginException(
                                 "No User found for portalId = " + portalId);
                     }
                 }

                 request.setAttribute(getUserIdAttributeName(), portalUser
                         .getId());

                 credentials = new String[3];
                 credentials[0] = String.valueOf(user.getUserId());
                 credentials[1] = user.getPassword();
                 credentials[2] = Boolean.TRUE.toString();
             } catch (UserEmailAddressException ex) {
                 request
                         .setAttribute(
                                 getAuthnErrorMessageAttributeName(),
                                 "The email address that is associated with your account"
                                         + " is invalid. Please contact your identity provider "
                                         + "administrator to correct it.");

             } catch (DuplicateUserEmailAddressException ex) {
                 request
                         .setAttribute(
                                 getAuthnErrorMessageAttributeName(),
                                 "A user with the email address '"
                                         + portalUser.getPerson()
                                         .getEmailAddress()
                                         + "' already exists. Either authenticate using the credentials associated with that account, or ask the portal adminstrator ("
                                         + getPortalAdminEmailAddress()
                                         + ") to delete your existing portal account.");

             } catch (ReservedUserEmailAddressException ex) {
                 request
                         .setAttribute(
                                 getAuthnErrorMessageAttributeName(),
                                 "The email address '"
                                         + portalUser.getPerson()
                                         .getEmailAddress()
                                         + "' is reserved. Please contact the portal adminstrator ("
                                         + getPortalAdminEmailAddress() + ").");
             }

         } catch (Exception ex) {
             logger.error("Unexpected error while authenticating: "
                     + ex.getMessage(), ex);
             request
                     .setAttribute(
                             getAuthnErrorMessageAttributeName(),
                             "An error was encountered during authentication. Please contact the adminstrator (" + getPortalAdminEmailAddress() + ").");
         } finally {
             try {
                 if (authnTicket != null) {
                     getAuthnTicketDao().delete(authnTicket);
                 }
             } catch (Exception ex) {
                 logger.error("Error deleting authentication ticket: "
                         + ex.getMessage(), ex);
             }
         }
         return credentials;
     }

     @Required
     public EncryptionService getEncryptionService() {
         return encryptionService;
     }

     public void setEncryptionService(EncryptionService encryptionService) {
         this.encryptionService = encryptionService;
     }

     @Required
     public String getTicketParameterName() {
         return ticketParameterName;
     }

     public void setTicketParameterName(String ticketParameterName) {
         this.ticketParameterName = ticketParameterName;
     }

     @Required
     public PortalUserDao getPortalUserDao() {
         return portalUserDao;
     }

     public void setPortalUserDao(PortalUserDao portalUserDao) {
         this.portalUserDao = portalUserDao;
     }

     @Required
     public AuthnTicketDao getAuthnTicketDao() {
         return authnTicketDao;
     }

     public void setAuthnTicketDao(AuthnTicketDao authnTicketDao) {
         this.authnTicketDao = authnTicketDao;
     }


 }
