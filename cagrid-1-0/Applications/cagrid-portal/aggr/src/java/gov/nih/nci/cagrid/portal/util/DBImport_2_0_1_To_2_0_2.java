/**
 * 
 */
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.aggr.regsvc.DomainModelBuilder;
import gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceMetadataBuilder;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
public class DBImport_2_0_1_To_2_0_2 {

	private static final DateFormat DATE_FORMAT = DateFormat
			.getDateTimeInstance();
	private static final String SEPARATOR = "@@@";
	private HibernateTemplate hibernateTemplate;
	private Session session;
	private File inDir;

	public DBImport_2_0_1_To_2_0_2(HibernateTemplate hibernateTemplate,
			Session session, String inDir) {
		this.hibernateTemplate = hibernateTemplate;
		this.session = session;
		this.inDir = new File(inDir);
	}

	public void run() {

		FileFilter directoryFilter = new FileFilter() {

			public boolean accept(File file) {
				return file.isDirectory();
			}

		};
		FileFilter xmlFileFilter = new FileFilter() {

			public boolean accept(File file) {
				return file.isFile() && file.getAbsolutePath().endsWith(".xml");
			}

		};

		Map<String, GridService> gridServiceMap = new HashMap<String, GridService>();
		Map<String, CQLQuery> queryMap = new HashMap<String, CQLQuery>();
		File[] serviceDirs = new File(inDir.getAbsolutePath() + "/services")
				.listFiles(directoryFilter);
		for (File f : serviceDirs) {

			GridService gridService = new GridService();

			Metadata meta = readMetadata(f);
			if (meta.dmodel != null) {
				gridService = new GridDataService();
			}
			String[] data = readDataFile(
					new File(f.getAbsolutePath() + "/service.dat")).get(0);
			String url = data[0];
			List l = session.createQuery("from GridService where url = :url")
					.setParameter("url", url).list();
			if (l.size() != 0) {
				System.out.println("Skipping service: " + url);
				gridServiceMap.put(PortalUtils.createHash(url), (GridService) l
						.get(0));
				continue;
			}
			System.out.println("Creating service: " + url);

			String idxUrl = data[1];
			String metadataHash = data[2];
			IndexService idxSvc = getIndexService(idxUrl);
			gridService.setUrl(url);
			gridService.setMetadataHash(metadataHash);
			gridService.getIndexServices().add(idxSvc);
			session.saveOrUpdate(gridService);
			idxSvc.getServices().add(gridService);
			session.saveOrUpdate(idxSvc);

			gridServiceMap.put(PortalUtils.createHash(url), gridService);

			List<String[]> statusData = readDataFile(new File(f
					.getAbsolutePath()
					+ "/status.dat"));
			for (String[] statusRow : statusData) {
				StatusChange sc = new StatusChange();
				sc.setService(gridService);
				try {
					sc.setTime(DATE_FORMAT.parse(statusRow[0]));
				} catch (Exception ex) {
					throw new RuntimeException("Error parsing date: "
							+ ex.getMessage(), ex);
				}
				sc.setStatus(ServiceStatus.valueOf(statusRow[1]));
				session.saveOrUpdate(sc);
				gridService.getStatusHistory().add(sc);
			}
			session.saveOrUpdate(gridService);

			ServiceMetadataBuilder sMetaBuilder = new ServiceMetadataBuilder();
			sMetaBuilder.setGridService(gridService);
			sMetaBuilder.setHibernateTemplate(hibernateTemplate);
			sMetaBuilder.setPersist(true);
			ServiceMetadata sMetaOut = sMetaBuilder.build(meta.smeta);
			sMetaOut.setService(gridService);
			session.saveOrUpdate(sMetaOut);
			gridService.setServiceMetadata(sMetaOut);
			session.saveOrUpdate(gridService);

			if (gridService instanceof GridDataService) {
				GridDataService dataService = (GridDataService) gridService;
				DomainModelBuilder builder = new DomainModelBuilder();
				builder.setGridService(dataService);
				builder.setHibernateTemplate(hibernateTemplate);
				builder.setPersist(true);
				DomainModel modelOut;
				try {
					modelOut = builder.build(meta.dmodel);
				} catch (Exception ex) {
					throw new RuntimeException(
							"Error persisting domain model: " + ex.getMessage(),
							ex);
				}
				modelOut.setService(dataService);
				session.saveOrUpdate(modelOut);
				dataService.setDomainModel(modelOut);
				session.saveOrUpdate(dataService);
			}
			session.saveOrUpdate(gridService);
			session.flush();

		}

		File[] queryFiles = new File(inDir.getAbsolutePath() + "/queries")
				.listFiles(xmlFileFilter);
		for (File queryFile : queryFiles) {
			String hash = queryFile.getName().substring(0,
					queryFile.getName().indexOf("."));
			String xml = readFile(queryFile);
			CQLQuery query = new CQLQuery();
			query.setHash(hash);
			query.setXml(xml);
			session.saveOrUpdate(query);
			queryMap.put(hash, query);
		}

		for (File userDir : new File(inDir.getAbsolutePath() + "/users")
				.listFiles(directoryFilter)) {

			String[] userData = readDataFile(
					new File(userDir.getAbsolutePath() + "/user.dat")).get(0);

			List l = session.createQuery(
					"from PortalUser where gridIdentity = :ident")
					.setParameter("identi", userData[0]).list();
			if (l.size() != 0) {
				System.out.println("Skipping user " + userData[0]);
				continue;
			}

			System.out.println("Creating user: " + userData[0]);

			Person person = new Person();
			person.setFirstName(userData[1]);
			person.setLastName(userData[2]);
			person.setEmailAddress(userData[3]);
			person.setPhoneNumber(userData[4]);
			session.saveOrUpdate(person);
			PortalUser user = new PortalUser();
			user.setPerson(person);
			user.setGridIdentity(userData[0]);
			session.saveOrUpdate(user);

			List<String[]> queryInstancesData = readDataFile(new File(userDir
					.getAbsolutePath()
					+ "/queryInstances.dat"));
			for (String[] queryInstanceData : queryInstancesData) {

				if (queryInstanceData.length != 7) {
					continue;
				}

				CQLQueryInstance instance = new CQLQueryInstance();

				CQLQuery query = queryMap.get(queryInstanceData[0]);
				instance.setQuery(query);
				GridDataService dataService = (GridDataService) gridServiceMap
						.get(queryInstanceData[6]);
				if (dataService == null) {
					throw new RuntimeException("missing data service");
				}
				instance.setDataService(dataService);
				try {
					instance.setCreateTime(DATE_FORMAT
							.parse(queryInstanceData[2]));
				} catch (Exception ex) {

				}
				try {
					instance.setStartTime(DATE_FORMAT
							.parse(queryInstanceData[3]));
				} catch (Exception ex) {

				}
				try {
					instance.setFinishTime(DATE_FORMAT
							.parse(queryInstanceData[4]));
				} catch (Exception ex) {

				}
				instance.setPortalUser(user);
				session.saveOrUpdate(instance);
				user.getQueryInstances().add(instance);
				session.saveOrUpdate(user);
			}
			List<String[]> sharedQueriesData = readDataFile(new File(userDir
					.getAbsolutePath()
					+ "/sharedQueries.dat"));
			for (String[] sharedQueryData : sharedQueriesData) {

				if (sharedQueryData.length != 6) {
					continue;
				}

				SharedCQLQuery sharedQuery = new SharedCQLQuery();
				GridDataService targetService = (GridDataService) gridServiceMap
						.get(sharedQueryData[5]);
				if (targetService == null) {
					throw new RuntimeException("missing data service");
				}
				sharedQuery.setTargetService(targetService);

				UMLClass umlClass = null;
				String umlClassName = sharedQueryData[4];
				DomainModel domainModel = targetService.getDomainModel();
				if (domainModel == null) {
					System.err.println(targetService.getUrl()
							+ " has no domain model");
					continue;
				}
				for (UMLClass klass : targetService.getDomainModel()
						.getClasses()) {
					String className = klass.getPackageName() + "."
							+ klass.getClassName();
					if (className.equals(umlClassName)) {
						umlClass = klass;
						break;
					}
				}
				if (umlClass == null) {
					throw new RuntimeException("missing UML class");
				}
				sharedQuery.setTargetClass(umlClass);

				CQLQuery query = queryMap.get(sharedQueryData[0]);
				if (query == null) {
					throw new RuntimeException("missing query");
				}
				sharedQuery.setCqlQuery(query);

				sharedQuery.setName(sharedQueryData[1]);

				try {
					sharedQuery.setShareDate(DATE_FORMAT
							.parse(sharedQueryData[2]));
				} catch (Exception ex) {
					throw new RuntimeException("Error parsing date: "
							+ ex.getMessage(), ex);
				}

				sharedQuery.setDescription(sharedQueryData[3]);

				sharedQuery.setOwner(user);
				session.saveOrUpdate(sharedQuery);
				user.getSharedQueries().add(sharedQuery);
				session.saveOrUpdate(user);
			}

			session.saveOrUpdate(user);
			session.flush();
		}

	}

