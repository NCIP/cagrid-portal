/**
 * $Id $
 */
package gov.nih.nci.cagrid.browser.beans;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SemanticSearchConfig {
	
	private String vocabulary;
	private int limit;
	private String evsGridServiceUrl;
	public String getEvsGridServiceUrl() {
		return evsGridServiceUrl;
	}
	public void setEvsGridServiceUrl(String evsGridServiceUrl) {
		this.evsGridServiceUrl = evsGridServiceUrl;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getVocabulary() {
		return vocabulary;
	}
	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}

}
