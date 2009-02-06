package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.portlet.FilteredContentGenerator;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectoryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.MapBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
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
public class CachedMap extends FilteredContentGenerator {

    private GridServiceDao gridServiceDao;
    private ParticipantDao participantDao;

    private Map<ServiceDirectoryType, MapBean> cachedServiceMap = Collections.synchronizedMap(new HashMap<ServiceDirectoryType, MapBean>());


    public MapBean get(ServiceDirectoryType type) {
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

        for (GridService service : getFilter().filter(getGridServiceDao().getAllAnalyticalServices())) {
            _defaulMap.addService(service);
            _allMap.addService(service);
            _anMap.addService(service);
        }
        for (GridService service : getFilter().filter(getGridServiceDao().getAllDataServices())) {
            _defaulMap.addService(service);
            _allMap.addService(service);
            _dsMap.addService(service);
        }

        for (Participant participant : getParticipantDao().getAll())
            _defaulMap.addParticipant(participant);

        cachedServiceMap.put(null, _defaulMap);
        cachedServiceMap.put(ServiceDirectoryType.ALL, _allMap);
        cachedServiceMap.put(ServiceDirectoryType.ANALYTICAL, _anMap);
        cachedServiceMap.put(ServiceDirectoryType.DATA, _dsMap);

        logger.debug("Cached Map refreshed");
    }

    private MapBean createMap() {
        return (MapBean) getApplicationContext().getBean("mapBeanPrototype");
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
