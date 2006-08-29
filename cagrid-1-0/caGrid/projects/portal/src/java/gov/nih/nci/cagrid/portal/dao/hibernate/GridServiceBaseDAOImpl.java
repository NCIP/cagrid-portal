package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.GridServiceBaseDAO;
import gov.nih.nci.cagrid.portal.domain.*;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 8:20:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridServiceBaseDAOImpl extends BaseDAOImpl
        implements GridServiceBaseDAO {

    /**
     * Will return the business key depending
     * on the type of object
     *
     * @param obj
     * @return
     * @throws RecordNotFoundException
     */
    public Integer getBusinessKey(DomainObject obj) throws RecordNotFoundException {
        List resultSet = null;

        if (obj instanceof IndexService) {
            IndexService idx = (IndexService) obj;
            resultSet = getHibernateTemplate().find("Select index.pk from IndexService index where index.EPR = ?", idx.getEPR());

        } else if (obj instanceof RegisteredService) {
            RegisteredService service = (RegisteredService) obj;
            resultSet = getHibernateTemplate().find("Select service.pk from RegisteredService service where service.EPR = ?",
                    service.getEPR());

        } else if (obj instanceof ResearchCenter) {
            ResearchCenter rc = (ResearchCenter) obj;
            resultSet = getHibernateTemplate().find("Select rc.pk from ResearchCenter rc where rc.displayName = ? and rc.latitude = ? and rc.longitude = ?",
                    new Object[]{rc.getDisplayName(), rc.getLatitude(), rc.getLongitude()});
            // If coordinates don't return a RC check with name and postal code
            if (resultSet.size() < 1) {
                resultSet = getHibernateTemplate().find("Select rc.pk from ResearchCenter rc where rc.displayName = ? and rc.postalCode = ?", new Object[]{rc.getDisplayName(), rc.getPostalCode()});
            }

        } else if (obj instanceof UMLClass) {
            UMLClass umlClass = (UMLClass) obj;
            resultSet = getHibernateTemplate().find("Select uc.pk from UMLClass uc where uc.className = ? and uc.packageName = ?", new Object[]{umlClass.getClassName(), umlClass.getPackageName()});
        }

        Integer id;
        try {
            id = (Integer) resultSet.get(0);
        } catch (Exception e) {
            throw new RecordNotFoundException();
        }

        return id;
    }
}
