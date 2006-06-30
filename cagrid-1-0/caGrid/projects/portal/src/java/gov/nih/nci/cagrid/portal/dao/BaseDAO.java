package gov.nih.nci.cagrid.portal.dao;

import java.util.List;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 5:58:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BaseDAO {


    public List loadAll(Class type);

     public void saveOrUpdate(Collection objects);

    public void saveOrUpdate(Object obj);
}
