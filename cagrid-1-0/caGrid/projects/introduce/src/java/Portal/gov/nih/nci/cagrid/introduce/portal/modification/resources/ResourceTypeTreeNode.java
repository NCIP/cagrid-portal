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

package gov.nih.nci.cagrid.introduce.portal.modification.resources;

import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 * Node for representing namepspace
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @created Nov 22, 2004
 * @version $Id: MakoGridServiceTreeNode.java,v 1.21 2005/04/20 17:28:54 ervin
 *          Exp $
 */
public class ResourceTypeTreeNode extends DefaultMutableTreeNode {
	private ServiceType serviceType;
	private ServiceInformation info;
	private ResourcePopUpMenu popUpMenu;
	private ResourcesJTree tree;


	public ResourceTypeTreeNode(ServiceType serviceType, ServiceInformation info, ResourcesJTree tree) {
		super();
		this.info = info;
		this.setUserObject(serviceType);
		this.serviceType = serviceType;
		this.tree = tree;
		this.popUpMenu = new ResourcePopUpMenu(this);
		initialize();
	}


	private void initialize() {
		if (serviceType.getMethods() != null) {
			MethodsTypeTreeNode newNode = new MethodsTypeTreeNode(serviceType.getMethods(), (DefaultTreeModel)tree.getModel(), info);
			((DefaultTreeModel)tree.getModel()).insertNodeInto(newNode, this, this.getChildCount());
		}
		if (serviceType.getResourcePropertiesList() != null) {
			ResourcePropertiesTypeTreeNode newNode = new ResourcePropertiesTypeTreeNode(serviceType
				.getResourcePropertiesList(), (DefaultTreeModel)tree.getModel(), info);
		((DefaultTreeModel)tree.getModel()).insertNodeInto(newNode, this, this.getChildCount());
		}
	}


	public ImageIcon getOpenIcon() {
		return IntroduceLookAndFeel.getResourceIcon();
	}


	public ImageIcon getClosedIcon() {
		return IntroduceLookAndFeel.getResourceIcon();
	}


	public String toString() {
		return ((ServiceType) this.getUserObject()).getName();
	}
	
	public ResourcePopUpMenu getPopUpMenu(){
		return popUpMenu;
	}

}
