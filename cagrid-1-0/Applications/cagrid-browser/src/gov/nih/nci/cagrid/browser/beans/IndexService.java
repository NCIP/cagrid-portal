package gov.nih.nci.cagrid.browser.beans;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;

//~--- classes ----------------------------------------------------------------

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 23, 2005
 * Time: 10:59:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndexService {
    private static SelectItem[] indexItems = null;

    //~--- fields -------------------------------------------------------------

    private ArrayList indexList = new ArrayList();

    /* Currently selected url. This is the index that will be used */
    private String url = "http://cagrid-registry.nci.nih.gov/ogsa/services/cagrid/IndexService";

    //~--- get methods --------------------------------------------------------

    public SelectItem[] getIndexItems() {

            indexItems = new SelectItem[indexList.size()+1];

        //First element is empty
            indexItems[0] = new SelectItem(this.url);

            for (int i = 0; i < indexList.size(); i++) {
                indexItems[i+1] = new SelectItem(indexList.get(i));
            }

        return indexItems;
    }

    public ArrayList getIndexList() {
        return indexList;
    }

    public String getUrl() {
        if (url == null) {

            /* chose a default index */
            url = (String) indexList.get(0);
        }

        return url;
    }

    //~--- set methods --------------------------------------------------------

    public void setIndexList(ArrayList indexList) {
        this.indexList = indexList;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static void setIndexItems(SelectItem[] indexItems) {
        IndexService.indexItems = indexItems;
    }

    public void changeURLListener(ValueChangeEvent event){
        this.url = (String)event.getNewValue();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
