/**
 * 
 */
package org.cagrid.installer.steps;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Condition;

/**
 * @author joshua
 *
 */
public class TreeNodeStep implements WizardStep, Condition {
	
	private WizardStep step;
	private Condition condition;
	
	public TreeNodeStep(WizardStep step){
		this(step, new Condition(){
			public boolean evaluate(WizardModel model) {
				return true;
			}
		});
	}
	
	public TreeNodeStep(WizardStep step, Condition condition){
		this.step = step;
		this.condition = condition;
	}

	public boolean evaluate(WizardModel model) {
		return condition.evaluate(model);
	}

	public void abortBusy() {
		step.abortBusy();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		step.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		step.addPropertyChangeListener(propertyName, listener);
	}

	public void applyState() throws InvalidStateException {
		step.applyState();
	}

	public Icon getIcon() {
		return step.getIcon();
	}

	public String getName() {
		return step.getName();
	}

	public Dimension getPreferredSize() {
		return step.getPreferredSize();
	}

	public String getSummary() {
		return step.getSummary();
	}

	public Component getView() {
		return step.getView();
	}

	public void init(WizardModel model) {
		step.init(model);
	}

	public boolean isBusy() {
		return step.isBusy();
	}

	public boolean isComplete() {
		return step.isComplete();
	}

	public void prepare() {
		step.prepare();
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		step.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		step.removePropertyChangeListener(propertyName, listener);
	}
	
}
