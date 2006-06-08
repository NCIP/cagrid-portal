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

package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
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
public class ResourcePropertiesTypeTreeNode extends DefaultMutableTreeNode {
	private ResourcePropertiesListType resourceProperties;
	private DefaultTreeModel model;
	private ResourcePropertiesPopUpMenu menu;
	private ServiceInformation info;
	
	public ResourcePropertiesTypeTreeNode(ResourcePropertiesListType methods,DefaultTreeModel model,ServiceInformation info) {
		super();
		this.resourceProperties = methods;
		this.setUserObject("Resource Properties");
		this.model = model;
		this.menu = new ResourcePropertiesPopUpMenu(this);
		this.info = info;
		initialize();
	}
	
	private void initialize(){
		if(resourceProperties!=null && resourceProperties.getResourceProperty()!=null){
			for(int i = 0; i < resourceProperties.getResourceProperty().length; i++){
				ResourcePropertyType resource = resourceProperties.getResourceProperty(i);
				ResourcePropertyTypeTreeNode newNode = new ResourcePropertyTypeTreeNode(resource,model);
				model.insertNodeInto(newNode,this,this.getChildCount());
			}
		}
	}
	
	public void add(ResourcePropertyType resourceProperty){
		if(resourceProperties==null){
			System.err.println("ERROR: cannot add new resource when the resourcePropertys container is null.");
		}
		//add new resourceProperty to array in bean
		//this seems to be a wierd way be adding things....
		ResourcePropertyType[] newResourceProperty;
		int newLength = 0;
		if (resourceProperties!=null && resourceProperties.getResourceProperty()!=null) {
			newLength = resourceProperties.getResourceProperty().length + 1;
			newResourceProperty = new ResourcePropertyType[newLength];
			System.arraycopy(resourceProperties.getResourceProperty(), 0, newResourceProperty, 0,resourceProperties.getResourceProperty().length);
		} else {
			newLength = 1;
			newResourceProperty = new ResourcePropertyType[newLength];
		}
		newResourceProperty[newLength - 1] = resourceProperty;
		resourceProperties.setResourceProperty(newResourceProperty);
		
		ResourcePropertyTypeTreeNode newNode = new ResourcePropertyTypeTreeNode(resourceProperty,model);
		model.insertNodeInto(newNode,this,this.getChildCount());
	}
	
	public void removeResourceProperty(ResourcePropertyTypeTreeNode node){
		
		ResourcePropertyType[] newResourceProperties = new ResourcePropertyType[resourceProperties.getResourceProperty().length-1];
		int resourcePropertyCount = 0;
		for(int i = 0; i < resourceProperties.getResourceProperty().length; i++){
			if(!resourceProperties.getResourceProperty(i).equals(node.getUserObject())){
				newResourceProperties[resourcePropertyCount++] = (ResourcePropertyType)node.getUserObject();
			}
		}
		resourceProperties.setResourceProperty(newResourceProperties);
		
		model.removeNodeFromParent(node);
	}
	
	public JPopupMenu getPopUpMenu(){
		return menu;
	}
	
	public String toString(){
		return this.getUserObject().toString();
	}

	public ServiceInformation getInfo() {
		return info;
	}

	public void setInfo(ServiceInformation info) {
		this.info = info;
	}

	public ResourcePropertiesListType getResourceProperties() {
		return resourceProperties;
	}

	public void setResourceProperties(ResourcePropertiesListType resourceProperties) {
		this.resourceProperties = resourceProperties;
	}
	
	public ImageIcon getOpenIcon() {
		return IntroduceLookAndFeel.getResourcePropertiesIcon();
	}


	public ImageIcon getClosedIcon() {
		return IntroduceLookAndFeel.getResourcePropertiesIcon();
	}

}
