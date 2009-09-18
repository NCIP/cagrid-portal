/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com>Manav Kher</a>
 */
public class BrowseViewDetailsController extends AbstractController {

    private static final Log logger = LogFactory
            .getLog(BrowseViewDetailsController.class);
    private CatalogEntryDao catalogEntryDao;
    private String objectName;
    private Map<String, String> entryTypeViewMap = new HashMap<String, String>();
    private String emptyViewName;
    private CatalogEntryViewBeanFactory catalogEntryViewBeanFactory;
    private UserModel userModel;

    protected ModelAndView handleRenderRequestInternal(RenderRequest request,
                                                       RenderResponse response) throws Exception {

        CatalogEntry entry = getCatalogEntry(request);
        if (entry == null) {
            throw new RuntimeException("No catalog entry found.");
        }
        getUserModel().setCurrentCatalogEntry(entry);


        String viewName = (String) PortletUtils.getMapValueForType(entry.getClass(),
                getEntryTypeViewMap());
        if (viewName == null) {
            throw new RuntimeException("Couldn't determine view name for: "
                    + entry);
        }
        ModelAndView mav = null;
        mav = new ModelAndView(viewName);
        mav.addObject(getObjectName(), getCatalogEntryViewBeanFactory()
                .newCatalogEntryViewBean(entry));
        if (request.getParameter("viewMode") != null) {
            mav.addObject("viewMode", request.getParameter("viewMode"));
        }

        return mav;
    }

    protected CatalogEntry getCatalogEntry(RenderRequest request) {
        CatalogEntry entry = null;
        Integer entryId = null;
        try {
            entryId = Integer.valueOf(request.getParameter("entryId"));
        } catch (Exception ex) {

        }
        if (entryId != null) {
            entry = getCatalogEntryDao().getById(entryId);
        } else {
            entry = getUserModel().getCurrentCatalogEntry();
            if (entry == null) {
                throw new RuntimeException("No current catalog entry.");
            }
            //do not load if this is a new CE being created            
            if (entry.getId() != null) {
                entry = getCatalogEntryDao().getById(entry.getId());
            }
        }

        return entry;
    }

    public CatalogEntryDao getCatalogEntryDao() {
        return catalogEntryDao;
    }

    public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
        this.catalogEntryDao = catalogEntryDao;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Map<String, String> getEntryTypeViewMap() {
        return entryTypeViewMap;
    }

    public void setEntryTypeViewMap(Map<String, String> entryTypeViewMap) {
        this.entryTypeViewMap = entryTypeViewMap;
    }

    public String getEmptyViewName() {
        return emptyViewName;
    }

    public void setEmptyViewName(String emptyViewName) {
        this.emptyViewName = emptyViewName;
    }

    public CatalogEntryViewBeanFactory getCatalogEntryViewBeanFactory() {
        return catalogEntryViewBeanFactory;
    }

    public void setCatalogEntryViewBeanFactory(
            CatalogEntryViewBeanFactory catalogEntryViewBeanFactory) {
        this.catalogEntryViewBeanFactory = catalogEntryViewBeanFactory;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

}
