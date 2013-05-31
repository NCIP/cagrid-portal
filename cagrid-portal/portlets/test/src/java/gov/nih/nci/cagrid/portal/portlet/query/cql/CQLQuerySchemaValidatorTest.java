/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.portal.PortalTestUtils;
import gov.nih.nci.cagrid.portal.portlet.PortalPortletIntegrationTestBase;
import org.springframework.validation.Errors;

import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author kherm manav.kher@semanticbits.com
 */

public class CQLQuerySchemaValidatorTest extends PortalPortletIntegrationTestBase {


    private CQLQuerySchemaValidator cqlQuerySchemaValidator;
    private Errors mockErrors;


    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        cqlQuerySchemaValidator = (CQLQuerySchemaValidator) getApplicationContext().getBean("cqlQuerySchemaValidator");
        mockErrors = mock(Errors.class);

    }

    @Override
    protected void prepareTestInstance() throws Exception {
        super.prepareTestInstance();
    }


    public void testValidator() throws SchemaValidationException, IOException {
        CQLQueryCommand command = new CQLQueryCommand();
        command.setCqlQuery(PortalTestUtils.readFileASString("test/data/count_query.xml"));


        cqlQuerySchemaValidator.validate(command, mockErrors);
        cqlQuerySchemaValidator.validate(command, mockErrors);
        cqlQuerySchemaValidator.validate(command, mockErrors);
        cqlQuerySchemaValidator.validate(command, mockErrors);
        verifyZeroInteractions(mockErrors);

        command.setCqlQuery(PortalTestUtils.readFileASString("test/data/invalidCQL.xml"));
        cqlQuerySchemaValidator.validate(command, mockErrors);
        verify(mockErrors).rejectValue(anyString(), anyString(), anyString());
    }


}
