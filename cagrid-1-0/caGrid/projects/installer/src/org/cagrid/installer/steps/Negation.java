/**
 * 
 */
package org.cagrid.installer.steps;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class Negation implements Condition {
	
	private Condition condition;
	
	public Negation(Condition condition){
		this.condition = condition;
	}

	/* (non-Javadoc)
	 * @see org.pietschy.wizard.models.Condition#evaluate(org.pietschy.wizard.WizardModel)
	 */
	public boolean evaluate(WizardModel model) {
		return !this.condition.evaluate(model);
	}

}
