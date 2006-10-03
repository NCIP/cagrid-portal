package gov.nih.nci.cagrid.portal.map;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 3, 2006
 * Time: 11:09:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class MapComponentUtils {

    private MapComponentUtils() {
    }

    static String getStringValue(FacesContext context, ValueBinding vb) {
        Object value = vb.getValue(context);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
