package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.encoding.SDKDeserializerFactory;
import gov.nih.nci.cagrid.encoding.SDKSerializerFactory;

import java.io.File;

import javax.xml.namespace.QName;


/** 
 *  DataServiceConstants
 *  Assorted constants for data services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Mar 31, 2006 
 * @version $Id$ 
 */
public class DataServiceConstants {
	// metadata schema constants
	public static final String METADATA_SCHEMA_LOCATION = "xsd" + File.separator + "cagrid" + File.separator + "types";
	public static final String CADSR_METADATA_SCHEMA_LOCATION = METADATA_SCHEMA_LOCATION + File.separator + "cadsr";
	public static final String CADSR_DOMAIN_SCHEMA = CADSR_METADATA_SCHEMA_LOCATION + File.separator + "3.0_gov.nih.nci.cadsr.domain.xsd";
	public static final String CADSR_UMLPROJECT_SCHEMA = CADSR_METADATA_SCHEMA_LOCATION + File.separator + "3.0_gov.nih.nci.cadsr.umlproject.domain.xsd";
	public static final String DATA_METADATA_SCHEMA = METADATA_SCHEMA_LOCATION + File.separator + "data" + File.separator + "data.xsd";
	public static final String COMMON_METADATA_SCHEMA = METADATA_SCHEMA_LOCATION + File.separator + "common" + File.separator + "common.xsd";
	public static final String CAGRID_METADATA_SCHEMA = METADATA_SCHEMA_LOCATION + File.separator + "caGridMetadata.xsd";
	public static final String DATA_METADATA_URI = "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice";
	
	// sdk serializer constants
	public static final String SDK_SERIALIZER = SDKSerializerFactory.class.getName();
	public static final String SDK_DESERIALIZER = SDKDeserializerFactory.class.getName();

	// query schema constants
	public static final String CQL_QUERY_SCHEMA = "1_gov.nih.nci.cagrid.CQLQuery.xsd";
	public static final String CQL_RESULT_SET_SCHEMA = "1_gov.nih.nci.cagrid.CQLResultSet.xsd";
	public static final String CQL_QUERY_URI = "http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery";
	public static final String CQL_RESULT_SET_URI = "http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet";
	public static final String CQL_QUERY_TYPE = gov.nih.nci.cagrid.cqlquery.CQLQuery.class.getName();
	public static final String CQL_RESULT_SET_TYPE = gov.nih.nci.cagrid.cqlresultset.CQLQueryResults.class.getName();
	public static final QName CQL_QUERY_QNAME = new QName(CQL_QUERY_URI, "CQLQuery");
	public static final QName CQL_RESULT_SET_QNAME = new QName(CQL_RESULT_SET_URI, "CQLQueryResults");
	public static final QName CQL_RESULT_COLLECTION_QNAME = new QName(CQL_RESULT_SET_URI, "CQLQueryResultCollection");
	
	// query method constants
	public static final String QUERY_METHOD_NAME = "query";
	public static final String QUERY_METHOD_RETURN_TYPE = CQL_RESULT_SET_TYPE;
	public static final String QUERY_METHOD_PARAMETER_TYPE = CQL_QUERY_TYPE;
	public static final String QUERY_METHOD_PARAMETER_NAME = "cqlQuery";
	public static final String QUERY_IMPLEMENTATION_ADDED = "queryImplAdded";
	
	// data service naming constants
	public static final String DATA_SERVICE_PACKAGE = "gov.nih.nci.cagrid.data";
	public static final String DATA_SERVICE_SERVICE_NAME = "DataService";
	public static final String DATA_SERVICE_NAMESPACE = "http://" + DATA_SERVICE_PACKAGE + "/" + DATA_SERVICE_SERVICE_NAME;
	public static final String DATA_SERVICE_PORT_TYPE_NAME = DATA_SERVICE_SERVICE_NAME + "PortType";
	
	// query processor constants
	public static final String QUERY_PROCESSOR_CLASS_PROPERTY = "queryProcessorClass";
	public static final String QUERY_PROCESSOR_STUB_NAME = "StubCQLQueryProcessor";
	public static final String QUERY_PROCESSOR_STUB_DEPRICATED_MESSAGE = 
		"/*\n" +
		" * This class is no longer used by the Data Service,\n" +
		" * and should be removed if the implementation it\n" +
		" * contains is no longer needed.\n" +
		" */\n\n";
	
	// query processor config constants
	public static final String QUERY_PROCESSOR_CONFIG_PREFIX = "cqlQueryProcessorConfig_";
	
	// exceptions
	public static final String DATA_SERVICE_EXCEPTIONS_NAMESPACE = "http://gov.nih.nci.cagrid.data/DataServiceExceptions";
	public static final String DATA_SERVICE_EXCEPTIONS_SCHEMA = "DataServiceExceptions.xsd";
	public static final String QUERY_PROCESSING_EXCEPTION_NAME = "QueryProcessingException";
	public static final String MALFORMED_QUERY_EXCEPTION_NAME = "MalformedQueryException";
	public static final QName QUERY_PROCESSING_EXCEPTION_QNAME = new QName(DATA_SERVICE_EXCEPTIONS_NAMESPACE, QUERY_PROCESSING_EXCEPTION_NAME);
	public static final QName MALFORMED_QUERY_EXCEPTION_QNAME = new QName(DATA_SERVICE_EXCEPTIONS_NAMESPACE, MALFORMED_QUERY_EXCEPTION_NAME);
	
	// validation constants
	public static final String DATA_SERVICE_PARAMS_PREFIX = "dataService";
	public static final String CQL_VALIDATOR_CLASS = DATA_SERVICE_PARAMS_PREFIX + "_cqlValidatorClass";
	public static final String DOMAIN_MODEL_VALIDATOR_CLASS = DATA_SERVICE_PARAMS_PREFIX + "_domainModelValidatorClass";
	public static final String VALIDATE_CQL_FLAG = DATA_SERVICE_PARAMS_PREFIX + "_validateCqlFlag";
	public static final String VALIDATE_DOMAIN_MODEL_FLAG = DATA_SERVICE_PARAMS_PREFIX + "_validateDomainModelFlag";
	
	// DomainModel QName
	public static final QName DOMAIN_MODEL_QNAME = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel");
	
	// service metadata QName
	public static final QName SERVICE_METADATA_QNAME = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	
	// service URL parameter constants
	public static final String CADSR_SERVICE_URL = "CADSR_URL";
	public static final String GME_SERVICE_URL = "GME_URL";
	
	private DataServiceConstants() {
		
	}
}
