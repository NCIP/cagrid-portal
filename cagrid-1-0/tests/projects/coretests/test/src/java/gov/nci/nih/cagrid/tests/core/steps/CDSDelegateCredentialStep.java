/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.DelegatedCredential;
import gov.nci.nih.cagrid.tests.core.GridCredential;

import java.util.List;

import org.cagrid.gaards.cds.client.DelegationUserClient;
import org.cagrid.gaards.cds.common.AllowedParties;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.common.ProxyLifetime;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;

import com.atomicobject.haste.framework.Step;

public class CDSDelegateCredentialStep extends Step implements
		DelegatedCredential {

	private String serviceURL;
	private GridCredential delegator;
	private List<GridCredential> allowedParties;
	private ProxyLifetime delegationLifetime;
	private ProxyLifetime delegatedCredentialsLifetime;
	private DelegatedCredentialReference delegatedCredentialReference;

	public CDSDelegateCredentialStep(String serviceURL,
			GridCredential delegator, List<GridCredential> allowedParties,
			ProxyLifetime delegatedCredentialsLifetime) {
		this(serviceURL, delegator, allowedParties, null,
				delegatedCredentialsLifetime);
	}

	public CDSDelegateCredentialStep(String serviceURL,
			GridCredential delegator, List<GridCredential> allowedParties,
			ProxyLifetime delegationLifetime,
			ProxyLifetime delegatedCredentialsLifetime) {
		this.serviceURL = serviceURL;
		this.delegator = delegator;
		this.allowedParties = allowedParties;
		this.delegationLifetime = delegationLifetime;
		this.delegatedCredentialsLifetime = delegatedCredentialsLifetime;
	}

	@Override
	public void runStep() throws Throwable {
		assertNotNull(this.serviceURL);
		assertNotNull(this.allowedParties);
		assertNotNull(this.delegator);
		assertNotNull(this.delegator.getCredential());
		assertNotNull(this.delegatedCredentialsLifetime);

		IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
		AllowedParties ap = new AllowedParties();
		String[] id = new String[allowedParties.size()];
		for (int i = 0; i < allowedParties.size(); i++) {
			assertNotNull(allowedParties.get(i).getCredential());
			id[i] = allowedParties.get(i).getCredential().getIdentity();
		}
		ap.setGridIdentity(id);
		policy.setAllowedParties(ap);
		DelegationUserClient client = new DelegationUserClient(this.serviceURL,
				this.delegator.getCredential());
		this.delegatedCredentialReference = client.delegateCredential(
				this.delegationLifetime, policy,
				this.delegatedCredentialsLifetime);

	}

	public DelegatedCredentialReference getDelegatedCredentialReference() {
		return this.delegatedCredentialReference;
	}
}
