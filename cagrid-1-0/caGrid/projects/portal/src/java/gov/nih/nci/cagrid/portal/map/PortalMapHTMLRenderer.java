package gov.nih.nci.cagrid.portal.map;

import org.apache.log4j.Category;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 3, 2006
 * Time: 11:41:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortalMapHTMLRenderer extends HtmlRenderer {

    /**
     * The logger.
     */
    private static final Category log = Category.getInstance(PortalMapHTMLRenderer.class);

    //pluggable renderer. Not sure how else to plug this in to the context
    private HtmlRenderer delegatedRenderer = null;


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException, PortalMapComponentException {

        RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        PortalMapComponent component = (PortalMapComponent) uiComponent;

        //If google maps are to be used.
        // Chose appropriate Renderer to render the map
        if (component.getUseGoogle() == "true") {
            GoogleMapRenderer googleRenderer = new GoogleMapRenderer();
            this.delegatedRenderer = googleRenderer;
        }

        //If no delegating renderer has been instantiated then
        //report as an exception
        if (this.delegatedRenderer == null) {
            log.error("No alternate map renderer available");
            throw new PortalMapComponentException("No alternate map renderer available");
        }

        this.delegatedRenderer.encodeBegin(facesContext, uiComponent);
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        delegatedRenderer.encodeChildren(facesContext, uiComponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        delegatedRenderer.encodeEnd(facesContext, uiComponent);
    }

    /**
     * Enable the rendering of children
     *
     * @return
     */
    public boolean getRendersChildren() {
        return true;
    }
}
