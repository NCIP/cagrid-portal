package gov.nih.nci.cagrid.portal.portlet.impromptu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.util.UrlPathHelper;

public class ImpromptuQueryViewController extends ParameterizableViewController {

    static public Map<ImpromptuQuery, UUID> submited= Collections.synchronizedMap(new HashMap<ImpromptuQuery, UUID>());
    static public Map<String, String> results = Collections.synchronizedMap(new HashMap<String, String>());

    static private String tail(final String s) {
        String result = "";
        if (s != null) {
            result = s.trim();
            int pos = result.lastIndexOf("/");
            result = result.substring(pos);
        }
        return result;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("results");
        String key = ImpromptuQueryViewController.tail((new UrlPathHelper()).getOriginatingRequestUri(request));
        mav.addObject("a", ImpromptuQueryViewController.results.get(key));
        return mav;
    }

}
