package gov.nih.nci.cagrid.introduce.info;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;


/**
 * Used for organizing imports for wsdl
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class NamespaceInformation {

	private NamespaceType namespace;
	private String prefix;

	
	public NamespaceInformation(NamespaceType namespace, String prefix){
		this.namespace = namespace;
		this.prefix = prefix;
	}	
	

	public NamespaceType getNamespace() {
		return namespace;
	}

	public void setNamespace(NamespaceType namespace) {
		this.namespace = namespace;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}