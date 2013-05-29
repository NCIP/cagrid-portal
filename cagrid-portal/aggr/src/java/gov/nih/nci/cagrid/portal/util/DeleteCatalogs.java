/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipInstanceDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class. Will delete all un-published
 * catalogs. Un-Published catalogs are the ones
 * that are created automatically and not by users
 * through the UI.
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DeleteCatalogs {

    static Log logger = LogFactory.getLog(DeleteCatalogs.class);
    private ApplicationContext ctx;

    public DeleteCatalogs() {
        ctx = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db.xml"
                });
    }

    public static void main(String[] args) {
        DeleteCatalogs util = new DeleteCatalogs();
        util.deleteUnPublishedCatalogs();

    }

    /**
     * Will delete duplicate Information Models. Will delete the one
     * created by the Portal and not by a user.
     */
    public void deleteUnPublishedCatalogs() {


        logger.debug("Will delete all catalog entries which have not been published");

        HibernateTemplate templ = (HibernateTemplate) ctx
                .getBean("hibernateTemplate");

        CatalogEntryRelationshipInstanceDao relInstDao = (CatalogEntryRelationshipInstanceDao) ctx
                .getBean("catalogEntryRelationshipInstanceDao");

        List<CatalogEntry> allNullAuthorEntries = templ
                .find("from CatalogEntry ce where ce.published = false");

        for (CatalogEntry catalog : (Iterable<CatalogEntry>) allNullAuthorEntries) {
            deleteRelationships(templ, relInstDao, catalog);
            templ.delete(catalog);
        }

        logger.debug("Deleted " + allNullAuthorEntries.size() + " catalogs from the database.");
    }


    protected static void deleteRelationships(HibernateTemplate templ,
                                              CatalogEntryRelationshipInstanceDao relInstDao, CatalogEntry ce) {

        List<CatalogEntryRelationshipInstance> relInsts = relInstDao
                .getRelationshipsForCatalogEntry(ce.getId());

        final List<Integer> relIds = new ArrayList<Integer>();
        for (CatalogEntryRelationshipInstance rel : relInsts) {
            relIds.add(rel.getId());
        }
        if (relIds.size() > 0) {
            templ.execute(new HibernateCallback() {

                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {

                    Query query = session
                            .createQuery(
                                    "update CatalogEntryRelationshipInstance set roleA = null where id in (:ids)")
                            .setParameterList("ids", relIds);
                    query.executeUpdate();
                    session.flush();
                    query = session
                            .createQuery(
                                    "update CatalogEntryRelationshipInstance set roleB = null where id in (:ids)")
                            .setParameterList("ids", relIds);
                    query.executeUpdate();
                    session.flush();

                    query = session
                            .createQuery(
                                    "delete from CatalogEntryRoleInstance c where c.relationship.id in (:ids)")
                            .setParameterList("ids", relIds);
                    query.executeUpdate();
                    session.flush();

                    query = session
                            .createQuery(
                                    "delete from CatalogEntryRelationshipInstance c where c.id in (:ids)")
                            .setParameterList("ids", relIds);

                    query.executeUpdate();
                    session.flush();

                    return null;
                }

            });
        }

        // for (CatalogEntryRelationshipInstance rel : relInsts) {

        // CatalogEntryRoleInstance roleA = rel.getRoleA();
        // roleA.setRelationship(null);
        // roleA.setCatalogEntry(null);
        // templ.save(roleA);
        //
        // CatalogEntryRoleInstance roleB = rel.getRoleB();
        // roleB.setRelationship(null);
        // roleB.setCatalogEntry(null);
        // templ.save(roleB);
        //
        // rel.setRoleA(null);
        // rel.setRoleB(null);
        // relInstDao.save(rel);
        //
        // System.out.println("########### deleting roleA: " + roleA.getId());
        // templ.delete(roleA);
        // System.out.println("########### deleting roleB: " + roleB.getId());
        // templ.delete(roleB);

        // }

        // for (CatalogEntryRelationshipInstance r : relInsts) {
        // CatalogEntryRelationshipInstance rel = relInstDao
        // .getById(r.getId());
        // if (rel.getRoleA() != null) {
        // throw new RuntimeException("roleA still exists for "
        // + rel.getId());
        // }
        // if (rel.getRoleB() != null) {
        // throw new RuntimeException("roleB still exists for "
        // + rel.getId());
        // }
        // try {
        // System.out.println("########### deleting rel: " + rel.getId());
        // relInstDao.delete(rel);
        // } catch (Throwable t) {
        // throw new RuntimeException("Error deleting relationship: "
        // + rel.getId());
        // }
        // }
    }
}
