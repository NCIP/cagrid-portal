package org.cagrid.gme.persistance.test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.domain.XMLSchemaDocument;
import org.cagrid.gme.test.GMEIntegrationTestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class HibernateStoreTestCase extends GMEIntegrationTestCase {

    private SessionFactory sessionFactory;


    public void testStore() throws Exception {
        Session s = sessionFactory.openSession();
        s.beginTransaction();

        XMLSchema schema = new XMLSchema();
        schema.setTargetNamespace(new URI("http://foo"));

        XMLSchemaDocument doc = new XMLSchemaDocument();
        doc.setSystemID("foo");
        doc.setSchemaText("my schema");

        XMLSchemaDocument doc2 = new XMLSchemaDocument();
        doc2.setSystemID("foo2");
        doc2.setSchemaText("my schema");

        Set<XMLSchemaDocument> docSet = new HashSet<XMLSchemaDocument>();
        docSet.add(doc);
        docSet.add(doc2);
        schema.setSchemaDocuments(docSet);

        s.save(schema);

        XMLSchema schema2 = new XMLSchema();
        schema2.setTargetNamespace(new URI("http://foo2"));

        XMLSchemaDocument doc3 = new XMLSchemaDocument();
        doc3.setSystemID("foo");
        doc3.setSchemaText("my schema");

        Set<XMLSchemaDocument> docSet2 = new HashSet<XMLSchemaDocument>();
        docSet2.add(doc3);
        schema2.setSchemaDocuments(docSet2);

        s.save(schema2);

        s.getTransaction().commit();

        // s.beginTransaction();
        // s.delete(schema);
        // s.getTransaction().commit();

    }


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
