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
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.builder.AggregateTargetsCommand;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CQLQueryBeanTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-portlets-test.xml",
        };
    }


    public void testBean() throws Exception {

        final UMLClass _mockUMLClass = mock(UMLClass.class);
        DomainModel _mockDomain = mock(DomainModel.class);
        GridDataService _mockService = mock(GridDataService.class);

        stub(_mockUMLClass.getModel()).toReturn(_mockDomain);
        stub(_mockDomain.getService()).toReturn(_mockService);

        CQLQueryBean bean = new CQLQueryBean() {

            @Override
            public UMLClass getUmlClass() {
                return _mockUMLClass;
            }


        };

        CQLQueryBean prototypeBean = (CQLQueryBean) getApplicationContext().getBean("cqlQueryBeanPrototype");
        bean.setFormulators(prototypeBean.getFormulators());

        assertNotNull(bean.toXml());

        AggregateTargetsCommand targets = new AggregateTargetsCommand();
        targets.getSelected().add("http://service1.org");
        targets.getSelected().add("http://service2.org");

        bean.setAggregateTargets(targets);

        stub(_mockService.getUrl()).toReturn("http://service");


        assertNotNull("Query cannot be formed", bean.toXml());

    }
}
