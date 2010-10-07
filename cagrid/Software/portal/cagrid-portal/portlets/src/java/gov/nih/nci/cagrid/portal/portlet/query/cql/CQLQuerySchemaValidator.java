package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import org.springframework.validation.Errors;

/**
 * Will validate CQL query against CQL schema
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CQLQuerySchemaValidator extends CQLQueryCommandValidator {

    private String cqlSchema;
    private String dcqlSchema;

    @Override
    public void validate(Object target, Errors errors) {
        boolean error = false;

        CQLQueryCommand command = (CQLQueryCommand) target;
        if (command.getCqlQuery() == null) {
            logger.info("Query XML has not been set. Skipping validation");
            return;
        }

        try {
            String schema = command.isDcql()? getDcqlSchema():getCqlSchema();

            if (command.isDcql()) {
                logger.debug("Is a DCQL query. WIll validate against DCQL query");
                schema = getDcqlSchema();
            }
            SchemaValidator.validate(this.getClass().getClassLoader().getResource(schema).getFile(),command.getCqlQuery());
        } catch (SchemaValidationException e) {
            logger.info("Invalid query submitted", e);
            error = true;
        }

        if (error) {
            // using cqlQuery filed name for backward compatibility with UI
            errors.rejectValue("cqlQuery", PortletConstants.BAD_CQL_MSG,
                    "Could not validate query XML against XML Schema. Query XML is invalid");
        }

    }

    public String getCqlSchema() {
        return cqlSchema;
    }

    public void setCqlSchema(String cqlSchema) {
        this.cqlSchema = cqlSchema;
    }

    public String getDcqlSchema() {
        return dcqlSchema;
    }

    public void setDcqlSchema(String dcqlSchema) {
        this.dcqlSchema = dcqlSchema;
    }
}
