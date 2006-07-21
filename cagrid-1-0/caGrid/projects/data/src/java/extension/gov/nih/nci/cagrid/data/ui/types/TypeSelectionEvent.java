package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
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
	private NamespaceType namespace;
	private SchemaElementType type;

	public TypeSelectionEvent(TargetTypesTree tree, NamespaceType namespace, SchemaElementType type) {
		super(tree);
		this.namespace = namespace;
		this.type = type;
	}
	
	
	public NamespaceType getNamespaceType() {
		return namespace;
	}
	
	
	public SchemaElementType getSchemaElementType() {
		return type;
	}
}
