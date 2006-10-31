package gov.nih.nci.cagrid.portal.map;

import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
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

    private final String _newLine = "\n";

    private HashSet mapNodes = new HashSet();

    private String BOLD_ELEM = "b";

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
        writeMapScriptResouces(facesContext, GoogleMapRendererUtils.encodeGoogleMapScriptUrl());
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        //ToDO should come from properties file
        writeMapScriptResouces(facesContext, "js/CustomMapScript.js");

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
        out.write(mapVar + ".setCenter(new GLatLng(40, -95), 4);");
        //enable zooming
        out.write(mapVar + ".enableDoubleClickZoom();");
        out.write(mapVar + ".addControl(new GSmallMapControl());");

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

        UIData data = (UIData) uiComponent;

        for (int i = 0; i < data.getRowCount(); i++) {
            data.setRowIndex(i);
            //scrolled past the last row
            if (!data.isRowAvailable()) {
                break;
            }
            MapNode node = (MapNode) data.getRowData();
            boolean addoffset = !mapNodes.add(node);

            String lat = GoogleMapRendererUtils.getCoordValue(node.getGeoValues().getLatitude(), addoffset);
            String lng = GoogleMapRendererUtils.getCoordValue(node.getGeoValues().getLongitude(), addoffset);

            if (lat != null && lng != null) {
                out.write("var point = new GLatLng(" + lat + "," + lng + ");");
                out.write(_newLine);
                out.write("var marker" + i + " = new GMarker(point);");
                out.write(_newLine);
                out.write("GEvent.addListener(marker" + i + ", \"click\", function() {");
                out.write(_newLine);
                out.write("marker" + i + ".openInfoWindowHtml(\"");
                out.write("<span style=\\\"font-size: 0.9em;\\\">");

                // Display the Title of the mapNode in bold
                out.startElement(BOLD_ELEM, null);
                out.write(node.getTitle());
                out.endElement(BOLD_ELEM);

                //Write the html description
                for (Iterator iter = node.getDisplayText().iterator(); iter.hasNext();) {
                    out.startElement(HTML.BR_ELEM, null);
                    out.endElement(HTML.BR_ELEM);
                    out.write((String) iter.next());
                    //enter break
                    out.startElement(HTML.BR_ELEM, null);
                    out.endElement(HTML.BR_ELEM);
                }

                //end openInfoWindowHtml
                out.endElement(HTML.SPAN_ELEM);
                out.write("\");");
                out.write("});");

                //out.write(mapVar + ".addOverlay(createMarker(point,\"" + rc.getShortName() + "\",\"" + rc.getDisplayName() + "\"))");
                out.write("map.addOverlay(marker" + i + ");");
            }

        }//end for loop
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
