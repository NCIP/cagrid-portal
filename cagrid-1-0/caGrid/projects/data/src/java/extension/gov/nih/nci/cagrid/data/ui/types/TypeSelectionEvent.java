package gov.nih.nci.cagrid.data.ui.types;

import java.util.EventObject;

/** 
 *  TypeSelectionEvent
 *  Event object for type selection
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 21, 2006 
 * @version $Id$ 
 */
public class TypeSelectionEvent extends EventObject {

	public TypeSelectionEvent(TargetTypesTree tree) {
		super(tree);
	}
}
