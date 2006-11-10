package gov.nih.nci.cagrid.browser.ajax;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jul 10, 2005
 * Time: 3:39:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscoveryCompletetionBean {
    private String[] keywords;

    //~--- constructors -------------------------------------------------------

    public DiscoveryCompletetionBean() {
        initializeDiscoveryKeywords();
    }

    //~--- methods ------------------------------------------------------------

    public void initializeDiscoveryKeywords() {
        keywords = new String[]{
                "Analytical Service Name", "Data Service Name", "EVS Concept",
                "Object Attribute", "Object Association", "Object-Class",
                "Object Model", "Research Center Fax", "Research Center Name",
                "Research Center POC", "Research Center Type",
        };
    }

    //~--- get methods --------------------------------------------------------

    public String[] getKeywords() {
        return keywords;
    }

    //~--- set methods --------------------------------------------------------

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }
}

//~ Formatted by Jindent --- http://www.jindent.com
