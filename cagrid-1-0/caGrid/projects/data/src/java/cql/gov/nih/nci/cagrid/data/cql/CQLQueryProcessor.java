package gov.nih.nci.cagrid.data.cql;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/** 
 *  CQLQueryProcessor
 *  Abstract class the service providers must extend to process 
 *  CQL Queries to a caGrid data service 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 25, 2006 
 * @version $Id$ 
 */
public abstract class CQLQueryProcessor {	
	private Properties params;
	private InputStream wsddStream;
	
	public CQLQueryProcessor() {
	
	}
	
	
	/**
	 * Initialize the query processor with the properties it requires as specified
	 * in the map provided by getRequiredParameters().  Additionally, the parameter
	 * <code>AXIS_WSDD_CONFIG_STREAM</code> will be defined and mapped to
	 * an <code>InputStream</code> object containing the configuration of the
	 * current axis engine.
	 * @param parameters
	 * 		The parameters as configured by the user.  The set of keys must contain all
	 * 		of the keys contained in the Properties object returned 
	 * 		by <code>getRequiredParamters()</code>.  The values in the parameters will
	 *		be either the user defined value or the default value from 
	 *		<code>getRequiredParameters()</code>.
	 * @param wsddStream
	 * 		The input stream which contains the wsdd configuration for the data service.
	 * 		This stream may be important to locating type mappings for serializing and
	 * 		deserializing beans.
	 * @throws InitializationException
	 */
	public void initialize(Properties parameters, InputStream wsdd) throws InitializationException {
		// validate the parameters
		Set required = new HashSet(getRequiredParameters().keySet());
		required.removeAll(parameters.keySet());
		if (required.size() != 0) {
			// some required parameters NOT specified!
			StringBuffer error = new StringBuffer();
			error.append("Required parameters for query processor ");
			error.append(getClass().getName()).append(" not specified: ");
			Iterator requiredKeyIter = required.iterator();
			while (requiredKeyIter.hasNext()) {
				error.append(requiredKeyIter.next());
				if (requiredKeyIter.hasNext()) {
					error.append(", ");
				}
			}
			throw new InitializationException(error.toString());
		}
		this.params = parameters;
		this.wsddStream = wsdd;
	}
	
	
	/**
	 * @return
	 * 		The parameters as configured by the user.  The set of keys must contain all
	 * 		of the keys contained in the Properties object returned 
	 * 		by <code>getRequiredParamters()</code>.  The values in the parameters will
	 *		be either the user defined value or the default value from 
	 *		<code>getRequiredParameters()</code>.
	 */
	protected Properties getConfiguredParameters() {
		return this.params;
	}
	
	
	/**
	 * @return
	 * 		The input stream which contains the wsdd configuration for the data service.
	 * 		This stream may be important to locating type mappings for serializing and
	 * 		deserializing beans.
	 */
	protected InputStream getConfiguredWsddStream() {
		return this.wsddStream;
	}
	
	
	/**
	 * Processes the CQL Query
	 * @param cqlQuery
	 * @return
	 * @throws MalformedQueryException
	 * 		Should be thrown when the query itself does not conform to the
	 * 		CQL standard or attempts to perform queries outside of 
	 * 		the exposed domain model
	 * @throws QueryProcessingException
	 * 		Thrown for all exceptions in query processing not related
	 * 		to the query being malformed
	 */
	public abstract CQLQueryResults processQuery(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException;
	
	
	/**
	 * Get a Properties object of parameters the query processor will require 
	 * on initialization.  
	 * 
	 * Subclasses can override this method to return a map describing paramters
	 * their implementation needs.
	 * 
	 * The keys are the names of parameters the query processor 
	 * requires, the values are the defaults for those properties.  The default value
	 * of a property may be <code>NULL</code> if it is an optional paramter.
	 * The keys MUST be valid java variable names.  They MUST start with a <i>lowercase</i>
	 * character, and must NOT contain spaces or punctuation.
	 * 
	 * @return
	 */
	public Properties getRequiredParameters() {
		return new Properties();
	}
}
