package gov.nih.nci.cagrid.portal.portlet;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import java.util.Map;
import java.util.Iterator;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AjaxViewGenerator extends org.springframework.web.portlet.handler.PortletContentGenerator{


    public String getView(String viewName, Map<String,Object>reqAttributes) throws Exception{
        WebContext webContext = WebContextFactory.get();

        for(Iterator iterator = reqAttributes.entrySet().iterator();iterator.hasNext();){
            Map.Entry<String,Object> entry =  (Map.Entry<String,Object>)  iterator.next() ;
            webContext.getHttpServletRequest().setAttribute(entry.getKey(),entry.getValue()) ;
        }
        return webContext.forwardToString(viewName);
    }
}
