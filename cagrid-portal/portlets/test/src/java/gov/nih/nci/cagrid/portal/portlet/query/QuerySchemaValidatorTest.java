package gov.nih.nci.cagrid.portal.portlet.query;

import org.junit.Test;
import org.junit.Before;
import org.springframework.validation.Errors;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import gov.nih.nci.cagrid.portal.PortalTestUtils;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQuerySchemaValidator;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.query.shared.XMLSchemaValidatorFactory;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class QuerySchemaValidatorTest {


    CQLQuerySchemaValidator validator;

    @Before
    public void setup() throws Exception{
        validator = new CQLQuerySchemaValidator();
        validator.setCqlXMLSchemaValidator(XMLSchemaValidatorFactory.initialize("1_gov.nih.nci.cagrid.CQLQuery-1.3.xsd"));
        validator.setDcqlXMLSchemaValidator(XMLSchemaValidatorFactory.initialize("Distributed_CQL_schema_2.0.xsd"));
    }

    @Test
    public void fail1() throws Exception {
        CQLQueryCommand query = new CQLQueryCommand();
        query.setCqlQuery("invalid");

        Errors mockErrors = mock(Errors.class);
        validator.validate(query, mockErrors);

        verify(mockErrors).rejectValue(anyString(), anyString(), anyString());
        verifyNoMoreInteractions(mockErrors);

    }

    @Test
    public void cql() throws Exception {
        String cqlQuery = PortalTestUtils.readFileASString("test/data/sampleCQL1.xml");

        CQLQueryCommand query = new CQLQueryCommand();
        query.setCqlQuery(cqlQuery);

        Errors mockErrors = mock(Errors.class);
        validator.validate(query, mockErrors);

        verifyNoMoreInteractions(mockErrors);
    }

    @Test
    public void cqlPredicate() throws Exception {
        String cqlQuery = PortalTestUtils.readFileASString("test/data/sampleCQLPredicate.xml");

        CQLQueryCommand query = new CQLQueryCommand();
        query.setCqlQuery(cqlQuery);

        Errors mockErrors = mock(Errors.class);
        validator.validate(query, mockErrors);

        //should not validate predicate with no value
        verify(mockErrors).rejectValue(anyString(), anyString(), anyString());
        verifyNoMoreInteractions(mockErrors);
    }



    public void cqlNotNull() throws Exception {
        String cqlQuery = PortalTestUtils.readFileASString("test/data/sampleCQLNotNull.xml");

        CQLQueryCommand query = new CQLQueryCommand();
        query.setCqlQuery(cqlQuery);

        Errors mockErrors = mock(Errors.class);
        validator.validate(query, mockErrors);

        verifyNoMoreInteractions(mockErrors);
    }

    @Test
    public void dcql() throws Exception {
        String dcqlQuery = PortalTestUtils.readFileASString("test/data/sampleDCQL1.xml");

        CQLQueryCommand query = new CQLQueryCommand();
        query.setCqlQuery(dcqlQuery);

        Errors mockErrors = mock(Errors.class);
        validator.validate(query, mockErrors);

        verifyNoMoreInteractions(mockErrors);
    }
}

