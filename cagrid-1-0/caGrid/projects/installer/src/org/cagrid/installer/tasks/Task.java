/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface Task {
	
	String getName();
	String getDescription();
	Object execute(Map state) throws Exception;

}
