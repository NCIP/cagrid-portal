/*-----------------------------------------------------------------------------
 * Copyright (c) 2003-2004, The Ohio State University,
 * Department of Biomedical Informatics, Multiscale Computing Laboratory
 * All rights reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3  All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement: This product includes
 *    material developed by the Mobius Project (http://www.projectmobius.org/).
 * 
 * 4. Neither the name of the Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * 5. Products derived from this Software may not be called "Mobius"
 *    nor may "Mobius" appear in their names without prior written
 *    permission of Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *---------------------------------------------------------------------------*/

package org.cagrid.grape;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.cagrid.grape.model.ConfigurationDescriptor;
import org.cagrid.grape.model.ConfigurationDescriptors;
import org.cagrid.grape.model.ConfigurationGroup;
import org.cagrid.grape.model.ConfigurationGroups;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */

public abstract class ConfigurationBaseTreeNode extends DefaultMutableTreeNode {

	private ConfigurationTree tree;

	private ConfigurationManager configurationManager;
	
	private JPanel displayPanel;
	
	private ConfigurationWindow configurationWindow;
	

	public ConfigurationBaseTreeNode(ConfigurationWindow window, ConfigurationTree tree,
			ConfigurationManager configurationManager) {
		this.tree = tree;
		this.configurationManager = configurationManager;
		this.configurationWindow = window;
	}

	public ConfigurationTree getTree() {
		return tree;
	}
	

	public ConfigurationWindow getConfigurationWindow() {
		return configurationWindow;
	}

	public JPanel getDisplayPanel() {
		return displayPanel;
	}

	public void setDisplayPanel(JPanel displayPanel) {
		this.displayPanel = displayPanel;
	}
	
	public void showPanel(){
			if(getDisplayPanel()!=null){
				getConfigurationWindow().showDisplayPanel(getIdentifier());
			}
	}
	
	public void addToDisplay(){
		if(getDisplayPanel()!=null){
			getConfigurationWindow().addDisplayPanel(getIdentifier(), getDisplayPanel());
		}
	}
	

	protected void processConfigurationGroups(ConfigurationGroups list)
			throws Exception {
		if (list != null) {
			ConfigurationGroup[] group = list.getConfigurationGroup();
			if (group != null) {
				for (int i = 0; i < group.length; i++) {
					this.processConfigurationGroup(group[i]);
				}
			}
		}
	}

	protected void processConfigurationDescriptors(ConfigurationDescriptors list)
			throws Exception {

		if (list != null) {
			ConfigurationDescriptor[] des = list.getConfigurationDescriptor();
			if (des != null) {
				for (int i = 0; i < des.length; i++) {
					this.processConfigurationDescriptor(des[i]);
				}

			}
		}
	}

	protected void processConfigurationGroup(ConfigurationGroup des)
			throws Exception {
		if (des != null) {
			ConfigurationGroupTreeNode node = new ConfigurationGroupTreeNode(getConfigurationWindow(),getTree(),
					getConfigurationManager(), des);
			this.add(node);
			node.addToDisplay();
		}
	}

	protected void processConfigurationDescriptor(ConfigurationDescriptor des) throws Exception {
		if (des != null) {
			ConfigurationDescriptorTreeNode node = new ConfigurationDescriptorTreeNode(getConfigurationWindow(),getTree(),
					getConfigurationManager(), des);
			this.add(node);
			node.addToDisplay();
		}
	}

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public abstract ImageIcon getIcon();

	public abstract String toString();
	
	public String getIdentifier(){
		ConfigurationBaseTreeNode node = (ConfigurationBaseTreeNode)this.getParent();
		if(node==null){
			return "Preferences";
		}else{
			return node.getIdentifier()+":"+toString();
		}
	}
	
}
