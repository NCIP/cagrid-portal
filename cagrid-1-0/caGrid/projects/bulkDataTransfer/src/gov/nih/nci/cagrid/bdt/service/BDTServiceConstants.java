package gov.nih.nci.cagrid.bdt.service;

import javax.xml.namespace.QName;


public interface BDTServiceConstants {
	public static final QName METADATA_QNAME = new QName("http://cagrid.nci.nih.gov/1/BulkDataTransferMetadata", "BulkDataTransferMetadata");
	public static final String METADATA_SCHEMA = "BulkDataTransferServiceMetadata.xsd";
	public static final String BDT_REF_SCHEMA = "BulkDataHandlerReference.xsd";
	public static final String ENUMERATION_SCHEMA = "enumeration.xsd";
	public static final String ADDRESSING_SCHEMA = "addressing.xsd";
	public static final String TRANSFER_SCHEMA = "transfer.xsd";
}