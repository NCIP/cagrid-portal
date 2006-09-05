package gov.nih.nci.cagrid.metadata;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import java.io.Reader;
import java.io.Writer;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.utils.ClassUtils;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.w3c.dom.Element;


public class MetadataUtils {

	private static final String METADATA_WSDD = "/gov/nih/nci/cagrid/metadata/Metadata-client-config.wsdd";


	/**
	 * Obtain the service metadata from the specified service.
	 * 
	 * @param serviceEPR
	 * @return
	 * @throws InvalidResourcePropertyException
	 * @throws RemoteResourcePropertyRetrievalException
	 * @throws ResourcePropertyRetrievalException
	 */
	public static ServiceMetadata getServiceMetadata(EndpointReferenceType serviceEPR)
		throws InvalidResourcePropertyException, RemoteResourcePropertyRetrievalException,
		ResourcePropertyRetrievalException {
		Element resourceProperty = ResourcePropertyHelper.getResourceProperty(serviceEPR,
			MetadataConstants.CAGRID_MD_QNAME);
		ServiceMetadata result;
		try {
			result = (ServiceMetadata) ObjectDeserializer.toObject(resourceProperty, ServiceMetadata.class);
		} catch (DeserializationException e) {
			throw new ResourcePropertyRetrievalException("Unable to deserailize ServiceMetadata: " + e.getMessage(), e);
		}
		return result;
	}


	/**
	 * Obtain the data service metadata from the specified service.
	 * 
	 * @param serviceEPR
	 * @return
	 * @throws InvalidResourcePropertyException
	 * @throws RemoteResourcePropertyRetrievalException
	 * @throws ResourcePropertyRetrievalException
	 */
	public static DomainModel getDomainModel(EndpointReferenceType serviceEPR) throws InvalidResourcePropertyException,
		RemoteResourcePropertyRetrievalException, ResourcePropertyRetrievalException {
		Element resourceProperty = ResourcePropertyHelper.getResourceProperty(serviceEPR,
			MetadataConstants.CAGRID_DATA_MD_QNAME);

		DomainModel result;
		try {
			result = (DomainModel) ObjectDeserializer.toObject(resourceProperty, DomainModel.class);
		} catch (DeserializationException e) {
			throw new ResourcePropertyRetrievalException("Unable to deserailize DomainModel: " + e.getMessage(), e);
		}
		return result;
	}


	/**
	 * Write the XML representation of the specified metadata to the specified
	 * writer. If either are null, an IllegalArgumentException will be thown.
	 * 
	 * @param metadata
	 * @param writer
	 * @throws Exception
	 */
	public static void serializeServiceMetadata(ServiceMetadata metadata, Writer writer) throws Exception {
		if (metadata == null || writer == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		Utils.serializeObject(metadata, MetadataConstants.CAGRID_MD_QNAME, writer, ClassUtils.getResourceAsStream(
			MetadataUtils.class, METADATA_WSDD));
	}


	/**
	 * Create an instance of the service metadata from the specified reader. The
	 * reader must point to a stream that contains an XML representation of the
	 * metadata. If the reader is null, an IllegalArgumentException will be
	 * thown.
	 * 
	 * @param xmlReader
	 * @return
	 * @throws Exception
	 */
	public static ServiceMetadata deserializeServiceMetadata(Reader xmlReader) throws Exception {
		if (xmlReader == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		return (ServiceMetadata) Utils.deserializeObject(xmlReader, ServiceMetadata.class, ClassUtils
			.getResourceAsStream(MetadataUtils.class, METADATA_WSDD));
	}


	/**
	 * Write the XML representation of the specified metadata to the specified
	 * writer. If either are null, an IllegalArgumentException will be thown.
	 * 
	 * @param domainModel
	 * @param writer
	 * @throws Exception
	 */
	public static void serializeDomainModel(DomainModel domainModel, Writer writer) throws Exception {
		if (domainModel == null || writer == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		Utils.serializeObject(domainModel, MetadataConstants.CAGRID_DATA_MD_QNAME, writer, ClassUtils
			.getResourceAsStream(MetadataUtils.class, METADATA_WSDD));
	}


	/**
	 * Create an instance of the data service metadata from the specified
	 * reader. The reader must point to a stream that contains an XML
	 * representation of the metadata. If the reader is null, an
	 * IllegalArgumentException will be thown.
	 * 
	 * @param xmlReader
	 * @return
	 * @throws Exception
	 */
	public static DomainModel deserializeDomainModel(Reader xmlReader) throws Exception {
		if (xmlReader == null) {
			throw new IllegalArgumentException("Null is not a valid argument");
		}
		return (DomainModel) Utils.deserializeObject(xmlReader, DomainModel.class, ClassUtils.getResourceAsStream(
			DomainModel.class, METADATA_WSDD));
	}

}
