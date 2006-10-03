package gov.nih.nci.cagrid.portal.map;

import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.taglib.html.HtmlComponentBodyTagBase;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 2, 2006
 * Time: 3:15:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalMapTag extends HtmlComponentBodyTagBase {
    public static final String USE_GOOGLE_ATTR = "useGoogle";
    /*HTML attributes for the map*/

    private String align;
    private String var;
    // value --> already implemented in UIComponentTagBase

    private String useGoogle;

    public String getComponentType() {
        return PortalMapComponent.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return PortalMapComponent.RENDERER_TYPE;
    }

    public void release() {
        super.release();    //To change body of overridden methods use File | Settings | File Templates.
        align = null;
        var = null;
        useGoogle = null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);    //To change body of overridden methods use File | Settings | File Templates.

        setStringProperty(component, HTML.ALIGN_ATTR, align);
        setStringProperty(component, JSFAttr.VAR_ATTR, var);
        setStringProperty(component, USE_GOOGLE_ATTR, useGoogle);
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setUseGoogle(String useGoogle) {
        this.useGoogle = useGoogle;
    }
}
