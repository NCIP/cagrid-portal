/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.client.IdPAuthenticationClient;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gridca.ui.ProxyManager; // import
// gov.nih.nci.cagrid.gridca.portal.ProxyManager;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import org.globus.gsi.GlobusCredential;

import com.atomicobject.haste.framework.Step;


/**
 * This step authenticates a user with dorian and saves the proxy as the globus
 * default proxy.
 * 
 * @author Patrick McConnell
 */
public class DorianAuthenticateStep extends Step {
    private String userId;
    private String password;
    private String serviceURL;
    private int hours;
    private SAMLAssertion saml;
    private GlobusCredential credential;


    public DorianAuthenticateStep(String serviceURL) {
        this("dorian", "password", serviceURL);
    }


    public DorianAuthenticateStep(String userId, String password, String serviceURL) {
        this(userId, password, serviceURL, 12);
    }


    public DorianAuthenticateStep(String userId, String password, String serviceURL, int hours) {
        super();

        this.userId = userId;
        this.password = password;
        this.serviceURL = serviceURL;
        this.hours = hours;
    }


    @Override
    public void runStep() throws Throwable {
        BasicAuthCredential authCred = new BasicAuthCredential();
        authCred.setUserId(this.userId);
        authCred.setPassword(this.password);
        IdPAuthenticationClient client = new IdPAuthenticationClient(this.serviceURL, authCred);
        this.saml = client.authenticate();

        IFSUserClient c2 = new IFSUserClient(this.serviceURL);
        this.credential = c2.createProxy(this.saml, new ProxyLifetime(this.hours, 0, 0), 2);
        ProxyManager.getInstance().addProxy(this.credential);
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
