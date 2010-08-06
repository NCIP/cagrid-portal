package gov.nih.nci.cagrid.portal.portlet.gss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class RefreshController extends ParameterizableViewController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("refresh-started-ok");
        //String key = tail((new UrlPathHelper()).getOriginatingRequestUri(request));
        //mav.addObject("a", ImpromptuQueryStorage.instance.getResult(key));
        return mav;
    }

}
