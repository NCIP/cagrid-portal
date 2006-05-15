package gov.nih.nci.cagrid.data.common;

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
	public static final String METADATA_SCHEMA_LOCATION = "metadata" + File.separator + "cagrid" + File.separator + "types";
	public static final String CADSR_METADATA_SCHEMA_LOCATION = METADATA_SCHEMA_LOCATION + File.separator + "cadsr";
	public static final String CADSR_DOMAIN_SCHEMA = CADSR_METADATA_SCHEMA_LOCATION + File.separator + "3.0_gov.nih.nci.cadsr.domain.xsd";
	public static final String CADSR_UMLPROJECT_SCHEMA = CADSR_METADATA_SCHEMA_LOCATION + File.separator + "3.0_gov.nih.nci.cadsr.umlproject.domain.xsd";
	public static final String DATA_METADATA_SCHEMA = METADATA_SCHEMA_LOCATION + File.separator + "data" + File.separator + "data.xsd";
	public static final String COMMON_METADATA_SCHEMA = METADATA_SCHEMA_LOCATION + File.separator + "common" + File.separator + "common.xsd";
	public static final String DATA_METADATA_URI = "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice";
	
	// sdk serializer constants
	public static final String SDK_SERIALIZER = SDKSerializerFactory.class.getName();
	public static final String SDK_DESERIALIZER = SDKDeserializerFactory.class.getName();

	// query schema constants
	public static final String CQL_QUERY_SCHEMA = "1_gov.nih.nci.cagrid.CQLQuery.xsd";
	public static final String CQL_RESULT_SET_SCHEMA = "1_gov.nih.nci.cagrid.CQLResultSet.xsd";
	public static final String CQL_QUERY_URI = "http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery";
	public static final String CQL_RESULT_SET_URI = "http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet";
	public static final String CQL_QUERY_TYPE = gov.nih.nci.cagrid.cqlquery.Object.class.getName();
	public static final String CQL_RESULT_SET_TYPE = gov.nih.nci.cagrid.cqlresultset.CQLQueryResults.class.getName();
	
	// query method constants
	public static final String QUERY_METHOD_NAME = "query";
	public static final String QUERY_METHOD_RETURN_TYPE = CQL_RESULT_SET_TYPE;
	public static final String QUERY_METHOD_PARAMETER_TYPE = CQL_QUERY_TYPE;
	public static final String QUERY_METHOD_PARAMETER_NAME = "cqlQuery";
	
	// query processor constants
	public static final String QUERY_PROCESSOR_CLASS_PROPERTY = "QueryProcessorClass";
	public static final String QUERY_PROCESSOR_ADDITIONAL_JARS_ELEMENT = "QueryProcessorJars";
	public static final String QUERY_PROCESSOR_JAR_ELEMENT = "Jar";
	
	/*
	public static final String[] QUERY_METHOD_EXCEPTIONS = new String[] {
		QueryProcessingException.class.getName(), MalformedQueryException.class.getName()
	};
	*/
	// FIXME: this goes away when introduce supports using typed exceptions from packages
	// outside of the generated source tree
	public static final String[] QUERY_METHOD_EXCEPTIONS = new String[] {
		"QueryProcessingException", "MalformedQueryException"
	};
	public static final String QUERY_PROCESSOR_ELEMENT_NAME = "queryProcessorClass";
	
	// cadsr info constants
	public static final String CADSR_ELEMENT_NAME = "caDSRInformation";
	public static final String CADSR_URL_ATTRIB = "url";
	public static final String CADSR_PROJECT_ATTRIB = "project";
	public static final String CADSR_PACKAGE_ATTRIB = "package";
	
	// target data model
	public static final String DATA_MODEL_ELEMENT_NAME = "targetDataModel";
	
	// DomainModel QName
	public static final QName DOMAIN_MODEL_QNAME = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel");

	private DataServiceConstants() {
		
	}
}
