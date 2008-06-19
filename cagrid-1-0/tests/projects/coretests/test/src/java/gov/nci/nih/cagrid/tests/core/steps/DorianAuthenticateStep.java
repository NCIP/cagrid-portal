/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.Constants;
import gov.nci.nih.cagrid.tests.core.GridCredential;
import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import org.cagrid.gaards.dorian.client.IFSUserClient;
import org.cagrid.gaards.dorian.client.IdPUserClient;
import org.cagrid.gaards.dorian.federation.ProxyLifetime;
import org.cagrid.gaards.dorian.idp.BasicAuthCredential;
import org.globus.gsi.GlobusCredential;


/**
 * This step authenticates a user with dorian and saves the proxy as the globus
 * default proxy.
 * 
 * @author Patrick McConnell
 */
public class DorianAuthenticateStep extends Step implements GridCredential{
    private String userId;
    private String password;
    private String serviceURL;
    private int hours;
    private SAMLAssertion saml;
    private GlobusCredential credential;
    private int delegationPathLength;


    public DorianAuthenticateStep(String serviceURL) {
        this("dorian", Constants.DORIAN_ADMIN_PASSWORD, serviceURL);
    }


    public DorianAuthenticateStep(String userId, String password, String serviceURL) {
        this(userId, password, serviceURL, 12,2);
    }


    public DorianAuthenticateStep(String userId, String password, String serviceURL, int hours, int delegationPathLength) {
        super();
        this.userId = userId;
        this.password = password;
        this.serviceURL = serviceURL;
        this.hours = hours;
        this.delegationPathLength = delegationPathLength;
    }
    


    @Override
    public void runStep() throws Throwable {
        BasicAuthCredential authCred = new BasicAuthCredential();
        authCred.setUserId(this.userId);
        authCred.setPassword(this.password);
        IdPUserClient client = new IdPUserClient(this.serviceURL);
        this.saml = client.authenticate(authCred);

        IFSUserClient c2 = new IFSUserClient(this.serviceURL);
        this.credential = c2.createProxy(this.saml, new ProxyLifetime(this.hours, 0, 0), this.delegationPathLength);
        //ProxyManager.getInstance().addProxy(this.credential);
        ProxyUtil.saveProxyAsDefault(this.credential);
    }


    public GlobusCredential getCredential() {
        return this.credential;
    }


    public void setCredential(GlobusCredential credential) {
        this.credential = credential;
    }


    public SAMLAssertion getSaml() {
        return this.saml;
    }


    public void setSaml(SAMLAssertion saml) {
        this.saml = saml;
    }
}
