package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.domain.PointOfContact;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.PointOfContactManager;
import org.apache.log4j.Category;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
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
    private int listSize;

    private PointOfContact navigatedPOC;

    private Category _logger = Category.getInstance(getClass().getName());

    public List getList() {
        return list;
    }

    public String navigateToPOC() throws FacesException {
        try {
            Integer pk = new Integer((String) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequestParameterMap().get("navigatedPOCPk"));
            navigatedPOC = (PointOfContact) pocManager.getObjectByPrimaryKey(PointOfContact.class, pk);

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
            list = pocManager.keywordSearch(keyword);
        } catch (PortalRuntimeException e) {
            new FacesException(e);
        }
    }

    public int getListSize() {
        return list.size();
    }

    public void setList(List list) {
        this.list = list;
    }


    public void setPocManager(PointOfContactManager pocManager) {
        this.pocManager = pocManager;
    }


    public PointOfContact getNavigatedPOC() {
        return navigatedPOC;
    }
}
