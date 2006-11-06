package gov.nih.nci.cagrid.portal.web;

import javax.faces.FacesException;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 30, 2006
 * Time: 9:09:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeywordSearchSetup {
    private String searchKeyword;
    private String searchCategory;
    private List searchCategoryItems = new ArrayList();
    private List searchCategorySelected = new ArrayList();

    private final String SEARCH_CATEGORY_ALL = "all";
    private final String SEARCH_CATEGORY_SERVICES = "services";
    private final String SEARCH_CATEGORY_PEOPLE = "people";
    private final String SEARCH_CATEGORY_PARTICIPANTS = "participants";


    public KeywordSearchSetup() {
        SelectItem all = new SelectItem(SEARCH_CATEGORY_ALL, "All");
        SelectItem services = new SelectItem(SEARCH_CATEGORY_SERVICES, "caGrid Services");
        SelectItem people = new SelectItem(SEARCH_CATEGORY_PEOPLE, "People");
        SelectItem participants = new SelectItem(SEARCH_CATEGORY_PARTICIPANTS, "caBIG Participants");

        searchCategorySelected.add(SEARCH_CATEGORY_SERVICES);
        searchCategorySelected.add(SEARCH_CATEGORY_PEOPLE);
        searchCategorySelected.add(SEARCH_CATEGORY_PARTICIPANTS);

        searchCategoryItems.add(services);
        searchCategoryItems.add(people);
        searchCategoryItems.add(participants);


    }

    public String navigateToKeywordSearchResults() throws FacesException {
        ServicesList services = (ServicesList) PortalWebUtils.getBean("services");
        CaBIGParticipantList particpants = (CaBIGParticipantList) PortalWebUtils.getBean("participants");
        PeopleList people = (PeopleList) PortalWebUtils.getBean("people");

        //clear the search result
        services.setList(null);
        particpants.setList(null);
        people.setList(null);

        /** Do search **/
        if (searchKeyword != null && searchKeyword.length() > 1 && searchCategorySelected.size() > 0) {
            /** Search For categories selected **/
            if (searchCategorySelected.contains(SEARCH_CATEGORY_SERVICES)) {
                /** Forward the search **/
                services.setupKeywordSearch(searchKeyword);
            }
            if (searchCategorySelected.contains(SEARCH_CATEGORY_PARTICIPANTS)) {
                particpants.setupKeywordSearch(searchKeyword);
            }
            if (searchCategorySelected.contains(SEARCH_CATEGORY_PEOPLE)) {
                people.setupKeywordSearch(searchKeyword);
            }

            return "success";
        }
        /** search term not valid **/

        return "null";
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
    }

    public List getSearchCategorySelected() {
        return searchCategorySelected;
    }

    public void setSearchCategorySelected(List searchCategorySelected) {
        this.searchCategorySelected = searchCategorySelected;
    }

    public List getSearchCategoryItems() {
        return searchCategoryItems;
    }

    public void setSearchCategoryItems(List searchCategoryItems) {
        this.searchCategoryItems = searchCategoryItems;
    }
}
