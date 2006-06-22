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

package gov.nih.nci.cagrid.introduce.portal.modification.services;

import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.methods.MethodsTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertiesTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.ResourcePropertyTypeTreeNode;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;


/**
 * Renders the grid service tree
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * 
 * @created Nov 17, 2004
 * @version $Id$
 */
public class ServicesTreeRenderer extends DefaultTreeCellRenderer {

	private Font normal = null;


	public ServicesTreeRenderer() {
		super();
	}


	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
		boolean leaf, int row, boolean localHasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, localHasFocus);
		if (normal == null) {
			normal = this.getFont();
		}
		this.setFont(normal);
		if (value instanceof ServiceTypeTreeNode) {
			ServiceTypeTreeNode node = (ServiceTypeTreeNode) value;
			this.setIcon(node.getOpenIcon());
			this.setOpenIcon(node.getOpenIcon());
			this.setClosedIcon(node.getClosedIcon());
			this.setFont(normal.deriveFont(Font.BOLD));
			this.setText(node.toString());
		} else if (value instanceof ServicesTypeTreeNode) {
			ServicesTypeTreeNode node = (ServicesTypeTreeNode) value;
			this.setIcon(null);
			this.setOpenIcon(null);
			this.setClosedIcon(null);
			this.setText(node.toString());
			this.setFont(normal.deriveFont(Font.BOLD, 12));
		} else if (value instanceof MethodsTypeTreeNode) {
			MethodsTypeTreeNode node = (MethodsTypeTreeNode) value;
			this.setIcon(node.getOpenIcon());
			this.setOpenIcon(node.getOpenIcon());
			this.setClosedIcon(node.getClosedIcon());
			this.setText(node.toString());
			this.setFont(normal);
		} else if (value instanceof MethodTypeTreeNode) {
			MethodTypeTreeNode node = (MethodTypeTreeNode) value;
			this.setIcon(node.getOpenIcon());
			this.setOpenIcon(node.getOpenIcon());
			this.setClosedIcon(node.getClosedIcon());
			this.setText(node.toString());
			this.setFont(normal.deriveFont(Font.ITALIC));
		} else if (value instanceof ResourcePropertiesTypeTreeNode) {
			ResourcePropertiesTypeTreeNode node = (ResourcePropertiesTypeTreeNode) value;
			this.setIcon(node.getOpenIcon());
			this.setOpenIcon(node.getOpenIcon());
			this.setClosedIcon(node.getClosedIcon());
			this.setText(node.toString());
			this.setFont(normal);
		} else if (value instanceof ResourcePropertyTypeTreeNode) {
			ResourcePropertyTypeTreeNode node = (ResourcePropertyTypeTreeNode) value;
			this.setIcon(node.getOpenIcon());
			this.setOpenIcon(node.getOpenIcon());
			this.setClosedIcon(node.getClosedIcon());
			this.setText(node.toString());
			this.setFont(normal.deriveFont(Font.ITALIC));
			this.setForeground(Color.GRAY);
		}
		return this;
	}

}
