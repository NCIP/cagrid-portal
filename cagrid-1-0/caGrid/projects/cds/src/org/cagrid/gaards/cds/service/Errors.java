package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultHelper;

import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;

public class Errors {

	public static String UNEXPECTED_DATABASE_ERROR = "An unexpected database error occurred.";
	public static String UNEXPECTED_ERROR_LOADING_CERTIFICATE_CHAIN = "An unexpected error occurred in loading the certificate chain.";
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
	
	
	
	public static CDSInternalFault getDatabaseFault(Exception e) {
		CDSInternalFault f = new CDSInternalFault();
		f.setFaultString(Errors.UNEXPECTED_DATABASE_ERROR);
		FaultHelper helper = new FaultHelper(f);
		helper.addFaultCause(e);
		f = (CDSInternalFault) helper.getFault();
		return f;
	}

}
