package gov.nih.nci.cagrid.fqp.common;

import javax.xml.namespace.QName;

import org.cagrid.fqp.execution.QueryExecutionParameters;
import org.cagrid.fqp.execution.TargetDataServiceQueryBehavior;


public interface FederatedQueryProcessorConstants {
	public static final String SERVICE_NS = "http://fqp.cagrid.nci.nih.gov/FederatedQueryProcessor";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "FederatedQueryProcessorKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "FederatedQueryProcessorResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName SERVICEMETADATA = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	
    public static final String NAMESPACE_FQP_RESULTS = "http://fqp.cagrid.nci.nih.gov/FederatedResults";
    public static final String RESULTS_SERVICE_NAME = "FederatedQueryResults";

    // Results metadata updated for use with Notification
    public static final QName RESULTS_METADATA_QNAME = 
        new QName("http://fqp.cagrid.nci.nih.gov/FederatedQueryResultsMetadata", "FederatedQueryExecutionStatus");
    
    public static final TargetDataServiceQueryBehavior DEFAULT_TARGET_QUERY_BEHAVIOR = 
        new TargetDataServiceQueryBehavior(Boolean.TRUE, Integer.valueOf(0), Integer.valueOf(5));
    public static final QueryExecutionParameters DEFAULT_QUERY_EXECUTION_PARAMETERS = 
        new QueryExecutionParameters(Boolean.FALSE, DEFAULT_TARGET_QUERY_BEHAVIOR);
}
