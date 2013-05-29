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


    @Transactional
	public void sync() {
	   logger.info("************************\nSummary queries background tasks started " + new java.util.Date());
       for (SummaryQueryWithLocations currentQueryWithLocations : queries) {
    	   String cql = currentQueryWithLocations.getQuery();

    	   String umlClassName = PortletUtils.getTargetUMLClassName(cql);
    	   logger.debug(" Class Name :" + PortletUtils.getTargetUMLClassName(cql));

           int count = 0 ;
           try {
               if (!StringUtils.isEmpty(currentQueryWithLocations.getUrl())) {
                   // query provided url
                   logger.debug(" using provided URL " +currentQueryWithLocations.getUrl());
                   GridService service = gridServiceDao.getByUrl(currentQueryWithLocations.getUrl());
                   if (service != null) {
                       count = executeQuery (service.getCatalog() , currentQueryWithLocations.getCqlQuery() );
                       UMLClass umlClass = getUMLClass(umlClassName,service.getId());
                       if (count != 0 && umlClass != null) {
                        saveResults(service , umlClass , currentQueryWithLocations.getCaption(), count);
                       }
                   }

               } else {
                   // auto discovery ..
                   logger.debug(" auto discovery ");
                   List<GridServiceEndPointCatalogEntry> endPoints = sharedQueryCatalogEntryManagerFacade.getAvailableEndpoints(cql);
                   logger.debug(" # of available end points :" + endPoints.size());
                   for (GridServiceEndPointCatalogEntry endPoint : endPoints) {
                     count = executeQuery (endPoint , currentQueryWithLocations.getCqlQuery() );
                     UMLClass umlClass = getUMLClass(umlClassName,endPoint.getAbout().getId());
                     if (count != 0 && umlClass != null) {
                       saveResults(endPoint.getAbout() , umlClass , currentQueryWithLocations.getCaption(), count);
                     }
                   }
               }
           } catch (Exception e) {
              e.printStackTrace();
           }
       }

       cachedMap.refreshCache();
   	   logger.info("************************\nSummary queries background tasks end " + new java.util.Date());
	}

    
    private int executeQuery(GridServiceEndPointCatalogEntry endPoint , CQLQuery cql  ) {
		int count = 0;
        if (endPoint.isData() && !endPoint.isHidden()) {
			   logger.debug ("Processing end point ID :" + endPoint.getId() + " , Grid Service ID : " + endPoint.getAbout().getId()+ " , URL : " + endPoint.getAbout().getUrl());
			   try {
                     DataServiceClient client = new DataServiceClient(endPoint.getAbout().getUrl());
                     CQLQueryResults result = client.query(cql);
                     Long countL = result.getCountResult().getCount();
                     return countL.intValue();
			   } catch (Exception e) {
				   logger.warn("Could not query service at " + e.getMessage());
			   }

		}
        return count;
   }


	private UMLClass getUMLClass(String umlClassName,int serviceId) {
		return umlClassDao.getClassInGivenService(umlClassName,serviceId);
	}

  
	private void saveResults(GridService service , UMLClass umlClass, String caption, int count) throws Exception {
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
