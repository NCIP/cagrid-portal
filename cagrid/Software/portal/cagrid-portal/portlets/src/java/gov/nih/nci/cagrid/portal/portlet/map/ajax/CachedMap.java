package gov.nih.nci.cagrid.portal.portlet.map.ajax;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceUmlClassDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.GridServiceUmlClass;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.portlet.FilteredContentGenerator;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectoryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectoryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.MapBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

/**
 * Bean that caches the map of
 * all the services. Configured as a
 * singleton bean.
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 * @author akkalas srini.akkala@semanticbits.com
 */
public class CachedMap<E extends Enum> extends FilteredContentGenerator {

    private GridServiceDao gridServiceDao;
    private ParticipantDao participantDao;
    private SummaryBean summary;
    private GridServiceUmlClassDao gridServiceUmlClassDao;

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
    
    public static String removeCharAt(String s, int pos) {
    	   return s.substring(0,pos)+s.substring(pos+1);
    }
    
    /**
     * builds html to show summeries on home page (summeries/view.jsp)
     * generated html sample 
	 *	<div class='gss_section'>caArray Statistics:</div>  
	 * 		<div class='gss_line'><span><a href="javascript:selectItemsForCounts('767','SERVICE','Array')"> # of Arrays throughout caBIG¨ : </a></span><span>15</span></div>
	 *	    <div class='gss_line'><span><a href="javascript:selectItemsForCounts('767','SERVICE','Experiment')"> # of Experiments throughout caBIG¨ : </a></span><span>3</span></div>
	 *  <div class='gss_section'>caBIO Statistics:</div>
	 *       <div class='gss_line'><span><a href="javascript:selectItemsForCounts('769','SERVICE','Pathway')"> # of Pathways throughout caBIG¨ : </a></span><span>1777</span></div>
	 *       <div class='gss_line'><span><a href="javascript:selectItemsForCounts('769','SERVICE','Gene')"> # of Genes throughout caBIG¨ : </a></span><span>259071</span></div>
	 *	----
     */
	public String getClassCountHtml() {
		// category map , key is the caption (caArray , caBIO etc ) .
		// for each ctegory we store list uml class info (ClassCounter) . 
		Map<String,List<ClassCounter>> groupByCaptionMap = new HashMap<String,List<ClassCounter>>();		
		// class name is the key , details( name , count , caption etc ) are the value 
		Map<String,ClassCounter> classCountMap = new HashMap<String,ClassCounter>();
		
		List<GridServiceUmlClass> gridServiceUmlClassList = gridServiceUmlClassDao.getAll();
		
		// what catelogs does each class belong to .
		Map<String,Set> classCatalogMap = new HashMap<String,Set>();
		
		for (GridServiceUmlClass gridServiceUmlClass:gridServiceUmlClassList) {
			// get counts for each class per service and build the map
			int count = gridServiceUmlClass.getObjectCount();
			String className = gridServiceUmlClass.getUmlClass().getClassName();
			Object obj = classCountMap.get(className);
			if (obj == null) {
				ClassCounter cc = new ClassCounter();
				cc.setClassName(className);
				cc.setCount(count);
				cc.setCaption(gridServiceUmlClass.getCaption());
				classCountMap.put(className, cc);
			} else {
				ClassCounter cc = (ClassCounter)obj;
				cc.setCount(cc.getCount()+count);
				classCountMap.put(className, cc);
			}
			
			// build catelog ids for each class . 
			Integer catalogId = gridServiceUmlClass.getGridService().getCatalog().getId();
			Object cat = classCatalogMap.get(className);
			if (cat == null) {
				Set s = new HashSet();
				s.add(catalogId);
				classCatalogMap.put(className, s);
			} else {
				Set s = (Set)cat;
				s.add(catalogId);
				classCatalogMap.put(className, s);
			}

		}
        
        Iterator it = classCountMap.entrySet().iterator();
        // set up classes by caption . 
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            ClassCounter cc = (ClassCounter)pairs.getValue();
            Object obj = groupByCaptionMap.get(cc.getCaption());
            if (obj == null) {
            	List<ClassCounter> list = new ArrayList<ClassCounter>();
            	list.add(cc);
            	groupByCaptionMap.put(cc.getCaption(), list);
            } else {
            	List<ClassCounter> list = (List<ClassCounter>)obj;
            	list.add(cc);
            	groupByCaptionMap.put(cc.getCaption(), list);
            }
        }
        
        // build html 
        StringBuffer sb = new StringBuffer();
        Iterator catItr = groupByCaptionMap.entrySet().iterator();
        String divS = "<div class='gss_line'>";
        String divE = "</div>";
        String spanS = "<span>";
        String spanE = "</span>";
        while (catItr.hasNext()) {
            Map.Entry pairs = (Map.Entry)catItr.next();
            List<ClassCounter> list = (List<ClassCounter>)groupByCaptionMap.get(pairs.getKey());
            sb.append("<div class='gss_section'>"+pairs.getKey()+" Statistics:</div>");
            for (ClassCounter cc:list) {
            	sb.append(divS);
                sb.append(spanS);
                Iterator catalogIds = ((Set)classCatalogMap.get(cc.getClassName())).iterator();
                String catIds = "";
                while(catalogIds.hasNext()) {
                	catIds = catIds + catalogIds.next() + ",";
            	}
                int li = catIds.lastIndexOf(',');
                catIds = removeCharAt(catIds,li);
                
                sb.append("<a href=\"javascript:selectItemsForCounts('"+catIds+"','SERVICE','"+cc.getClassName()+"')\"> " + "# of "+cc.getClassName()+"s throughout caBIG¨ : " + "</a>");
                sb.append(spanE);
                sb.append(spanS);
                sb.append(cc.getCount());
                sb.append(spanE);
                sb.append(divE);
                
            }
        }

        return sb.toString();
        
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
        

        summary.setClassCountStats(getClassCountHtml());
        
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

	public void setGridServiceUmlClassDao(
			GridServiceUmlClassDao gridServiceUmlClassDao) {
		this.gridServiceUmlClassDao = gridServiceUmlClassDao;
	}

}

