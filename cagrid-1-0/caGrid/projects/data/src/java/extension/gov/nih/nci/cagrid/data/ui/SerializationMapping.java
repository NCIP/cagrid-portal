package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import java.util.Vector;

/** 
 *  SerializationMapping
 *  Container class to maintain mapping from Schema element type to
 *  the serializer / deserializer for it
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 13, 2006 
 * @version $Id$ 
 */
public class SerializationMapping {
	public static final String SDK_SERIALIZER = "gov.nih.nci.cagrid.encoding.SDKSerializerFactory";
	public static final String SDK_DESERIALIZER = "gov.nih.nci.cagrid.encoding.SDKDeserializerFactory";
	
	private NamespaceType nsType;
	private SchemaElementType elemType;

	public SerializationMapping(NamespaceType namespace, SchemaElementType elementType) {
		this.nsType = namespace;
		this.elemType = elementType;
	}


	/**
	 * @return Returns the elemType.
	 */
	public SchemaElementType getElemType() {
		return elemType;
	}


	/**
	 * @return Returns the nsType.
	 */
	public NamespaceType getNsType() {
		return nsType;
	}
	
	
	public Vector toVector() {
		Vector v = new Vector(5);
		v.add(nsType.getNamespace());
		v.add(elemType.getType());
		v.add(elemType.getClassName());
		v.add(elemType.getSerializer());
		v.add(elemType.getDeserializer());
		return v;
	}
}
