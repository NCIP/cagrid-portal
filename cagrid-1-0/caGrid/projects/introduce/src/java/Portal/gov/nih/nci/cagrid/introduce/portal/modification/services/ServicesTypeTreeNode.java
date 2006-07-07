package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 * Node for representing namepspace
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @created Nov 22, 2004
 * @version $Id$
 */
public class ServicesTypeTreeNode extends DefaultMutableTreeNode {

	private ServicesPopUpMenu menu;
	private ServicesType services;
	private DefaultTreeModel model;
	private ServiceInformation info;


	public ServicesTypeTreeNode(ServiceInformation info) {
		super();
		this.info = info;
		menu = new ServicesPopUpMenu(this);
		this.setUserObject("Service Contexts");

	}


	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}


	public DefaultTreeModel getModel() {
		return this.model;
	}


	public ServiceInformation getInfo() {
		return this.info;
	}


	public void setServices(ServicesType services, DefaultTreeModel model) {
		this.model = model;
		this.services = services;
		if (services.getService() != null) {
			// starting from one because we want to skip service 0, service 0 is
			// the main service.
			for (int i = 1; i < services.getService().length; i++) {
				ServiceTypeTreeNode newNode = new ServiceTypeTreeNode(services.getService(i), info, model);
				model.insertNodeInto(newNode, this, this.getChildCount());
			}
		}
	}


	public void removeResource(ServiceTypeTreeNode node) {
		ServiceType[] newResourceProperty = new ServiceType[services.getService().length - 1];
		newResourceProperty[0] = services.getService(0);
		int newResourcePropertyCount = 1;
		for (int i = 0; i < this.getChildCount(); i++) {
			ServiceTypeTreeNode treenode = (ServiceTypeTreeNode) this.getChildAt(i);
			if (!treenode.equals(node)) {
				newResourceProperty[newResourcePropertyCount++] = (ServiceType) treenode.getUserObject();
			}
		}

		services.setService(newResourceProperty);

		model.removeNodeFromParent(node);
	}


	public String toString() {
		return this.getUserObject().toString();
	}


	public ServiceTypeTreeNode addService(ServiceType type) {
		ServiceTypeTreeNode newNode = new ServiceTypeTreeNode(type, info, model);
		model.insertNodeInto(newNode, this, this.getChildCount());
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

		return newNode;
	}


	public ServicesPopUpMenu getPopUpMenu() {
		return this.menu;
	}
}
