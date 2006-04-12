package gov.nih.nci.cagrid.syncgts.core;

import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.client.GTSSearchClient;
import gov.nih.nci.cagrid.syncgts.bean.AddedTrustedCAs;
import gov.nih.nci.cagrid.syncgts.bean.Message;
import gov.nih.nci.cagrid.syncgts.bean.MessageType;
import gov.nih.nci.cagrid.syncgts.bean.Messages;
import gov.nih.nci.cagrid.syncgts.bean.RemovedTrustedCAs;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescriptor;
import gov.nih.nci.cagrid.syncgts.bean.SyncReport;
import gov.nih.nci.cagrid.syncgts.bean.TrustedCA;

import java.io.File;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
public class SyncGTS {

	private SyncDescription description;
	private Map caListings;
	private Map listingsById;
	private Logger logger;
	private int nextFileId = 0;
	private List messages;
	private HistoryManager history;


	public SyncGTS(SyncDescription prop) {
		this.description = prop;
		this.history = new HistoryManager();
		logger = Logger.getLogger(this.getClass().getName());
	}


	private void reset() {
		this.caListings = null;
		this.listingsById = null;
		this.nextFileId = 0;
		messages = new ArrayList();
	}


	private synchronized int getNextFileId() {
		nextFileId = nextFileId + 1;
		boolean found = true;
		while (found) {
			if (!listingsById.containsKey(new Integer(nextFileId))) {
				found = false;
			} else {
				nextFileId = nextFileId + 1;
			}
		}
		return nextFileId;
	}


