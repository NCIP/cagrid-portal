package gov.nih.nci.cagrid.authorization.impl;

import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import gov.nih.nci.cagrid.authorization.PENodeSelector;

public class RegExPENodeSelectorMapping {

	private String pattern;
	private PENodeSelector selector;
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public PENodeSelector getSelector() {
		return selector;
	}
	public void setSelector(PENodeSelector selector) {
		this.selector = selector;
	}
	
	public boolean matches(QName operation){
		return Pattern.matches(getPattern(), operation.toString());
	}
	
}
