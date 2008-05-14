package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.builder.AggregateTargetsCommand;
import gov.nih.nci.cagrid.portal.portlet.PortletIntegrationTestBase;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;


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

        System.out.println(bean.toXml());

        assertNotNull(bean.toXml());

    }
}
