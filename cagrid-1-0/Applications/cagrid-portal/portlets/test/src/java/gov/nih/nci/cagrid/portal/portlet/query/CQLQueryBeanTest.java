package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.PortletIntegrationTestBase;
import gov.nih.nci.cagrid.portal.portlet.query.builder.AggregateTargetsCommand;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CQLQueryBeanTest extends PortletIntegrationTestBase {


    public void testBean() throws Exception {
        CQLQueryBean bean = (CQLQueryBean) getApplicationContext().getBean("cqlQueryBeanPrototype");

        UMLClass mockUmlClass = mock(UMLClass.class);
        bean.setUmlClass(mockUmlClass);

        assertNotNull(bean.toXml());

        AggregateTargetsCommand targets = new AggregateTargetsCommand();
        targets.getSelected().add("http://service1.org");
        targets.getSelected().add("http://service2.org");

        bean.setAggregateTargets(targets);

        UMLClass _mockUMLClass = mock(UMLClass.class);
        DomainModel _mockDomain = mock(DomainModel.class);
        GridDataService _mockService = mock(GridDataService.class);
        stub(_mockUMLClass.getModel()).toReturn(_mockDomain);
        stub(_mockDomain.getService()).toReturn(_mockService);
        stub(_mockService.getUrl()).toReturn("http://service");

        bean.setUmlClass(_mockUMLClass);

        assertNotNull("Query cannot be formed", bean.toXml());

    }
}
