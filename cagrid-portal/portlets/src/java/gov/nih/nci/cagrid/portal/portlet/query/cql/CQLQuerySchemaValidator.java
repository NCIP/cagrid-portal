package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal.portlet.query.shared.XMLSchemaValidatorFactory;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Required;

/**
 * Will validate CQL query against CQL schema
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CQLQuerySchemaValidator extends CQLQueryCommandValidator {

    SchemaValidator cqlXMLSchemaValidator, dcqlXMLSchemaValidator;

    @Override
    public void validate(Object target, Errors errors) {
        boolean error = false;

        CQLQueryCommand command = (CQLQueryCommand) target;
        if (command.getCqlQuery() == null) {
            logger.info("Query XML has not been set. Skipping validation");
            return;
        }

        try {
            SchemaValidator validator;
            if (!command.isDcql()) {
                validator = cqlXMLSchemaValidator;

            } else {
                logger.debug("Is a DCQL query. WIll validate against DCQL query");
                validator = dcqlXMLSchemaValidator;
            }
            validator.validate(command.getCqlQuery());
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

    public SchemaValidator getCqlXMLSchemaValidator() {
        return cqlXMLSchemaValidator;
    }

    public void setCqlXMLSchemaValidator(SchemaValidator cqlXMLSchemaValidator) {
        this.cqlXMLSchemaValidator = cqlXMLSchemaValidator;
    }

    public SchemaValidator getDcqlXMLSchemaValidator() {
        return dcqlXMLSchemaValidator;
    }

    public void setDcqlXMLSchemaValidator(SchemaValidator dcqlXMLSchemaValidator) {
        this.dcqlXMLSchemaValidator = dcqlXMLSchemaValidator;
    }
}
