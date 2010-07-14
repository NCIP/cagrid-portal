package gov.nih.nci.cagrid.portal.portlet.impromptu;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ImpromptuQueryValidator implements Validator {

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return ImpromptuQuery.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "query", "impromptu.query.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "endpointUrl", "impromptu.url.empty");
    }

}
