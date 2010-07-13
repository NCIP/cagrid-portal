package gov.nih.nci.cagrid.portal.portlet.gss;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ImpromptuQueryViewController extends ParameterizableViewController {

    static public Map<UUID, String> results = Collections.synchronizedMap(new HashMap<UUID, String>());
    
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
        UUID uuid = UUID.fromString(ImpromptuQueryViewController.tail(request.getRequestURL().toString()));
        mav.addObject("a", ImpromptuQueryViewController.results.get(uuid));
        return mav;
    }

}