	private String readFile(File file) {
		StringBuilder sb = new StringBuilder();
		try {

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (Exception ex) {
			throw new RuntimeException(
					"Error reading file: " + ex.getMessage(), ex);
		}
		return sb.toString();
	}

	private IndexService getIndexService(String idxUrl) {
		IndexService idxSvc = null;
		List<IndexService> l = session.createQuery(
				"from IndexService where url = :url").setParameter("url",
				idxUrl).list();
		if (l.size() == 1) {
			idxSvc = l.get(0);
		} else {
			idxSvc = new IndexService();
			idxSvc.setUrl(idxUrl);
			session.saveOrUpdate(idxSvc);
		}
		return idxSvc;
	}

	private List<String[]> readDataFile(File file) {
		List<String[]> data = new ArrayList<String[]>();
		try {

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				List<String> tokens = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer(line, SEPARATOR);
				while (st.hasMoreTokens()) {
					tokens.add(st.nextToken());
				}
				data.add((String[]) tokens.toArray(new String[tokens.size()]));
			}
			br.close();
		} catch (Exception ex) {
			throw new RuntimeException(
					"Error reading file: " + ex.getMessage(), ex);
		}
		return data;
	}

	private Metadata readMetadata(File f) {

		Metadata meta = new Metadata();
		try {

			meta.smeta = MetadataUtils
					.deserializeServiceMetadata(new FileReader(f
							.getAbsolutePath()
							+ "/serviceMetadata.xml"));
		} catch (Exception ex) {
			throw new RuntimeException("Error reading service metadata: "
					+ ex.getMessage(), ex);
		}
		try {
			meta.dmodel = MetadataUtils.deserializeDomainModel(new FileReader(f
					.getAbsolutePath()
					+ "/domainModel.xml"));
		} catch (Exception ex) {

		}

		return meta;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "classpath:applicationContext-db.xml" });
		final HibernateTemplate templ = (HibernateTemplate) ctx
				.getBean("hibernateTemplate");
		final String inDir = args.length == 1 ? args[0] : "export";

		templ.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				DBImport_2_0_1_To_2_0_2 i = new DBImport_2_0_1_To_2_0_2(templ,
						session, inDir);
				i.run();
				return null;
			}
		});
	}

}
