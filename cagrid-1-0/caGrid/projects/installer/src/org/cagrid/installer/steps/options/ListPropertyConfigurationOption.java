/**
 * 
 */
package org.cagrid.installer.steps.options;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ListPropertyConfigurationOption extends
		AbstractPropertyConfigurationOption {
	
	private String[] choices;
	
	public ListPropertyConfigurationOption(){
		
	}
	
	public ListPropertyConfigurationOption(String name, String description, String[] choices){
		super(name, description, false);
		this.choices = choices;
	}
	
	public ListPropertyConfigurationOption(String name, String description, String[] choices, boolean required){
		super(name, description, required);
		this.choices = choices;
	}

	public String[] getChoices() {
		return choices;
	}

	public void setChoices(String[] choices) {
		this.choices = choices;
	}

}
