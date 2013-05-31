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
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CQLQueryDaoTest extends QueryDaoTestBase<CQLQueryDao> {


    private String _hash = new String("#hash1");


    @Test
    public void retreive() {
        CQLQuery query = getDao().getById(-1);
        assertNotNull(query);
    }

    @Test
    public void hash() {
        CQLQuery query = getDao().getById(-1);
        assertNotNull(query);
        assertEquals(query.getHash(), _hash);


        String _nhash = new String("#hash1");
        query.setHash(_nhash);
        getDao().save(query);

        query = getDao().getByHash(_nhash);
        assertEquals(query.getHash(), _nhash);

    }

    public void search() {
        CQLQuery example = new CQLQuery();
        example.setHash(_hash);

        assertNotNull(getDao().getByExample(example));
        assertEquals(getDao().getByHash(_hash), getDao().getByExample(example));
        assertEquals(getDao().getByExample(example), getDao().getById(-1));
        getDao().getByHash(_hash).equals(getDao().getById(-1));

        example.setXml("xml");
        assertNotNull(getDao().getByExample(example));
        assertEquals(getDao().getById(-2), getDao().getByExample(example));

    }

    @Test
    public void hashAndEquals() {
        CQLQuery example1 = new CQLQuery();
        example1.setHash(_hash);

        CQLQuery example2 = new CQLQuery();
        example2.setHash(_hash);

        CQLQuery example3 = new CQLQuery();
        example3.setHash("differentHash");

        assertTrue(example1.equals(example2));
        assertFalse(example2.equals(example3));
    }

    @Override
    public String getNamingStrategy() {
        return "CqlQueryDaoTest";
    }
}
