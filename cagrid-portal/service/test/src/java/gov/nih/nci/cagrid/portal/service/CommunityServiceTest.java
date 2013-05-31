/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.service;

import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.util.TestPropsValues;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CommunityServiceTest extends DuelDBServiceTestBase {

    @Test
    public void testCreateCommunity() {

        ApplicationContext ctx = getServiceApplicationContext();

        // Create the user
        User user = null;
        try {
            user = addUser();
        } catch (Exception ex) {
            fail("Error creating user: " + ex.getMessage());
        }

        // Create the community
        CommunityService communityService = (CommunityService) ctx
                .getBean("communityService");
        String communityName = "Test Community";
        String communityDescription = "Test Community Description";
        PortalUser portalUser = new PortalUser();
        portalUser.setPortalId(TestPropsValues.COMPANY_ID + ":"
                + user.getUserId());

        CommunityCatalogEntry communityCe = new CommunityCatalogEntry();
        communityCe.setName(communityName);
        communityCe.setDescription(communityDescription);

        // Test that the user CANNOT create the community
        try {
            communityService.createCommunity(portalUser, communityCe);
            fail("Use shouldn't be able to create community.");
        } catch (Exception ex) {

        }

        // Assign appropriate roles
        try {
            RoleServiceUtil.addUserRoles(user.getUserId(),
                    new long[]{getCatalogUserRole().getRoleId()});
        } catch (Exception ex) {
            fail("Error adding Catalog User role to user: " + ex.getMessage());
        }

        // Test that the user CAN create the community
        try {
            communityService.createCommunity(portalUser, communityCe);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error creating community: " + ex.getMessage());
        }
        assertNotNull("No ID assigned", communityCe.getId());

        // Test that it is owned by the user
        Group community = null;
        try {
            community = GroupServiceUtil.getGroup(communityCe.getCommunityId());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error getting group: " + ex.getMessage());
        }
        assertNotNull(community);
        assertEquals(community.getCreatorUserId(), user.getUserId());

        // Update the community
        String updatedCommunityName = "Updated Community Name";
        communityCe.setName(updatedCommunityName);
        try {
            communityService.updateCommunity(portalUser, communityCe);
        } catch (Exception ex) {
            fail("Error updating community: " + ex.getMessage());
        }
        try {
            community = GroupServiceUtil.getGroup(communityCe.getCommunityId());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error getting updated community ce: " + ex.getMessage());
        }
        assertNotNull(community);
        assertEquals(community.getName(), updatedCommunityName);

        long communityId = communityCe.getCommunityId();
        try {
            community = GroupServiceUtil.getGroup(communityId);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error getting updated group: " + ex.getMessage());
        }
        assertEquals(communityCe.getCommunityUrl(), community.getFriendlyURL());

        // Create second user
        User user2 = null;
        try {
            user2 = addUser();
        } catch (Exception ex) {
            fail("Error creating second user: " + ex.getMessage());
        }
        PortalUser portalUser2 = new PortalUser();
        portalUser2.setPortalId(TestPropsValues.COMPANY_ID + ":"
                + user2.getUserId());

        int ceId = communityCe.getId();
        try {
            communityService.deleteCommunity(portalUser2, communityCe);
            fail("Second user shouldn't be able to create community.");
        } catch (Exception ex) {

        }

        try {
            communityCe = (CommunityCatalogEntry) communityService
                    .getById(ceId);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error getting community catalog entry after failed delete: "
                    + ex.getMessage());
        }
        assertNotNull("Community catalog entry is null after failed delete",
                communityCe);

        // Delete the community
        try {
            communityService.deleteCommunity(portalUser, communityCe);
        } catch (Exception ex) {
            fail("Error deleting community: " + ex.getMessage());
        }
        try {
            GroupServiceUtil.getGroup(communityId);
            fail("Group should not exist");
        } catch (Exception ex) {

        }
    }

}
