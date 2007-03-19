package org.cagrid.grape;

import javax.swing.JPanel;

import org.cagrid.grape.model.ConfigurationEditor;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @created Oct 14, 2004
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class ConfigurationEditorBasePanel extends JPanel {

	private Object configurationObject;
	private ConfigurationEditorTreeNode treeNode;

	public ConfigurationEditorBasePanel(ConfigurationEditorTreeNode treeNode,Object conf) {
		this.configurationObject = conf;
		this.setTreeNode(treeNode);
	}


	public Object getConfigurationObject() {
		return configurationObject;
	}
	
	public void addEditor(ConfigurationEditor editor, Object object) throws Exception{
		getTreeNode().addConfigurationEditor(editor, object);
	}


	public ConfigurationEditorTreeNode getTreeNode() {
		return treeNode;
	}


	public void setTreeNode(ConfigurationEditorTreeNode treeNode) {
		this.treeNode = treeNode;
	}
	
	

}
