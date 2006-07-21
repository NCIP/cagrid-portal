package gov.nih.nci.cagrid.discovery;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.Reader;
import java.io.Writer;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.utils.ClassUtils;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.w3c.dom.Element;


public class MetadataUtils {

	private static final String METADATA_WSDD = "/gov/nih/nci/cagrid/discovery/Metadata-client-config.wsdd";


	public static ServiceMetadata getServiceMetadata(EndpointReferenceType serviceEPR) throws Exception {
		Element resourceProperty = ResourcePropertyHelper.getResourceProperty(serviceEPR,
			MetadataConstants.CAGRID_MD_QNAME);

		return (ServiceMetadata) ObjectDeserializer.toObject(resourceProperty, ServiceMetadata.class);
	}


	public static DomainModel getDomainModel(EndpointReferenceType serviceEPR) throws Exception {
		Element resourceProperty = ResourcePropertyHelper.getResourceProperty(serviceEPR,
			MetadataConstants.CAGRID_DATA_MD_QNAME);

		return (DomainModel) ObjectDeserializer.toObject(resourceProperty, DomainModel.class);
	}


	public static void serializeServiceMetadata(ServiceMetadata metadata, Writer writer) throws Exception {
		if (metadata == null || writer == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		Utils.serializeObject(metadata, MetadataConstants.CAGRID_DATA_MD_QNAME, writer, ClassUtils.getResourceAsStream(
			MetadataUtils.class, METADATA_WSDD));
	}


	public static ServiceMetadata deserializeServiceMetadata(Reader xmlReader) throws Exception {
		if (xmlReader == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		return (ServiceMetadata) Utils.deserializeObject(xmlReader, ServiceMetadata.class, ClassUtils
			.getResourceAsStream(MetadataUtils.class, METADATA_WSDD));
	}


	public static void serializeDomainModel(DomainModel domainModel, Writer writer) throws Exception {
		if (domainModel == null || writer == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		Utils.serializeObject(domainModel, MetadataConstants.CAGRID_MD_QNAME, writer, ClassUtils.getResourceAsStream(
			MetadataUtils.class, METADATA_WSDD));
	}


	public static DomainModel deserializeDomainModel(Reader xmlReader) throws Exception {
		if (xmlReader == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		return (DomainModel) Utils.deserializeObject(xmlReader, DomainModel.class, ClassUtils.getResourceAsStream(
			DomainModel.class, METADATA_WSDD));
	}

}
