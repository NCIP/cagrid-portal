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
package gov.nih.nci.cagrid.portal.portlet.browse.community;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;
import gov.nih.nci.cagrid.portal.portlet.browse.CatalogEntryRoleTypeViewBean;
import gov.nih.nci.cagrid.portal.portlet.browse.CatalogEntryViewBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 */
public class CommunityCatalogEntryViewBean extends CatalogEntryViewBean {

    private String memberRelationshipTypeName;
    private CatalogEntryRoleTypeViewBean memberRoleType;

    public CommunityCatalogEntryViewBean() {

    }

    protected boolean handleSpecialRole(CatalogEntryRoleInstance role) {
        boolean handle = super.handleSpecialRole(role);

        if (!handle
                && role.getRelationship().getType().getName().equals(
                getMemberRelationshipTypeName())) {
            handle = true;

            if (memberRoleType == null) {
                memberRoleType = new CatalogEntryRoleTypeViewBean(role
                        .getType());
            }
            memberRoleType.addRoleInstance(role);

        }

        return handle;
    }

    public String getMemberRelationshipTypeName() {
        return memberRelationshipTypeName;
    }

    public void setMemberRelationshipTypeName(String memberRelationshipTypeName) {
        this.memberRelationshipTypeName = memberRelationshipTypeName;
    }

    public CatalogEntryRoleTypeViewBean getMemberRoleType() {
        return memberRoleType;
    }

    public void setMemberRoleType(CatalogEntryRoleTypeViewBean memberRoleType) {
        this.memberRoleType = memberRoleType;
    }

}
