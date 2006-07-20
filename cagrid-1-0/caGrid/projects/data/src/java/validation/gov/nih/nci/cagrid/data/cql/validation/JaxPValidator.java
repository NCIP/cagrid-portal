package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;


/** 
 *  CQLValidator
 *  Validates a CQL query document against the CQL schema
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 16, 2006 
 * @version $Id$ 
 */
public class JaxPValidator extends CQLValidator {
	private static DomainModelValidator domainValidator = null;
	
	private SchemaValidator validator;
	private QName queryQname;
	
	public JaxPValidator(String xsdFilename) throws SchemaValidationException {
		validator = new SchemaValidator(xsdFilename);
		queryQname = new QName(DataServiceConstants.CQL_QUERY_URI, "CQLQuery");
	}
	
	
	public void validateStructure(CQLQuery query) throws MalformedQueryException {
		// have to convert the query back to XML to be handed off to the schema validator
		StringWriter objectWriter = new StringWriter();
		try {
			FileInputStream configStream = new FileInputStream(new File("client-config.wsdd"));
			Utils.serializeObject(query, queryQname, objectWriter, configStream);
		} catch (Exception ex) {
			throw new MalformedQueryException("Error serializing the query: " + ex.getMessage(), ex);
		}
		String xmlText = objectWriter.getBuffer().toString();
		try {
			validator.validate(xmlText);
		} catch (SchemaValidationException ex) {
			throw new MalformedQueryException(ex.getMessage(), ex);
		}
	}
	
	
	public void validateDomain(CQLQuery query, DomainModel model) throws MalformedQueryException {
		if (domainValidator == null) {
			domainValidator = new DomainModelValidator();
		}
		domainValidator.validateDomain(query, model);
	}
}
