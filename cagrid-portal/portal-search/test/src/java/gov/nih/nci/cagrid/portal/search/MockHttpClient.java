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
package gov.nih.nci.cagrid.portal.search;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;

import java.io.IOException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class MockHttpClient extends HttpClient {

    private static boolean assertRan = false;

    public MockHttpClient() {
    }


    @Override
    public int executeMethod(HttpMethod httpMethod) throws IOException, HttpException {
        assertRan = true;
        return 0;
    }

    public static boolean assertJustRan() {
        if (assertRan) {
            assertRan = false;
            return !assertRan;
        }
        return assertRan;

    }
}
 
