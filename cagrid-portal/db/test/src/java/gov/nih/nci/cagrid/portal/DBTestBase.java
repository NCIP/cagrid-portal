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

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import org.junit.Before;

/**
 * Will create a test DB and insert sample data
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DBTestBase<T extends AbstractDao> extends DaoTestBase<T> {


    @Before
    public void initialize() throws Exception {
        TestDB.loadData(getDataSet());
    }

    protected String getDataSet() throws Exception {
        return "test/data/" + getNamingStrategy() + ".xml";
    }


}
