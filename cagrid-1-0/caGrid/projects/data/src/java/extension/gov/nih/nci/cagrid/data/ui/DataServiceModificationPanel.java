package gov.nih.nci.cagrid.data.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import gov.nih.nci.cagrid.introduce.extension.ServiceModificationUIPanel;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;

/** 
 *  DataServiceModificationPanel
 *  Panel to modify target data types for a data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 5, 2006 
 * @version $Id$ 
 */
public class DataServiceModificationPanel extends ServiceModificationUIPanel {
	
	private NamespacesJTree namespacesTree = null;
	private DataServiceTypesTable typesTable = null;

	public DataServiceModificationPanel(ServiceInformation info) {
		super(info);
	}
	
	
	private void initialize() {
		// update the namespaces on focus of the panel
		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				getNamespacesTree().setNamespaces(getServiceInfo().getNamespaces());
			}
		});
	}
	
	
	private NamespacesJTree getNamespacesTree() {
		if (namespacesTree == null) {
			namespacesTree = new NamespacesJTree(getServiceInfo().getNamespaces());
		}
		return namespacesTree;
	}
	
	
	private DataServiceTypesTable getTypesTable() {
		if (typesTable == null) {
			typesTable = new DataServiceTypesTable();
		}
		return typesTable;
	}
}
