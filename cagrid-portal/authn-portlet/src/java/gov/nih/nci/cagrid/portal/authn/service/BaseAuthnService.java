/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.authn.service;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.bean.SAMLAssertion;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.dorian.client.DorianClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.DelegationPathLength;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
import gov.nih.nci.cagrid.portal.authn.AuthnTimeoutException;
import gov.nih.nci.cagrid.portal.authn.domain.IdPAuthnInfo;
import org.cagrid.gaards.authentication.client.AuthenticationServiceClient;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.KeyUtil;
import org.globus.gsi.GlobusCredential;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BaseAuthnService implements AuthnService {
    private static final String EMAIL_EXP = "/*[local-name()='Assertion']/*[local-name()='AttributeStatement']/*[local-name()='Attribute' and @AttributeName='urn:mace:dir:attribute-def:mail']/*[local-name()='AttributeValue']/text()";
    private static final String FIRST_NAME_EXP = "/*[local-name()='Assertion']/*[local-name()='AttributeStatement']/*[local-name()='Attribute' and @AttributeName='urn:mace:dir:attribute-def:givenName']/*[local-name()='AttributeValue']/text()";
    private static final String LAST_NAME_EXP = "/*[local-name()='Assertion']/*[local-name()='AttributeStatement']/*[local-name()='Attribute' and @AttributeName='urn:mace:dir:attribute-def:sn']/*[local-name()='AttributeValue']/text()";
    private int proxyLifetimeHours = 12;
    private int proxyLifetimeMinutes = 0;
    private int proxyLifetimeSeconds = 0;
    private int delegationPathLength = 0;
    private long timeout = 10000L;
    private long ticketLifetime = 10000L;

    public IdPAuthnInfo authenticateToIdP(String username, String password,
            String idpUrl) throws AuthnServiceException,
            InvalidCredentialFault, AuthnTimeoutException {

        BasicAuthenticationCredential bac = new BasicAuthenticationCredential();
        bac.setUserId(username);
        bac.setPassword(password);
        Credential credential = new Credential();
        credential.setBasicAuthenticationCredential(bac);
        AuthenticationServiceClient idpClient;
        try {
            idpClient = new AuthenticationServiceClient(idpUrl);
        } catch (Exception ex) {
            throw new AuthnServiceException(
                    "Error instantiating AuthenticationServiceClient: "
                            + ex.getMessage(), ex);
        }

        GetSAMLThread getSaml = new GetSAMLThread(idpClient, credential);
        getSaml.start();
        try {
            getSaml.join(getTimeout());
        } catch (Exception ex) {

        }
        if (getSaml.ex != null) {
            if (getSaml.ex instanceof InvalidCredentialFault) {
                throw (InvalidCredentialFault) getSaml.ex;
            } else if (getSaml.ex instanceof InsufficientAttributeFault) {
                throw new AuthnServiceException(
                        ((InsufficientAttributeFault) getSaml.ex)
                                .getFaultString(), getSaml.ex);
            } else if (getSaml.ex instanceof AuthenticationProviderFault) {
                throw new AuthnServiceException(
                        ((AuthenticationProviderFault) getSaml.ex)
                                .getFaultString(), getSaml.ex);
            } else {
                throw new AuthnServiceException("Error getting SAML: "
                        + getSaml.ex.getMessage(), getSaml.ex);
            }
        }
           if (!getSaml.finished) {
            throw new AuthnTimeoutException(
                    "Authentication with IDP timed out.");
        }

        String email = null;
        String firstName = null;
        String lastName = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(getSaml.saml
                    .getXml().getBytes()));
            XPath xpathEngine = XPathFactory.newInstance().newXPath();
            email = (String) xpathEngine.evaluate(EMAIL_EXP, doc,
                    XPathConstants.STRING);
            firstName = (String) xpathEngine.evaluate(FIRST_NAME_EXP, doc,
                    XPathConstants.STRING);
            lastName = (String) xpathEngine.evaluate(LAST_NAME_EXP, doc,
                    XPathConstants.STRING);
        } catch (Exception ex) {
            throw new AuthnServiceException(
                    "Error getting user information from SAML: "
                            + ex.getMessage(), ex);
        }

        return new IdPAuthnInfo(username, email, firstName, lastName,
                getSaml.saml.getXml());
    }



    public GlobusCredential authenticateToIFS(String ifsUrl, String saml)
            throws AuthnServiceException, AuthnTimeoutException {
        GlobusCredential cred = null;

        ProxyLifetime lifetime = new ProxyLifetime();
        lifetime.setHours(getProxyLifetimeHours());
        lifetime.setMinutes(getProxyLifetimeMinutes());
        lifetime.setSeconds(getProxyLifetimeSeconds());

        DorianClient ifsClient = null;
        try {
            ifsClient = new DorianClient(ifsUrl);
        } catch (Exception ex) {
            throw new AuthnServiceException(
                    "Error instantiating DorianClient: " + ex.getMessage(), ex);
        }
        KeyPair pair = null;
        PublicKey key = null;
        try {
            pair = KeyUtil.generateRSAKeyPair512();
            key = new PublicKey(KeyUtil.writePublicKey(pair.getPublic()));
        } catch (Exception ex) {
            throw new AuthnServiceException("Error generating key pair: "
                    + ex.getMessage(), ex);
        }

        gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml2 = null;
        try {
            saml2 = new gov.nih.nci.cagrid.dorian.bean.SAMLAssertion(saml);
        } catch (Exception ex) {
            throw new AuthnServiceException("Error parsing SAMLAssertion: "
                    + ex.getMessage(), ex);
        }

        GetCertsThread getCerts = new GetCertsThread(ifsClient, saml2, key,
                lifetime, new DelegationPathLength(getDelegationPathLength()));
        getCerts.start();
        try {
            getCerts.join(getTimeout());
        } catch (Exception ex) {

        }

        if (getCerts.ex != null) {
            String msg = null;
            if (getCerts.ex instanceof InvalidAssertionFault) {
                msg = ((InvalidAssertionFault) getCerts.ex).getFaultString();
            } else if (getCerts.ex instanceof InvalidProxyFault) {
                msg = ((InvalidProxyFault) getCerts.ex).getFaultString();
            } else if (getCerts.ex instanceof UserPolicyFault) {
                msg = ((UserPolicyFault) getCerts.ex).getFaultString();
            } else if (getCerts.ex instanceof PermissionDeniedFault) {
                msg = ((PermissionDeniedFault) getCerts.ex).getFaultString();
            } else {
                msg = getCerts.ex.getMessage();
            }
            throw new AuthnServiceException("Error authenticating to IFS: "
                    + msg, getCerts.ex);
        }

        if (!getCerts.finished) {
            throw new AuthnTimeoutException(
                    "Authentication with IFS timed out.");
        }

        X509Certificate[] certs = new X509Certificate[getCerts.certs.length];
        for (int i = 0; i < getCerts.certs.length; i++) {
            try {
                certs[i] = CertUtil.loadCertificate(getCerts.certs[i]
                        .getCertificateAsString());
            } catch (Exception ex) {
                throw new AuthnServiceException("Error loading certificate: "
                        + ex.getMessage(), ex);
            }
        }

        try {
            cred = new GlobusCredential(pair.getPrivate(), certs);
        } catch (Exception ex) {
            throw new AuthnServiceException(
                    "Error instantiating GlobusCredential: " + ex.getMessage(),
                    ex);
        }
        return cred;
    }

    public int getProxyLifetimeHours() {
        return proxyLifetimeHours;
    }

    public void setProxyLifetimeHours(int proxyLifetimeHours) {
        this.proxyLifetimeHours = proxyLifetimeHours;
    }

    public int getProxyLifetimeMinutes() {
        return proxyLifetimeMinutes;
    }

    public void setProxyLifetimeMinutes(int proxyLifetimeMinutes) {
        this.proxyLifetimeMinutes = proxyLifetimeMinutes;
    }

    public int getProxyLifetimeSeconds() {
        return proxyLifetimeSeconds;
    }

    public void setProxyLifetimeSeconds(int proxyLifetimeSeconds) {
        this.proxyLifetimeSeconds = proxyLifetimeSeconds;
    }

    public int getDelegationPathLength() {
        return delegationPathLength;
    }

    public void setDelegationPathLength(int delegationPathLength) {
        this.delegationPathLength = delegationPathLength;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTicketLifetime() {
        return ticketLifetime;
    }

    public void setTicketLifetime(long ticketLifetime) {
        this.ticketLifetime = ticketLifetime;
    }

    private static class GetSAMLThread extends Thread {
        private AuthenticationServiceClient client;
        private Credential credential;
        SAMLAssertion saml;
        Exception ex;
        boolean finished;

        GetSAMLThread(AuthenticationServiceClient client, Credential credential) {
            this.client = client;
            this.credential = credential;
        }

        public void run() {
            try {
                this.saml = this.client.authenticate(this.credential);
                this.finished = true;
            } catch (Exception ex) {
                this.ex = ex;
            }
        }
    }

    private static class GetCertsThread extends Thread {
        private DorianClient client;
        private gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml;
        private PublicKey key;
        private ProxyLifetime lifetime;
        private DelegationPathLength delegationPathLength;
        Exception ex;
        gov.nih.nci.cagrid.dorian.bean.X509Certificate[] certs;
        boolean finished;

        GetCertsThread(DorianClient client,
                gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml,
                PublicKey key, ProxyLifetime lifetime,
                DelegationPathLength delegationPathLength) {
            this.client = client;
            this.saml = saml;
            this.key = key;
            this.lifetime = lifetime;
            this.delegationPathLength = delegationPathLength;
        }

        public void run() {
            try {
                this.certs = this.client.createProxy(this.saml, this.key,
                        this.lifetime, this.delegationPathLength);
                this.finished = true;
            } catch (Exception ex) {
                this.ex = ex;
            }
        }
    }
}
