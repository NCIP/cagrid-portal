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

package gov.nih.nci.cagrid.gridgrouper.plugin.ui;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouper;
import gov.nih.nci.cagrid.gridgrouper.client.Stem;
import gov.nih.nci.cagrid.gridgrouper.grouper.StemI;
import gov.nih.nci.cagrid.gridgrouper.ui.GridGrouperLookAndFeel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;

import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GridGroupersTreeNode extends GridGrouperBaseTreeNode {

	private Map groupers;

	public GridGroupersTreeNode(GridGrouperExpressionBuilder editor) {
		super(editor);
		this.groupers = new HashMap();
	}

	public synchronized void addGridGrouper(GridGrouper grouper) {
		if (groupers.containsKey(grouper.getName())) {
			PortalUtils.showErrorMessage("The Grid Grouper Service "
					+ grouper.getName() + " has already been added!!!");
		} else {
			int id = getEditor().getProgress().startEvent(
					"Loading Grid Grouper Service.... ");
			try {
				StemI root = grouper.getRootStem();
				StemTreeNode node = new StemTreeNode(getEditor(),
						((Stem) root), true);
				synchronized (getTree()) {
					this.add(node);
					getTree().reload(this);
				}
				node.loadStem();
				getEditor().getProgress().stopEvent(id,
						"Grid Grouper Service Successfully Loaded!!!");
				this.groupers.put(grouper.getName(), node);
			} catch (Exception e) {
				PortalUtils.showErrorMessage(e);
				getEditor().getProgress().stopEvent(id,
						"Error loading Grid Grouper Service!!!");
			}

		}

	}

	public synchronized void refresh() {
		Map old = groupers;
		groupers = new HashMap();
		this.removeAllChildren();
		Iterator itr = old.values().iterator();
		while (itr.hasNext()) {
			final StemTreeNode node = (StemTreeNode) itr.next();
			MobiusRunnable runner = new MobiusRunnable() {
				public void execute() {
					addGridGrouper(node.getGridGrouper());
				}
			};
			try {
				PortalResourceManager.getInstance().getThreadManager()
						.executeInBackground(runner);
			} catch (Exception t) {
				t.getMessage();
			}

		}

	}

	public void removeSelectedGridGrouper() {
		GridGrouperBaseTreeNode node = this.getTree().getCurrentNode();
		if (node == null) {
			PortalUtils
					.showErrorMessage("No service selected, please select a Grid Grouper Service!!!");
		} else {
			if (node instanceof StemTreeNode) {
				StemTreeNode stn = (StemTreeNode) node;
				if (stn.isRootStem()) {
					synchronized (getTree()) {
						stn.removeFromParent();
						this.groupers.remove(stn.getGridGrouper().getName());
						getTree().reload(this);
					}
				} else {
					PortalUtils
							.showErrorMessage("No service selected, please select a Grid Grouper Service!!!");
				}
			} else {
				PortalUtils
						.showErrorMessage("No service selected, please select a Grid Grouper Service!!!");
			}
		}

	}
	public ImageIcon getIcon() {
		return GridGrouperLookAndFeel.getGridGrouperServicesIcon16x16();
	}

	public String toString() {

		return "Grid Grouper Service(s)";
	}
}
