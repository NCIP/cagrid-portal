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

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
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
        CQLQueryCommand command = (CQLQueryCommand) obj;

        try {
            //First check if dcql
            gov.nih.nci.cagrid.dcql.DCQLQuery query = (gov.nih.nci.cagrid.dcql.DCQLQuery) Utils
                    .deserializeObject(new StringReader(command.getCqlQuery()),
                            gov.nih.nci.cagrid.dcql.DCQLQuery.class);
            StringWriter w = new StringWriter();
            Utils.serializeObject(query, DCQLConstants.DCQL_QUERY_QNAME,
                    w);
            String dcql = w.toString();
            command.setCqlQuery(dcql);
            command.setDcql(true);
        } catch (Exception e) {
            try {
                gov.nih.nci.cagrid.cqlquery.CQLQuery query = (gov.nih.nci.cagrid.cqlquery.CQLQuery) Utils
                        .deserializeObject(new StringReader(command.getCqlQuery()),
                                gov.nih.nci.cagrid.cqlquery.CQLQuery.class);
                StringWriter w = new StringWriter();
                Utils.serializeObject(query, DataServiceConstants.CQL_QUERY_QNAME,
                        w);
                String cql = w.toString();
                command.setCqlQuery(cql);

                //if its a cql whech service url
                super.validate(obj, errors);

            } catch (Exception ex) {
                errors.rejectValue("cqlQuery", PortletConstants.BAD_CQL_MSG, null,
                        "Could not parse query XML. Query XML is invalid");
            }

        }

    }
}
