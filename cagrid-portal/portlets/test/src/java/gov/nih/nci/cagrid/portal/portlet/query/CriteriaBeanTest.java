/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.portlet.query.cql.AssociationBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriteriaBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriterionBean;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CriteriaBeanTest {


    @Test
    public void testFindNested() {
        CriteriaBean bean = new CriteriaBean();

        String path = "some/path";

        final AssociationBean mockAssociation = mock(AssociationBean.class);
        bean.setAssociations(new HashSet<AssociationBean>() {
            {
                add(mockAssociation);
            }
        });


        CriteriaBean mockCriteria = mock(CriteriaBean.class);

        when(mockAssociation.getCriteriaBean()).thenReturn(mockCriteria);
        when(mockCriteria.find(anyString())).thenReturn(mock(CriterionBean.class));

        assertNotNull(bean.find(path));

    }

    @Test
    public void testFindSimple() {
        CriteriaBean bean = new CriteriaBean();

        String path = "path";

        final CriterionBean mockCriterion = mock(CriterionBean.class);
        bean.setCriteria(new HashSet<CriterionBean>() {
            {
                add(mockCriterion);
            }
        });


        UMLAttribute mockAttr = mock(UMLAttribute.class);
        when(mockCriterion.getUmlAttribute()).thenReturn(mockAttr);
        when(mockAttr.getName()).thenReturn(path);

        assertNotNull(bean.find(path));


    }

    @Test
    public void testUpdate() {
        CriteriaBean bean = new CriteriaBean();
        String path = "path";

        final CriterionBean mockCriterion = mock(CriterionBean.class);
        Set<CriterionBean> crits = new HashSet<CriterionBean>() {
            {
                add(mockCriterion);
            }
        };

        bean.setCriteria(crits);

        bean.update(path, mockCriterion, false);

        assertEquals("Criteria should have been added", crits.size(), 1);


        path = "some/path";

        final AssociationBean mockAssociation = mock(AssociationBean.class);
        bean.setAssociations(new HashSet<AssociationBean>() {
            {
                add(mockAssociation);
            }
        });

        when(mockAssociation.getRoleName()).thenReturn("path");
        when(mockAssociation.getCriteriaBean()).thenReturn(bean);
//             bean.update(path,mockCriterion,false);


    }
}
