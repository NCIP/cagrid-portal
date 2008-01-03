package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.CaBIGWorkspaceManager;
import gov.nih.nci.cagrid.portal.manager.GridServiceManager;
import gov.nih.nci.cagrid.portal.manager.PointOfContactManager;
import gov.nih.nci.cagrid.portal.manager.ResearchCenterManager;
import org.apache.log4j.Category;

import javax.faces.FacesException;

/**
 * Sets up the Directory section.
 * Controls navigation to the
 * RegisteredServices, PointOfContact, ResearchCenter
 * directory listings.
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 12, 2006
 * Time: 4:11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectorySetup {

    private GridServiceManager gridServiceManager;
    private ResearchCenterManager rcManager;
    private PointOfContactManager pocManager;
    private CaBIGWorkspaceManager caBIGManager;

    private Category _logger = Category.getInstance(getClass().getName());

    public String navigateToServicesDirectory() throws FacesException {
        _logger.debug("Setting up directory of services");

        try {
            ServicesList services = (ServicesList) PortalWebUtils.getBean("services");
            services.setList(gridServiceManager.getUniqueServices());
        } catch (PortalRuntimeException e) {
            //log so admin knows
            _logger.error(e);
            //send this to the view for the user
            throw new FacesException(e);
        }
        _logger.debug("Navigating to services directory");
        return "success";
    }

    public String navigateToCenterDirectory() throws FacesException {
        _logger.debug("Setting up directory of centers");

        try {
            CenterList centers = (CenterList) PortalWebUtils.getBean("centers");
            centers.setList(rcManager.getUniqueCenters());
        } catch (PortalRuntimeException e) {
            //log so admin knows
            _logger.error(e);
            //send this to the view for the user
            throw new FacesException(e);
        }
        _logger.debug("Navigating to centers directory");
        return "success";
    }

    public String navigateToPeopleDirectory() throws FacesException {
        _logger.debug("Setting up directory of people");

        try {
            PeopleList people = (PeopleList) PortalWebUtils.getBean("people");
            people.setList(pocManager.getUniquePeople());
        } catch (PortalRuntimeException e) {
            //log so admin knows
            _logger.error(e);
            //send this to the view for the user
            throw new FacesException(e);
        }
        _logger.debug("Navigating to people directory");
        return "success";
    }

    public String navigateToParticipantDirectory() throws FacesException {
        _logger.debug("Setting up directory of caBIG participants");

        try {
            CaBIGParticipantList participants = (CaBIGParticipantList) PortalWebUtils.getBean("participants");
            participants.setList(caBIGManager.getUniqueParticipants());
        } catch (PortalRuntimeException e) {
            throw new FacesException(e);
        }
        return "success";
    }

    public void setGridServiceManager(GridServiceManager gridServiceManager) {
        this.gridServiceManager = gridServiceManager;
    }

    public void setRcManager(ResearchCenterManager rcManager) {
        this.rcManager = rcManager;
    }

    public void setPocManager(PointOfContactManager pocManager) {
        this.pocManager = pocManager;
    }


    public void setCaBIGManager(CaBIGWorkspaceManager caBIGManager) {
        this.caBIGManager = caBIGManager;
    }
}