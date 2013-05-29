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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import gov.nih.nci.cagrid.portal.domain.news.NewsItem;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class NewsItemValidator implements Validator {

	private NewsChannelValidator channelValidator = new NewsChannelValidator();
	
	/**
	 * 
	 */
	public NewsItemValidator() {

	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return NewsItem.class.isAssignableFrom(klass);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		NewsItem item = (NewsItem)obj;
		channelValidator.commonNewsValidation(item.getTitle(), item.getLink(), item.getDescription(), errors);
		if(!errors.hasErrors()){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "height",
					"field.required.newsItem.height", null,
					"A height value is.");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "width",
					"field.required.newsItem.width", null,
					"A width value is.");
		}
	}

}
