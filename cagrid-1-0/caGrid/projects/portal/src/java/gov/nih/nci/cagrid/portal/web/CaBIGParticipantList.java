package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.CaBIGParticipant;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.CaBIGWorkspaceManager;
import org.apache.log4j.Category;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 26, 2006
 * Time: 2:15:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CaBIGParticipantList {

    private List list;
    private int count;

    private CaBIGWorkspaceManager caBIGManager;
    private CaBIGParticipant navigatedParticipant;

    private Category _logger = Category.getInstance(getClass().getName());

    public String navigateToParticipant() throws FacesException {
        try {
            Integer pk = new Integer((String) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("navigatedParticipantPk"));

            navigatedParticipant = (CaBIGParticipant) caBIGManager.getObjectByPrimaryKey(CaBIGParticipant.class, pk);
        } catch (NumberFormatException e) {
            _logger.error(e);
            throw new FacesException(e);
        } catch (PortalRuntimeException e) {
            _logger.error(e);
            throw new FacesException(e);
        }
        return "success";
    }

    public void setupKeywordSearch(String keyword) throws FacesException {
        try {
            list = caBIGManager.keywordSearch(keyword);
        } catch (PortalRuntimeException e) {
            new FacesException(e);
        }
    }

    /**
     * count of all Participants *
     */
    public int getCount() {
        return caBIGManager.getCount(CaBIGParticipant.class);
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getListSize() {
        return list.size();
    }

    public CaBIGWorkspaceManager getCaBIGManager() {
        return caBIGManager;
    }

    public void setCaBIGManager(CaBIGWorkspaceManager caBIGManager) {
        this.caBIGManager = caBIGManager;
    }

    public CaBIGParticipant getNavigatedParticipant() {
        return navigatedParticipant;
    }

    public void setNavigatedParticipant(CaBIGParticipant navigatedParticipant) {
        this.navigatedParticipant = navigatedParticipant;
    }
}
