package gov.nih.nci.cagrid.portal.authn.web.controllers;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewGreetingController extends AbstractController {

    private String view;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView(getView());
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
