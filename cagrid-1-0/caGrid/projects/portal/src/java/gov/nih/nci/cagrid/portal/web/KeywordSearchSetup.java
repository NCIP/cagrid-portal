package gov.nih.nci.cagrid.portal.web;

import javax.faces.FacesException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 30, 2006
 * Time: 9:09:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeywordSearchSetup {
    private String searchKeyword;


    public String navigateToKeywordSearchResults() throws FacesException {

        if (searchKeyword != null) {
            /** Forward the search **/
            ServicesList servicesList = (ServicesList) PortalWebUtils.getBean("services");
            servicesList.setupKeywordSearch(searchKeyword);

            CenterList centerList = (CenterList) PortalWebUtils.getBean("centers");
            centerList.setupKeywordSearch(searchKeyword);

            PeopleList peopleList = (PeopleList) PortalWebUtils.getBean("people");
            peopleList.setupKeywordSearch(searchKeyword);

            return "success";
        }

        return "null";
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
