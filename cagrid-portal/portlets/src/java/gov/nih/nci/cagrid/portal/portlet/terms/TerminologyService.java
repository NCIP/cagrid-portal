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

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TerminologyService {
	
	private String addTermsFormInputsRenderServletUrl;
	
	private TerminologyProvider terminologyProvider;

	/**
	 * 
	 */
	public TerminologyService() {

	}

	public TerminologyProvider getTerminologyProvider() {
		return terminologyProvider;
	}

	public void setTerminologyProvider(TerminologyProvider terminologyProvider) {
		this.terminologyProvider = terminologyProvider;
	}

	public List<TermBean> getChildTerms(TermBean term) {
		return terminologyProvider.getChildTerms(term);
	}

	public List<TermBean> getDescendants(TermBean term) {
		return terminologyProvider.getDescendants(term);
	}

	public List<TermBean> getPathToRoot(TermBean term) {
		return terminologyProvider.getPathToRoot(term);
	}

	public List<TerminologyBean> listTerminologies() {
		return terminologyProvider.listTerminologies();
	}
	
	public List<TermBean> getRootTerms(TerminologyBean terminology) {
		return terminologyProvider.getRootTerms(terminology);
	}
	
	public String renderAddTermsFormInputs(String terminologyUri, String namespace){
		String html = null;
		TerminologyBean terminology = null;
		for(TerminologyBean t : getTerminologyProvider().listTerminologies()){
			if(t.getUri().equals(terminologyUri)){
				terminology = t;
				break;
			}
		}
		if(terminology == null){
			throw new RuntimeException("No terminology found for " + terminologyUri);
		}
		try{
			List<TermBean> terms = getTerminologyProvider().getRootTerms(terminology);
			WebContext webContext = WebContextFactory.get();
			HttpServletRequest request = webContext.getHttpServletRequest();
			request.setAttribute("terms", terms);
			request.setAttribute("namespace", namespace);
			html = webContext
					.forwardToString(getAddTermsFormInputsRenderServletUrl());
			
		}catch(Exception ex){
			throw new RuntimeException("Error rendering terms form inputs: " + ex.getMessage(), ex);
		}
		return html;
	}

	public String getAddTermsFormInputsRenderServletUrl() {
		return addTermsFormInputsRenderServletUrl;
	}

	public void setAddTermsFormInputsRenderServletUrl(
			String addTermsFormInputsRenderServletUrl) {
		this.addTermsFormInputsRenderServletUrl = addTermsFormInputsRenderServletUrl;
	}

}
