package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.ResearchCenterManager;
import org.apache.log4j.Category;

import javax.faces.FacesException;
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


    private Category _logger = Category.getInstance(getClass().getName());


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
