package gov.nih.nci.cagrid.portal.portlet.summary;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SummaryViewController extends AbstractController {

    private String solrServiceUrl;
    private String successViewName;


    @Override
    protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessViewName());
        mav.addObject("solrServiceUrl", getSolrServiceUrl());
        return mav;

    }

    public String getSolrServiceUrl() {
        return solrServiceUrl;
    }

    public void setSolrServiceUrl(String solrServiceUrl) {
        this.solrServiceUrl = solrServiceUrl;
    }

    public String getSuccessViewName() {
        return successViewName;
    }

    public void setSuccessViewName(String successViewName) {
        this.successViewName = successViewName;
    }
}
