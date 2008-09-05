package gov.nih.nci.cagrid.portal.portlet.diagnostics;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.portlet.discovery.filter.ServiceFilter;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "portalStatusDiagnostic",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "portalStatusDiagnosticService"))

public class PortalStatusDiagnosticService extends AbstractDiagnosticService {

    private GridServiceDao gridServiceDao;
    private ServiceFilter serviceFilter;


    public DiagnosticResult diagnoseInternal(String Url) {
        DiagnosticResult _result = new DiagnosticResult(DiagnosticType.STATUS, DiagnosticResultStatus.FAILED);
        try {
            GridService _service = gridServiceDao.getByUrl(Url);
            // this will throw exception if _service is null           
            _result.setDetail("Service is in " + _service.getCurrentStatus() + " status");

//            if(!serviceFilter.willBeFiltered(_service)){
            _result.setStatus(DiagnosticResultStatus.PASSED);
//            }


        } catch (Exception e) {
            _result.setDetail("Service not in Portal");
        }
        return _result;
    }


    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public ServiceFilter getServiceFilter() {
        return serviceFilter;
    }

    public void setServiceFilter(ServiceFilter serviceFilter) {
        this.serviceFilter = serviceFilter;
    }
}