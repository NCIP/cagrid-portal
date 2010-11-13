package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.annotation.UpdatesCatalogs;
import gov.nih.nci.cagrid.portal.domain.GridServiceUmlClass;

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
    	List list = getHibernateTemplate().find(
    			"select gsu.umlClass.className as className , sum(gsu.objectCount) as count from GridServiceUmlClass gsu where gsu.caption = ? group by className order by className", new Object[]{caption});
    	Map map = new HashMap();
    	for (int i=0; i<list.size();i++) {
    		Object[] obj = (Object[])list.get(i);
    		map.put(obj[0], obj[1]);
    	}
    	return map;
   }
    
   public Map<Object,Set> getCatalogIdsGroupedByClassName() {
	   List list = getHibernateTemplate().find("select gsu.umlClass.className as className , gsu.gridService.catalog.id as id from GridServiceUmlClass gsu");
	   Map<Object ,Set> map = new HashMap<Object , Set>();
	   	for (int i=0; i<list.size();i++) {
	   		Object[] obj = (Object[])list.get(i);
	   		Object cat = map.get(obj[0]);
	   		if (cat == null) {
				Set s = new HashSet();
				s.add(obj[1]);
				map.put(obj[0], s);
			} else {
				Set s = (Set)cat;
				s.add(obj[1]);
				map.put(obj[0], s);
			}

	   	}
	   	return map;
   }

}
