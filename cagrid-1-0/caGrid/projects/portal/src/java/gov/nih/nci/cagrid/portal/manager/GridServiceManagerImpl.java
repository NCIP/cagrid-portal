package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.*;
import gov.nih.nci.cagrid.portal.exception.GeoCoderRetreivalException;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;
import gov.nih.nci.cagrid.portal.utils.GeoCoderUtility;

import java.util.Iterator;
import java.util.Set;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 28, 2006
 * Time: 5:24:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridServiceManagerImpl extends BaseManagerImpl
        implements GridServiceManager {

    public GridServiceManagerImpl() {
        //for spring
    }

    /**
     * Override base implementation
     */
    public void save(IndexService idx) {
        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(idx);
            _logger.debug("Setting id for index:" + objectID);
            idx.setPk(objectID);

            //if id is found then we need to attach it to session
            //indexDAO.merge(idx);
        } catch (RecordNotFoundException e) {
            // New object since id does not exist
            // Do nothing as this is not unexpected
        }
        super.save(idx);
    }


    public void save(RegisteredService rService) {

        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(rService);
            rService.setPk(objectID);
        } catch (RecordNotFoundException e) {
            //do nothing
        }

        DomainModel dModel = rService.getDomainModel();
        if (dModel != null) {
            //check if model exists in db and reassign id
            try {
                dModel.setPk(rService.getPk());
                dModel.setPk(gridServiceBaseDAO.getBusinessKey(dModel));
            } catch (RecordNotFoundException e) {
                //do nothing
            }

            Set classes = dModel.getUmlClassCollection();
            for (Iterator iter = classes.iterator(); iter.hasNext();) {
                UMLClass umlClass = (UMLClass) iter.next();
                try {
                    umlClass.setPk(gridServiceBaseDAO.getBusinessKey(umlClass));
                } catch (RecordNotFoundException e) {
                    //do nothing as this is not unexpected
                }
            }

        }
        super.save(rService);
    }


    /**
     * Manages storing of a Research Center
     *
     * @param rc
     */
    public void save(ResearchCenter rc) {
        //check if geo Co-ords have been set
        if (rc.getGeoCoords() == null) {
            try {
                GeoCoderUtility coder = new GeoCoderUtility();
                coder.getGeoCode4RC(rc);
            } catch (GeoCoderRetreivalException e) {
                //already logged just bypass the exception
                rc.setGeoCoords("N/A");
            }
        }
        try {
            Integer objectID = gridServiceBaseDAO.getBusinessKey(rc);
            rc.setPk(objectID);
        } catch (RecordNotFoundException e) {
            _logger.debug("Record not found for Research  Center. Hibernate will assign new id");
        }
        super.save(rc);
    }


}
