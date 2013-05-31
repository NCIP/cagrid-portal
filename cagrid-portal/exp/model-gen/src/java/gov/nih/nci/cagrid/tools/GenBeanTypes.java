/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
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
