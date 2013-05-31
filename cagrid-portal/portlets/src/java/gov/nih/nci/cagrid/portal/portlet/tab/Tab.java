/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.tab;

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
	private boolean visible = true;
	private boolean authnRequired = false;

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
		return "[name:" + getName() + ", selected:" + isSelected() + ", visible:" + isVisible() + ", authnRequired:" + isAuthnRequired() + "]";
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<Tab> getVisibleChildren() {
		List<Tab> visibleChildren = new ArrayList<Tab>();
		for(Tab t : getChildren()){
			if(t.isVisible()){
				visibleChildren.add(t);
			}
		}
		return visibleChildren;
	}

	public boolean isAuthnRequired() {
		return authnRequired;
	}

	public void setAuthnRequired(boolean authnRequired) {
		this.authnRequired = authnRequired;
	}
}
