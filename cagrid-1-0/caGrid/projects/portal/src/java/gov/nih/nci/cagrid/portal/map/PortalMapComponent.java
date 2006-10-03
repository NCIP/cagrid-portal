package gov.nih.nci.cagrid.portal.map;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 2, 2006
 * Time: 2:14:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortalMapComponent extends UIData {

    public static final String COMPONENT_TYPE = "gov.nih.nci.cagrid.portal.Map";
    public static final String RENDERER_TYPE = "gov.nih.nci.cagrid.portal.Map";

    private String style = null;
    private String styleClass = null;
    private String align = null;
    private String useGoogle = null;

    public PortalMapComponent() {
        setRendererType(RENDERER_TYPE);
    }

    public void encodeBegin(FacesContext context) throws PortalMapComponentException, IOException {
        setRowIndex(-1);
        super.encodeBegin(context);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[28];
        values[0] = super.saveState(context);
        values[1] = align;
        values[2] = style;
        values[3] = styleClass;
        values[4] = useGoogle;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        align = (String) values[1];
        style = (String) values[2];
        styleClass = (String) values[3];
        useGoogle = (String) values[4];
    }

    public String getAlign() {
        if (align != null) return align;
        ValueBinding vb = getValueBinding("align");
        return vb != null ? MapComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getStyle() {
        if (style != null) return style;
        ValueBinding vb = getValueBinding("style");
        return vb != null ? MapComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        if (styleClass != null) return styleClass;
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? MapComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getUseGoogle() {
        if (useGoogle != null) return useGoogle;
        ValueBinding vb = getValueBinding("useGoogle");
        return vb != null ? MapComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setUseGoogle(String useGoogle) {
        this.useGoogle = useGoogle;
    }

}
