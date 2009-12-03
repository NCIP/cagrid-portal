package gov.nih.nci.cagrid.portal.portlet.browse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstanceState;
import gov.nih.nci.cagrid.portal.portlet.browse.sharedQuery.QueryExecutionManager;
import gov.nih.nci.cagrid.portal.portlet.query.QueryService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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

        QueryService mockModel = mock(QueryService.class);
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
