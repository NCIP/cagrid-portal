package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.PointOfContactManager;

import javax.faces.FacesException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 29, 2006
 * Time: 1:01:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PeopleList {

    private List list;
    private PointOfContactManager pocManager;

    public List getList() {
        return list;
    }

    public void setupKeywordSearch(String keyword) throws FacesException {
        try {
            list = pocManager.keywordSearch(keyword);
        } catch (PortalRuntimeException e) {
            new FacesException(e);
        }
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setPocManager(PointOfContactManager pocManager) {
        this.pocManager = pocManager;
    }
}
