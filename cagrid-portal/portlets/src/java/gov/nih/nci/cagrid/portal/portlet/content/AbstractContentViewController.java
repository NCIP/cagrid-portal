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
package gov.nih.nci.cagrid.portal.portlet.content;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.portlet.mvc.Controller;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractContentViewController implements InitializingBean, Controller {
	
	private String viewNameParam;
	private String viewNameDefault;

	public String getViewNameDefault() {
		return viewNameDefault;
	}

	public void setViewNameDefault(String viewNameDefault) {
		this.viewNameDefault = viewNameDefault;
	}

	public String getViewNameParam() {
		return viewNameParam;
	}

	public void setViewNameParam(String viewNameParam) {
		this.viewNameParam = viewNameParam;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(getViewNameParam(), "The viewNameParam property is required.");
		Assert.notNull(getViewNameDefault(), "The viewNameDefault property is required.");
	}

}
