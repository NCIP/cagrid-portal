package gov.nih.nci.cagrid.portal.map;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 10, 2006
 * Time: 10:35:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMapRendererUtils {

    private static final String GOOGLE_MAP_KEY = "google.map.key.value";
    private static final String GOOGLE_MAP_SCRIPT_URL = "google.map.script.url";

    /** Utility methods begin **/
    /**
     */
    protected static String encodeGoogleMapScriptUrl() throws IOException {

        //Todo Use Spring instead of hardcoding
        //load properties file
        ResourceBundle mapProps = ResourceBundle.getBundle("googleMap");


        String googleScriptURL = mapProps.getString(GOOGLE_MAP_SCRIPT_URL);
        String googleMapKeyValue = mapProps.getString(GOOGLE_MAP_KEY);

        String gMapsURL = googleScriptURL + "&key=" + googleMapKeyValue;
        return gMapsURL;
    }

    protected static String getCoordValue(Float coord, boolean offset) {
        BigDecimal big = new BigDecimal(coord.floatValue());
        if (offset)
            return big.add(new BigDecimal(Math.random())).toString();

        return big.toString();
    }
}
