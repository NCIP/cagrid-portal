package gov.nih.nci.cagrid.portal.portlet.browse;

import org.springframework.web.portlet.ModelAndView;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BaseSearchSupportingController extends BaseCatalogEntryAbstractController {


    protected BrowseTypeEnum getBrowseType(PortletRequest request) {
        return BrowseTypeEnum.valueOf(request
                .getPreferences().getValue(BrowseParams.BROWSE_TYPE,
                BrowseTypeEnum.DATASET.toString()));

    }

    private void addRenderParam(ActionRequest req, ActionResponse res, String param){
        if(req.getParameterMap().containsKey(param)){
            res.setRenderParameter(param,req.getParameter(param));
        }

    }
    public void handleActionRequestInternal(ActionRequest req, ActionResponse res)
            throws Exception {
        addRenderParam(req,res,BrowseParams.SEARCH_KEYWORD);
        addRenderParam(req,res,BrowseParams.SELECTED_IDS);
        addRenderParam(req,res,BrowseParams.AREA_OF_FOCUS);
        addRenderParam(req,res,BrowseParams.SELECTED_CATALOG_TYPE);
        addRenderParam(req,res,BrowseParams.SELECTED_CATALOG_LABEL);
        addRenderParam(req,res,BrowseParams.ENTRY_ID);
        addRenderParam(req,res,"operation");
    }

    /**
     * Will encode the MAV with parameters need to create a SOLR search query.
     * Used to pass state between browse pages (search results and details page)
     *
     * @param mav
     * @param request
     */
    public void encodeWithSearchParams(ModelAndView mav, RenderRequest request) {

        //catalog type
        if (request.getParameterMap().containsKey(BrowseParams.CATALOG_TYPE))
            mav.addObject(BrowseParams.CATALOG_TYPE, request.getParameter(BrowseParams.CATALOG_TYPE));

        /** these parameters are need to preserve browse view state **/
        // search keyword (wildcard by default)
        String searchKeyword = request.getParameter(BrowseParams.SEARCH_KEYWORD) != null ? request.getParameter(BrowseParams.SEARCH_KEYWORD) : "*:*";
        mav.addObject(BrowseParams.SEARCH_KEYWORD, searchKeyword);

        //if particular catalogs need to be displayed
        if (request.getParameterMap().containsKey(BrowseParams.SELECTED_IDS))
            mav.addObject(BrowseParams.SELECTED_IDS, request.getParameter(BrowseParams.SELECTED_IDS));

        //area of focus (All by default)
        if (request.getParameterMap().containsKey(BrowseParams.AREA_OF_FOCUS))
            mav.addObject(BrowseParams.AREA_OF_FOCUS, request.getParameter(BrowseParams.AREA_OF_FOCUS));

        //selected catalog type
        if (request.getParameterMap().containsKey(BrowseParams.SELECTED_CATALOG_TYPE))
            mav.addObject(BrowseParams.SELECTED_CATALOG_TYPE, request.getParameter(BrowseParams.SELECTED_CATALOG_TYPE));

        //selected catalog label
        if (request.getParameterMap().containsKey(BrowseParams.SELECTED_CATALOG_LABEL))
            mav.addObject(BrowseParams.SELECTED_CATALOG_LABEL, request.getParameter(BrowseParams.SELECTED_CATALOG_LABEL));


    }

}
