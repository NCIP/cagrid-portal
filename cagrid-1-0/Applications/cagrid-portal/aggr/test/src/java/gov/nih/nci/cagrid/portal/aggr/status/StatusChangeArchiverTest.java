package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class StatusChangeArchiverTest extends TestCase {

    @MockitoAnnotations.Mock
    GridServiceDao _mockDao;
    final GridService service = new GridService();
    List<GridService> services;
    StatusChangeArchiver archiver;

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        services = new ArrayList<GridService>();
        services.add(service);
        doReturn(services).when(_mockDao).getAll();

        archiver = new StatusChangeArchiver();
        archiver.setGridServiceDao(_mockDao);
    }

    public void testDoArchiveWithNullStatus() {

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GridService savedGridService = (GridService) args[0];
                assertEquals(service.getCurrentStatus(), savedGridService.getCurrentStatus());
                assertEquals(savedGridService.getStatusHistory().size(), 0);
                return null;
            }
        }).when(_mockDao).save(new GridService());

        StatusChangeArchiver archiver = new StatusChangeArchiver();
        archiver.setGridServiceDao(_mockDao);

        try {
            archiver.doArchive();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        verify(_mockDao, times(1)).save(new GridService());
    }

    public void testDoArhiveWithDormanStatus() {
        service.getStatusHistory().add(getStatus(ServiceStatus.DORMANT));

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GridService savedGridService = (GridService) args[0];
                assertEquals(service.getCurrentStatus(), savedGridService.getCurrentStatus());
                assertEquals(savedGridService.getCurrentStatus(), ServiceStatus.DORMANT);
                assertArchived(0, savedGridService);
                return null;
            }
        }).when(_mockDao).save(new GridService());


        try {
            archiver.doArchive();
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    public void testDoArhviveDormant2() {
        service.getStatusHistory().add(getStatus(ServiceStatus.ACTIVE));
        service.getStatusHistory().add(getStatus(ServiceStatus.DORMANT));

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GridService savedGridService = (GridService) args[0];
                assertEquals(service.getCurrentStatus(), savedGridService.getCurrentStatus());
                assertEquals(savedGridService.getCurrentStatus(), ServiceStatus.DORMANT);
                assertArchived(0, savedGridService);
                return null;
            }
        }).when(_mockDao).save(new GridService());

        try {
            archiver.doArchive();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testDoArhiveDormant3() {

        service.getStatusHistory().add(getStatus(ServiceStatus.ACTIVE));
        service.getStatusHistory().add(getStatus(ServiceStatus.DORMANT));
        service.getStatusHistory().add(getStatus(ServiceStatus.ACTIVE));

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GridService savedGridService = (GridService) args[0];
                assertEquals(service.getCurrentStatus(), savedGridService.getCurrentStatus());
                assertEquals(savedGridService.getCurrentStatus(), ServiceStatus.ACTIVE);
                assertArchived(2, savedGridService);
                return null;
            }
        }).when(_mockDao).save(new GridService());

        try {
            archiver.doArchive();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testDoArhiveNewService() {
        service.getStatusHistory().add(getStatus(ServiceStatus.UNKNOWN));
        service.getStatusHistory().add(getStatus(ServiceStatus.INACTIVE));
        service.getStatusHistory().add(getStatus(ServiceStatus.INACTIVE));

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GridService savedGridService = (GridService) args[0];
                assertEquals(service.getCurrentStatus(), savedGridService.getCurrentStatus());
                assertEquals(savedGridService.getCurrentStatus(), ServiceStatus.INACTIVE);
                assertArchived(0, savedGridService);
                return null;
            }
        }).when(_mockDao).save(new GridService());

        try {
            archiver.doArchive();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private StatusChange getStatus(ServiceStatus s) {
        StatusChange change = new StatusChange();
        change.setStatus(s);
        return change;

    }

    private void assertArchived(int i, GridService service) {
        int count = 0;
        for (StatusChange history : service.getStatusHistory()) {
            if (history.isArchived())
                count++;
        }
        assertEquals("Unexpeceted # of status' are archived", i, count);
    }

    @Override
    protected void tearDown() throws Exception {
        verify(_mockDao, times(1)).save(new GridService());
        service.getStatusHistory().clear();

    }
}
