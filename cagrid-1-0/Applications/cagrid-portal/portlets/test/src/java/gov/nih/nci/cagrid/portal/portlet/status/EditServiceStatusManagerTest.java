package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.LiferayUser;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EditServiceStatusManagerTest extends TestCase {
    GridServiceDao _mockDao;
    EditServiceStatusManager manager;
    GridService service;

    @Override
    protected void setUp() throws Exception {
        _mockDao = mock(GridServiceDao.class);
        DiscoveryModel _mockModel = mock(DiscoveryModel.class);
        doReturn(mock(LiferayUser.class)).when(_mockModel).getLiferayUser();

        manager = new EditServiceStatusManager();
        manager.setGridServiceDao(_mockDao);
        manager.setDiscoveryModel(_mockModel);

        service = new GridService();
        doReturn(service).when(_mockDao).getById(anyInt());
    }


    public void testbanUnbanService(){
        manager.banUnbanService(1);

        verify(_mockDao, times(1)).banService(new GridService());

        StatusChange sc =new StatusChange();
        sc.setStatus(ServiceStatus.BANNED);

        service.getStatusHistory().add(sc);
        manager.banUnbanService(1);
        verify(_mockDao, times(1)).unbanService(new GridService());

    }

    public void testReloadMetadata(){
 
        service.setMetadataHash("xyz");
        doReturn(service).when(_mockDao).getById(anyInt());

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GridService savedGridService = (GridService) args[0];
                assertNull(savedGridService.getMetadataHash());
                return null;
            }
        }).when(_mockDao).save(new GridService());


        assertTrue(manager.reloadMetadata(1));
        verify(_mockDao,times(1)).save(new GridService());
    }


}
