package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

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
	private SchemaElementType type;

	public TypeSelectionEvent(TargetTypesTree tree, SchemaElementType type) {
		super(tree);
		this.type = type;
	}
	
	
	public SchemaElementType getSchemaElementType() {
		return type;
	}
}
