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
