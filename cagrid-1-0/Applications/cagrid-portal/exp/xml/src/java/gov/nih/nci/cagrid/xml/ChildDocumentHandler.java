/**
 * 
 */
package gov.nih.nci.cagrid.xml;

import java.util.Collection;

import org.w3c.dom.Document;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface ChildDocumentHandler {
	void insert(String className, ObjectPath path, Document parent, Document child);
	void insert(String classname, ObjectPath path, Document parent, Collection<Document> children);
}
