package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import junit.framework.TestCase;

import org.cagrid.gaards.cds.common.AllowedParties;
import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.service.policy.IdentityPolicyHandler;
import org.cagrid.gaards.cds.stubs.types.InvalidPolicyFault;
import org.cagrid.gaards.cds.testutils.Utils;

public class IdentityPolicyHandlerTest extends TestCase {

	public void testCreateDestroy() {
		IdentityPolicyHandler handler = null;
		try {
			handler = Utils.getIdentityPolicyHandler();
			handler.removeAllStoredPolicies();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				handler.removeAllStoredPolicies();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testStoreInvalidPolicy() {
		IdentityPolicyHandler handler = null;
		try {
			DelegationIdentifier id = new DelegationIdentifier();
			id.setDelegationId(1);
			handler = Utils.getIdentityPolicyHandler();
			try {
				handler.storePolicy(id, new InvalidDelegationPolicy());
				fail("Should not be able to store invalid policy.");
			} catch (InvalidPolicyFault e) {
				String s = e.getFaultString();
				String expected = "The policy handler "
						+ IdentityPolicyHandler.class.getName()
						+ " does not support the policy";
				if (s.indexOf(expected) == -1) {
					fail("Should not be able to store invalid policy.");
				}
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				handler.removeAllStoredPolicies();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testStoreEmptyPolicy() {
		IdentityPolicyHandler handler = null;
		try {
			DelegationIdentifier id = new DelegationIdentifier();
			id.setDelegationId(1);
			handler = Utils.getIdentityPolicyHandler();
			IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
			try {
				handler.storePolicy(id, policy);
				fail("Should not be able to store empty policy.");
			} catch (InvalidPolicyFault e) {
				String s = e.getFaultString();
				String expected = "No allowed parties provided.";
				if (s.indexOf(expected) == -1) {
					fail("Should not be able to store empty policy.");
				}
			}
			AllowedParties ap = new AllowedParties();
			policy.setAllowedParties(ap);
			try {
				handler.storePolicy(id, policy);
				fail("Should not be able to store empty policy.");
			} catch (InvalidPolicyFault e) {
				String s = e.getFaultString();
				String expected = "No allowed parties provided.";
				if (s.indexOf(expected) == -1) {
					fail("Should not be able to store empty policy.");
				}
			}

			ap.setGridIdentity(new String[0]);
			try {
				handler.storePolicy(id, policy);
				fail("Should not be able to store empty policy.");
			} catch (InvalidPolicyFault e) {
				String s = e.getFaultString();
				String expected = "No allowed parties provided.";
				if (s.indexOf(expected) == -1) {
					fail("Should not be able to store empty policy.");
				}
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				handler.removeAllStoredPolicies();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testStoreExistingPolicy() {
		IdentityPolicyHandler handler = null;
		try {
			DelegationIdentifier id = new DelegationIdentifier();
			id.setDelegationId(1);
			handler = Utils.getIdentityPolicyHandler();
			IdentityDelegationPolicy policy = new IdentityDelegationPolicy();
			AllowedParties ap = new AllowedParties();
			ap
					.setGridIdentity(new String[] { "/C=US/O=abc/OU=xyz/OU=caGrid/CN=jdoe" });
			policy.setAllowedParties(ap);
			handler.storePolicy(id, policy);
			assertTrue(handler.policyExists(id));
			try {
				handler.storePolicy(id, policy);
				fail("Should not be able to store a policy that already exists.");
			} catch (InvalidPolicyFault e) {
				String s = e.getFaultString();
				String expected = "A policy already exists for the delegation";
				if (s.indexOf(expected) == -1) {
					fail("Should not be able to store a policy that already exists.");
				}
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				handler.removeAllStoredPolicies();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
