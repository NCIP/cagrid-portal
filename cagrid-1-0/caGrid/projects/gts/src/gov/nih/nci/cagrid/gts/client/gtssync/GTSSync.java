package gov.nih.nci.cagrid.gts.client.gtssync;

import gov.nih.nci.cagrid.gts.client.GTSSearchClient;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.globus.common.CoGProperties;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GTSSync {

	private GTSSyncProperties prop;
	private Map caListings;
	private Map listingsById;
	private Logger logger;
	private int nextFileId = 0;


	public GTSSync(GTSSyncProperties prop) {
		this.prop = prop;
		logger = Logger.getLogger(this.getClass().getName());
	}


	private void reset() {
		this.caListings = null;
		this.listingsById = null;
		this.nextFileId = 0;
	}


	private int getNextFileId() {
		nextFileId = nextFileId + 1;
		boolean found = false;
		while (!found) {
			if (listingsById.containsKey(new Integer(nextFileId))) {
				found = true;
			} else {
				nextFileId = nextFileId + 1;
			}
		}
		return nextFileId;
	}


	public void sync() throws Exception {
		try {
			this.readInCurrentCADirectory();
			List services = prop.getGTSServices();
			if (services != null) {
				for (int i = 0; i < services.size(); i++) {
					String uri = (String) services.get(i);
					String hash = String.valueOf(uri.hashCode());
					GTSSearchClient client = new GTSSearchClient(uri);
				}
			}

		} catch (Exception e) {
			// TODO: Handle Exception
			throw e;
		}
	}


	private void readInCurrentCADirectory() throws Exception {
		caListings = new HashMap();
		this.listingsById = new HashMap();
		String caDir = CoGProperties.getDefault().getCaCertLocations();
		logger.info("Taking Snapshot of Trusted CA Directory (" + caDir + ")....");
		File dir = new File(caDir);
		if (dir.exists()) {
			if (!dir.isDirectory()) {
				throw new Exception("The Trusted Certificates directory, " + dir.getAbsolutePath()
					+ " is not a directory.");
			}

		} else {
			boolean create = dir.mkdirs();
			if (!create) {
				throw new Exception("The Trusted Certificates directory, " + dir.getAbsolutePath()
					+ " does not exist and could not be created.");
			}
		}
		File[] list = dir.listFiles();
		for (int i = 0; i < list.length; i++) {
			String fn = list[i].getName();
			int index = fn.lastIndexOf(".");
			if (index == -1) {
				handleUnexpectedFile(list[i]);
				break;
			}
			String name = fn.substring(0, index);
			String extension = fn.substring(index + 1);

			TrustedCAListing ca = (TrustedCAListing) this.caListings.get(name);
			if (ca == null) {
				ca = new TrustedCAListing(name);
				caListings.put(name, ca);
			}

			if (extension.matches("[0-9]+")) {
				ca.setFileId(Integer.valueOf(extension));
				ca.setCertificate(list[i]);
			} else if (extension.matches("[r]{1}[0-9]+")) {
				ca.setCRL(list[i]);
			} else if (extension.equals("signing_policy")) {
				ca.setSigningPolicy(list[i]);
			} else {
				handleUnexpectedFile(list[i]);
				break;
			}

		}
		Iterator itr = this.caListings.values().iterator();
		logger.debug("Found " + caListings.size() + " Trusted CAs found!!!");
		while (itr.hasNext()) {
			TrustedCAListing ca = (TrustedCAListing) itr.next();
			if (ca.isValid()) {
				this.listingsById.put(ca.getFileId(), ca);
				logger.debug(ca.toPrintText());
			} else {
				if ((!this.prop.deleteUnknownFiles()) && (ca.getFileId() != null)) {
					this.listingsById.put(ca.getFileId(), ca);
				}
				handleUnexpectedCA(ca);
			}
		}
		logger.info("DONE -Taking Snapshot of Trusted CA Directory, " + caListings.size() + " Trusted CAs found!!!");
	}


	private void handleUnexpectedCA(TrustedCAListing ca) {
		if (this.prop.deleteUnknownFiles()) {
			logger.warn("The ca " + ca.getName() + " is invalid and will be removed!!!");
			if (ca.getCertificate() != null) {
				ca.getCertificate().delete();
			}
			if (ca.getCRL() != null) {
				ca.getCRL().delete();
			}
			if (ca.getSigningPolicy() != null) {
				ca.getSigningPolicy().delete();
			}
		} else {
			logger.warn("The CA " + ca.getName() + " is invalid.!!!");
		}
	}


	private void handleUnexpectedFile(File f) {
		if (this.prop.deleteUnknownFiles()) {
			logger.warn("The file " + f.getAbsolutePath() + " is unexpected and will be removed!!!");
			f.delete();
		} else {
			logger.warn("The file " + f.getAbsolutePath() + " is unexpected and will be ignored!!!");
		}
	}


	public static void main(String[] args) {
		try {
			GTSSyncProperties props = new GTSSyncProperties();
			props.setDeleteUnknownFiles(false);
			GTSSync sync = new GTSSync(props);
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
