/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.sample;

import junit.framework.TestCase;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EVSSearchHelperTest extends TestCase {

    public void testSearch() throws Exception{
        EVSSearchHelper _helper = new EVSSearchHelper();
        System.out.println(_helper.conceptCodeSearch("Gene"));
        assertNotNull(_helper.conceptCodeSearch("Gene"));

    }
}
