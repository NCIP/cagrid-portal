package gov.nih.nci.cagrid.portal.portlet.map.ajax;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.portlet.FilteredContentGenerator;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectoryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectoryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.MapBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean that caches the map of
 * all the services. Configured as a
 * singleton bean.
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CachedMap<E extends Enum> extends FilteredContentGenerator {

    private GridServiceDao gridServiceDao;
    private ParticipantDao participantDao;
    private SummaryBean summary;

    private Map<Enum, MapBean> cachedServiceMap = Collections.synchronizedMap(new HashMap<Enum, MapBean>());


    public CachedMap() {
        summary = new SummaryBean();
    }

    public MapBean get(E type) {
        if (cachedServiceMap.containsKey(type))
            return cachedServiceMap.get(type);
        else
            return createMap();

    }

    @Transactional
    public void refreshCache() {
        logger.debug("Refreshing cached Map");

        // will renew on each refresh
        MapBean _defaulMap = createMap();
        MapBean _allMap = createMap();
        MapBean _dsMap = createMap();
        MapBean _anMap = createMap();
        MapBean _pMap = createMap();

        List<GridService> _aServices = getFilter().filter(getGridServiceDao().getAllAnalyticalServices());
        summary.setAnalyticalServices(_aServices.size());
        for (GridService service : _aServices) {
            _defaulMap.addService(service);
            _allMap.addService(service);
            _anMap.addService(service);
        }

        List<GridService> _dServices = getFilter().filter(getGridServiceDao().getAllDataServices());
        summary.setDataServices(_dServices.size());
        for (GridService service : _dServices) {
            _defaulMap.addService(service);
            _allMap.addService(service);
            _dsMap.addService(service);
        }

        List<Participant> _pList = getParticipantDao().getAll();
        summary.setParticipants(_pList.size());
        for (Participant participant : _pList) {
            _defaulMap.addParticipant(participant);
            _pMap.addParticipant(participant);
        }

        cachedServiceMap.put(null, _defaulMap);
        cachedServiceMap.put(ParticipantDirectoryType.ALL, _pMap);
        cachedServiceMap.put(ServiceDirectoryType.ALL, _allMap);
        cachedServiceMap.put(ServiceDirectoryType.ANALYTICAL, _anMap);
        cachedServiceMap.put(ServiceDirectoryType.DATA, _dsMap);

        logger.debug("Cached Map refreshed");
    }

    private MapBean createMap() {
        return (MapBean) getApplicationContext().getBean("mapBeanPrototype");
    }

    public SummaryBean getSummary() {
        return summary;
    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public ParticipantDao getParticipantDao() {
        return participantDao;
    }

    public void setParticipantDao(ParticipantDao participantDao) {
        this.participantDao = participantDao;
    }
}
