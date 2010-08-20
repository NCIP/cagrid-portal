package gov.nih.nci.cagrid.portal.portlet.gss;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class RefreshController extends ParameterizableViewController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("refresh-started-ok");
        /*Principal p = request.getUserPrincipal();
        System.out.println("Principal = " + p);
        if (p != null) {
            System.out.println("RefreshController: Principal name = " + p.getName());
        }*/
        if (request.isUserInRole("portal-admin")) {
            boolean b = GridSummaryService.instance.triggerCalculator();
            mav.addObject("triggered", new Boolean(b));
        } else {
            mav.addObject("triggered", "User not in role ");
        }
        return mav;
    }

}
