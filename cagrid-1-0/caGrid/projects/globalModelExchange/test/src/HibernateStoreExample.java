import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.gme.domain.XMLSchemaDocument;
import org.cagrid.gme.service.domain.XMLSchemaInformation;
import org.cagrid.gme.test.GMEIntegrationTestCaseBase;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class HibernateStoreExample extends GMEIntegrationTestCaseBase {

    protected SessionFactory sessionFactory;


    public void testStore() throws Exception {
        Session s = sessionFactory.openSession();
        s.beginTransaction();

        XMLSchemaInformation schemaInfo = new XMLSchemaInformation();
        XMLSchema schema = new XMLSchema();
        schemaInfo.setSchema(schema);
        schema.setTargetNamespace(new URI("http://foo"));

        XMLSchemaDocument doc = new XMLSchemaDocument();
        doc.setSystemID("foo");
        doc.setSchemaText("my schema");

        XMLSchemaDocument doc2 = new XMLSchemaDocument();
        doc2.setSystemID("foo2");
        doc2.setSchemaText("my schema");

        schema.setRootDocument(doc);
        Set<XMLSchemaDocument> docSet = new HashSet<XMLSchemaDocument>();
        docSet.add(doc2);
        schema.setAdditionalSchemaDocuments(docSet);

        s.save(schemaInfo);

        XMLSchemaInformation schemaInfo2 = new XMLSchemaInformation();
        XMLSchema schema2 = new XMLSchema();
        schemaInfo2.setSchema(schema2);
        schema2.setTargetNamespace(new URI("http://foo2"));

        XMLSchemaDocument doc3 = new XMLSchemaDocument();
        doc3.setSystemID("foo");
        doc3.setSchemaText("my schema");

        schema2.setRootDocument(doc3);

        Set<XMLSchemaInformation> imports = new HashSet<XMLSchemaInformation>();
        imports.add(schemaInfo);
        schemaInfo2.setImports(imports);

        s.save(schemaInfo2);

        s.getTransaction().commit();

        // s.beginTransaction();
        // s.delete(schema);
        // s.getTransaction().commit();
        printSchemas(s);
        s.close();

        XMLSchemaInformation nschemaInfo = new XMLSchemaInformation();
        XMLSchema nschema = new XMLSchema();
        nschemaInfo.setSchema(nschema);
        nschema.setTargetNamespace(new URI("http://foo"));
        nschemaInfo.setId(schemaInfo.getId());

        XMLSchemaDocument ndoc = new XMLSchemaDocument();
        ndoc.setSystemID("foo");
        ndoc.setSchemaText("my NEW schema");
        nschema.setRootDocument(ndoc);

        XMLSchemaInformation nschemaInfo2 = new XMLSchemaInformation();
        XMLSchema nschema2 = new XMLSchema();
        nschemaInfo2.setSchema(nschema2);

        nschema2.setTargetNamespace(new URI("http://foo2"));
        nschemaInfo2.setId(schemaInfo2.getId());

        XMLSchemaDocument ndoc3 = new XMLSchemaDocument();
        ndoc3.setSystemID("foo2");
        ndoc3.setSchemaText("my schema");

        nschema2.setRootDocument(ndoc3);

        XMLSchemaInformation nschemaInfo3 = new XMLSchemaInformation();
        XMLSchema nschema3 = new XMLSchema();
        nschemaInfo3.setSchema(nschema3);

        nschema3.setTargetNamespace(new URI("http://foo3"));
        XMLSchemaDocument ndoc4 = new XMLSchemaDocument();
        ndoc4.setSystemID("foo3");
        ndoc4.setSchemaText("my schema");
        nschema3.setRootDocument(ndoc4);

        s = sessionFactory.openSession();
        s.beginTransaction();
        s.saveOrUpdate(nschemaInfo);
        s.saveOrUpdate(nschemaInfo2);
        s.saveOrUpdate(nschemaInfo3);
        s.getTransaction().commit();

        printSchemas(s);
        s.close();

    }


    private void printSchemas(Session s) {
        Criteria crit = s.createCriteria(XMLSchemaInformation.class);
        List schemas = crit.list();

        for (Iterator iterator = schemas.iterator(); iterator.hasNext();) {
            System.out.println("\n");
            XMLSchemaInformation pschemaInfo = (XMLSchemaInformation) iterator.next();
            XMLSchema pschema = pschemaInfo.getSchema();
            System.out.println(pschema.getTargetNamespace() + "\n==========");
            System.out.println("root document:");
            System.out.println("\t" + pschema.getRootDocument().getSystemID() + " = "
                + pschema.getRootDocument().getSchemaText());
            System.out.println("other documents:");
            Set<XMLSchemaDocument> schemaDocuments = pschema.getAdditionalSchemaDocuments();
            for (XMLSchemaDocument schemaDocument : schemaDocuments) {
                System.out.println("\t" + schemaDocument.getSystemID() + " = " + schemaDocument.getSchemaText());
            }
            System.out.println("imports:");
            for (XMLSchemaInformation imported : pschemaInfo.getImports()) {
                System.out.println("\t" + imported.getSchema().getTargetNamespace());
            }

        }
    }


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
