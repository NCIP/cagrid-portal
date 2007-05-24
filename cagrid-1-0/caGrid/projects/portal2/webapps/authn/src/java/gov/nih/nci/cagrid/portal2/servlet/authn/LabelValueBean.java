/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class LabelValueBean {
	private String label;
	private String value;
	
	public LabelValueBean(){}
	public LabelValueBean(String label, String value){
		this.label = label;
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
