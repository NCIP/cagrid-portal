/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class BasicTask extends AbstractTask {

	/**
	 * @param name
	 * @param description
	 */
	public BasicTask(String name, String description) {
		super(name, description);
	}


	/* (non-Javadoc)
	 * @see org.cagrid.installer.tasks.Task#execute(java.util.Map)
	 */
	public Object execute(Map state) throws Exception {
		Object result = internalExecute(state);
		setLastStep(getStepCount());
		return result;
	}
	
	protected abstract Object internalExecute(Map state) throws Exception;
}
