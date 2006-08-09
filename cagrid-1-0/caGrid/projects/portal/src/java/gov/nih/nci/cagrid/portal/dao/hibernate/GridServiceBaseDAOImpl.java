package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.GridServiceBaseDAO;
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
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

    /** Will return the business key depending
     * on the type of object
     *
     * @param obj
     * @return
     * @throws RecordNotFoundException
     */
    public Integer getBusinessKey(DomainObject obj) throws RecordNotFoundException {
        List resultSet = null;

        if(obj instanceof IndexService){
            IndexService idx = (IndexService)obj;
             resultSet = getHibernateTemplate().find("Select index.pk from IndexService index where index.EPR = ?", idx.getEPR() );
        }
        else if(obj instanceof RegisteredService){
            RegisteredService service = (RegisteredService)obj;
                resultSet = getHibernateTemplate().find("Select service.pk from RegisteredService service where service.EPR = ?",
                        service.getEPR());
        }
        else if(obj instanceof ResearchCenter){
            ResearchCenter rc = (ResearchCenter)obj;
                resultSet = getHibernateTemplate().find("Select service.pk from ResearchCenter rc where rc.geoCoords = ?",
                        rc.getGeoCoords());
        }
        Integer id;
        try {
             id = (Integer)resultSet.get(0);
        } catch (Exception e) {
            throw new RecordNotFoundException();
        }

        return id;
    }
}
