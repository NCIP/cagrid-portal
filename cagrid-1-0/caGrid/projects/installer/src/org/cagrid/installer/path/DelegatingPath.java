/**
 * 
 */
package org.cagrid.installer.path;

import java.util.ArrayList;

import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.Path;
import org.pietschy.wizard.models.PathVisitor;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DelegatingPath implements NamedChainablePath {
	
	private String name;
	private String description;
	
	/**
	 * The path that will be delegated to.
	 */
	private ChainablePath path;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ChainablePath getPath() {
		return path;
	}
	public void setPath(ChainablePath path) {
		this.path = path;
	}
	public void acceptVisitor(PathVisitor visitor) {
		path.acceptVisitor(visitor);
	}
	public void addStep(WizardStep step) {
		path.addStep(step);
	}
	public boolean contains(WizardStep step) {
		return path.contains(step);
	}
	public WizardStep firstStep() {
		return path.firstStep();
	}
	public ArrayList getSteps() {
		return path.getSteps();
	}
	public boolean isFirstStep(WizardStep step) {
		return path.isFirstStep(step);
	}
	public boolean isLastStep(WizardStep step) {
		return path.isLastStep(step);
	}
	public WizardStep lastStep() {
		return path.lastStep();
	}
	public WizardStep nextStep(WizardStep currentStep) {
		return path.nextStep(currentStep);
	}
	public WizardStep previousStep(WizardStep currentStep) {
		return path.previousStep(currentStep);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setNextPath(Path path) {
		this.path.setNextPath(path);
	}

	
	
}
