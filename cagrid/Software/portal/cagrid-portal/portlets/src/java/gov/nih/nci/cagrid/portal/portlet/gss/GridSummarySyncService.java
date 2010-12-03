package gov.nih.nci.cagrid.portal.portlet.gss;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceUmlClassDao;
import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.GridServiceUmlClass;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.browse.sharedQuery.SharedQueryCatalogEntryManagerFacade;
import gov.nih.nci.cagrid.portal.portlet.map.ajax.CachedMap;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Timer task to iterated thru configured queries and populate GridServiceUmlClass
 * @author akkalas srini.akkala@semanticbits.com
 *
 */
public class GridSummarySyncService {

	private SharedQueryCatalogEntryManagerFacade sharedQueryCatalogEntryManagerFacade ;

    private GridServiceUmlClassDao gridServiceUmlClassDao;

    private GridServiceDao gridServiceDao;

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
    	   String umlClassName = PortletUtils.getTargetUMLClassName(cql);
    	   logger.debug(" Class Name :" + PortletUtils.getTargetUMLClassName(cql));
    	   logger.debug(" # of available end points :" + endPoints.size());

           System.out.println(" custom url for " +currentQueryWithLocations.getCaption()+ ".... : " + currentQueryWithLocations.getUrl() );


    	   if (!StringUtils.isEmpty(currentQueryWithLocations.getUrl())) {
    		   // query provided url
    		   queryCustomUrl(currentQueryWithLocations.getUrl() , currentQueryWithLocations, umlClassName);
    	   } else {
    		   // auto discovery ..
    		   autoDiscovery (endPoints , currentQueryWithLocations , umlClassName );
    	   }
    	   cachedMap.refreshCache();
    	   logger.info("************************\nSummary queries background tasks end " + new java.util.Date());
       }
	}
	private void queryCustomUrl(String url , SummaryQueryWithLocations currentQueryWithLocations , String umlClassName){
	    System.out.println("Querying custom url .... : " + url );
        try {
		  GridService service = gridServiceDao.getByUrl(url);
		  if (service == null) {
			  logger.error(" Service Entry for provided url not found :" + url);
			  return;
		  }
		  GridServiceEndPointCatalogEntry endPoint = service.getCatalog();
		  if (endPoint == null) {
			  logger.error(" Service is not associated with catalog :" + service.getId() +">"+url);
			  return;
		  }

		  if (endPoint.isData() && !endPoint.isHidden()) {
			  UMLClass umlClass = getUMLClass(umlClassName,service.getId());
		      if (umlClass == null ) {
		       	 return;
		       }
	           int count = executeQuery (currentQueryWithLocations.getCqlQuery() , service.getUrl());
	           if (count == 0) {
	        	   return;
	           }
	          saveResults(service , umlClass , currentQueryWithLocations.getCaption(), count);
		  }
	   } catch (Exception e) {
		   logger.warn("Could not query service at " + e.getMessage());
	   }
	}
    private void autoDiscovery(List<GridServiceEndPointCatalogEntry> endPoints , SummaryQueryWithLocations currentQueryWithLocations , String umlClassName  ) {
  	  for (GridServiceEndPointCatalogEntry endPoint : endPoints) {
		   if (endPoint.isData() && !endPoint.isHidden()) {
			   logger.debug("Processing end point ID :" + endPoint.getId() + " , Grid Service ID : " + endPoint.getAbout().getId()+ " , URL : " + endPoint.getAbout().getUrl());

			   try {
				  GridService service = endPoint.getAbout();
    			  UMLClass umlClass = getUMLClass(umlClassName,service.getId());
    		      if (umlClass == null ) {
    		       	 return;
    		       }
                   int count = executeQuery (currentQueryWithLocations.getCqlQuery() , service.getUrl());
                   if (count == 0) {
                	   continue;
                   }
                  saveResults(service , umlClass , currentQueryWithLocations.getCaption(), count);
			   } catch (Exception e) {
				   logger.warn("Could not query service at " + e.getMessage());
			   }

		   }
	   }
   }

	private int executeQuery (CQLQuery cql , String serviceUrl) throws Exception {
		 DataServiceClient client = new DataServiceClient(serviceUrl);
         CQLQueryResults result = client.query(cql);
         Long count = result.getCountResult().getCount();
         return count.intValue();
	}

	private UMLClass getUMLClass(String umlClassName,int serviceId) {
		return umlClassDao.getClassInGivenService(umlClassName,serviceId);
	}

    @Transactional
	private void saveResults(GridService service , UMLClass umlClass, String caption, int count) {
         GridServiceUmlClass gridServiceUmlClass =gridServiceUmlClassDao.getByGridServiceAndUmlClass(service.getId(), umlClass.getId());
          if (gridServiceUmlClass == null) {
              gridServiceUmlClass = new GridServiceUmlClass();
              gridServiceUmlClass.setUmlClass(umlClass);
              gridServiceUmlClass.setGridService(service);
              gridServiceUmlClass.setCaption(caption);
          }
          gridServiceUmlClass.setObjectCount(count);
          gridServiceUmlClassDao.save(gridServiceUmlClass);
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

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

}
