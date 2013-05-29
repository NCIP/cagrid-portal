/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com>Manav Kher</a>
 */
public class BrowseViewDetailsController extends BaseSearchSupportingController {

    private static final Log logger = LogFactory
            .getLog(BrowseViewDetailsController.class);
    private Map<String, String> entryTypeViewMap = new HashMap<String, String>();
    private String emptyViewName;
    private CatalogEntryViewBeanFactory catalogEntryViewBeanFactory;



    protected ModelAndView handleRenderRequestInternal(RenderRequest request,
                                                       RenderResponse response) throws Exception {

        CatalogEntry entry = null;
        try {
            entry = getCatalogEntry(request);
        } catch (Exception e) {
            logger.warn("Could not load catalog entry. No catlog entry found", e);
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

        encodeWithSearchParams(mav, request);
        mav.addObject(getObjectName(), getCatalogEntryViewBeanFactory()
                .newCatalogEntryViewBean(entry));
        if (request.getParameter("viewMode") != null) {
            mav.addObject("viewMode", request.getParameter("viewMode"));
        }

        return mav;
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


}
