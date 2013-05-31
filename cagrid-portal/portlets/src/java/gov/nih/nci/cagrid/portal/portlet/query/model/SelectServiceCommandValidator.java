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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.model;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectServiceCommandValidator implements Validator {
	
	protected static final Log logger = LogFactory.getLog(SelectServiceCommandValidator.class);

	private GridServiceDao gridServiceDao;
	
	/**
	 * 
	 */
	public SelectServiceCommandValidator() {

	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return SelectServiceCommand.class.isAssignableFrom(klass);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		SelectServiceCommand command = (SelectServiceCommand)obj;
		try {
			String url = command.getDataServiceUrl().trim();
			new URL(url);
			command.setDataServiceUrl(url);
		} catch (Exception ex) {
			errors
					.rejectValue("dataServiceUrl",
							PortletConstants.BAD_URL_MSG,
							new String[] { command.getDataServiceUrl() },
							"Invalid URL");
		}
		
		GridDataService dataService = null;
		try {
			dataService = (GridDataService) getGridServiceDao().getByUrl(
					command.getDataServiceUrl().trim());
		} catch (ClassCastException ex) {
			errors.rejectValue("dataServiceUrl",
					PortletConstants.WRONG_SERVICE_TYPE,
					new String[] { command.getDataServiceUrl() }, command
							.getDataServiceUrl()
							+ " doesn't point to a data service.");
		} catch(Exception ex){
			errors.rejectValue("dataServiceUrl", "db.search.error", "Error encountered");
			logger.error(ex);
		}
		
		if (!errors.hasErrors() && dataService == null) {
			errors.rejectValue("dataServiceUrl",
					PortletConstants.NONEXSTING_DATA_SERVICE_MSG,
					new String[] { command.getDataServiceUrl() },
					"No service found for url: "
							+ command.getDataServiceUrl());
		}
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}
}
