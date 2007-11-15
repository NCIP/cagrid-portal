package gov.nih.nci.cagrid.cadsr.uml2xml;

import gov.nih.nci.cagrid.cadsr.common.exceptions.CaDSRGeneralException;
import gov.nih.nci.cagrid.cadsr.uml2xml.file.configuration.ElementType;
import gov.nih.nci.cagrid.cadsr.uml2xml.file.configuration.NamespaceType;
import gov.nih.nci.cagrid.cadsr.uml2xml.file.configuration.NamespaceTypeDefaultUMLPackageBinding;
import gov.nih.nci.cagrid.cadsr.uml2xml.file.configuration.NamespaceTypeElementBindings;
import gov.nih.nci.cagrid.cadsr.uml2xml.file.configuration.UML2XMLBindingConfiguration;
import gov.nih.nci.cagrid.cadsr.uml2xml.file.configuration.UMLPackageBindingType;
import gov.nih.nci.cagrid.common.Utils;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author oster
 * 
 */
public class UML2XMLBindingConfigurationFileImpl implements UML2XMLBinding {

	protected static Log LOG = LogFactory.getLog(UML2XMLBindingConfigurationFileImpl.class.getName());

	public static final String DEFAULT_CONFIG_LOCATION = "/uml2xmlbinding.xml";
	private UML2XMLBindingConfiguration config = null;


	public UML2XMLBindingConfigurationFileImpl() {
		LOG.info("Using default config location (" + DEFAULT_CONFIG_LOCATION + ")");
		InputStream is = this.getClass().getResourceAsStream(DEFAULT_CONFIG_LOCATION);
		if (is == null) {
			String message = "The configuration file was not found at classpath location (" + DEFAULT_CONFIG_LOCATION
				+ ")";
			LOG.error(message);
			throw new RuntimeException(message);
		}
		try {
			this.config = (UML2XMLBindingConfiguration) Utils.deserializeObject(new InputStreamReader(is),
				UML2XMLBindingConfiguration.class);
		} catch (Exception e) {
			String message = "Problem processing the configuration file found at classpath location ("
				+ DEFAULT_CONFIG_LOCATION + "):" + e.getMessage();
			LOG.error(message, e);
			throw new RuntimeException(message, e);
		}
	}


	public UML2XMLBindingConfigurationFileImpl(UML2XMLBindingConfiguration config) {
		this.config = config;
		if (config == null) {
			throw new IllegalArgumentException("A valid (non-null) configuration object must be specified!");
		}
	}


	public String determineClassName(QName qname) throws CaDSRGeneralException {
		ElementType elementTypeForElement = getElementTypeForElement(qname);
		if (elementTypeForElement != null) {
			return elementTypeForElement.getUmlClassName();
		} else {
			// no specific entry found, use the default assumption that
			// elementname and classname are the same
			return qname.getLocalPart();
		}
	}


	public String determinePackageName(QName qname) throws CaDSRGeneralException {
		UMLPackageBindingType mappingForElement = getMappingForElement(qname);
		return mappingForElement.get_package();
	}


	public String determineCaDSRContext(QName qname) throws CaDSRGeneralException {
		UMLPackageBindingType mappingForElement = getMappingForElement(qname);
		return mappingForElement.getProjectContext();
	}


	public String determineCaDSRProjectShortName(QName qname) throws CaDSRGeneralException {
		UMLPackageBindingType mappingForElement = getMappingForElement(qname);
		return mappingForElement.getProjectShortName();
	}


	public String determineCaDSRProjectVersion(QName qname) throws CaDSRGeneralException {
		UMLPackageBindingType mappingForElement = getMappingForElement(qname);
		return mappingForElement.getProjectVersion();
	}


	private NamespaceType getNamespaceType(QName qname) throws CaDSRGeneralException {
		NamespaceType[] namespaces = this.config.getNamespaces().getNamespace();
		for (int i = 0; i < namespaces.length; i++) {
			NamespaceType ns = namespaces[i];
			if (qname.getNamespaceURI().equals(ns.getNamespace().toString())) {
				return ns;
			}
		}
		throw new CaDSRGeneralException("Unable to locate namespace entry for:" + qname.getNamespaceURI());
	}


	private ElementType getElementTypeForElement(QName qname) throws CaDSRGeneralException {
		// first find the namespace's entry
		NamespaceType namespaceEntry = getNamespaceType(qname);

		NamespaceTypeElementBindings elementBindings = namespaceEntry.getElementBindings();
		if (elementBindings != null) {
			ElementType[] elements = elementBindings.getElement();
			if (elements != null) {
				for (int i = 0; i < elements.length; i++) {
					ElementType elm = elements[i];
					if (qname.getLocalPart().equals(elm.getElementName())) {
						return elm;
					}
				}
			}
		}
		return null;
	}


	/**
	 * @param qname
	 * @return
     *      The package binding for the element identified by the qname
	 * @throws CaDSRGeneralException
	 */
	private UMLPackageBindingType getMappingForElement(QName qname) throws CaDSRGeneralException {
		// first find the namespace's entry
		NamespaceType namespaceEntry = getNamespaceType(qname);

		ElementType elementTypeForElement = getElementTypeForElement(qname);
		if (elementTypeForElement != null) {
			return elementTypeForElement.getUMLPackageBinding();
		}

		NamespaceTypeDefaultUMLPackageBinding defaultUMLPackageBinding = namespaceEntry.getDefaultUMLPackageBinding();
		if (defaultUMLPackageBinding == null || defaultUMLPackageBinding.getUMLPackageBinding() == null) {
			throw new CaDSRGeneralException("Unable to locate ElementBinding for:" + qname.getNamespaceURI()
				+ " and no default mapping is defined!");
		}

		return defaultUMLPackageBinding.getUMLPackageBinding();

	}

}
