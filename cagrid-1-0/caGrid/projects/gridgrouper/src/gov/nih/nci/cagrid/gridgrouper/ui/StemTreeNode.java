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

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouper;
import gov.nih.nci.cagrid.gridgrouper.client.Group;
import gov.nih.nci.cagrid.gridgrouper.client.Stem;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.cagrid.gridgrouper.grouper.StemI;

import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class StemTreeNode extends GridGrouperBaseTreeNode {

	private Stem stem;

	private boolean rootStem;

	public StemTreeNode(GroupManagementBrowser browser, Stem stem,
			boolean root) {
		super(browser);
		this.rootStem = root;
		this.stem = stem;
	}

	public void loadStem() throws Exception {
		this.removeAllChildren();
		Set set = stem.getChildStems();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			StemI stem = (StemI) itr.next();
			StemTreeNode node = new StemTreeNode(getBrowser(),
					((Stem) stem), false);
			synchronized (getTree()) {
				this.add(node);
				TreeNode parent = this.getParent();
				if (parent != null) {
					getTree().reload(parent);
				} else {
					getTree().reload();
				}
			}
			node.loadStem();
		}
		Set grps = stem.getChildGroups();
		Iterator itr2 = grps.iterator();
		while (itr2.hasNext()) {
			GroupI group = (GroupI)itr2.next();
			GroupTreeNode node = new GroupTreeNode(getBrowser(),(Group)group);
			synchronized (getTree()) {
				this.add(node);
				TreeNode parent = this.getParent();
				if (parent != null) {
					getTree().reload(parent);
				} else {
					getTree().reload();
				}
			}
		}
	}

	public void refresh() {
		int id = getBrowser().getProgress().startEvent(
				"Refreshing " + toString() + ".... ");
		try {
			stem = (Stem) stem.getGridGrouper().findStem(
					stem.getName());
			if (parent != null) {
				getTree().reload(parent);
			} else {
				getTree().reload();
			}
			loadStem();
			getBrowser().getProgress().stopEvent(id,
					"Refreshed " + toString() + "!!!");
		} catch (Exception e) {
			getBrowser().getProgress().stopEvent(id,
					"Error refreshing " + toString() + "!!!");
			PortalUtils.showErrorMessage(e);
		}
	}

	public ImageIcon getIcon() {
		if (this.rootStem) {
			return GridGrouperLookAndFeel.getGrouperIcon16x16();
		} else {
			return GridGrouperLookAndFeel.getStemIcon();
		}
	}

	public String toString() {
		if (this.rootStem) {
			return stem.getGridGrouper().getName();
		} else {
			return stem.getDisplayExtension();
		}
	}

	public boolean isRootStem() {
		return rootStem;
	}

	public GridGrouper getGridGrouper() {
		return stem.getGridGrouper();
	}

	public Stem getStem() {
		return stem;
	}
	
	

}
