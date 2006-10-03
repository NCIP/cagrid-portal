package gov.nih.nci.cagrid.portal.map;

import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 3, 2006
 * Time: 12:55:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapRendererUtils {


    public static boolean renderHTMLAttributes(ResponseWriter writer,
                                               UIComponent component, String[] attributes) throws IOException {
        boolean somethingDone = false;
        for (int i = 0, len = attributes.length; i < len; i++) {
            String attrName = attributes[i];
            if (renderHTMLAttribute(writer, component, attrName, attrName)) {
                somethingDone = true;
            }
        }
        return somethingDone;
    }

    /**
     * @return true, if the attribute was written
     * @throws java.io.IOException
     */
    public static boolean renderHTMLAttribute(ResponseWriter writer,
                                              UIComponent component, String componentProperty, String htmlAttrName)
            throws IOException {
        Object value = component.getAttributes().get(componentProperty);
        return renderHTMLAttribute(writer, componentProperty, htmlAttrName,
                value);
    }

    /**
     * @return true, if the attribute was written
     * @throws java.io.IOException
     */
    public static boolean renderHTMLAttribute(ResponseWriter writer,
                                              String componentProperty, String attrName, Object value)
            throws IOException {
        if (!RendererUtils.isDefaultAttributeValue(value)) {
            // render JSF "styleClass" and "itemStyleClass" attributes as "class"
            String htmlAttrName =
                    attrName.equals(HTML.STYLE_CLASS_ATTR) ?
                            HTML.CLASS_ATTR : attrName;
            writer.writeAttribute(htmlAttrName, value, componentProperty);
            return true;
        }

        return false;
    }


}
