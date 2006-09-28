package gov.nih.nci.cagrid.introduce.extensions.metadata.codegen;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPreProcessor;
import gov.nih.nci.cagrid.introduce.extensions.metadata.constants.MetadataConstants;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Just sets the metadata filelocation if its not set.
 * 
 * @author oster
 * 
 */
public class MetadataCodegenPreProcessor implements CodegenExtensionPreProcessor {
	private static final String DEFAULT_FILENAME = "serviceMetadata.xml";

	protected static Log LOG = LogFactory.getLog(MetadataCodegenPreProcessor.class.getName());


	public void preCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {

		String filename = setFilename(info);
		if (filename == null) {
			LOG.error("Unable to locate Service Metadata resource property.");
			return;
		} else {
			LOG.debug("Set service metadata file location to:" + filename);
		}
	}


	/**
	 * @return
	 */
	private String setFilename(ServiceInformation info) {
		ServiceType mainServ = info.getServiceDescriptor().getServices().getService()[0];
		ResourcePropertiesListType resourcePropertiesList = mainServ.getResourcePropertiesList();
		if (resourcePropertiesList != null && resourcePropertiesList.getResourceProperty() != null) {
			ResourcePropertyType[] resourceProperty = resourcePropertiesList.getResourceProperty();
			for (int i = 0; i < resourceProperty.length; i++) {
				ResourcePropertyType rp = resourceProperty[i];
				if (rp.getQName().equals(MetadataConstants.SERVICE_METADATA_QNAME)) {
					String fileLocation = rp.getFileLocation();
					if (fileLocation == null || fileLocation.trim().equals("")) {
						rp.setFileLocation(DEFAULT_FILENAME);
					}

					return rp.getFileLocation();

				}
			}
		}

		return null;
	}

}
