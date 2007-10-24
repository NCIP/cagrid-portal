/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class Tab {
	
	private Tab parent;
	private List<Tab> children = new ArrayList<Tab>();
	private String name;
	private String label;
	private boolean selected;
	

	/**
	 * 
	 */
	public Tab() {

	}
	
	public Tab find(String path) {
		Tab tab = null;
		String myPath = getPath();
		if (path.equals(myPath)) {
			tab = this;
		} else if(path.startsWith(myPath)) {
			for (Tab n : getChildren()) {
				tab = n.find(path);
				if(tab != null){
					break;
				}
			}
		}
		return tab;
	}
	
	public String getPath(){
		String path = "/" + getName();
		Tab p = getParent();
		while(p != null){
			path = "/" + p.getName() + path;
			p = p.getParent();
		}
		return path;
	}


	public Tab getParent() {
		return parent;
	}


	public void setParent(Tab parent) {
		this.parent = parent;
	}


	public List<Tab> getChildren() {
		return children;
	}


	public void setChildren(List<Tab> children) {
		this.children = children;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String toString(){
		return "[name=" + getName() + ", selected=" + isSelected() + "]";
	}
}
