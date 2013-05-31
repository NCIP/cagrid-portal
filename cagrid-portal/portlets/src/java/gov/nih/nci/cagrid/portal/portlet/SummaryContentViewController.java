/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet;

import org.springframework.web.portlet.mvc.AbstractController;
import org.springframework.web.portlet.ModelAndView;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SummaryContentViewController extends AbstractController {

    private String successViewName;


    @Override
    protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessViewName());
        return mav;

    }

    public String getSuccessViewName() {
        return successViewName;
    }

    public void setSuccessViewName(String successViewName) {
        this.successViewName = successViewName;
    }

    
}