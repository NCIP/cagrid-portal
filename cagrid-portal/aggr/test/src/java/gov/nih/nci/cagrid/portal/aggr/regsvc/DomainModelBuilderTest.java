/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.*;
import junit.framework.TestCase;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.FileReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class DomainModelBuilderTest extends TestCase {

    /**
     *
     */
    public DomainModelBuilderTest() {

    }

    /**
     * @param name
     */
    public DomainModelBuilderTest(String name) {
        super(name);

    }

    public void testValidModel() {

        try {
            TestDB.create();
            ApplicationContext ctx = TestDB.getApplicationContext();
            HibernateTemplate templ = (HibernateTemplate) ctx
                    .getBean("hibernateTemplate");
            gov.nih.nci.cagrid.metadata.dataservice.DomainModel modelIn = MetadataUtils
                    .deserializeDomainModel(new FileReader(
                            "test/data/domainModel.xml"));
            GridDataService svc = new GridDataService();
            svc.setUrl("http://some.org");
            templ.save(svc);
            DomainModelBuilder builder = new DomainModelBuilder();
            builder.setGridService(svc);
            builder.setHibernateTemplate(templ);
            builder.setPersist(true);
            DomainModel modelOut = builder.build(modelIn);
            templ.save(modelOut);
            templ.flush();

            final Integer id = modelOut.getId();
            final Set<String> roleNames = new HashSet<String>();
            roleNames.add("terms");
            roleNames.add("proteins");
            roleNames.add("chromosome");

            templ.execute(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    DomainModel model = (DomainModel) session.get(
                            DomainModel.class, id);
                    String className = "Gene";

                    for (UMLClass umlClass : model.getClasses()) {
                        if (umlClass.getClassName().equals(className)) {
                            for (UMLAssociationEdge edge : umlClass
                                    .getAssociations()) {
                                UMLAssociation assoc = null;
                                if (edge instanceof SourceUMLAssociationEdge) {
                                    assoc = ((SourceUMLAssociationEdge) edge)
                                            .getAssociation();
                                } else if (edge instanceof TargetUMLAssociationEdge) {
                                    assoc = ((TargetUMLAssociationEdge) edge)
                                            .getAssociation();
                                }
                                if (!assoc.getSource().getType().getClassName()
                                        .equals(className)) {
                                    String role = assoc
                                            .getSource().getRole();
                                    System.out.println("Removed role " + role + ", "
                                            + roleNames.remove(role));
                                } else if (!assoc.getTarget().getType()
                                        .getClassName().equals(className)) {
                                    String role = assoc
                                            .getTarget().getRole();
                                    System.out.println("Removed role " + role + ", "
                                            + roleNames.remove(role));
                                } else if (assoc.getTarget().getType()
                                        .getClassName().equals(className)
                                        && assoc.getSource().getType()
                                        .getClassName().equals(
                                                className)) {
                                    String source = assoc
                                            .getSource().getRole();
                                    String target = assoc
                                            .getTarget().getRole();
                                    System.out.println("Removed role " + target + ", "
                                            + roleNames.remove(target)
                                            + " and " + source + ", "
                                            + roleNames.remove(source));
                                }
                            }
                        }
                    }
                    return null;
                }
            });

            assertTrue("Didn't find the following target roles: " + roleNames,
                    roleNames.size() == 0);
            TestDB.drop();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error encountered: " + ex.getMessage());
        }

    }
}
