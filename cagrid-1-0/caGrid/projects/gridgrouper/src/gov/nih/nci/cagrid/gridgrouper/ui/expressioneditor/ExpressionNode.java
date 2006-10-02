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

package gov.nih.nci.cagrid.gridgrouper.ui.expressioneditor;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridgrouper.bean.LogicalOperator;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipQuery;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipStatus;
import gov.nih.nci.cagrid.gridgrouper.client.Group;
import gov.nih.nci.cagrid.gridgrouper.ui.GridGrouperLookAndFeel;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class ExpressionNode extends ExpressionBaseTreeNode {

	private MembershipExpression expression;

	private boolean rootStem;

	public ExpressionNode(GridGrouperExpressionEditor editor,
			MembershipExpression expression, boolean root) {
		super(editor);
		this.rootStem = root;
		this.expression = expression;
	}
	
	public synchronized void clearExpression(){
		expression.setMembershipExpression(null);
		expression.setMembershipQuery(null);
		super.removeAllChildren();
	}
	
	public synchronized void resetExpression(MembershipExpression expression){
		super.removeAllChildren();
		this.expression = expression;
		this.refresh();
	}

	public void loadExpression() {
		this.removeAllChildren();
		MembershipExpression[] exps = this.expression.getMembershipExpression();
		if (exps != null) {
			for (int i = 0; i < exps.length; i++) {
				ExpressionNode node = new ExpressionNode(getEditor(), exps[i],
						false);
				synchronized (getTree()) {
					this.add(node);
					TreeNode parent = this.getParent();
					if (parent != null) {
						getTree().reload(parent);
					} else {
						getTree().reload();
					}
				}
				node.loadExpression();
			}
		}
		MembershipQuery[] queries = this.expression.getMembershipQuery();
		if (queries != null) {
			for (int i = 0; i < queries.length; i++) {
				QueryNode node = new QueryNode(getEditor(), queries[i]);
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
	}

	public void addGroup(Group grp) {
		MembershipQuery[] mq = expression.getMembershipQuery();
		int size = 0;
		if (mq != null) {
			size = mq.length;
		}
		MembershipQuery[] nmq = new MembershipQuery[size + 1];
		for (int i = 0; i < size; i++) {
			if (mq[i].getGroupIdentifier().equals(grp.getGroupIdentifier())) {
				PortalUtils.showErrorMessage("The group "
						+ grp.getDisplayName()
						+ " has already exists in the expression!!!");
				return;
			}
			nmq[i] = mq[i];
		}
		nmq[size] = new MembershipQuery();
		nmq[size].setGroupIdentifier(grp.getGroupIdentifier());
		nmq[size].setMembershipStatus(MembershipStatus.MEMBER_OF);
		expression.setMembershipQuery(nmq);
		loadExpression();
	}

	public void addAndExpression() {
		MembershipExpression exp = new MembershipExpression();
		exp.setLogicRelation(LogicalOperator.AND);
		addExpression(exp);
	}

	public void addOrExpression() {
		MembershipExpression exp = new MembershipExpression();
		exp.setLogicRelation(LogicalOperator.OR);
		addExpression(exp);
	}

	public void addExpression(MembershipExpression exp) {
		MembershipExpression[] me = expression.getMembershipExpression();
		int size = 0;
		if (me != null) {
			size = me.length;
		}
		MembershipExpression[] nme = new MembershipExpression[size + 1];
		for (int i = 0; i < size; i++) {
			nme[i] = me[i];
		}
		nme[size] = exp;
		expression.setMembershipExpression(nme);
		loadExpression();
	}

	public void removeExpression(MembershipExpression exp) {
		MembershipExpression[] me = expression.getMembershipExpression();
		MembershipExpression[] nme = new MembershipExpression[me.length - 1];
		if (me.length == 1) {
			expression.setMembershipExpression(null);
			refresh();
			return;
		} else {
			int count = 0;
			for (int i = 0; i < me.length; i++) {
				if (me[i] != exp) {
					nme[count] = me[i];
					count = count + 1;
				}
			}
			expression.setMembershipExpression(nme);
			TreePath currentSelection = getEditor().getExpressionTree()
					.getSelectionPath();
			getEditor().getExpressionTree().setSelectionPath(
					currentSelection.getParentPath());
			loadExpression();
		}
	}

	public void removeGroup(MembershipQuery query) {
		MembershipQuery[] qe = expression.getMembershipQuery();
		MembershipQuery[] nqe = new MembershipQuery[qe.length - 1];
		if (qe.length == 1) {
			expression.setMembershipQuery(null);
			refresh();
			return;
		} else {
			int count = 0;
			for (int i = 0; i < qe.length; i++) {
				if (qe[i] != query) {
					nqe[count] = qe[i];
					count = count + 1;
				}
			}
			expression.setMembershipQuery(nqe);
			TreePath currentSelection = getEditor().getExpressionTree()
					.getSelectionPath();
			getEditor().getExpressionTree().setSelectionPath(
					currentSelection.getParentPath());
			loadExpression();
		}
	}

	public void refresh() {
		loadExpression();
		TreeNode parent = this.getParent();
		if (parent != null) {
			getTree().reload(parent);
		} else {
			getTree().reload();
		}
	}

	public ImageIcon getIcon() {
		return GridGrouperLookAndFeel.getStemIcon16x16();
	}

	public String toString() {
		return expression.getLogicRelation().getValue();
	}

	public boolean isRootExpression() {
		return rootStem;
	}

	public MembershipExpression getExpression() {
		return expression;
	}

}
