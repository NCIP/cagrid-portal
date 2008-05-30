/**
 * 
 */
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cadsr.domain.Context;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.aggr.MetadataThread;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.SourceUMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.TargetUMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NonUniqueResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.protocol.gme.SchemaNode;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
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

	public static List<XMLSchema> getXMLSchemas(DomainModel domainModel,
			String cadsrUrl, String gmeUrl) {
		List<XMLSchema> schemas = new ArrayList<XMLSchema>();

		Project proj = new Project();
		proj.setShortName(domainModel.getProjectShortName());
		proj.setVersion(domainModel.getProjectVersion());
		String context = "caBIG";
		try {
			CaDSRServiceI cadsrService = new CaDSRServiceClient(cadsrUrl);
			Context ctx = cadsrService.findContextForProject(proj);
			context = ctx.getName();
		} catch (Exception ex) {
			// logger.warn("Coudn't get context from project '"
			// + proj.getShortName() + "': " + ex.getMessage()
			// + ". Using '" + context + "'.", ex);
		}
		String projectVersion = proj.getVersion();
		if(projectVersion == null){
			projectVersion = "1.0";
		}
		if (projectVersion.indexOf(".") < 0) {
			projectVersion += ".0";
		}
		Set<String> packageNames = new HashSet<String>();
		for (UMLClass klass : domainModel.getClasses()) {
			packageNames.add(klass.getPackageName());
		}
		for (String packageName : packageNames) {
			String schemaUrl = "gme://" + proj.getShortName() + "." + context
					+ "/" + projectVersion + "/" + packageName;
			String schemaContents = getXmlSchemaContent(schemaUrl, gmeUrl);
			if (schemaContents != null) {
				XMLSchema xmlSchema = new XMLSchema();
				xmlSchema.setNamespace(schemaUrl);
				xmlSchema.setSchemaContent(schemaContents);
				schemas.add(xmlSchema);
			}
		}
		return schemas;
	}

	public static String getXmlSchemaContent(String namespace, String gmeUrl) {
		String content = null;
		try {
			Namespace ns = new Namespace(namespace);
			GridServiceResolver.getInstance().setDefaultFactory(
					new GlobusGMEXMLDataModelServiceFactory());
			XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
					.getInstance().getGridService(gmeUrl);
			SchemaNode schema = handle.getSchema(ns, false);
			content = schema.getSchemaContents();
		} catch (Exception ex) {
			// logger.warn("Error getting XML schema with namespace '" +
			// namespace
			// + "': " + ex.getMessage());
		}
		return content;
	}

	public static XMLSchema getXMLSchemaForQName(HibernateTemplate templ,
			String qName, String gmeUrl) {
		XMLSchema xmlSchema = null;
		try {
			int idx = qName.indexOf("{");
			if (idx != -1) {
				String namespace = qName.substring(idx + 1, qName.indexOf("}",
						idx + 1));
				XMLSchema eg = new XMLSchema();
				eg.setNamespace(namespace);
				eg = (XMLSchema) PortalUtils.getByExample(templ, eg);
				if (eg == null) {
					String content = PortalUtils.getXmlSchemaContent(namespace,
							gmeUrl);
					if (content != null) {
						xmlSchema = new XMLSchema();
						xmlSchema.setNamespace(namespace);
						xmlSchema.setSchemaContent(content);
					}
				} else {
					xmlSchema = eg;
				}
			}
		} catch (Exception ex) {
			// logger.warn("Couldn't get XMLSchema for QName '" + qName + "': "
			// + ex.getMessage());
		}
		if (xmlSchema != null) {
			logger.debug("######### Found schema for QName: " + qName);
		}
		return xmlSchema;
	}
	
	public static List<UMLAssociationEdge> getOtherEdges(String className, List<UMLAssociationEdge> edges){
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


	public static void main(String[] args) throws Exception {

		// System.out.println(createHash("Howdy!"));
//		String url = "https://cagrid-cccwfu.wfubmc.edu:8443/wsrf/share/schema/CaaersDataService";
		String url = "https://cagrid-cccwfu.wfubmc.edu:8443/wsrf/share/schema/CaaersDataService";
		Metadata m = getMetadata(
				url,
				10000);
		StringWriter w = new StringWriter();
		MetadataUtils.serializeDomainModel(m.dmodel, w);
		System.out.println(w.getBuffer());

	}

}
