/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.portlet.UserModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.portlet.ModelAndView;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class BrowseViewController extends BaseSearchSupportingController implements InitializingBean {

    private HibernateTemplate hibernateTemplate;
    private String successViewName;
    private String userGuideUrl;

    private UserModel userModel;

    private static final Log logger = LogFactory.getLog(BrowseViewController.class);

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
      */
    public void afterPropertiesSet() throws Exception {

    }


    /*
      * (non-Javadoc)
      *
      * @see org.springframework.web.portlet.mvc.Controller#handleRenderRequest(javax.portlet.RenderRequest,
      *      javax.portlet.RenderResponse)
      * ToDo use properties not strings
      */
    public ModelAndView handleRenderRequest(RenderRequest request,
                                            RenderResponse response) throws Exception {
    
        ModelAndView mav = new ModelAndView(getSuccessViewName());
        BrowseTypeEnum browseType = getBrowseType(request);

        
        Object className = getClassName(request);
        if (className != null) {
        	mav.addObject(BrowseParams.CLASS_NAME,className);
        } 
        
        mav.addObject(BrowseParams.BROWSE_TYPE, browseType.toString());
        String entryTypeName = null;
        if (browseType.equals(BrowseTypeEnum.DATASET)) {
            entryTypeName = "DataSetCatalogEntry";
            //both data sets and information models
            mav.addObject(BrowseParams.CATALOG_TYPE, "dataset information_model terminology");
        } else if (browseType.equals(BrowseTypeEnum.COMMUNITY)) {
            entryTypeName = "CommunityCatalogEntry";
            mav.addObject(BrowseParams.CATALOG_TYPE, "community");
        } else if (browseType.equals(BrowseTypeEnum.INSTITUTION)) {
            entryTypeName = "InstitutionCatalogEntry";
            mav.addObject(BrowseParams.CATALOG_TYPE, "institution");
        } else if (browseType.equals(BrowseTypeEnum.PERSON)) {
            entryTypeName = "PersonCatalogEntry";
            mav.addObject(BrowseParams.CATALOG_TYPE, "person poc");
        } else if (browseType.equals(BrowseTypeEnum.TOOL)) {
            entryTypeName = "ToolCatalogEntry";
            mav.addObject(BrowseParams.CATALOG_TYPE, "tool_* tools");
        } else if (browseType.equals(BrowseTypeEnum.ALL)) {
            entryTypeName = "CatalogEntry";
        } else {
            throw new RuntimeException("Unknown browse type: " + browseType);
        }
               mav.addObject("userGuideUrl", getUserGuideUrl());

        encodeWithSearchParams(mav, request);
        return mav;
    }

    public String getUserGuideUrl() {
        return userGuideUrl;
    }

    public void setUserGuideUrl(String userGuideUrl) {
        this.userGuideUrl = userGuideUrl;
    }

    public String getSuccessViewName() {
        return successViewName;
    }

    public void setSuccessViewName(String successViewName) {
        this.successViewName = successViewName;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }


}
