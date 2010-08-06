package gov.nih.nci.cagrid.portal.portlet.impromptu;

import java.net.URLDecoder;

import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

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

        try {
            ImpromptuQuery q = (ImpromptuQuery) obj;

            /* TODO: this shouldn't be necessary */
            String urlDecodedQuery = q.getQuery().replace("& ", "&");
            urlDecodedQuery = urlDecodedQuery.replace("&lt;", "<");
            urlDecodedQuery = urlDecodedQuery.replace("&gt;", ">");
            urlDecodedQuery = URLDecoder.decode(urlDecodedQuery, "UTF-8");
            if (PortletUtils.isCQL(urlDecodedQuery)) {
                ValidationUtils.rejectIfEmptyOrWhitespace(e, "endpointUrl", "impromptu.url.empty");
            }
        } catch (Exception ex) {
            //
        }
    }

}
