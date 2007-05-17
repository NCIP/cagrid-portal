/**
 * 
 */
package org.cagrid.installer.path;

import org.pietschy.wizard.models.Path;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface NamedPath extends Path {
	
	String getName();
	String getDescription();

}
