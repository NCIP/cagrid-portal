package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.ResearchCenterManager;
import org.apache.log4j.Category;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 28, 2006
 * Time: 5:18:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CenterList {

    private List list;
    private ResearchCenter navigatedCenter;
    private ResearchCenterManager rcManager;
    private int listSize;

    private Category _logger = Category.getInstance(getClass().getName());

    public String navigateToCenter() throws FacesException {
        try {
            Integer pk = new Integer((String) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("navigatedCenterPk"));

            navigatedCenter = (ResearchCenter) rcManager.getObjectByPrimaryKey(ResearchCenter.class, pk);
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
            list = rcManager.keywordSearch(keyword);
        } catch (PortalRuntimeException e) {
            new FacesException(e);
        }
    }

    public List getList() {
        return list;
    }

    public int getListSize() {
        return list.size();
    }

    public void setList(List list) {
        this.list = list;
    }

    public ResearchCenter getNavigatedCenter() {
        return navigatedCenter;
    }

    public void setNavigatedCenter(ResearchCenter navigatedCenter) {
        this.navigatedCenter = navigatedCenter;
    }

    public ResearchCenterManager getRcManager() {
        return rcManager;
    }

    public void setRcManager(ResearchCenterManager rcManager) {
        this.rcManager = rcManager;
    }

}
