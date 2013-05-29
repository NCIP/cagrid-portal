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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.terms;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public interface TerminologyProvider {

	List<TerminologyBean> listTerminologies();

	List<TermBean> getRootTerms(TerminologyBean terminology);

	List<TermBean> getChildTerms(TermBean term);

	List<TermBean> getPathToRoot(TermBean term);

	List<TermBean> getDescendants(TermBean term);
	
	TermBean getTermForUri(String uri);

}
