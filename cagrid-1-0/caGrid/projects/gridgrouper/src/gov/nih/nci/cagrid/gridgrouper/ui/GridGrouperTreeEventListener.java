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

package gov.nih.nci.cagrid.gridgrouper.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GridGrouperTreeEventListener extends MouseAdapter {

	private GridGrouperTree tree;

	private GroupManagementBrowser browser;

	private HashMap popupMappings;

	public GridGrouperTreeEventListener(GridGrouperTree owningTree,
			GroupManagementBrowser browser) {
		this.tree = owningTree;
		this.popupMappings = new HashMap();
		this.browser = browser;
		this.associatePopup(StemTreeNode.class, new StemNodeMenu(browser,
				this.tree));
		this.associatePopup(GroupTreeNode.class, new GroupNodeMenu(browser,
				this.tree));
	}

	/**
	 * Associate a GridServiceTreeNode type with a popup menu
	 * 
	 * @param serviceType
	 * @param popup
	 */
	public void associatePopup(Class nodeType, JPopupMenu popup) {
		this.popupMappings.put(nodeType, popup);
	}

	public void mouseEntered(MouseEvent e) {
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
		if ((e.isPopupTrigger()) || (SwingUtilities.isRightMouseButton(e))) {
			DefaultMutableTreeNode currentNode = this.tree.getCurrentNode();
			GridGrouperTreeNodeMenu popup = null;
			if (currentNode != null) {
				popup = (GridGrouperTreeNodeMenu) popupMappings.get(currentNode
						.getClass());
			}
			if (popup != null) {
				if (currentNode.getChildCount() > 0) {
					popup.toggleRemove(false);
				} else if ((currentNode instanceof StemTreeNode)
						&& (((StemTreeNode) currentNode).isRootStem())) {
					popup.toggleRemove(false);
				} else {
					popup.toggleRemove(true);
				}
				popup.show(e.getComponent(), e.getX(), e.getY());
			}

		} else if (e.getClickCount() == 2) {
			browser.getContentManager().addNode(this.tree.getCurrentNode());
		}
	}
}
