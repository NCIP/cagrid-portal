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
package gov.nih.nci.cagrid.hibernate;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.cagrid.data.utilities.ResultsCreationException;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CastorCQLQueryResultsBuilder implements CQLQueryResultsBuilder {

	/**
	 * 
	 */
	public CastorCQLQueryResultsBuilder() {

	}

	/* (non-Javadoc)
	 * @see CQLQueryResultsBuilder#build(java.util.List)
	 */
	public CQLQueryResults build(List rawResults, CQLQuery cqlQuery, Mappings classToQName) {

		CQLQueryResults cqlResults = null;
        // determine which type of results to package up
        if (cqlQuery.getQueryModifier() != null) {
            QueryModifier mods = cqlQuery.getQueryModifier();
            if (mods.isCountOnly()) {
                long count = Long.parseLong(rawResults.get(0).toString());
                cqlResults = CQLResultsCreationUtil.createCountResults(count, cqlQuery.getTarget().getName());
            } else { // attributes
                String[] attributeNames = null;
                List<Object[]> resultsAsArrays = null;
                if (mods.getDistinctAttribute() != null) {
                    attributeNames = new String[] {mods.getDistinctAttribute()};
                    resultsAsArrays = new LinkedList<Object[]>();
                    for (Object o : rawResults) {
                        resultsAsArrays.add(new Object[] {o});
                    }
                } else { // multiple attributes
                    attributeNames = mods.getAttributeNames();
                    resultsAsArrays = new LinkedList<Object[]>();
                    for (Object o : rawResults) {
                        resultsAsArrays.add((Object[]) o);
                    }
                }
                cqlResults = CQLResultsCreationUtil.createAttributeResults(
                    resultsAsArrays, cqlQuery.getTarget().getName(), attributeNames);
            }
        } else {
            try {
                cqlResults = CQLResultsCreationUtil.createObjectResults(
                    rawResults, cqlQuery.getTarget().getName(), classToQName);
            } catch (ResultsCreationException ex) {
                throw new RuntimeException("Error packaging query results: " + ex.getMessage(), ex);
            }
        }
		
		return cqlResults;
	}

}
