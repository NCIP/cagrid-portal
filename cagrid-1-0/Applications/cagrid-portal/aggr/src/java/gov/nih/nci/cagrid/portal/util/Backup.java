/**
 * 
 */
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class Backup {

	private static final String SEPARATOR = "@@@";
	private static final DateFormat DATE_FORMAT = DateFormat
			.getDateTimeInstance();
	private Session session;
	private File outDir;

	public Backup(Session session, String outDir) {
		this.session = session;
		this.outDir = new File(outDir);
	}

	public void backup() {
		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		List<GridService> gridServices = (List<GridService>) session
				.createQuery("from GridService").list();
		for (GridService gridService : gridServices) {
			if (gridService.getCurrentStatus().equals(ServiceStatus.ACTIVE)) {
				try {
					String url = gridService.getUrl();

					System.out.println("Backing up " + url);

					Metadata meta = PortalUtils.getMetadata(url, 10000);
					String urlHash = PortalUtils.createHash(url);
					File gsDir = new File(outDir.getAbsolutePath()
							+ "/services/" + urlHash);
					gsDir.mkdirs();
					File gsFile = new File(gsDir.getAbsolutePath()
							+ "/service.dat");
					IndexService idxSvc = gridService.getIndexServices().get(0);
					writeToFile(gsFile, gridService.getUrl() + SEPARATOR
							+ idxSvc.getUrl() + SEPARATOR
							+ gridService.getMetadataHash());

					StringWriter w = new StringWriter();
					MetadataUtils.serializeServiceMetadata(meta.smeta, w);
					File smFile = new File(gsDir.getAbsolutePath()
							+ "/serviceMetadata.xml");
					writeToFile(smFile, w.getBuffer().toString());
					if (meta.dmodel != null) {
						w = new StringWriter();
						File dmFile = new File(gsDir.getAbsolutePath()
								+ "/domainModel.xml");
						MetadataUtils.serializeDomainModel(meta.dmodel, w);
						writeToFile(dmFile, w.getBuffer().toString());
					}
					StringBuilder sb = new StringBuilder();

					for (StatusChange status : gridService.getStatusHistory()) {
						sb.append(DATE_FORMAT.format(status.getTime())).append(
								SEPARATOR).append(status.getStatus()).append(
								"\n");
					}
					File statusFile = new File(gsDir.getAbsolutePath()
							+ "/status.dat");
					writeToFile(statusFile, sb.toString());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		File queriesDir = new File(outDir.getAbsolutePath() + "/queries");
		queriesDir.mkdir();
		List<CQLQuery> queries = (List<CQLQuery>) session.createQuery(
				"from CQLQuery").list();
		for (CQLQuery query : queries) {
			File queryFile = new File(queriesDir.getAbsolutePath() + "/"
					+ query.getHash() + ".xml");
			writeToFile(queryFile, query.getXml());
		}

		File usersDir = new File(outDir.getAbsolutePath() + "/users");
		usersDir.mkdir();
		List<PortalUser> users = (List<PortalUser>) session.createQuery(
				"from PortalUser").list();
		for (PortalUser user : users) {
			String gridIdent = user.getGridIdentity();

			System.out.println("Backing up " + gridIdent);

			String gridIdentHash = PortalUtils.createHash(gridIdent);
			File userDir = new File(usersDir.getAbsolutePath() + "/"
					+ gridIdentHash);
			userDir.mkdir();
			File userDatFile = new File(userDir.getAbsolutePath() + "/user.dat");
			Person person = user.getPerson();
			writeToFile(userDatFile, user.getGridIdentity() + SEPARATOR
					+ person.getFirstName() + SEPARATOR + person.getLastName()
					+ SEPARATOR + person.getEmailAddress() + SEPARATOR
					+ person.getPhoneNumber());

			File sharedQueryDatFile = new File(userDir.getAbsolutePath()
					+ "/sharedQueries.dat");
			StringBuilder sharedQueryData = new StringBuilder();
			for (SharedCQLQuery sharedQuery : user.getSharedQueries()) {
				String targetClass = sharedQuery.getTargetClass()
						.getPackageName()
						+ "." + sharedQuery.getTargetClass().getClassName();
				String targetServiceHash = PortalUtils.createHash(sharedQuery
						.getTargetService().getUrl());
				String description = sharedQuery.getDescription();
				if (description != null) {
					try {
						StringBuilder b = new StringBuilder();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(new ByteArrayInputStream(
										description.getBytes())));
						String line = null;
						while ((line = br.readLine()) != null) {
							b.append(line).append(" ");
						}
						description = b.toString();
					} catch (Exception ex) {
						throw new RuntimeException("Error reading description: " + ex.getMessage(), ex);
					}
				}
				sharedQueryData.append(sharedQuery.getCqlQuery().getHash())
						.append(SEPARATOR).append(sharedQuery.getName())
						.append(SEPARATOR).append(
								DATE_FORMAT.format(sharedQuery.getShareDate()))
						.append(SEPARATOR).append(description)
						.append(SEPARATOR).append(targetClass)
						.append(SEPARATOR).append(targetServiceHash).append(
								"\n");
			}
			writeToFile(sharedQueryDatFile, sharedQueryData.toString());

			File queryInstancesDatFile = new File(userDir.getAbsolutePath()
					+ "/queryInstances.dat");
			StringBuilder queryInstancesData = new StringBuilder();
			for (QueryInstance instance : user.getQueryInstances()) {

				CQLQueryInstance cqlInstance = (CQLQueryInstance) instance;

				String userHash = PortalUtils.createHash(cqlInstance
						.getPortalUser().getGridIdentity());
				String serviceHash = PortalUtils.createHash(cqlInstance
						.getDataService().getUrl());
				String createTime = cqlInstance.getCreateTime() != null ? DATE_FORMAT
						.format(cqlInstance.getCreateTime())
						: "";
				String startTime = cqlInstance.getStartTime() != null ? DATE_FORMAT
						.format(cqlInstance.getStartTime())
						: "";
				String finishTime = cqlInstance.getFinishTime() != null ? DATE_FORMAT
						.format(cqlInstance.getFinishTime())
						: "";
				String result = cqlInstance.getResult() != null ? cqlInstance
						.getResult() : "";
				queryInstancesData.append(cqlInstance.getQuery().getHash())
						.append(SEPARATOR).append(userHash).append(SEPARATOR)
						.append(createTime).append(SEPARATOR).append(startTime)
						.append(SEPARATOR).append(finishTime).append(SEPARATOR)
						.append(result).append(SEPARATOR).append(serviceHash)
						.append("\n");
			}
			writeToFile(queryInstancesDatFile, queryInstancesData.toString());
		}
	}

	private void writeToFile(File file, String value) {
		try {
			FileWriter w = new FileWriter(file);
			w.write(value);
			w.flush();
			w.close();
		} catch (Exception ex) {
			throw new RuntimeException("Error writing to file: "
					+ ex.getMessage(), ex);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "classpath:applicationContext-db.xml" });
		HibernateTemplate templ = (HibernateTemplate) ctx
				.getBean("hibernateTemplate");
		final String outDir = args.length == 1 ? args[0] : "export";
		templ.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Backup b = new Backup(session, outDir);
				b.backup();
				return null;
			}
		});

	}

}
