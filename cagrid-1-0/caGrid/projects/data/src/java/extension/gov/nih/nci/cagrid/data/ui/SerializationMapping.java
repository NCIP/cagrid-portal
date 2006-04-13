package gov.nih.nci.cagrid.data.ui;

import java.util.Vector;

import javax.swing.JButton;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

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
	private NamespaceType nsType;
	private SchemaElementType elemType;
	private String serializer;
	private String deserializer;
	private String encoding;

	public SerializationMapping(NamespaceType namespace, SchemaElementType elementType) {
		this(namespace, elementType, "", "", "");
	}
	
	
	public SerializationMapping(NamespaceType namespace, SchemaElementType elementType,
		String serializer, String deserializer, String encoding) {
		this.nsType = namespace;
		this.elemType = elementType;
		this.serializer = serializer;
		this.deserializer = deserializer;
		this.encoding = encoding;
	}


	/**
	 * @return Returns the deserializer.
	 */
	public String getDeserializer() {
		return deserializer;
	}


	/**
	 * @param deserializer The deserializer to set.
	 */
	public void setDeserializer(String deserializer) {
		this.deserializer = deserializer;
	}


	/**
	 * @return Returns the encoding.
	 */
	public String getEncoding() {
		return encoding;
	}


	/**
	 * @param encoding The encoding to set.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	/**
	 * @return Returns the serializer.
	 */
	public String getSerializer() {
		return serializer;
	}


	/**
	 * @param serializer The serializer to set.
	 */
	public void setSerializer(String serializer) {
		this.serializer = serializer;
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
		Vector v = new Vector(6);
		v.add(nsType.getNamespace());
		v.add(elemType.getType());
		v.add(elemType.getClassName());
		v.add(new JButton(serializer));
		v.add(new JButton(deserializer));
		v.add(new JButton(encoding));
		return v;
	}
}
