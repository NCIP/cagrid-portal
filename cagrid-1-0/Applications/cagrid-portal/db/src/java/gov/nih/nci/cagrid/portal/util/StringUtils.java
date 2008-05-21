package gov.nih.nci.cagrid.portal.util;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class StringUtils {

    /**
     * Will take out special characters from postal code.
     *
     * @param postalCode
     * @return
     */
    public static String parsePostalCode(String postalCode) {
        while (postalCode != null && postalCode.indexOf(".") > -1) {
            postalCode = postalCode.substring(0, postalCode.indexOf("."));
        }
        return postalCode;
    }

    public static String getEmptyIfNull(String str) {
        if (str == null)
            return "";
        else return str;
    }

}


