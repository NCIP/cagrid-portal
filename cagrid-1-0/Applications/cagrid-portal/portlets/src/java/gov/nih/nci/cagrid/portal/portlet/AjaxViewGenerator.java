package gov.nih.nci.cagrid.portal.portlet;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AjaxViewGenerator extends FilteredContentGenerator {

    private String view;

    public String getView(String viewName, Map<String, Object> reqAttributes) throws Exception {
        WebContext webContext = WebContextFactory.get();

        for (Iterator iterator = reqAttributes.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
            webContext.getHttpServletRequest().setAttribute(entry.getKey(), entry.getValue());
        }
        return webContext.forwardToString(viewName);
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
