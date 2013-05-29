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
package gov.nih.nci.cagrid.portal.portlet.query.results;

import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class StringMatchServiceErrorInterpretor implements
		ServiceErrorInterpretor, InitializingBean {

	private Pattern p;
	private String pattern;
	private String message;

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.query.results.ServiceErrorInterpretor#getErrorMessage(java.lang.String)
	 */
	public String getErrorMessage(String serviceError) {
		if(serviceError != null && p.matcher(serviceError).matches()){
			return getMessage();
		}
		return null;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void afterPropertiesSet() throws Exception {
		p = Pattern.compile(getPattern(), Pattern.DOTALL);
	}

}
