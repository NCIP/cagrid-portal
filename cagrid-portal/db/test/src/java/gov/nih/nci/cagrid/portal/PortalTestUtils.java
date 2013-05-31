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
package gov.nih.nci.cagrid.portal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalTestUtils {

    public static Long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Will create a string of specified length
     * @param length in bytes
     * @return
     */
    public static String createReallyLongString(int length){
        byte[] _arr = new byte[length];
        return new String(_arr);

    }

    public static String readFileASString(String file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

        return sb.toString();
    }
}
