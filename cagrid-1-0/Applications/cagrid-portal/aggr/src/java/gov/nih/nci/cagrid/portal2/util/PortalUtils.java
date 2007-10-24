/**
 * 
 */
package gov.nih.nci.cagrid.portal2.util;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal2.aggr.MetadataThread;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class PortalUtils {

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
					// return session.createCriteria(egObj.getClass())
					// .add(example).list();
				}
			});
		}
	}

	public static boolean isEmpty(String email) {
		return email == null || email.trim().length() == 0;
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

	public static Metadata getMetadata(String serviceUrl, long timeout) {
		Metadata meta = new Metadata();
		MetadataThread t = new MetadataThread(serviceUrl);
		t.start();
		try {
			t.join(timeout);
		} catch (InterruptedException ex) {
			throw new RuntimeException("Metadata thread interrupted");
		}
		if (t.getEx() != null) {
			throw new RuntimeException("Metadata thread encountered error: "
					+ t.getEx().getMessage(), t.getEx());
		} else if (!t.isFinished()) {
			throw new RuntimeException("Metadata query to " + serviceUrl
					+ " timed out.");
		}
		meta.smeta = t.getServiceMetadata();
		meta.dmodel = t.getDomainModel();

		return meta;
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
	

	public static void main(String[] args) throws Exception {

//		System.out.println(createHash("Howdy!"));
		Metadata m = getMetadata("http://caintegrator-stage.nci.nih.gov/wsrf/services/cagrid/CgomCgems", 10000);
		StringWriter w = new StringWriter();
		MetadataUtils.serializeDomainModel(m.dmodel, w);
		System.out.println(w.getBuffer());

	}

}
