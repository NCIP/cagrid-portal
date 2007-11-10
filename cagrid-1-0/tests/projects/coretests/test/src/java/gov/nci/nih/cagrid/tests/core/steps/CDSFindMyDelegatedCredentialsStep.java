/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.GridCredential;

import java.util.ArrayList;
import java.util.List;

import org.cagrid.gaards.cds.client.DelegationUserClient;
import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.common.DelegationRecord;
import org.cagrid.gaards.cds.common.DelegationRecordFilter;
import org.globus.gsi.GlobusCredential;

import com.atomicobject.haste.framework.Step;

public class CDSFindMyDelegatedCredentialsStep extends Step implements
		GridCredential {

	private GridCredential credential;

	private GlobusCredential proxy = null;
	private String uri;
	private DelegationRecordFilter filter;
	private List<DelegationIdentifier> expected;

	public CDSFindMyDelegatedCredentialsStep(String uri,
			GridCredential credential) {
		this(uri, credential, null, null);
	}

	public CDSFindMyDelegatedCredentialsStep(String uri,
			GridCredential credential, List<DelegationIdentifier> expected) {
		this(uri, credential, new DelegationRecordFilter(), expected);
	}

	public CDSFindMyDelegatedCredentialsStep(String uri,
			GridCredential credential, DelegationRecordFilter f,
			List<DelegationIdentifier> expected) {
		this.uri = uri;
		this.credential = credential;
		this.filter = f;
		this.expected = expected;
		if (expected == null) {
			this.expected = new ArrayList<DelegationIdentifier>();
		}
	}

	@Override
	public void runStep() throws Throwable {
		assertNotNull(uri);
		assertNotNull(this.credential);
		assertNotNull(this.credential.getCredential());
		DelegationUserClient client = new DelegationUserClient(uri,
				this.credential.getCredential());
		List<DelegationRecord> records = client.findMyDelegatedCredentials(filter);
		assertEquals(expected.size(), records.size());
		for (int i = 0; i < records.size(); i++) {
			boolean found = false;
			for (int j = 0; j < expected.size(); j++) {
				if (records.get(i).getDelegationIdentifier().equals(
						expected.get(j))) {
					found = true;
					break;
				}
			}
			assertTrue(found);
		}

	}

	public GlobusCredential getCredential() {
		return this.proxy;
	}

}
