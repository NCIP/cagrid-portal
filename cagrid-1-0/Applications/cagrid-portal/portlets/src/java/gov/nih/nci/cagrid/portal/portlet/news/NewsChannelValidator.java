/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import java.net.URL;

import gov.nih.nci.cagrid.portal.domain.news.NewsChannel;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class NewsChannelValidator implements Validator {

	private int maxTitleLength = 50;

	/**
	 * 
	 */
	public NewsChannelValidator() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class klass) {
		return NewsChannel.class.isAssignableFrom(klass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		NewsChannel channel = (NewsChannel) obj;
		commonNewsValidation(channel.getTitle(), channel.getLink(), channel
				.getDescription(), errors);
	}

	public void commonNewsValidation(String title, String link,
			String description, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title",
				"field.required.newsChannel.title", null,
				"A title is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "link",
				"field.required.newsChannel.link", null, "A link is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description",
				"field.required.newsChannel.description", null,
				"A description is required.");

		if (!errors.hasErrors()) {
			if (title.length() > getMaxTitleLength()) {
				errors.rejectValue("title", "field.maxLength",
						new String[] { String.valueOf(getMaxTitleLength()) },
						"The length of this field should not exceed "
								+ getMaxTitleLength() + " characters.");
			}
			try {
				new URL(link);
			} catch (Exception ex) {
				errors.rejectValue("link", PortletConstants.BAD_URL_MSG,
						new String[] { link }, "Invalid URL");
			}
		}
	}

	public int getMaxTitleLength() {
		return maxTitleLength;
	}

	public void setMaxTitleLength(int maxTitleLength) {
		this.maxTitleLength = maxTitleLength;
	}

}
