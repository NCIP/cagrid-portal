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
package gov.nih.nci.cagrid.portal.portlet.map.ajax;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceUmlClassDao;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		StringBuffer sb = new StringBuffer();
        String divS = "<div class='gss_line'>";
        String divE = "</div>";
        String spanS = "<span>";
        String spanE = "</span>";
		
		// get captions 
		List<String> captions = gridServiceUmlClassDao.getUniqueCaptions();
		Map<Object,Set> catalogIdsMap = gridServiceUmlClassDao.getCatalogIdsGroupedByClassName();
		for  (String caption:captions) {			
			Map classCountMap = gridServiceUmlClassDao.getAggregatedClassCountByCaption(caption);
			Iterator it = classCountMap.entrySet().iterator();
			if(classCountMap.size()>0){
				sb.append("<div class='gss_section'>"+caption+" Statistics:</div>");
			}
	        // set up classes by caption . 
	        while (it.hasNext()) {
	        	Map.Entry ps = (Map.Entry)it.next();
	        	String className = ps.getKey().toString();
	        	String count = ps.getValue().toString();
	        	String keyTogetCatIds = className+"_"+caption;
	        	sb.append(divS);
                sb.append(spanS);
                String catIds = "";
                Iterator catalogIds = ((Set)catalogIdsMap.get(keyTogetCatIds)).iterator();
                while(catalogIds.hasNext()) {
                	catIds = catIds + catalogIds.next() + ",";
            	}
                int li = catIds.lastIndexOf(',');
                catIds = removeCharAt(catIds,li);
                sb.append("<a href=\"javascript:selectItemsForCounts('"+catIds+"','SERVICE','"+className+"')\"> " + "# of "+className+"s" + "</a>:");
                sb.append(spanE);
                sb.append(spanS);
                sb.append(count);
                sb.append(spanE);
                sb.append(divE);
	        }
		}
		//System.out.println(sb.toString());
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

