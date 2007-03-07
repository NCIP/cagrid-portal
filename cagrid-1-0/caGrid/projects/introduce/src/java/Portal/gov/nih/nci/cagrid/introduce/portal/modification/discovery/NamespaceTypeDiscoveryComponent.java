package gov.nih.nci.cagrid.introduce.portal.modification.discovery;

import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespacesType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;


/**
 * Classes which extend this class should maintain the proper policy for
 * downloading and storing schemas. There are 3 policies: replace ignore and
 * error Replace: if there are any schema/namespace to bring in that are already
 * existing in the services current namespace list then just replace them
 * Ignore: if there are any schema/namespace to bring in that are already
 * existing in the services current namespace list then just ignore them, i.e do
 * not overwrite the schema that already exists and do not create a new
 * namespace type. Error: if there are any schema/namespace to bring in that are
 * already existing in the services current namespace list then set the error
 * messages and return null. In this case the developer should make sure the
 * class does not pull any schemas down and does not return any new namespace
 * types.
 * 
 * @author hastings
 */
public abstract class NamespaceTypeDiscoveryComponent extends JPanel {
	public static final String REPLACE = "replace";
	public static final String IGNORE = "ignore";
	public static final String ERROR = "error";

	private DiscoveryExtensionDescriptionType descriptor;
	private NamespacesType currentNamespaces;
	protected List errors;


	public NamespaceTypeDiscoveryComponent(DiscoveryExtensionDescriptionType descriptor,
		NamespacesType currentNamespaces) {
		this.descriptor = descriptor;
		this.currentNamespaces = currentNamespaces;
		errors = new ArrayList();
	}


	public DiscoveryExtensionDescriptionType getDescriptor() {
		return descriptor;
	}


	public NamespacesType getCurrentNamespaces() {
		return currentNamespaces;
	}


	public boolean namespaceAlreadyExists(String namespaceURI) {
		if (CommonTools.getNamespaceType(currentNamespaces, namespaceURI) != null) {
			return true;
		}
		return false;
	}


	protected void addError(String Error) {
		errors.add(Error);
	}


	public String getErrorMessage() {
		String ErrorMessage = "";
		Iterator it = errors.iterator();
		while (it.hasNext()) {
			ErrorMessage += (String) it.next();
			if (it.hasNext()) {
				ErrorMessage += "\n";
			}
		}
		errors = new ArrayList();
		return ErrorMessage;
	}


	public abstract NamespaceType[] createNamespaceType(File schemaDestinationDir, String namespaceExistsPolicy);

}
