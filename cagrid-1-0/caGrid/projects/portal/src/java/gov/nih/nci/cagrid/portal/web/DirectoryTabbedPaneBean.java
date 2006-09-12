package gov.nih.nci.cagrid.portal.web;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 12, 2006
 * Time: 11:39:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryTabbedPaneBean implements Serializable {


    private boolean servicePaneVisible = true;
    private boolean centerPaneVisible = true;
    private boolean pocPaneVisible = true;

    private static final long serialVersionUID = 1L;

    public boolean isServicePaneVisible() {
        return servicePaneVisible;
    }

    public void setServicePaneVisible(boolean servicePaneVisible) {
        this.servicePaneVisible = servicePaneVisible;
    }

    public boolean isCenterPaneVisible() {
        return centerPaneVisible;
    }

    public void setCenterPaneVisible(boolean centerPaneVisible) {
        this.centerPaneVisible = centerPaneVisible;
    }

    public boolean isPocPaneVisible() {
        return pocPaneVisible;
    }

    public void setPocPaneVisible(boolean pocPaneVisible) {
        this.pocPaneVisible = pocPaneVisible;
    }

}
