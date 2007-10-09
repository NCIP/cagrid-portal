package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultHelper;

import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;

public class Errors {

	public static String UNEXPECTED_DATABASE_ERROR = "An unexpected database error occurred.";
	public static String UNEXPECTED_ERROR_LOADING_CERTIFICATE_CHAIN = "An unexpected error occurred in loading the certificate chain.";
	public static String UNEXPECTED_ERROR_EXTRACTING_IDENTITY_FROM_CERTIFICATE_CHAIN = "An unexpected error occurred in extracting the grid identity from the certificate chain.";
	public static String UNEXPECTED_ERROR_LOADING_DELEGATION_POLICY = "An unexpected error occurred in loading the delegation policy.";
	public static String DELEGATION_RECORD_DOES_NOT_EXIST = "The delegation record does not exist.";
	public static String KEY_MANAGER_CHANGED = "The key manager cannot be changed.";
	public static String DELEGATION_POLICY_NOT_SUPPORTED = "The delegation policy specified is not supported.";
	public static String INVALID_KEY_LENGTH_SPECIFIED = "Invalid key length specified.";
	public static String MULTIPLE_HANDLERS_FOUND_FOR_POLICY = "Multiple handlers found for handling the policy, ";
	public static String INITIATOR_DOES_NOT_MATCH_APPROVER = "The approver must be the same entity as the initiator.";
	public static String CERTIFICATE_CHAIN_NOT_SPECIFIED = "No certificate chain specified.";
	public static String INSUFFICIENT_CERTIFICATE_CHAIN_SPECIFIED = "Insufficient certificate chain specified.";
	public static String IDENTITY_DOES_NOT_MATCH_INITIATOR = "The identity of the delegated credentials does not match the identity of the initiator.";
	public static String PUBLIC_KEY_DOES_NOT_MATCH = "The public key generated for the delegation does not match the public key supplied in the certificate.";
	public static String INVALID_CERTIFICATE_CHAIN = "The certificate chain provided is not valid.";
	
	
	public static CDSInternalFault getDatabaseFault(Exception e) {
		return getInternalFault(UNEXPECTED_DATABASE_ERROR, e);
	}

	public static CDSInternalFault getInternalFault(String error) {
		CDSInternalFault f = new CDSInternalFault();
		f.setFaultString(error);
		return f;
	}

	public static CDSInternalFault getInternalFault(String error, Exception e) {
		CDSInternalFault f = new CDSInternalFault();
		f.setFaultString(error);
		FaultHelper helper = new FaultHelper(f);
		helper.addFaultCause(e);
		f = (CDSInternalFault) helper.getFault();
		return f;
	}

	public static DelegationFault getDelegationFault(String error) {
		DelegationFault f = new DelegationFault();
		f.setFaultString(error);
		return f;
	}

}
