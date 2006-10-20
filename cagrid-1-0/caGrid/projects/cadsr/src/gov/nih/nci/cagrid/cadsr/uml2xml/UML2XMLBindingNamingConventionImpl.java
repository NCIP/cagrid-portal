package gov.nih.nci.cagrid.cadsr.uml2xml;

import gov.nih.nci.cagrid.cadsr.common.CaDSRGeneralException;

import javax.xml.namespace.QName;

import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.Namespace;


/**
 * @author oster
 * 
 */
public class UML2XMLBindingNamingConventionImpl implements UML2XMLBinding {

	public String determineClassName(QName qname) throws CaDSRGeneralException {
		try {
			Namespace ns = new Namespace(qname.getNamespaceURI());
		} catch (MalformedNamespaceException e) {
			throw new CaDSRGeneralException("Namespace is not of expected format:" + e.getMessage(), e);
		}
		return qname.getLocalPart();
	}


	public String determinePackageName(QName qname) throws CaDSRGeneralException {
		String namespace = qname.getNamespaceURI();
		int i = namespace.lastIndexOf("/");
		if (i <= 0 || i == namespace.length() - 1) {
			throw new CaDSRGeneralException("Namespace is expected to have a meaningful package name after the last /");
		}

		String pack = namespace.substring(i + 1);
		return pack.toLowerCase();
	}


	public String determineCaDSRContext(QName qname) throws CaDSRGeneralException {
		String namespace = qname.getNamespaceURI();
		try {
			Namespace ns = new Namespace(namespace);
			String domain = ns.getDomain();
			int ind = domain.lastIndexOf(".");
			if (ind < 0) {
				throw new CaDSRGeneralException(
					"Can't handle namespace format; namespace domain not of expected format:" + domain);
			}

			return domain.substring(ind + 1);
		} catch (MalformedNamespaceException e) {
			throw new CaDSRGeneralException("Namespace is not of expected format:" + e.getMessage(), e);
		}
	}


	public String determineCaDSRProjectShortName(QName qname) throws CaDSRGeneralException {
		String namespace = qname.getNamespaceURI();
		try {
			Namespace ns = new Namespace(namespace);
			String domain = ns.getDomain();
			int ind = domain.indexOf(".");
			if (ind < 0) {
				throw new CaDSRGeneralException(
					"Can't handle namespace format; namespace domain not of expected format:" + domain);
			}

			return domain.substring(0, ind);
		} catch (MalformedNamespaceException e) {
			throw new CaDSRGeneralException("Namespace is not of expected format:" + e.getMessage(), e);
		}
	}


	public String determineCaDSRProjectVersion(QName qname) throws CaDSRGeneralException {
		String namespace = qname.getNamespaceURI();
		try {
			Namespace ns = new Namespace(namespace);
			String localNamespace = ns.getName();
			int ind = localNamespace.indexOf("/");
			if (ind < 0) {
				throw new CaDSRGeneralException(
					"Can't handle namespace format; namespace domain not of expected format:" + localNamespace);
			}

			return localNamespace.substring(0, ind);
		} catch (MalformedNamespaceException e) {
			throw new CaDSRGeneralException("Namespace is not of expected format:" + e.getMessage(), e);
		}
	}

}
