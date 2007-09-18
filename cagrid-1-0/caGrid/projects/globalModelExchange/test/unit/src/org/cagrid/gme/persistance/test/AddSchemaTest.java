package org.cagrid.gme.persistance.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.cagrid.gme.persistence.torque.generated.NamespaceImportMap;
import org.cagrid.gme.persistence.torque.generated.SchemaEntity;
import org.cagrid.gme.persistence.torque.generated.SchemaEntityPeer;
import org.cagrid.gme.persistence.torque.generated.SchemaText;



public class AddSchemaTest extends TestCase {
	static {

		try {
			Torque.init("etc/torque.properties");
		} catch (TorqueException e) {
			e.printStackTrace();
		}
	}


	public void testInsert() {

		// This is the first way that works:
		// 1) first I setup the schema,
		// 2) then i create a text and add the schema to it (as each text may be
		// referenced by many schemas according to our model)
		// 3) then i save the text and it automatically saves the schema

		try {
			// setup the entity
			SchemaEntity en = new SchemaEntity();
			String ns = "http://www.foo.bar/testing/" + System.currentTimeMillis();
			en.setNamespaceUri(ns);

			// setup the text
			SchemaText text = new SchemaText();
			text.setSchemaText("I was created by testInsert at:" + System.currentTimeMillis());
			text.addSchemaEntity(en);
			text.save();

			SchemaEntity en2 = new SchemaEntity();
			String ns2 = "http://www.foobar.org/testing/" + System.currentTimeMillis();
			en2.setNamespaceUri(ns2);

			// setup the text
			SchemaText text2 = new SchemaText();
			text2.setSchemaText("I was created by testInsert at:" + System.currentTimeMillis());
			text2.addSchemaEntity(en2);
			text2.save();

			// imports itself
			NamespaceImportMap nsImport = new NamespaceImportMap();
			nsImport.setSchemaEntityRelatedBySchemaId(SchemaEntityPeer.getByNamespace(ns));
			nsImport.setSchemaEntityRelatedByImportedSchemaId(SchemaEntityPeer.getByNamespace(ns));
			nsImport.save();

			// imports the second one
			NamespaceImportMap nsImport2 = new NamespaceImportMap();
			nsImport2.setSchemaEntityRelatedBySchemaId(SchemaEntityPeer.getByNamespace(ns));
			nsImport2.setSchemaEntityRelatedByImportedSchemaId(SchemaEntityPeer.getByNamespace(ns2));
			nsImport2.save();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		System.out.println("=====TEST INSERT=====");
		printSchemas();

	}


	private void printSchemas() {
		Criteria crit = new Criteria();
		List schemas = null;
		try {
			schemas = SchemaEntityPeer.doSelect(crit);
			for (Iterator i = schemas.iterator(); i.hasNext();) {
				SchemaEntity schema = (SchemaEntity) i.next();
				System.out.println("Found schema(" + schema.getId() + "), with URL=" + schema.getNamespaceUri());
				System.out.println("\t\tTEXT=" + schema.getSchemaText().getSchemaText());
			}
		} catch (TorqueException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testInsert2() {
		// This is the second way that works:
		// 1) first I setup and save the text,
		// 2) then i create the schema and set its text object
		// 3) then i save the schema

		try {
			// save the text first
			SchemaText text = new SchemaText();
			text.setSchemaText("I was created by testInsert2 at:" + System.currentTimeMillis());
			text.save();

			// setup the entity, referencing the text
			SchemaEntity en = new SchemaEntity();
			en.setNamespaceUri("http://www.foo.bar/testing/" + System.currentTimeMillis());
			en.setSchemaText(text);
			en.save();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		System.out.println("=====TEST INSERT 2=====");
		printSchemas();
	}

}