	public synchronized SyncReport sync() {
		SyncReport report = new SyncReport();
		try {
			reset();
			String dt = MobiusDate.getCurrentDateTimeAsString();
			report.setSyncDescription(description);
			report.setTimestamp(dt);
			this.readInCurrentCADirectory();
			Map master = new HashMap();
			String error = null;
			SyncDescriptor[] des = description.getSyncDescriptors();
			int dcount = 0;
			if (des != null) {
				dcount = des.length;
			}
			for (int i = 0; i < dcount; i++) {
				String uri = des[i].getGtsServiceURI();
				this.logger.info("Syncing with the GTS " + uri);
				GTSSearchClient client = new GTSSearchClient(uri);
				Map taMap = new HashMap();
				TrustedAuthorityFilter[] f = des[i].getTrustedAuthorityFilters();
				int fcount = 0;
				if (f != null) {
					fcount = f.length;
				}
				for (int j = 0; j < fcount; j++) {
					int filter = j + 1;
					try {
						TrustedAuthority[] tas = client.findTrustedAuthorities(f[j]);
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
						Message mess = new Message();
						mess.setType(MessageType.Error);
						mess.setValue("An error occurred syncing with " + uri + " using filter " + filter + "\n "
							+ e.getMessage());
						messages.add(mess);
						logger.error(mess.getValue(), e);
					}
				}

				// Write all to the master list;

				Iterator itr = taMap.values().iterator();
				while (itr.hasNext()) {
					TrustedAuthority ta = (TrustedAuthority) itr.next();

					if (master.containsValue(ta.getTrustedAuthorityName())) {
						TrustedCAListing gta = (TrustedCAListing) master.get(ta.getTrustedAuthorityName());
						String msg = "Conflict Detected: The Trusted Authority " + ta.getTrustedAuthorityName()
							+ " was determined to be trusted by both " + gta.getService() + " and " + uri + ".";
						Message mess = new Message();
						mess.setType(MessageType.Warning);
						mess.setValue(msg);
						messages.add(mess);
					} else {
						master.put(ta.getTrustedAuthorityName(), new TrustedCAListing(uri, ta));
					}
				}
				this.logger.debug("Done syncing with the GTS " + uri + " " + taMap.size()
					+ " Trusted Authority(s) found!!!");

				if (error != null) {
					break;
				}
			}

			// TODO: Write out tas.
			int taCount = 0;
			Iterator itr = master.values().iterator();
			List addedList = new ArrayList();
			while (itr.hasNext()) {
				taCount = taCount + 1;
				int fid = this.getNextFileId();
				String filePrefix = CoGProperties.getDefault().getCaCertLocations() + File.separator
					+ description.getFilePrefix() + "-" + dt + "-" + taCount;
				File caFile = new File(filePrefix + "." + fid);
				File crlFile = new File(filePrefix + ".r" + fid);
				try {
					TrustedCA ca = new TrustedCA();
					TrustedCAListing listing = (TrustedCAListing) itr.next();
					TrustedAuthority ta = listing.getTrustedAuthority();
					X509Certificate cert = CertUtil.loadCertificate(ta.getCertificate().getCertificateEncodedString());
					ca.setName(cert.getSubjectDN().getName());
					ca.setGts(listing.getService());
					CertUtil.writeCertificate(cert, caFile);
					ca.setCertificateFile(caFile.getAbsolutePath());
					logger.debug("Wrote out the certificate for the Trusted Authority " + ta.getTrustedAuthorityName()
						+ " to the file " + caFile.getAbsolutePath());
					if (ta.getCRL() != null) {
						if (ta.getCRL().getCrlEncodedString() != null) {
							X509CRL crl = CertUtil.loadCRL(ta.getCRL().getCrlEncodedString());
							CertUtil.writeCRL(crl, crlFile);
							ca.setCRLFile(crlFile.getAbsolutePath());
							logger.debug("Wrote out the CRL for the Trusted Authority " + ta.getTrustedAuthorityName()
								+ " to the file " + crlFile.getAbsolutePath());
						}
					}
					addedList.add(ca);
				} catch (Exception e) {
					logger.error("An unexpected error occurred writing out the Trusted Authorities!!!", e);
					caFile.delete();
					crlFile.delete();
				}
			}
			Message mess = new Message();
			mess.setType(MessageType.Info);
			mess.setValue("Successfully wrote out " + taCount + " Trusted Authority(s) to "
				+ CoGProperties.getDefault().getCaCertLocations());
			messages.add(mess);
			logger.info(mess.getValue());

			TrustedCA[] addedCAs = new TrustedCA[taCount];
			for (int i = 0; i < addedList.size(); i++) {
				addedCAs[i] = (TrustedCA) addedList.get(i);
			}
			AddedTrustedCAs atc = new AddedTrustedCAs();
			atc.setTrustedCA(addedCAs);
			report.setAddedTrustedCAs(atc);

			int removeCount = 0;
			List removedList = new ArrayList();
			Iterator del = caListings.values().iterator();
			while (del.hasNext()) {
				TrustedCAFileListing fl = (TrustedCAFileListing) del.next();
				if ((this.description.isDeleteExistingTrustedRoots())
					|| (fl.getName().indexOf(description.getFilePrefix()) >= 0)) {
					TrustedCA ca = new TrustedCA();
					removeCount = removeCount + 1;
					if (fl.getCertificate() != null) {
						X509Certificate cert = CertUtil.loadCertificate(fl.getCertificate());
						ca.setName(cert.getSubjectDN().getName());
						ca.setCertificateFile(fl.getCertificate().getAbsolutePath());
						if (fl.getCertificate().delete()) {
							logger.debug("Removed the certificate (" + fl.getCertificate().getAbsolutePath()
								+ ") for the CA " + ca.getName() + ".");
						} else {
							Message err = new Message();
							err.setType(MessageType.Error);
							err.setValue("Error removing the certificate (" + fl.getCertificate().getAbsolutePath()
								+ ") for the CA " + ca.getName() + ".");
							this.messages.add(err);
							logger.error(err.getValue());
						}
					}

					if (fl.getCRL() != null) {
						ca.setCRLFile(fl.getCRL().getAbsolutePath());
						if (fl.getCRL().delete()) {
							logger.debug("Removed the CRL (" + fl.getCRL().getAbsolutePath() + ") for the CA "
								+ ca.getName() + ".");
						} else {
							Message err = new Message();
							err.setType(MessageType.Error);
							err.setValue("Error removing the CRL (" + fl.getCRL().getAbsolutePath() + ") for the CA "
								+ ca.getName() + ".");
							this.messages.add(err);
							logger.error(err.getValue());
						}
					}

					if (fl.getSigningPolicy() != null) {
						ca.setSigningPolicyFile(fl.getSigningPolicy().getAbsolutePath());
						if (fl.getSigningPolicy().delete()) {
							logger.debug("Removed the Signing Policy (" + fl.getCertificate().getAbsolutePath()
								+ ") for the CA " + ca.getName() + ".");
						} else {
							Message err = new Message();
							err.setType(MessageType.Error);
							err.setValue("Error removing the Signing Policy (" + fl.getCRL().getAbsolutePath()
								+ ") for the CA " + ca.getName() + ".");
							this.messages.add(err);
							logger.error(err.getValue());
						}
					}

					removedList.add(ca);
				}
			}
			TrustedCA[] removedCAs = new TrustedCA[removedList.size()];
			for (int i = 0; i < removedList.size(); i++) {
				removedCAs[i] = (TrustedCA) removedList.get(i);
			}
			RemovedTrustedCAs rtc = new RemovedTrustedCAs();
			rtc.setTrustedCA(removedCAs);
			report.setRemovedTrustedCAs(rtc);
			Message mess2 = new Message();
			mess2.setType(MessageType.Info);
			mess2.setValue("Successfully removed " + removeCount + " Trusted Authority(s) from "
				+ CoGProperties.getDefault().getCaCertLocations());
			messages.add(mess2);
			logger.info(mess2.getValue());
		} catch (Exception e) {
			logger.fatal(e.getMessage(), e);
			Message error = new Message();
			error.setType(MessageType.Fatal);
			error.setValue(e.getMessage());
			messages.add(error);
		}
		// Add messages to the report
		Message[] list = new Message[messages.size()];
		for (int i = 0; i < messages.size(); i++) {
			list[i] = (Message) messages.get(i);
		}
		Messages reportMessages = new Messages();
		reportMessages.setMessage(list);
		report.setMessages(reportMessages);

		// Log Report;
		try {
			history.addReport(report);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return report;
	}


	private void readInCurrentCADirectory() throws Exception {
		caListings = new HashMap();
		this.listingsById = new HashMap();
		String caDir = CoGProperties.getDefault().getCaCertLocations();
		logger.info("Taking Snapshot of Trusted CA Directory (" + caDir + ")....");
		File dir = new File(caDir);
		if (dir.exists()) {
			if (!dir.isDirectory()) {
				Message mess = new Message();
				mess.setType(MessageType.Fatal);
				mess.setValue("The Trusted Certificates directory, " + dir.getAbsolutePath() + " is not a directory.");
				messages.add(mess);
				throw new Exception(mess.getValue());
			}

		} else {
			boolean create = dir.mkdirs();
			if (!create) {
				Message mess = new Message();
				mess.setType(MessageType.Fatal);
				mess.setValue("The Trusted Certificates directory, " + dir.getAbsolutePath()
					+ " does not exist and could not be created.");
				messages.add(mess);
				throw new Exception(mess.getValue());
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

			TrustedCAFileListing ca = (TrustedCAFileListing) this.caListings.get(name);
			if (ca == null) {
				ca = new TrustedCAFileListing(name);
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
			TrustedCAFileListing ca = (TrustedCAFileListing) itr.next();
			if (ca.isValid()) {
				this.listingsById.put(ca.getFileId(), ca);
				logger.debug(ca.toPrintText());
			} else {
				if ((!this.description.isDeleteInvalidFiles()) && (ca.getFileId() != null)) {
					this.listingsById.put(ca.getFileId(), ca);
				}
				handleUnexpectedCA(ca);
			}
		}
		Message mess = new Message();
		mess.setType(MessageType.Info);
		mess.setValue("A pre synchronization snapshot of the Trusted CA Directory found " + caListings.size()
			+ " Trusted CAs.");
		messages.add(mess);
		logger.info("DONE -Taking Snapshot of Trusted CA Directory, " + caListings.size() + " Trusted CAs found!!!");
	}


	private void handleUnexpectedCA(TrustedCAFileListing ca) {
		if (this.description.isDeleteInvalidFiles()) {
			Message mess = new Message();
			mess.setType(MessageType.Warning);
			mess.setValue("The ca " + ca.getName() + " is invalid and will be removed!!!");
			messages.add(mess);
			logger.warn(mess.getValue());

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
			Message mess = new Message();
			mess.setType(MessageType.Warning);
			mess.setValue("The CA " + ca.getName() + " is invalid.!!!");
			messages.add(mess);
			logger.warn(mess.getValue());
		}
	}


	private void handleUnexpectedFile(File f) {
		if (this.description.isDeleteInvalidFiles()) {
			Message mess = new Message();
			mess.setType(MessageType.Warning);
			mess.setValue("The file " + f.getAbsolutePath() + " is unexpected and will be removed!!!");
			messages.add(mess);
			logger.warn(mess.getValue());
			f.delete();
		} else {
			Message mess = new Message();
			mess.setType(MessageType.Warning);
			mess.setValue("The file " + f.getAbsolutePath() + " is unexpected and will be ignored!!!");
			messages.add(mess);
			logger.warn(mess.getValue());
		}
	}


	public static void main(String[] args) {
		try {
			SyncDescription description = new SyncDescription();
			SyncDescriptor[] des = new SyncDescriptor[1];
			des[0] = new SyncDescriptor();
			des[0].setGtsServiceURI("https://localhost:8443/wsrf/services/cagrid/GridTrustService");
			TrustedAuthorityFilter[] taf = new TrustedAuthorityFilter[1];
			taf[0] = new TrustedAuthorityFilter();
			taf[0].setStatus(Status.Trusted);
			des[0].setTrustedAuthorityFilters(taf);
			description.setSyncDescriptors(des);
			description.setFilePrefix("gts");
			description.setDeleteInvalidFiles(false);
			SyncGTS sync = new SyncGTS(description);
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
