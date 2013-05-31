/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
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


    public static String abbreviate(String longStr, int maxLength) {
        String _longStr = getEmptyIfNull(longStr);
        return _longStr.length() > maxLength ? _longStr.substring(0, maxLength - 2) + ".." : _longStr;
    }
    
    public static boolean isEmpty(String s){
    	return s == null || s.trim().length() == 0;
    }
}


