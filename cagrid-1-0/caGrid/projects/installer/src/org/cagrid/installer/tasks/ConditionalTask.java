/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConditionalTask implements Condition, Task {
	
	private Task task;
	private Condition condition;

	public ConditionalTask(Task task){
		this(task, new Condition(){

			public boolean evaluate(WizardModel arg0) {
				return true;
			}
			
		});
	}
	
	public ConditionalTask(Task task, Condition condition){
		this.task = task;
		this.condition = condition;
	}

	/* (non-Javadoc)
	 * @see org.pietschy.wizard.models.Condition#evaluate(org.pietschy.wizard.WizardModel)
	 */
	public boolean evaluate(WizardModel model) {
		return this.condition.evaluate(model);
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.tasks.Task#execute(java.util.Map)
	 */
	public Object execute(Map state) throws Exception {
		return this.task.execute(state);
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.tasks.Task#getDescription()
	 */
	public String getDescription() {
		return this.task.getDescription();
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.tasks.Task#getName()
	 */
	public String getName() {
		return this.task.getName();
	}

}
