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
