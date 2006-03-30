package gov.nih.nci.cagrid.gts.client.gtssync;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.client.GTSSearchClient;

import java.io.File;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.globus.common.CoGProperties;
import org.projectmobius.common.MobiusDate;


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
			//Need to make it so multiple certs of the same CA cannot be written
			//Need to make a priority for this.
			//Hash by service, prioritize by service order
			reset();
			this.readInCurrentCADirectory();
			int taCount = 1;
			String dt = MobiusDate.getCurrentDateTimeAsString();
			for (int i = 0; i < prop.getSyncDescriptorCount(); i++) {
				SyncDescriptor des = prop.getSyncDescriptor(i);
				String uri = des.getGTSServiceURI();
				String hash = String.valueOf(uri.hashCode());
				this.logger.info("Syncing with the GTS " + uri);
				GTSSearchClient client = new GTSSearchClient(uri);
				Map taMap = new HashMap();
				for (int j = 0; j < des.getFilterCount(); j++) {
					TrustedAuthorityFilter f = des.getFilter(j);
					int filter = j + 1;
					try {
						TrustedAuthority[] tas = client.findTrustedAuthorities(f);
						int length = 0;
						if (tas != null) {
							length = tas.length;
						}
						this.logger.debug("Successfully synced with " + uri + " using filter " + filter
							+ " the search found " + length + " Trusted Authority(s)!!!");

						for (int x = 0; x < length; x++) {
							taMap.put(tas[x].getTrustedAuthorityName(), tas[x]);
						}

					} catch (Exception e) {
						logger.error("An error occurred syncing with " + uri + " using filter " + filter + "!!!", e);
					}
				}
				// TODO: Write out tas.

				Iterator itr = taMap.values().iterator();
				while (itr.hasNext()) {
					int fid = this.getNextFileId();
					String filePrefix = CoGProperties.getDefault().getCaCertLocations() + File.separator + hash + "-"
						+ dt + "-" + taCount;
					File caFile = new File(filePrefix + "." + fid);
					File crlFile = new File(filePrefix + ".r" + fid);
					try {

						TrustedAuthority ta = (TrustedAuthority) itr.next();
						X509Certificate cert = CertUtil.loadCertificate(ta.getCertificate()
							.getCertificateEncodedString());
						CertUtil.writeCertificate(cert, caFile);
						logger.debug("Wrote out the certificate for the Trusted Authority "
							+ ta.getTrustedAuthorityName() + " to the file " + caFile.getAbsolutePath());
						if (ta.getCRL() != null) {
							if (ta.getCRL().getCrlEncodedString() != null) {
								X509CRL crl = CertUtil.loadCRL(ta.getCRL().getCrlEncodedString());
								CertUtil.writeCRL(crl, crlFile);
								logger.debug("Wrote out the CRL for the Trusted Authority "
									+ ta.getTrustedAuthorityName() + " to the file " + crlFile.getAbsolutePath());
							}
						}

					} catch (Exception e) {
						logger.error("An unexpected error occurred writing out the Trusted Authorities!!!", e);
						caFile.delete();
						crlFile.delete();
					}
					taCount = taCount + 1;
				}
				// TODO: Delete Other TAs.
				this.logger.info("Done syncing with the GTS " + uri + " " + taMap.size()
					+ " Trusted Authority(s) found!!!");
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
			SyncDescriptor des = new SyncDescriptor("https://localhost:8443/wsrf/services/cagrid/GridTrustService");
			des.addFilter(new TrustedAuthorityFilter());
			props.addSyncDescriptor(des);
			props.setDeleteUnknownFiles(false);
			GTSSync sync = new GTSSync(props);
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
