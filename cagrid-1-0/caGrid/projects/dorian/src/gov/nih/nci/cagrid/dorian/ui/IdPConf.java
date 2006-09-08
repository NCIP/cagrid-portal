package gov.nih.nci.cagrid.dorian.ui;

import gov.nih.nci.cagrid.dorian.ui.ifs.IdPAuthenticationPanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

public class IdPConf {

	private static final String NAME = "name";

	private static final String CLASS = "authPanelClass";

	private static final String PARAMETERS = "parameters";

	private static final String PARAMETER = "parameter";

	private static final String PARAMETER_NAME = "name";

	private static final String PARAMETER_VALUE = "value";

	private String name;

	private String authenticationPanelClass;

	private Map parameters = new HashMap();
	

	public IdPConf(Element e) throws Exception {
		this.name = e.getAttributeValue(NAME);
		if (name == null) {
			throw new Exception(
					"Each IdP must specify a name.");
		}
		this.authenticationPanelClass = e.getAttributeValue(CLASS);
		if (this.authenticationPanelClass == null) {
			throw new Exception(
					"Each IdP must specify a authentication panel class.");
		}
		
		Class c = null;
		try {
			c = Class.forName(this.authenticationPanelClass);
		} catch (Exception ex) {
			throw new Exception(
					"The Authentication Panel Class ("
							+ this.authenticationPanelClass
							+ ")  specified for the IdP, " + this.name
							+ " is invalid.");
		}
		
		if(!IdPAuthenticationPanel.class.isAssignableFrom(c)){
			throw new Exception(
					"The Authentication Panel Class ("
							+ this.authenticationPanelClass
							+ ")  specified for the IdP, " + this.name
							+ " must be a super class of "+IdPAuthenticationPanel.class.getName()+".");
		}
		
		Element params = e.getChild(PARAMETERS, e.getNamespace());
		if (params != null) {
			List list = params.getChildren(PARAMETER, e.getNamespace());
			for (int i = 0; i < list.size(); i++) {
				Element param = (Element) list.get(i);
				String pname = param.getAttributeValue(PARAMETER_NAME);
				if (pname == null) {
					throw new Exception(
							"Each parameter for the IdP, "
									+ name + " must have a name specified.");
				}
				String value = param.getAttributeValue(PARAMETER_VALUE);
				if (value == null) {
					throw new Exception("The " + pname
							+ " parameter for the IdP, " + name
							+ " must have a value specified.");
				}
				if (parameters.containsKey(pname)) {
					throw new Exception("The " + pname
							+ " parameter for the IdP, " + name
							+ " cannot be defined more than once.");
				} else {
					this.parameters.put(pname, value);
				}
			}

		}
	}

	public String getParameter(String name) {
		return (String) this.parameters.get(name);
	}

	public String getAuthenticationPanelClass() {
		return authenticationPanelClass;
	}

	public void setAuthenticationPanelClass(String authenticationPanelClass) {
		this.authenticationPanelClass = authenticationPanelClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return getName();
	}
}
