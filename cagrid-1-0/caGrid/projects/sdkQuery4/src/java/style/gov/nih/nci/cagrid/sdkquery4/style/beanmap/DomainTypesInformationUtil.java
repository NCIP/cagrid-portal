package gov.nih.nci.cagrid.sdkquery4.style.beanmap;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.sdkquery4.beans.domaininfo.DomainTypesInformation;

import java.io.Reader;
import java.io.Writer;

import javax.xml.namespace.QName;

/** 
 *  DomainTypesInformationUtil
 *  Utility for manipulating a domain types information instance
 * 
 * @author David Ervin
 * 
 * @created Jan 16, 2008 11:30:36 AM
 * @version $Id: DomainTypesInformationUtil.java,v 1.1 2008-01-16 17:05:31 dervin Exp $ 
 */
public class DomainTypesInformationUtil {
    
    public static final QName DOMAIN_TYPES_INFORMATION_QNAME = 
        new QName("http://SDKQuery4.caBIG/1/gov.nih.nci.cagrid.sdkquery4.beans.domaininfo", "DomainTypesInformation");
    
    public static DomainTypesInformation deserializeDomainTypesInformation(Reader reader) throws Exception {
        return (DomainTypesInformation) Utils.deserializeObject(reader, DomainTypesInformation.class);
    }
    
    
    public static void serializeDomainTypesInformation(DomainTypesInformation info, Writer writer) throws Exception {
        Utils.serializeObject(info, DOMAIN_TYPES_INFORMATION_QNAME, writer);
    }
    

    public static void main(String[] args) {
    }
}
