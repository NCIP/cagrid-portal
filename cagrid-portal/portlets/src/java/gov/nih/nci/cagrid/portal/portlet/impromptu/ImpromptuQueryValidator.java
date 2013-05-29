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
package gov.nih.nci.cagrid.portal.portlet.impromptu;

import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ImpromptuQueryValidator implements Validator {

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return ImpromptuQuery.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "query", "impromptu.query.empty");

        try {
            ImpromptuQuery q = (ImpromptuQuery) obj;

            String urlDecodedQuery = q.getQuery();

            try {
                
                urlDecodedQuery = urlDecodedQuery.replace("& ", "&");
                urlDecodedQuery = urlDecodedQuery.replace("&lt;", "<");
                urlDecodedQuery = urlDecodedQuery.replace("&gt;", ">");
                urlDecodedQuery = URLDecoder.decode(urlDecodedQuery, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                //do nothing. This can happen when CQL is submitted from browser
            }
            
            if (PortletUtils.isCQL(urlDecodedQuery)) {
                ValidationUtils.rejectIfEmptyOrWhitespace(e, "endpointUrl", "impromptu.url.empty");
            }
        } catch (Exception ex) {
            //
        }
    }

}
