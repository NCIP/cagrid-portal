package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.portlet.query.QueryModel;
import gov.nih.nci.cagrid.portal.portlet.browse.sharedQuery.QueryExecutionManager;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.Query;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstanceState;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class QueryExecutionManagerTest {

    QueryExecutionManager manager;
    int id = -1;

    @Before
    public void setup() {
        manager = new QueryExecutionManager();

        CQLQueryInstance mockQueryInstance = mock(CQLQueryInstance.class);
        when(mockQueryInstance.getId()).thenReturn(id);
        when(mockQueryInstance.getState()).thenReturn(QueryInstanceState.COMPLETE);

        List<QueryInstance> instanceList = new ArrayList<QueryInstance>();
        instanceList.add(mockQueryInstance);
        instanceList.add(mockQueryInstance);
        instanceList.add(mockQueryInstance);

        QueryModel mockModel = mock(QueryModel.class);
        when(mockModel.getSubmittedQueries()).thenReturn(instanceList);
//        manager.setQueryModel(mockModel);

    }

    @Test
    public void findInstance() {
//        assertNotNull(manager.loadInstance(String.valueOf(id)));
    }

    @Test
    public void validate() {
//        assertNull(manager.validate());

    }
}
