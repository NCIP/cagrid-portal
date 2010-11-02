package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.GridServiceUmlClass;

import java.util.List;

import javax.persistence.NonUniqueResultException;

public class GridServiceUmlClassDao extends AbstractDao<GridServiceUmlClass> {

	@Override
	public Class domainClass() {
		// TODO Auto-generated method stub
		return GridServiceUmlClass.class;
	}
	
    @Override
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

}
