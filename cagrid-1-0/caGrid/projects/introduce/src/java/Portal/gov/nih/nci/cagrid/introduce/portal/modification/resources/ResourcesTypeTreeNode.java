
package gov.nih.nci.cagrid.introduce.portal.modification.resources;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/** 
 *  Node for representing namepspace
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * 
 * @created Nov 22, 2004 
 * @version $Id$ 
 */
public class ResourcesTypeTreeNode extends DefaultMutableTreeNode {

	private ResourcesPopUpMenu menu;
	private ServicesType services;
	private ResourcesJTree tree;
	private ServiceInformation info;
	public ResourcesTypeTreeNode(ServicesType services, ServiceInformation info, ResourcesJTree tree) {
		super();
		this.info = info;
		this.services = services;
		this.tree = tree;
		menu = new ResourcesPopUpMenu(this);
		this.setUserObject("Service Contexts");
		initialize();
	}
	
	private void initialize() {
		if (services.getService() != null) {
			for (int i = 0; i < services.getService().length; i++) {
				//if(!services.getService(i).getName().equals(info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME))){
					ResourceTypeTreeNode newNode = new ResourceTypeTreeNode(services.getService(i),info,tree);
					((DefaultTreeModel)tree.getModel()).insertNodeInto(newNode, this, this.getChildCount());
				//}
			}
		}
		tree.expandAll(true);
	}
	
	public void removeResource(ResourceTypeTreeNode node){
		ServiceType[] newResourceProperty = new ServiceType[services.getService().length-1];
		int newResourcePropertyCount =0;
		for(int i = 0; i < this.getChildCount(); i++){
			ResourceTypeTreeNode treenode = (ResourceTypeTreeNode)this.getChildAt(i);
			if(!treenode.equals(node)){
				newResourceProperty[newResourcePropertyCount++] = (ServiceType)treenode.getUserObject();
			} 
		}
		
		services.setService(newResourceProperty);
		
		((DefaultTreeModel)tree.getModel()).removeNodeFromParent(node);
	}
	
	public String toString(){
		return this.getUserObject().toString();
	}
	
	public void addService(ServiceType type) {
			ResourceTypeTreeNode newNode = new ResourceTypeTreeNode(type,info,tree);
			((DefaultTreeModel)tree.getModel()).insertNodeInto(newNode, this, this.getChildCount());
			tree.expandPath(new TreePath(((DefaultTreeModel)tree.getModel()).getPathToRoot(newNode)));
			// keep servicestype consistant
			int currentLength = 0;
			if (services.getService() != null) {
				currentLength = services.getService().length;
			}
			ServiceType[] newServiceTypes = new ServiceType[currentLength + 1];
			if (currentLength > 0) {
				System.arraycopy(services.getService(), 0, newServiceTypes, 0, currentLength);
			}
			newServiceTypes[currentLength] = type;
			services.setService(newServiceTypes);
	}
	
	public ResourcesPopUpMenu getPopUpMenu(){
		return this.menu;
	}
}
