/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.fqp.common.DCQLConstants;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal.portlet.query.model.SelectServiceCommandValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CQLQueryCommandValidator extends SelectServiceCommandValidator
		implements Validator {

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

		CQLQueryCommand command = (CQLQueryCommand) obj;
		boolean error = false;
		try {
			command.setCqlQuery(serializeQuery(command,
					DataServiceConstants.CQL_QUERY_QNAME,
					gov.nih.nci.cagrid.cqlquery.CQLQuery.class));

			// if its a cql which service url
			super.validate(obj, errors);
		} catch (Exception ex) {
			if (!command.isDcql()) {
				error = true;
			} else {
				try {
					command.setCqlQuery(serializeQuery(command,
							DCQLConstants.DCQL_QUERY_QNAME,
							gov.nih.nci.cagrid.dcql.DCQLQuery.class));
				} catch (Exception e) {
					error = true;
				}
			}
		}
		if (error) {
			errors.rejectValue("cqlQuery", PortletConstants.BAD_CQL_MSG, null,
					"Could not parse query XML. Query XML is invalid");
		}
	}

	private String serializeQuery(CQLQueryCommand command, QName qName,
			Class klass) throws Exception {
		Object query = Utils.deserializeObject(new StringReader(command
				.getCqlQuery()), klass);
		StringWriter w = new StringWriter();
		Utils.serializeObject(query, qName, w);
		return w.toString();
	}
}
