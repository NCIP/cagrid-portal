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
package gov.nih.nci.cagrid.portal.portlet.sample;

import org.springframework.web.portlet.mvc.AbstractController;
import org.springframework.web.portlet.mvc.SimpleFormController;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.validation.BindException;

import javax.portlet.*;

import gov.nih.nci.cagrid.evsgridservice.client.EVSGridServiceClient;
import gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType;
import gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams;
import gov.nih.nci.evs.domain.MetaThesaurusConcept;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EVSPortletController extends SimpleFormController {

    EVSSearchHelper evsHelper;


    @Override
    protected void onSubmitAction(Object o, BindException e) throws Exception {
        SearchBean cmd = (SearchBean)o;
        try {
            cmd.setResult(evsHelper.conceptCodeSearch(cmd.getKeyword()));
        } catch (Exception e1) {
            e.printStackTrace();
            cmd.setResult("Error querying EVS");
        }
    }


    @Override
    protected ModelAndView onSubmitRender(Object o) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject(getCommandName(), o);
        return mav;
    }


    public EVSSearchHelper getEvsHelper() {
        return evsHelper;
    }

    public void setEvsHelper(EVSSearchHelper evsHelper) {
        this.evsHelper = evsHelper;
    }
}

 
 
