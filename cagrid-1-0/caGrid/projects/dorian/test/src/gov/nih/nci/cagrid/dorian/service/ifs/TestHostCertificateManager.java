package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.bean.X509Certificate;
import gov.nih.nci.cagrid.dorian.conf.CredentialLifetime;
import gov.nih.nci.cagrid.dorian.conf.CredentialPolicy;
import gov.nih.nci.cagrid.dorian.conf.IdentityFederationConfiguration;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRequest;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateUpdate;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidHostCertificateFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidHostCertificateRequestFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestHostCertificateManager extends TestCase {
	public final static String OWNER = "owner";
	private Database db;
	private CertificateAuthority ca;


	public void testCreateAndDestroy() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}

	}


	public void testCreateAndApproveManyHostCertificate() {
		try {
			int total = 5;
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			List<HostCertificateRequest> requests = new ArrayList<HostCertificateRequest>();
			List<Long> ids = new ArrayList<Long>();
			for (int i = 0; i < total; i++) {
				String host = "localhost" + i;
				HostCertificateRequest req = getHostCertificateRequest(host);
				String owner = OWNER + i;
				long id = hcm.requestHostCertifcate(owner, req);
				validateAfterCertificateRequest((i + 1), (i + 1), hcm, owner, req, id);
				requests.add(req);
				ids.add(Long.valueOf(id));
			}

			for (int i = 0; i < total; i++) {
				long id = ids.get(i).longValue();
				HostCertificateRequest req = requests.get(i);
				String owner = OWNER + i;
				HostCertificateRecord record = hcm.approveHostCertifcate(id);
				System.err.println((i + 1));
				validateAfterCertificateApproval(total, (i + 1), hcm, id, owner, req, record);
				HostCertificateFilter f = new HostCertificateFilter();
				f.setStatus(HostCertificateStatus.Pending);
				assertEquals(total - (i + 1), hcm.findHostCertificates(f).size());
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testCreateAndApproveHostCertificate() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testCreateDuplicateHostCertificate() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
			try {
				hcm.requestHostCertifcate(OWNER, getHostCertificateRequest("localhost"));
				fail("Should have Failed!!");
			} catch (InvalidHostCertificateRequestFault f) {

			}

			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(id);
			update.setStatus(HostCertificateStatus.Compromised);
			hcm.updateHostCertificateRecord(update);
			HostCertificateRequest req2 = getHostCertificateRequest("localhost");
			long id2 = hcm.requestHostCertifcate(OWNER, req2);
			hcm.approveHostCertifcate(id2);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testCreateHostCertificateWithACompromisedKey() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(id);
			update.setStatus(HostCertificateStatus.Compromised);
			hcm.updateHostCertificateRecord(update);
			try {
				req.setHostname("newhost");
				hcm.requestHostCertifcate(OWNER, req);
				fail("Should have Failed!!");
			} catch (InvalidHostCertificateRequestFault f) {

			}

			hcm.requestHostCertifcate(OWNER, getHostCertificateRequest("newhost"));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testCreateHostCertificateBadHostname() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			try {
				hcm.requestHostCertifcate(OWNER, getHostCertificateRequest(null));
				fail("Should have Failed!!");
			} catch (InvalidHostCertificateRequestFault f) {

			}

			try {
				hcm.requestHostCertifcate(OWNER, getHostCertificateRequest(" "));
				fail("Should have Failed!!");
			} catch (InvalidHostCertificateRequestFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testCreateHostCertificateInvalidPublicKey() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			try {
				HostCertificateRequest req = getHostCertificateRequest("localhost");
				req.setPublicKey(null);
				hcm.requestHostCertifcate(OWNER, req);
				fail("Should have Failed!!");
			} catch (InvalidHostCertificateRequestFault f) {

			}

			try {
				HostCertificateRequest req = getHostCertificateRequest("localhost");
				req.getPublicKey().setKeyAsString(null);
				hcm.requestHostCertifcate(OWNER, req);
				fail("Should have Failed!!");
			} catch (InvalidHostCertificateRequestFault f) {

			}

			try {
				HostCertificateRequest req = getHostCertificateRequest("localhost");
				req.getPublicKey().setKeyAsString(" ");
				hcm.requestHostCertifcate(OWNER, req);
				fail("Should have Failed!!");
			} catch (InvalidHostCertificateRequestFault f) {

			}

			try {
				HostCertificateRequest req = getHostCertificateRequest("localhost");
				req.getPublicKey().setKeyAsString("foobar");
				hcm.requestHostCertifcate(OWNER, req);
				fail("Should have Failed!!");
			} catch (InvalidHostCertificateRequestFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateHostCertificateStatusBeforeApproval() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);

			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(id);
			update.setStatus(HostCertificateStatus.Suspended);

			try {
				hcm.updateHostCertificateRecord(update);
				fail("Should have failed");
			} catch (InvalidHostCertificateFault f) {

			}
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
			hcm.updateHostCertificateRecord(update);
			assertEquals(HostCertificateStatus.Suspended, hcm.getHostCertificateRecord(id).getStatus());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateHostCertificateOwnerBeforeApproval() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			String newOwner = "newowner";
			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(id);
			update.setOwner(newOwner);
			hcm.updateHostCertificateRecord(update);
			assertEquals(newOwner, hcm.getHostCertificateRecord(id).getOwner());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateHostCertificateOwner() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
			String newOwner = "newowner";
			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(id);
			update.setOwner(newOwner);
			hcm.updateHostCertificateRecord(update);
			assertEquals(newOwner, hcm.getHostCertificateRecord(id).getOwner());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateAllHostCertificate() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
			String newOwner = "newowner";
			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(id);
			update.setOwner(newOwner);
			update.setStatus(HostCertificateStatus.Suspended);
			hcm.updateHostCertificateRecord(update);
			HostCertificateRecord r = hcm.getHostCertificateRecord(id);
			assertEquals(newOwner, r.getOwner());
			assertEquals(HostCertificateStatus.Suspended, r.getStatus());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateNonExistingHostCertificate() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);

			try {
				HostCertificateUpdate update = new HostCertificateUpdate();
				update.setId(100);
				update.setOwner("newowner");
				hcm.updateHostCertificateRecord(update);
				fail("Should have failed");
			} catch (InvalidHostCertificateFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateCompromisedHostCertificate() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
			HostCertificateUpdate u = new HostCertificateUpdate();
			u.setId(id);
			u.setStatus(HostCertificateStatus.Compromised);
			hcm.updateHostCertificateRecord(u);
			try {
				HostCertificateUpdate update = new HostCertificateUpdate();
				update.setId(id);
				update.setOwner("newowner");
				hcm.updateHostCertificateRecord(update);
				fail("Should have failed");
			} catch (InvalidHostCertificateFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateApprovedHostCertificateToPending() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
			try {
				HostCertificateUpdate update = new HostCertificateUpdate();
				update.setId(id);
				update.setStatus(HostCertificateStatus.Pending);
				hcm.updateHostCertificateRecord(update);
				fail("Should have failed");
			} catch (InvalidHostCertificateFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateHostCertificateStatus() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateRecord record = hcm.approveHostCertifcate(id);
			validateAfterCertificateApproval(hcm, id, OWNER, req, record);
			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(id);
			update.setStatus(HostCertificateStatus.Suspended);
			hcm.updateHostCertificateRecord(update);
			assertEquals(HostCertificateStatus.Suspended, hcm.getHostCertificateRecord(id).getStatus());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	public void testUpdateRejectedHostCertificate() {
		try {
			HostCertificateManager hcm = new HostCertificateManager(db, getConf(), ca);
			hcm.clearDatabase();
			HostCertificateRequest req = getHostCertificateRequest("localhost");
			long id = hcm.requestHostCertifcate(OWNER, req);
			validateAfterCertificateRequest(hcm, req, id);
			HostCertificateUpdate u = new HostCertificateUpdate();
			u.setId(id);
			u.setStatus(HostCertificateStatus.Rejected);
			hcm.updateHostCertificateRecord(u);
			try {
				HostCertificateUpdate update = new HostCertificateUpdate();
				update.setId(id);
				update.setOwner("newowner");
				hcm.updateHostCertificateRecord(update);
				fail("Should have failed");
			} catch (InvalidHostCertificateFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}


	private HostCertificateRequest getHostCertificateRequest(String host) throws Exception {
		KeyPair pair = KeyUtil.generateRSAKeyPair(ca.getConfiguration().getUserKeySize().getValue());
		HostCertificateRequest req = new HostCertificateRequest();
		req.setHostname(host);
		String keyStr = KeyUtil.writePublicKey(pair.getPublic());
		gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey pk = new gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey();
		pk.setKeyAsString(keyStr);
		req.setPublicKey(pk);
		return req;
	}


	private void validateAfterCertificateRequest(HostCertificateManager hcm, HostCertificateRequest req, long id)
		throws Exception {
		validateAfterCertificateRequest(1, 1, hcm, OWNER, req, id);
	}


	private void validateAfterCertificateRequest(int count, int statusCount, HostCertificateManager hcm, String owner,
		HostCertificateRequest req, long id) throws Exception {
		validateFindHostCertificates(count, statusCount, hcm, id, -1, null, req.getHostname(), owner, req
			.getPublicKey(), HostCertificateStatus.Pending, "");
	}


	private void validateAfterCertificateApproval(HostCertificateManager hcm, long id, String owner,
		HostCertificateRequest req, HostCertificateRecord record) throws Exception {
		validateAfterCertificateApproval(1, 1, hcm, id, owner, req, record);
	}


	private void validateAfterCertificateApproval(int count, int statusCount, HostCertificateManager hcm, long id,
		String owner, HostCertificateRequest req, HostCertificateRecord record) throws Exception {
		assertEquals(req.getHostname(), record.getHost());
		assertEquals(req.getPublicKey(), record.getPublicKey());
		assertEquals(owner, record.getOwner());
		assertEquals(HostCertificateStatus.Active, record.getStatus());
		String caSubject = ca.getCACertificate().getSubjectDN().getName();
		int caindex = caSubject.lastIndexOf(",");
		String caPreSub = caSubject.substring(0, caindex);
		String subject = caPreSub + ",CN=host/" + req.getHostname();
		assertEquals(subject, record.getSubject());
		HostCertificateRecord r = hcm.getHostCertificateRecord(id);
		assertEquals(record.getPublicKey(), r.getPublicKey());
		assertEquals(record.getCertificate(), r.getCertificate());
		assertEquals(record.getSerialNumber(), r.getSerialNumber());
		assertEquals(record.getId(), r.getId());
		assertEquals(record.getOwner(), r.getOwner());
		assertEquals(record.getHost(), r.getHost());
		assertEquals(record.getStatus(), r.getStatus());
		assertEquals(record.getSubject(), r.getSubject());
		assertEquals(record, r);
		validateFindHostCertificates(count, statusCount, hcm, id, record.getSerialNumber(), record.getCertificate(),
			req.getHostname(), owner, req.getPublicKey(), HostCertificateStatus.Active, subject);
	}


	private void validateFindHostCertificates(int count, int statusCount, HostCertificateManager hcm, long id, long sn,
		X509Certificate cert, String host, String owner, PublicKey key, HostCertificateStatus status, String subject)
		throws Exception {

		List<HostCertificateRecord> l1 = hcm.findHostCertificates(null);
		assertEquals(count, l1.size());
		if (count == 1) {
			validateHostCertificateRecord(l1.get(count - 1), id, sn, cert, host, owner, key, status, subject);
		}

		if (count == 1) {
			List<HostCertificateRecord> l2 = hcm.findHostCertificates(new HostCertificateFilter());
			assertEquals(count, l2.size());
			validateHostCertificateRecord(l2.get(count - 1), id, sn, cert, host, owner, key, status, subject);
		}
		HostCertificateFilter f3 = new HostCertificateFilter();
		f3.setHost(host);
		List<HostCertificateRecord> l3 = hcm.findHostCertificates(f3);
		assertEquals(1, l3.size());
		validateHostCertificateRecord(l3.get(0), id, sn, cert, host, owner, key, status, subject);

		HostCertificateFilter f4 = new HostCertificateFilter();
		f4.setId(new BigInteger(String.valueOf(id)));
		List<HostCertificateRecord> l4 = hcm.findHostCertificates(f4);
		assertEquals(1, l4.size());
		validateHostCertificateRecord(l4.get(0), id, sn, cert, host, owner, key, status, subject);

		HostCertificateFilter f5 = new HostCertificateFilter();
		f5.setOwner(owner);
		List<HostCertificateRecord> l5 = hcm.findHostCertificates(f5);
		assertEquals(1, l5.size());
		validateHostCertificateRecord(l5.get(0), id, sn, cert, host, owner, key, status, subject);

		if (sn >= 0) {
			HostCertificateFilter f6 = new HostCertificateFilter();
			f6.setSerialNumber(new BigInteger(String.valueOf(sn)));
			List<HostCertificateRecord> l6 = hcm.findHostCertificates(f6);
			assertEquals(1, l6.size());
			validateHostCertificateRecord(l6.get(0), id, sn, cert, host, owner, key, status, subject);
		}

		HostCertificateFilter f7 = new HostCertificateFilter();
		f7.setStatus(status);
		List<HostCertificateRecord> l7 = hcm.findHostCertificates(f7);
		assertEquals(statusCount, l7.size());
		if (statusCount == 1) {
			validateHostCertificateRecord(l7.get(0), id, sn, cert, host, owner, key, status, subject);
		} else if (statusCount > 1) {
			f7.setId(new BigInteger(String.valueOf(id)));
			l7 = hcm.findHostCertificates(f7);
			assertEquals(1, l7.size());
			validateHostCertificateRecord(l7.get(0), id, sn, cert, host, owner, key, status, subject);
		}

		if (gov.nih.nci.cagrid.common.Utils.clean(subject) != null) {
			HostCertificateFilter f8 = new HostCertificateFilter();
			f8.setSubject(subject);
			List<HostCertificateRecord> l8 = hcm.findHostCertificates(f8);
			assertEquals(1, l8.size());
			validateHostCertificateRecord(l8.get(0), id, sn, cert, host, owner, key, status, subject);
		}
	}


	private void validateHostCertificateRecord(HostCertificateRecord record, long id, long sn, X509Certificate cert,
		String host, String owner, PublicKey key, HostCertificateStatus status, String subject) {
		assertEquals(id, record.getId());
		assertEquals(sn, record.getSerialNumber());
		assertEquals(cert, record.getCertificate());
		assertEquals(host, record.getHost());
		assertEquals(owner, record.getOwner());
		assertEquals(key, record.getPublicKey());
		assertEquals(status, record.getStatus());
		assertEquals(subject, record.getSubject());
	}


	private IdentityFederationConfiguration getConf() {
		IdentityFederationConfiguration conf = new IdentityFederationConfiguration();
		CredentialPolicy cp = new CredentialPolicy();
		CredentialLifetime l = new CredentialLifetime();
		l.setYears(1);
		l.setMonths(0);
		l.setDays(0);
		l.setHours(0);
		l.setMinutes(0);
		l.setSeconds(0);
		cp.setCredentialLifetime(l);
		conf.setCredentialPolicy(cp);
		return conf;
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			ca = Utils.getCA(db);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
