/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.annotation.UpdatesCatalogs;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.GridServiceUmlClass;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NonUniqueResultException;

public class GridServiceUmlClassDao extends AbstractDao<GridServiceUmlClass> {	
	
	@Override
	public Class domainClass() {
		// TODO Auto-generated method stub
		return GridServiceUmlClass.class;
	}

    @Override
    @UpdatesCatalogs
    public void save(GridServiceUmlClass gridServiceUmlClass) {
        super.save(gridServiceUmlClass);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public GridServiceUmlClass getByGridServiceAndUmlClass(int gridServiceId , int umlClassId) {
    	GridServiceUmlClass svc = null;

        List svcs = getHibernateTemplate().find(
                "from GridServiceUmlClass where gridService.id = ? and umlClass.id = ? ", new Object[]{gridServiceId , umlClassId});

        if (svcs.size() > 1) {
            throw new NonUniqueResultException("Found " + svcs.size()
                    + " gridServiceId "+ gridServiceId + "umlClassId "+umlClassId);
        } else if (svcs.size() == 1) {
            svc = (GridServiceUmlClass) svcs.get(0);
        }
        return svc;
    }

    public List<String> getUniqueCaptions() {
    	List captions = getHibernateTemplate().find("select distinct caption from GridServiceUmlClass gsu order by caption");
    	return captions ;
    }


    public Map getAggregatedClassCountByCaption (String caption) {   	
    	//Get list of services whose status is not DORMANT and not BANNED (not hidden services)
    	List services = getHibernateTemplate().find("from GridService");    	
    	List<Integer> serviceIds = new ArrayList<Integer>();
    	for(int i=0;i<services.size();i++){
    		GridService service =(GridService) services.get(i);
    		if((!service.getCurrentStatus().equals(ServiceStatus.DORMANT)) && 
    			(!service.getCurrentStatus().equals(ServiceStatus.BANNED)) &&
    			service.getServiceMetadata()!=null && 
    			service.getServiceMetadata().getServiceDescription()!=null){   			
    			
    			serviceIds.add(service.getId());
    		}
    	}
    	
    	List list = new ArrayList();
    	if(serviceIds.size()>0){
    		String hql = "select gsu.umlClass.className as className , sum(gsu.objectCount) as count from GridServiceUmlClass gsu where gsu.caption = :caption and gsu.gridService.id in (:listOfIds)  group by className order by className";
    		String[] params = { "caption","listOfIds"};
        	Object[] values = { caption, serviceIds };
        	list = getHibernateTemplate().findByNamedParam(hql, params, values);
    	}
    	
    	Map map = new HashMap();
    	for (int i=0; i<list.size();i++) {
    		Object[] obj = (Object[])list.get(i);
    		map.put(obj[0], obj[1]);
    	}
    	return map;
   }

   public Map<Object,Set> getCatalogIdsGroupedByClassName() {
	   List list = getHibernateTemplate().find("select gsu.umlClass.className as className , gsu.gridService.catalog.id as id , caption from GridServiceUmlClass gsu");
	   Map<Object ,Set> map = new HashMap<Object , Set>();
	   	for (int i=0; i<list.size();i++) {
	   		Object[] obj = (Object[])list.get(i);
	   		String key = obj[0]+"_"+obj[2];
	   		Object cat = map.get(key);
	   		if (cat == null) {
				Set s = new HashSet();
				s.add(obj[1]);
				map.put(key, s);
			} else {
				Set s = (Set)cat;
				s.add(obj[1]);
				map.put(key, s);
			}

	   	}
	   	return map;
   }	

}
