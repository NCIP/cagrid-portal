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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
				l ( " in sync ");
			   logger.info("************************\nSummary queries background tasks started " + new java.util.Date());
		       for (SummaryQueryWithLocations currentQueryWithLocations : queries) {
		    	   String cql = currentQueryWithLocations.getQuery();
   		    	   List<GridServiceEndPointCatalogEntry> endPoints = sharedQueryCatalogEntryManagerFacade.getAvailableEndpoints(cql);
		    	  
		    	  for (GridServiceEndPointCatalogEntry endPoint : endPoints) {
		    		  l("url " + endPoint.getId() + " > " + endPoint.getAbout().getUrl());
		    		  
		    		   if (endPoint.isData()) {
		    			  
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
		 	                        
		 	                   GridServiceUmlClass gridServiceUmlClass =gridServiceUmlClassDao.getByGridServiceAndUmlClass(service.getId(), resultClass.getId());
		 	                   if (gridServiceUmlClass == null) {
			 	                   gridServiceUmlClass = new GridServiceUmlClass();
			 	                   gridServiceUmlClass.setUmlClass(resultClass);
			 	                   gridServiceUmlClass.setGridService(service);	 	                	   
		 	                   } 
		 	                   gridServiceUmlClass.setObjectCount(count.intValue());
		 	                   gridServiceUmlClassDao.save(gridServiceUmlClass);
		    			   } catch (Exception e) {
		    				   e.printStackTrace();
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
