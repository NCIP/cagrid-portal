package gov.nih.nci.cagrid.portal.map;

import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Rendering class for Google Maps
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 3, 2006
 * Time: 1:59:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMapRenderer extends HtmlRenderer {

    private String GOOGLE_MAP_SCRIPT_RESOURCE_KEY = HtmlRenderer.class.getName() + ".MAP_SCRIPTS_WRITTEN";

    //ToDo has to come from somewhere else. Maybe a properties file?
    private final String GOOGLE_MAP_KEY_VALUE = "ABQIAAAARxXVpwVgTtCYim7Kvfkm4BTBfUk9TZrBRaIteybtnU2KziHEpRRZjiilkGRYazxwPL9oucEwvUSErA";
    private final String GOOGLE_MAP_SCRIPT_SRC = "http://maps.google.com/maps?file=api&v=2&key=";

    private final String _newLine = "\n";

    private String[] MAP_ROOT_ELEMENT_ATTR = {
            HTML.ALIGN_ATTR,
            HTML.STYLE_ATTR,
            HTML.STYLE_CLASS_ATTR
    };

    private final String mapVar = "map";

    public GoogleMapRenderer() {
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter out = facesContext.getResponseWriter();

        //render scripts needed
        //ToDo make this optional
        writeMapScriptResouces(facesContext, encodeGoogleMapScriptUrl());
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        /*Start the div tag*/
        out.startElement(HTML.DIV_ELEM, uiComponent);
        MapRendererUtils.renderHTMLAttributes(out, uiComponent, MAP_ROOT_ELEMENT_ATTR);

        //Write id for div. Will be used when setting up map
        HtmlRendererUtils.writeIdIfNecessary(out, uiComponent, facesContext);

        //faces assigned id
        String mapCompID = uiComponent.getClientId(facesContext);

        /** Src the google script JavaScript api **/
        out.startElement(HTML.SCRIPT_ELEM, null);

        out.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

        /** Start writing map. Setup all properties herer **/
        out.write("var " + mapVar + " = new GMap2(document.getElementById(\"" + mapCompID + "\"));" + _newLine);
        //center over usa
        out.write(" map.setCenter(new GLatLng(40, -95), 4);");
        //enable zooming
        out.write(mapVar + ".enableDoubleClickZoom();");

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter out = facesContext.getResponseWriter();

        out.endElement(HTML.SCRIPT_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        out.endElement(HTML.DIV_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter out = facesContext.getResponseWriter();
        //setupicon
        out.write("var baseIcon = new GIcon();");
        out.write("baseIcon.image = \"http://labs.google.com/ridefinder/images/mm_20_red.png\";");
        out.write("baseIcon.shadow = \"http://labs.google.com/ridefinder/images/mm_20_shadow.png\";");
        out.write("baseIcon.iconSize = new GSize(20, 34);");
        out.write("baseIcon.shadowSize = new GSize(37, 34);");
        out.write("baseIcon.iconAnchor = new GPoint(9, 34);");

        //setup icon for research center
        out.write("var rcIcon = new GIcon(baseIcon);");
        out.write("rcIcon.image = \"http://www.google.com/mapfiles/markerR.png\";");

        UIData data = (UIData) uiComponent;
        for (int i = 0; i < data.getRowCount(); i++) {
            ResearchCenter rc = (ResearchCenter) data.getRowData();
            String lat = rc.getLatitude().toString();
            String lng = rc.getLongitude().toString();

            out.write("var point = new GLatLng(" + lat + "," + lng + ");");
            out.write("var marker = new GMarker(point, rcIcon);");
            out.write(mapVar + ".addOverlay(marker);");

            //scrolled past the last row
            if (!data.isRowAvailable())
                break;
        }

    }
    /** Utility methods begin **/
    /**
     * ToDo should be in a utility class *
     */
    private String encodeGoogleMapScriptUrl() throws UnsupportedEncodingException {
        String gMapsURL = GOOGLE_MAP_SCRIPT_SRC + GOOGLE_MAP_KEY_VALUE;
        return gMapsURL;
    }

    /**
     * Will write out the map scripting resources]
     * that are needed. All the JavaScript needed
     * by the Map component are loaded this way
     *
     * @param facesContext
     * @throws IOException
     */
    protected void writeMapScriptResouces(FacesContext facesContext, String resourcePath)
            throws IOException {
        Set mapScriptResource = getMapScriptResourceAlreadyWritten(facesContext);

        //Set add returns true only if item is added to Set
        //and false if already present
        if (mapScriptResource.add(resourcePath)) {
            ResponseWriter out = facesContext.getResponseWriter();
            out.startElement(HTML.SCRIPT_ELEM, null);
            out.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            out.writeAttribute(HTML.SRC_ATTR, resourcePath, null);
            out.endElement(HTML.SCRIPT_ELEM);
        }

    }


    /**
     * Make sure that map javascript
     * is written only once
     *
     * @param context
     * @return
     */
    private Set getMapScriptResourceAlreadyWritten(FacesContext context) {
        ExternalContext external = context.getExternalContext();
        Map requestMap = external.getRequestMap();
        Set written = (Set) requestMap.get(GOOGLE_MAP_SCRIPT_RESOURCE_KEY);

        if (written == null) {
            written = new HashSet();
            requestMap.put(GOOGLE_MAP_SCRIPT_RESOURCE_KEY, written);
        }
        return written;
    }
}
