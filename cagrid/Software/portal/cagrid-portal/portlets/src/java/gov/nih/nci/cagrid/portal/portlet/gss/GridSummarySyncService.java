package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.portal.dao.GridServiceUmlClassDao;
import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.GridServiceUmlClass;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.browse.sharedQuery.SharedQueryCatalogEntryManagerFacade;
import gov.nih.nci.cagrid.portal.portlet.map.ajax.CachedMap;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Timer task to iterated thru configured queries and populate GridServiceUmlClass
 * @author akkalas srini.akkala@semanticbits.com
 *
 */
public class GridSummarySyncService {
	
	private SharedQueryCatalogEntryManagerFacade sharedQueryCatalogEntryManagerFacade ;
    
    private GridServiceUmlClassDao gridServiceUmlClassDao;
    
    private UMLClassDao umlClassDao;
    
    private List<SummaryQueryWithLocations> queries;
    
    private final Log logger = LogFactory.getLog(getClass());
    
    private CachedMap cachedMap;
    

	private void l(Object o) {
        System.out.println(o);
    }
    
	public void sync() {
			   logger.info("************************\nSummary queries background tasks started " + new java.util.Date());
		       for (SummaryQueryWithLocations currentQueryWithLocations : queries) {
		    	   String cql = currentQueryWithLocations.getQuery();
   		    	   List<GridServiceEndPointCatalogEntry> endPoints = sharedQueryCatalogEntryManagerFacade.getAvailableEndpoints(cql);
   		    	   logger.debug(" Class Name :" + PortletUtils.getTargetUMLClassName(cql));
		    	   logger.debug(" # of available end points :" + endPoints.size());
		    	   
		    	  for (GridServiceEndPointCatalogEntry endPoint : endPoints) {
		    		  
		    		   if (endPoint.isData() && !endPoint.isHidden()) {
		    			   logger.debug("Processing end point ID :" + endPoint.getId() + " , Grid Service ID : " + endPoint.getAbout().getId()+ " , URL : " + endPoint.getAbout().getUrl());

		    			   try {
		    				  String umlClassName = PortletUtils.getTargetUMLClassName(cql);
		    				  GridService service = endPoint.getAbout();
		 	    			  String serviceUrl = service.getUrl();
		 	    			  
		 	    			   UMLClass resultClass = umlClassDao.getClassInGivenService(umlClassName,service.getId());
		 	                   if (resultClass == null ) {
		 	                	   return;
		 	                   }
		 	                   
		 	                   DataServiceClient client = new DataServiceClient(serviceUrl);
		 	                   CQLQueryResults result = client.query(currentQueryWithLocations.getCqlQuery());
		 	                   Long count = result.getCountResult().getCount();
		 	                   if (count.intValue() == 0) {
		 	                	  continue;
		 	                   }
		 	                   GridServiceUmlClass gridServiceUmlClass =gridServiceUmlClassDao.getByGridServiceAndUmlClass(service.getId(), resultClass.getId());
		 	                   if (gridServiceUmlClass == null) {
			 	                   gridServiceUmlClass = new GridServiceUmlClass();
			 	                   gridServiceUmlClass.setUmlClass(resultClass);
			 	                   gridServiceUmlClass.setGridService(service);	 
			 	                   gridServiceUmlClass.setCaption(currentQueryWithLocations.getCaption());
		 	                   } 
		 	                   gridServiceUmlClass.setObjectCount(count.intValue());
		 	                   gridServiceUmlClassDao.save(gridServiceUmlClass);
		    			   } catch (Exception e) {
		    				   logger.warn("Could not query service at " + e.getMessage());
		    			   }
		    			   
		    		   }
		    	   }
		       }  
		       cachedMap.refreshCache();
		       logger.info("************************\nSummary queries background tasks end " + new java.util.Date());
	}

	public void setGridServiceUmlClassDao(
			GridServiceUmlClassDao gridServiceUmlClassDao) {
		this.gridServiceUmlClassDao = gridServiceUmlClassDao;
	}

	public void setSharedQueryCatalogEntryManagerFacade(
			SharedQueryCatalogEntryManagerFacade sharedQueryCatalogEntryManagerFacade) {
		this.sharedQueryCatalogEntryManagerFacade = sharedQueryCatalogEntryManagerFacade;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}
	
    public void setQueries(List<SummaryQueryWithLocations> queries) {
        this.queries = queries;
    }
    
    public void setCachedMap(CachedMap cachedMap) {
		this.cachedMap = cachedMap;
	}

}
