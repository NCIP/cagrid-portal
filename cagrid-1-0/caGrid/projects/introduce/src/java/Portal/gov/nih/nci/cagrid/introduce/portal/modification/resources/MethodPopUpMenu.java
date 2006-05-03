package gov.nih.nci.cagrid.introduce.portal.modification.resources;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.MethodViewer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.xml.namespace.QName;

import org.projectmobius.portal.PortalResourceManager;

public class MethodPopUpMenu extends JPopupMenu {

	private JMenuItem removeMethodMenuItem = null;
	MethodTypeTreeNode node;
	/**
	 * This method initializes 
	 * 
	 */
	public MethodPopUpMenu(MethodTypeTreeNode node) {
		super();
		this.node = node;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.add(getRemoveMethodMenuItem());
	}

	/**
	 * This method initializes removeMethodMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getRemoveMethodMenuItem() {
		if (removeMethodMenuItem == null) {
			removeMethodMenuItem = new JMenuItem();
			removeMethodMenuItem.setText("Remove Method");
			removeMethodMenuItem.setIcon(IntroduceLookAndFeel.getAddIcon());
			removeMethodMenuItem.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					((MethodsTypeTreeNode)node.getParent()).removeMethod(node);
				}
			});
		}
		return removeMethodMenuItem;
	}

}
