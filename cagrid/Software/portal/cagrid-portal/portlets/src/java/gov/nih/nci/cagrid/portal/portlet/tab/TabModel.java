/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.tab;

import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class TabModel implements InitializingBean {

    private List<Tab> tabs = new ArrayList<Tab>();

    /**
     *
     */
    public TabModel() {

    }

    public List<List<Tab>> getRows() {
        List<List<Tab>> rows = new ArrayList<List<Tab>>();
        List<Tab> childTabs = getTabs();
        while (childTabs.size() > 0) {
            rows.add(childTabs);
            childTabs = getSelectedTab(childTabs).getVisibleChildren();
        }
        return rows;
    }

    public Tab select(String path) {
        return select(getTabs(), path.substring(1));
    }

    public Tab select(List<Tab> tabs, String path) {
        Tab tab = null;
        Tab selectedTab = getSelectedTab(tabs);
        if (selectedTab != null) {
            selectedTab.setSelected(false);
        }
        String[] parts = PortletUtils.parsePath(path);
        for (Tab t : tabs) {
            if (t.getName().equals(parts[0])) {
                t.setSelected(true);
                if (parts.length == 2) {
                    tab = select(t.getChildren(), parts[1]);
                }
            }
        }
        return tab;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
    }

    private Tab getSelectedTab(List<Tab> childTabs) {
        Tab selectedTab = null;
        for (Tab tab : childTabs) {
            if (tab.isSelected()) {
                selectedTab = tab;
                break;
            }
        }
        if (selectedTab == null) {
            selectedTab = childTabs.get(0);
            selectedTab.setSelected(true);
        }
        return selectedTab;
    }

    public String getCurrentPath() {
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        List<Tab> childTabs = getTabs();
        while (childTabs.size() > 0) {
            Tab selectedTab = getSelectedTab(childTabs);
            sb.append(selectedTab.getName());
            childTabs = selectedTab.getChildren();
            if (childTabs.size() > 0) {
                sb.append("/");
            }
        }
        return sb.toString();
    }

    public void afterPropertiesSet() throws Exception {
        for (Tab parent : getTabs()) {
            linkToChildren(parent);
        }
    }

    private void linkToChildren(Tab parent) {
        for (Tab child : parent.getChildren()) {
            child.setParent(parent);
            if (parent.isAuthnRequired()) {
                child.setAuthnRequired(parent.isAuthnRequired());
            }
            if (!parent.isVisible()) {
                child.setVisible(parent.isVisible());
            }
            linkToChildren(child);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trees:\n");
        for (Tab tab : getTabs()) {
            append(sb, 0, tab);
        }
        sb.append("Rows:\n");
        for (List<Tab> row : getRows()) {
            for (Tab tab : row) {
                sb.append(tab.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void append(StringBuilder sb, int depth, Tab parent) {
        for (int i = 0; i < depth; i++) {
            sb.append("\t");
        }
        sb.append(parent.toString()).append("\n");
        for (Tab child : parent.getChildren()) {
            append(sb, depth + 1, child);
        }
    }
}
