package gov.nih.nci.cagrid.portal.web;

import gov.nih.nci.cagrid.portal.manager.PointOfContactManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 29, 2006
 * Time: 1:01:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PeopleList {

    private List list;
    private PointOfContactManager pocManager;

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setPocManager(PointOfContactManager pocManager) {
        this.pocManager = pocManager;
    }
}
