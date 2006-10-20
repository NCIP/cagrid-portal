package gov.nih.nci.cagrid.cadsr.uml2xml;

import gov.nih.nci.cagrid.cadsr.common.exceptions.CaDSRGeneralException;

import javax.xml.namespace.QName;


/**
 * @author oster
 */
public interface UML2XMLBinding {
	public String determineClassName(QName qname) throws CaDSRGeneralException;


	public String determinePackageName(QName qname) throws CaDSRGeneralException;


	public String determineCaDSRContext(QName qname) throws CaDSRGeneralException;


	public String determineCaDSRProjectShortName(QName qname) throws CaDSRGeneralException;


	public String determineCaDSRProjectVersion(QName qname) throws CaDSRGeneralException;
}
