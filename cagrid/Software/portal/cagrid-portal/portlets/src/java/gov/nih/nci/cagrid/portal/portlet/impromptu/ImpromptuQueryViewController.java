package gov.nih.nci.cagrid.portal.portlet.impromptu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.util.UrlPathHelper;

public class ImpromptuQueryViewController extends ParameterizableViewController {

    /*
    private String tail(final String s) {
        String result = "";
        if (s != null) {
            result = s.trim();
            int pos = result.lastIndexOf("/");
            result = result.substring(pos);
        }
        return result;
    }
    */

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("results");
        //String key = tail((new UrlPathHelper()).getOriginatingRequestUri(request));
        //mav.addObject("a", ImpromptuQueryStorage.instance.getResult(key));
        return mav;
    }

}
