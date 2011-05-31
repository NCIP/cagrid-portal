/**
 * 
 */
package gov.nih.nci.cagrid.tools;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.sdkquery4.beans.domaininfo.DomainTypesInformation;
import gov.nih.nci.cagrid.sdkquery4.processor.DomainTypesInformationUtil;
import gov.nih.nci.cagrid.sdkquery4.style.beanmap.BeanTypeDiscoveryMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GenBeanTypes {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		File beansJar = new File(args[0]);
		DomainModel domainModel = MetadataUtils
				.deserializeDomainModel(new FileReader(args[1]));
		FileWriter writer = new FileWriter(args[2]);
		
		BeanTypeDiscoveryMapper d = new BeanTypeDiscoveryMapper(beansJar, domainModel);
		DomainTypesInformation info = d.discoverTypesInformation();
		DomainTypesInformationUtil.serializeDomainTypesInformation(info, writer);
		writer.flush();
		writer.close();
	}

}
