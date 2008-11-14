package org.cagrid.fqp.test.remote.secure.steps;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.cds.client.ClientConstants;
import org.cagrid.gaards.cds.client.DelegationUserClient;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.common.ProxyLifetime;
import org.cagrid.gaards.cds.common.Utils;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.globus.gsi.GlobusCredential;

public class DelegateCredentialStep extends Step {
    
    private static Log LOG = LogFactory.getLog(DelegateCredentialStep.class);
    
    private ServiceContainer cdsContainer = null;
    private File proxyFile = null;
    private File fqpHostCredential = null;
    
    public DelegateCredentialStep(ServiceContainer cdsContainer, File proxyFile, File fqpHostCredential) {
        this.cdsContainer = cdsContainer;
        this.proxyFile = proxyFile;
        this.fqpHostCredential = fqpHostCredential;
    }
    
    
    public void runStep() {
        // load the credential to delegate
        GlobusCredential proxyCredential = getProxyCredential();
        
        // load the FQP's host credential
        GlobusCredential fqpHostCredential = getFqpHostCredential();
        
        // set the delegation lifetime to 10 minutes
        ProxyLifetime delegationLifetime = new ProxyLifetime();
        delegationLifetime.setHours(0);
        delegationLifetime.setMinutes(10);
        delegationLifetime.setSeconds(0);
        
        // set the lifetime for services which obtain a delegated credential to 5 minutes
        ProxyLifetime issuedCredentialLifetime = new ProxyLifetime();
        issuedCredentialLifetime.setHours(0);
        issuedCredentialLifetime.setMinutes(5);
        issuedCredentialLifetime.setSeconds(0);

        // Specifies the path length of the credential being delegate the minumum is 1.
        int delegationPathLength = 1;
        
        // Specifies the path length of the credentials issued to allowed parties. A path length of 0 means that 
        //the requesting party cannot further delegate the credential.
        int issuedCredentialPathLength = 0;

        // Specifies the key length of the delegated credential
        int keySize = ClientConstants.DEFAULT_KEY_SIZE;

        // The policy stating which parties will be allowed to obtain a delegated credential. The CDS will only 
        // issue credentials to parties listed in this policy.
        List<String> parties = new ArrayList<String>();
        parties.add(fqpHostCredential.getIdentity());
        IdentityDelegationPolicy policy = Utils.createIdentityDelegationPolicy(parties);
        
        // get CDS EPR
        EndpointReferenceType cdsEPR = null;
        try {
            cdsEPR = cdsContainer.getServiceEPR("cagrid/CredentialDelegationService");
        } catch (MalformedURIException ex) {
            String message = "Error obtaining an EPR for the CDS: " + ex.getMessage();
            LOG.error(message, ex);
            fail(message);
        }
        
        // set up the client
        DelegationUserClient cdsClient = null;
        try {
            cdsClient = new DelegationUserClient(cdsEPR.toString());
        } catch (Exception ex) {
            String message = "Error creating CDS client: " + ex.getMessage();
            LOG.error(message, ex);
            fail(message);
        }
        
        // delegate the credential and get a reference
        DelegatedCredentialReference delegationReference = null;
        try {
            delegationReference = cdsClient.delegateCredential(
                delegationLifetime, delegationPathLength,
                policy, issuedCredentialLifetime,
                issuedCredentialPathLength, keySize); 
        } catch (Exception ex) {
            String message = "Error delegating credential to CDS: " + ex.getMessage();
            LOG.error(message, ex);
            fail(message);
        }
        
        
    }
    
    
    private GlobusCredential getProxyCredential() {
        GlobusCredential credential = null;
        try {
            FileInputStream fis = new FileInputStream(proxyFile);
            credential = new GlobusCredential(fis);
            fis.close();
        } catch (Exception ex) {
            String message = "Error obtaining proxy for delegation: " + ex.getMessage();
            LOG.error(message, ex);
            fail(message);
        }
        return credential;
    }
    
    
    private GlobusCredential getFqpHostCredential() {
        GlobusCredential credential = null;
        try {
            FileInputStream fis = new FileInputStream(fqpHostCredential);
            credential = new GlobusCredential(fis);
            fis.close();
        } catch (Exception ex) {
            String message = "Error obtaining FQP host credential: " + ex.getMessage();
            LOG.error(message, ex);
            fail(message);
        }
        return credential;
    }
}
