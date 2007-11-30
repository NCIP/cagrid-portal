/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal.portlet.query.model.SelectServiceCommand;
import gov.nih.nci.cagrid.portal.portlet.query.model.SelectServiceCommandValidator;

import java.io.StringReader;
import java.io.StringWriter;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CQLQueryCommandValidator extends SelectServiceCommandValidator implements Validator {

	/**
	 * 
	 */
	public CQLQueryCommandValidator() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return CQLQueryCommand.class.isAssignableFrom(klass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		
		super.validate(obj, errors);
		
		CQLQueryCommand command = (CQLQueryCommand) obj;
		
		try {
			gov.nih.nci.cagrid.cqlquery.CQLQuery query = (gov.nih.nci.cagrid.cqlquery.CQLQuery) Utils
					.deserializeObject(new StringReader(command.getCqlQuery()),
							gov.nih.nci.cagrid.cqlquery.CQLQuery.class);
			StringWriter w = new StringWriter();
			Utils.serializeObject(query, DataServiceConstants.CQL_QUERY_QNAME,
					w);
			String cql = w.toString();
			command.setCqlQuery(cql);
		} catch (Exception ex) {
			errors.rejectValue("cqlQuery", PortletConstants.BAD_CQL_MSG, null,
					"Could not parse CQL query.");
		}
	}

}
