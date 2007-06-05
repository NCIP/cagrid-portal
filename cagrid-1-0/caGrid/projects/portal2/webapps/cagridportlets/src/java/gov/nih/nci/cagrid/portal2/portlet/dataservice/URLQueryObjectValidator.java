package gov.nih.nci.cagrid.portal2.portlet.dataservice;

import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <A HREF="MAILTO:parmarv@mail.nih.gov">Vijay Parmar</A>
 *
 */
public class URLQueryObjectValidator implements Validator {
    
    
	 /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return clazz.equals(URLQueryObject.class);
    }

    public void validate(Object obj, Errors errors) {
    	URLQueryObject dataServiceQueryObject = (URLQueryObject) obj;
        
            logger.info("Validating with " + dataServiceQueryObject);
            if (null==dataServiceQueryObject.getUrl() || dataServiceQueryObject.getUrl().length()==0) {               
            
                ValidationUtils.rejectIfEmpty(errors, "url", "URL_REQUIRED", "URL is required.");
            }
            if (null==dataServiceQueryObject.getCqlQuery() || dataServiceQueryObject.getCqlQuery().length()==0) {
            	ValidationUtils.rejectIfEmpty(errors, "cqlQuery", "CQL_REQUIRED", "CQL Query is required.");
            }
        
    }

   

}
