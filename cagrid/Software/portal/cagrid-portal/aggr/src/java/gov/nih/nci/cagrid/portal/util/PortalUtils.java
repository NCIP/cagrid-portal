/**
 *
 */
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.SourceUMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.TargetUMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociationEdge;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import javax.persistence.NonUniqueResultException;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class PortalUtils {

    private static final Log logger = LogFactory.getLog(PortalUtils.class);

    public static Object getByExample(HibernateTemplate templ, Object egObj) {
        Object result = null;
        List results = searchByExample(templ, egObj, false);
        if (results.size() > 1) {
            throw new NonUniqueResultException("Found " + results.size() + " "
                    + egObj.getClass().getName() + " objects.");
        } else if (results.size() == 1) {
            result = results.get(0);
        }
        return result;
    }

    public static List searchByExample(HibernateTemplate templ,
                                       final Object egObj, final boolean inexactMatches) {
        if (egObj == null) {
            return new ArrayList();
        } else {
            return (List) templ.execute(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    Example example = Example.create(egObj).excludeZeroes();
                    if (inexactMatches) {
                        example.ignoreCase().enableLike(MatchMode.ANYWHERE);
                    }
                    return session.createCriteria(egObj.getClass())
                            .add(example).setFlushMode(FlushMode.MANUAL).list();
                }
            });
        }
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0 || text.trim().equals("null");
    }

    public static String createHash(String in) {
        String out = "";
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(in.getBytes());
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = bais.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            bais.close();

            byte[] b = complete.digest();
            for (int i = 0; i < b.length; i++) {
                out += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error creating hash: "
                    + ex.getMessage(), ex);
        }
        return out;
    }

    public static String createHashFromMetadata(Metadata meta) {
        try {
            StringWriter w = new StringWriter();
            MetadataUtils.serializeServiceMetadata(meta.smeta, w);
            if (meta.dmodel != null) {
                MetadataUtils.serializeDomainModel(meta.dmodel, w);
            }
            return createHash(w.getBuffer().toString());
        } catch (Exception ex) {
            throw new RuntimeException("Error creating hash from metadata: "
                    + ex.getMessage(), ex);
        }
    }

    public static List<UMLAssociationEdge> getOtherEdges(String className, List<UMLAssociationEdge> edges) {
        Set<UMLAssociationEdge> otherEdges = new HashSet<UMLAssociationEdge>();
        for (UMLAssociationEdge assocEdge : edges) {

            UMLAssociation assoc = null;
            if (assocEdge instanceof SourceUMLAssociationEdge) {
                assoc = ((SourceUMLAssociationEdge) assocEdge)
                        .getAssociation();
            } else if (assocEdge instanceof TargetUMLAssociationEdge) {
                assoc = ((TargetUMLAssociationEdge) assocEdge)
                        .getAssociation();
            }
            if (!assoc.getSource().getType().getClassName().equals(
                    className)) {
                if (!isEmpty(assoc.getSource().getRole())) {
                    otherEdges.add(assoc.getSource());
                }
            } else if (!assoc.getTarget().getType().getClassName().equals(
                    className)) {
                if (!isEmpty(assoc.getTarget().getRole())) {
                    otherEdges.add(assoc.getTarget());
                }
            } else if (assoc.getTarget().getType().getClassName().equals(
                    className)
                    && assoc.getSource().getType().getClassName().equals(
                    className)) {
                if (!isEmpty(assoc.getSource().getRole())) {
                    otherEdges.add(assoc.getSource());
                }
                if (!isEmpty(assoc.getTarget().getRole())) {
                    otherEdges.add(assoc.getTarget());
                }
            }
        }

        return new ArrayList<UMLAssociationEdge>(otherEdges);
    }

}
