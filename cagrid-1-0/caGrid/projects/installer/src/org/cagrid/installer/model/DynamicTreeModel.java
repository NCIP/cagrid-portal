/**
 * 
 */
package org.cagrid.installer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

import org.pietschy.wizard.AbstractWizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DynamicTreeModel extends AbstractWizardModel implements CaGridInstallerModel {

	private List<DefaultMutableTreeNode> rootNodes;

	private DefaultMutableTreeNode activeNode;

	private Stack<DefaultMutableTreeNode> history = new Stack<DefaultMutableTreeNode>();

	private Map state = new HashMap();

	public DynamicTreeModel(List<DefaultMutableTreeNode> rootNodes,
			WizardStep lastStep) {
		this.rootNodes = rootNodes;
		this.rootNodes.add(new DefaultMutableTreeNode(lastStep));
		for (DefaultMutableTreeNode rootNode : rootNodes) {
			Enumeration e = rootNode.preorderEnumeration();
			while (e.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
						.nextElement();
				WizardStep step = (WizardStep) node.getUserObject();
				addCompleteListener(step);
			}
		}
	}

	public List<DefaultMutableTreeNode> getRootNodes() {
		return rootNodes;
	}

	public Map getState() {
		return state;
	}
	public void setState(Map state){
		this.state = state;
	}

	public boolean isLastStep(WizardStep step) {
		return step == getLastStep();
	}

	public void lastStep() {
		DefaultMutableTreeNode lastNode = getLastNode();
		setActiveNode(lastNode);
		setActiveStep((WizardStep) lastNode.getUserObject());
	}

	public void nextStep() {
		this.history.push(getActiveNode());
		DefaultMutableTreeNode node = findNextNode();
		setActiveNode(node);
		setActiveStep((WizardStep) node.getUserObject());
	}

	private DefaultMutableTreeNode getNextNode(DefaultMutableTreeNode node){
		DefaultMutableTreeNode nextNode = node.getNextNode();
		if (nextNode == null) {
			int idx = getRootNodes().indexOf(node);
			if(idx + 1 < getRootNodes().size()){
				nextNode = getRootNodes().get(idx + 1);
			}
		}
		return nextNode;
	}
	
	protected DefaultMutableTreeNode findNextNode() {
		DefaultMutableTreeNode nextNode = getNextNode(getActiveNode());
		boolean evalNode = false;
		while (!evalNode && nextNode != null) {
			WizardStep step = (WizardStep) nextNode.getUserObject();
			System.out.println("Checking " + step.getName());
			if (step instanceof Condition) {
				System.out.println("Evaluating step " + step.getName());
				evalNode = ((Condition) step).evaluate(this);
			} else {
				evalNode = true;
			}
			if (!evalNode) {
				nextNode = getNextNode(nextNode);
			}
		}
		if (nextNode == null) {
			System.out.println("nextNode is null, using lastStep");
			nextNode = getLastNode();
		}
		return nextNode;
	}

	public void previousStep() {
		DefaultMutableTreeNode node = this.history.pop();
		setActiveNode(node);
		setActiveStep((WizardStep) node.getUserObject());
	}

	public void reset() {
		this.history.clear();
		DefaultMutableTreeNode firstNode = getRootNodes().get(0);
		setActiveNode(firstNode);
		setActiveStep((WizardStep) firstNode.getUserObject());
	}

	public Iterator stepIterator() {
		List<WizardStep> steps = new ArrayList<WizardStep>();
		for (DefaultMutableTreeNode rootNode : getRootNodes()) {
			Enumeration e = rootNode.preorderEnumeration();
			while (e.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
						.nextElement();
				steps.add((WizardStep) node.getUserObject());
			}
		}
		return Collections.unmodifiableList(steps).iterator();
	}

	public DefaultMutableTreeNode getActiveNode() {
		return activeNode;
	}

	public void setActiveNode(DefaultMutableTreeNode activeNode) {
		this.activeNode = activeNode;
	}

	public WizardStep getLastStep() {
		WizardStep lastStep = (WizardStep) getLastNode().getUserObject();
		return lastStep;
	}

	public DefaultMutableTreeNode getLastNode() {
		List<DefaultMutableTreeNode> nodes = getRootNodes();
		DefaultMutableTreeNode node = nodes.get(nodes.size() - 1);
		return node;
	}

	public boolean allStepsComplete() {
		for (Iterator i = stepIterator(); i.hasNext();) {
			WizardStep step = (WizardStep) i.next();
			if (step instanceof Condition && !((Condition) step).evaluate(this)) {
				continue;
			}
			if (!step.isComplete()) {
				return false;
			}
		}
		return true;
	}

	public void refreshModelState() {
		WizardStep activeStep = getActiveStep();
		setNextAvailable(activeStep != null && activeStep.isComplete()
				&& !isLastStep(activeStep));
		setPreviousAvailable(activeStep != null && !this.history.isEmpty());
		setLastAvailable(activeStep != null && allStepsComplete()
				&& !isLastStep(activeStep));
		setCancelAvailable(true);
	}

	public String getMessage(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}
