/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.dao.catalog.PointOfContactCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.PointOfContactCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.util.BeanUtils;
import gov.nih.nci.cagrid.portal.util.EmailValidator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 * Validates and stores POC catalog entries
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class PointOfContactCatalogService {

    PointOfContactCatalogEntryDao pointOfContactCatalogEntryDao;


    public PointOfContactCatalogEntry create(PointOfContact poc) {
        if (validate(poc))
            return getPointOfContactCatalogEntryDao().createCatalogAbout(poc);
        else return null;


    }

    private boolean validate(PointOfContact poc) {
        String emailAddress = BeanUtils.traverse(poc, "person.emailAddress");
        return emailAddress != null && EmailValidator.validateEmail(emailAddress);
    }

    public PointOfContactCatalogEntryDao getPointOfContactCatalogEntryDao() {
        return pointOfContactCatalogEntryDao;
    }

    @Required
    public void setPointOfContactCatalogEntryDao(PointOfContactCatalogEntryDao pointOfContactCatalogEntryDao) {
        this.pointOfContactCatalogEntryDao = pointOfContactCatalogEntryDao;
    }
}
