package org.cagrid.gaards.dorian.federation;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.ThreadManager;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bouncycastle.asn1.x509.CRLReason;
import org.cagrid.gaards.dorian.ca.CertificateAuthority;
import org.cagrid.gaards.dorian.ca.CertificateAuthorityFault;
import org.cagrid.gaards.dorian.common.LoggingObject;
import org.cagrid.gaards.dorian.service.PropertyManager;
import org.cagrid.gaards.dorian.service.util.AddressValidator;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateRequestFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserCertificateFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.dorian.stubs.types.UserPolicyFault;
import org.cagrid.gaards.pki.CRLEntry;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.tools.database.Database;
import org.cagrid.tools.groups.Group;
import org.cagrid.tools.groups.GroupException;
import org.cagrid.tools.groups.GroupManager;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdentityFederationManager extends LoggingObject implements Publisher {

    private final int CERTIFICATE_START_OFFSET_SECONDS = -10;

    private UserManager um;

    private TrustedIdPManager tm;

    private IdentityFederationProperties conf;

    private CertificateAuthority ca;

    private Object mutex = new Object();

    public static final String ADMINISTRATORS = "administrators";

    private Group administrators;

    private GroupManager groupManager;

    private HostCertificateManager hostManager;

    private ThreadManager threadManager;

    private boolean publishCRL = false;

    private CertificateBlacklistManager blackList;

    private UserCertificateManager userCertificateManager;


    public IdentityFederationManager(IdentityFederationProperties conf, Database db, PropertyManager properties,
        CertificateAuthority ca, FederationDefaults defaults) throws DorianInternalFault {
        this(conf, db, properties, ca, defaults, false);
    }


    public IdentityFederationManager(IdentityFederationProperties conf, Database db, PropertyManager properties,
        CertificateAuthority ca, FederationDefaults defaults, boolean ignoreCRL) throws DorianInternalFault {
        super();
        this.conf = conf;
        this.ca = ca;
        threadManager = new ThreadManager();
        this.blackList = new CertificateBlacklistManager(db);
        this.userCertificateManager = new UserCertificateManager(db, this, this.blackList);
        tm = new TrustedIdPManager(conf, db);
        um = new UserManager(db, conf, properties, ca, tm, this, defaults);
        um.buildDatabase();
        this.groupManager = new GroupManager(db);
        try {
            if (!this.groupManager.groupExists(ADMINISTRATORS)) {
                this.groupManager.addGroup(ADMINISTRATORS);
                this.administrators = this.groupManager.getGroup(ADMINISTRATORS);
                if (defaults.getDefaultUser() != null) {
                    this.administrators.addMember(defaults.getDefaultUser().getGridId());
                } else {
                    logWarning("COULD NOT ADD DEFAULT USER TO ADMINISTRATORS GROUP, NO DEFAULT USER WAS FOUND!!!");
                }
            } else {
                this.administrators = this.groupManager.getGroup(ADMINISTRATORS);
            }
        } catch (GroupException e) {
            logError(e.getMessage(), e);
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred in setting up the administrators group.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
        this.hostManager = new HostCertificateManager(db, this.conf, ca, this, blackList);

        try {
            TrustedIdP idp = tm.getTrustedIdP(CertUtil.loadCertificate(defaults.getDefaultIdP().getIdPCertificate()));
            if ((idp.getAuthenticationServiceURL() == null)
                && (defaults.getDefaultIdP().getAuthenticationServiceURL() != null)) {
                idp.setAuthenticationServiceURL(defaults.getDefaultIdP().getAuthenticationServiceURL());
                tm.updateIdP(idp);
            } else if ((defaults.getDefaultIdP().getAuthenticationServiceURL() != null)
                && (!defaults.getDefaultIdP().getAuthenticationServiceURL().equals(idp.getAuthenticationServiceURL()))) {
                idp.setAuthenticationServiceURL(defaults.getDefaultIdP().getAuthenticationServiceURL());
                tm.updateIdP(idp);
            }
        } catch (Exception e) {
            logError(e.getMessage(), e);
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred in ensuring the integrity of the Dorian IdP.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
        if (!ignoreCRL) {
            publishCRL = true;
            publishCRL();
        }
    }


    public String getIdentityAssignmentPolicy() {
        return um.getIdentityAssignmentPolicy();
    }


    public GridUserPolicy[] getUserPolicies(String callerGridIdentity) throws DorianInternalFault,
        PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        return tm.getAccountPolicies();
    }


    public String getUserIdVerifyTrustedIdP(X509Certificate idpCert, String identity) throws DorianInternalFault,
        InvalidUserFault, InvalidTrustedIdPFault, PermissionDeniedFault {
        if (identity == null) {
            PermissionDeniedFault fault = new PermissionDeniedFault();
            fault.setFaultString("No credentials specified.");
            throw fault;
        }
        TrustedIdP idp = tm.getTrustedIdPByDN(idpCert.getSubjectDN().getName());
        GridUser usr = um.getUser(identity);
        if (usr.getIdPId() != idp.getId()) {
            PermissionDeniedFault fault = new PermissionDeniedFault();
            fault.setFaultString("Not a valid user of the IdP " + idp.getName());
            throw fault;
        }
        return usr.getUID();
    }


    public TrustedIdP addTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws DorianInternalFault,
        InvalidTrustedIdPFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        return tm.addTrustedIdP(idp);
    }


    public void updateTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws DorianInternalFault,
        InvalidTrustedIdPFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        TrustedIdP curr = tm.getTrustedIdPById(idp.getId());
        boolean statusChanged = false;
        if ((idp.getStatus() != null) && (!idp.getStatus().equals(curr.getStatus()))) {
            statusChanged = true;
        }
        tm.updateIdP(idp);
        if (statusChanged) {
            publishCRL();
        }
    }


    public void removeTrustedIdP(String callerGridIdentity, long idpId) throws DorianInternalFault,
        InvalidTrustedIdPFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        tm.removeTrustedIdP(idpId);
        GridUserFilter uf = new GridUserFilter();
        uf.setIdPId(idpId);
        GridUser[] users = um.getUsers(uf);
        for (int i = 0; i < users.length; i++) {
            try {
                removeUser(users[i]);
            } catch (Exception e) {
                logError(e.getMessage(), e);
            }
        }
    }


    public TrustedIdP[] getTrustedIdPs(String callerGridIdentity) throws DorianInternalFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        return tm.getTrustedIdPs();
    }


    public GridUser getUser(String callerGridIdentity, long idpId, String uid) throws DorianInternalFault,
        InvalidUserFault, PermissionDeniedFault {
        GridUser caller = um.getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);

        return um.getUser(idpId, uid);
    }


    public GridUser[] findUsers(String callerGridIdentity, GridUserFilter filter) throws DorianInternalFault,
        PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        return um.getUsers(filter);
    }


    public void updateUser(String callerGridIdentity, GridUser usr) throws DorianInternalFault, InvalidUserFault,
        PermissionDeniedFault {
        GridUser caller = um.getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        um.updateUser(usr);
    }


    public void removeUserByLocalIdIfExists(X509Certificate idpCert, String localId) throws DorianInternalFault {
        try {
            TrustedIdP idp = tm.getTrustedIdPByDN(idpCert.getSubjectDN().getName());
            GridUser usr = um.getUser(idp.getId(), localId);
            removeUser(usr);
        } catch (InvalidUserFault e) {

        } catch (InvalidTrustedIdPFault f) {
            logError(f.getFaultString(), f);
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred removing the grid user, the IdP "
                + idpCert.getSubjectDN().getName() + " could not be resolved!!!");
            throw fault;
        }
    }


    private void removeUser(GridUser usr) throws DorianInternalFault, InvalidUserFault {
        try {
            um.removeUser(usr);
            this.userCertificateManager.removeCertificates(usr.getGridId());
            boolean publishCRLNow = this.hostManager.ownerRemovedUpdateHostCertificates(usr.getGridId(), false);
            this.groupManager.removeUserFromAllGroups(usr.getGridId());

            if (publishCRLNow) {
                publishCRL();
            }
        } catch (InvalidUserFault e) {
            throw e;
        } catch (GroupException e) {
            logError(e.getMessage(), e);
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred in removing the user from all groups.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
    }


    public void removeUser(String callerGridIdentity, GridUser usr) throws DorianInternalFault, InvalidUserFault,
        PermissionDeniedFault {
        GridUser caller = um.getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        removeUser(usr);
    }


    public void addAdmin(String callerGridIdentity, String gridIdentity) throws RemoteException, DorianInternalFault,
        PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        try {
            if (!this.administrators.isMember(gridIdentity)) {
                GridUser admin = getUser(gridIdentity);
                verifyActiveUser(admin);
                this.administrators.addMember(gridIdentity);
            }
        } catch (GroupException e) {
            logError(e.getMessage(), e);
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred in adding the user to the administrators group.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
    }


    public void removeAdmin(String callerGridIdentity, String gridIdentity) throws RemoteException,
        DorianInternalFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        try {
            this.administrators.removeMember(gridIdentity);
        } catch (GroupException e) {
            logError(e.getMessage(), e);
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred in removing the user from the administrators group.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
    }


    public String[] getAdmins(String callerGridIdentity) throws RemoteException, DorianInternalFault,
        PermissionDeniedFault {
        GridUser caller = getUser(callerGridIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        try {
            List<String> members = this.administrators.getMembers();
            String[] admins = new String[members.size()];
            for (int i = 0; i < members.size(); i++) {
                admins[i] = (String) members.get(i);
            }
            return admins;
        } catch (GroupException e) {
            logError(e.getMessage(), e);
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred determining the members of the administrators group.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
    }


    public X509Certificate requestUserCertificate(SAMLAssertion saml, PublicKey publicKey, CertificateLifetime lifetime)
        throws DorianInternalFault, InvalidAssertionFault, UserPolicyFault, PermissionDeniedFault {

        if (!saml.isSigned()) {
            InvalidAssertionFault fault = new InvalidAssertionFault();
            fault.setFaultString("The assertion specified is invalid, it MUST be signed by a trusted IdP");
            throw fault;
        }

        // Determine whether or not the assertion is expired
        Calendar cal = new GregorianCalendar();
        Date now = cal.getTime();
        if ((now.before(saml.getNotBefore())) || (now.after(saml.getNotOnOrAfter()))) {
            InvalidAssertionFault fault = new InvalidAssertionFault();
            fault.setFaultString("The Assertion is not valid at " + now + ", the assertion is valid from "
                + saml.getNotBefore() + " to " + saml.getNotOnOrAfter());
            throw fault;
        }

        // Make sure the assertion is trusted
        TrustedIdP idp = tm.getTrustedIdP(saml);
        SAMLAuthenticationStatement auth = getAuthenticationStatement(saml);

        // We need to verify the authentication method now
        boolean allowed = false;
        for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
            if (idp.getAuthenticationMethod(i).getValue().equals(auth.getAuthMethod())) {
                allowed = true;
            }
        }
        if (!allowed) {
            InvalidAssertionFault fault = new InvalidAssertionFault();
            fault.setFaultString("The authentication method " + auth.getAuthMethod()
                + " is not acceptable for the IdP " + idp.getName());
            throw fault;
        }

        // If the user does not exist, add them
        String uid = this.getAttribute(saml, idp.getUserIdAttributeDescriptor().getNamespaceURI(), idp
            .getUserIdAttributeDescriptor().getName());
        String email = this.getAttribute(saml, idp.getEmailAttributeDescriptor().getNamespaceURI(), idp
            .getEmailAttributeDescriptor().getName());
        String firstName = this.getAttribute(saml, idp.getFirstNameAttributeDescriptor().getNamespaceURI(), idp
            .getFirstNameAttributeDescriptor().getName());
        String lastName = this.getAttribute(saml, idp.getLastNameAttributeDescriptor().getNamespaceURI(), idp
            .getLastNameAttributeDescriptor().getName());

        AddressValidator.validateEmail(email);

        GridUser usr = null;
        if (!um.determineIfUserExists(idp.getId(), uid)) {
            try {
                usr = new GridUser();
                usr.setIdPId(idp.getId());
                usr.setUID(uid);
                usr.setFirstName(firstName);
                usr.setLastName(lastName);
                usr.setEmail(email);
                usr.setUserStatus(GridUserStatus.Pending);
                usr = um.addUser(idp, usr);
            } catch (Exception e) {
                logError(e.getMessage(), e);
                DorianInternalFault fault = new DorianInternalFault();
                fault.setFaultString("An unexpected error occurred in adding the user " + usr.getUID()
                    + " from the IdP " + idp.getName());
                FaultHelper helper = new FaultHelper(fault);
                helper.addFaultCause(e);
                fault = (DorianInternalFault) helper.getFault();
                throw fault;
            }
        } else {
            try {
                usr = um.getUser(idp.getId(), uid);
                boolean performUpdate = false;

                if ((usr.getFirstName() == null) || (!usr.getFirstName().equals(firstName))) {
                    usr.setFirstName(firstName);
                    performUpdate = true;
                }
                if ((usr.getLastName() == null) || (!usr.getLastName().equals(lastName))) {
                    usr.setLastName(lastName);
                    performUpdate = true;
                }
                if ((usr.getEmail() == null) || (!usr.getEmail().equals(email))) {
                    usr.setEmail(email);
                    performUpdate = true;
                }
                if (performUpdate) {
                    um.updateUser(usr);
                }

            } catch (Exception e) {
                logError(e.getMessage(), e);
                DorianInternalFault fault = new DorianInternalFault();
                fault.setFaultString("An unexpected error occurred in obtaining the user " + usr.getUID()
                    + " from the IdP " + idp.getName());
                FaultHelper helper = new FaultHelper(fault);
                helper.addFaultCause(e);
                fault = (DorianInternalFault) helper.getFault();
                throw fault;
            }
        }

        // Validate that the certificate is of valid length

        if (FederationUtils.getProxyValid(lifetime).after(FederationUtils.getMaxProxyLifetime(conf))) {
            UserPolicyFault fault = new UserPolicyFault();
            fault.setFaultString("The requested certificate lifetime exceeds the maximum certificate lifetime (hrs="
                + conf.getUserCertificateLifetime().getHours() + ", mins=" + conf.getUserCertificateLifetime().getMinutes()
                + ", sec=" + conf.getUserCertificateLifetime().getSeconds() + ")");
            throw fault;
        }

        // Run the policy
        AccountPolicy policy = null;
        try {
            Class c = Class.forName(idp.getUserPolicyClass());
            policy = (AccountPolicy) c.newInstance();
            policy.configure(conf, um);

        } catch (Exception e) {
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred in creating an instance of the user policy "
                + idp.getUserPolicyClass());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
        policy.applyPolicy(idp, usr);

        // Check to see if authorized
        this.verifyActiveUser(usr);

        // create user certificate

        try {

            String caSubject = ca.getCACertificate().getSubjectDN().getName();
            String sub = um.getUserSubject(caSubject, idp, usr.getUID());
            Calendar c1 = new GregorianCalendar();
            c1.add(Calendar.SECOND, CERTIFICATE_START_OFFSET_SECONDS);
            Date start = c1.getTime();
            Calendar c2 = new GregorianCalendar();
            c2.add(Calendar.HOUR, lifetime.getHours());
            c2.add(Calendar.MINUTE, lifetime.getMinutes());
            c2.add(Calendar.SECOND, lifetime.getSeconds());
            Date end = c2.getTime();
            X509Certificate userCert = ca.signCertificate(sub, publicKey, start, end);
            userCertificateManager.addUserCertifcate(usr.getGridId(), userCert);
            return userCert;
        } catch (Exception e) {
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred in creating a certificate for the user "
                + usr.getGridId() + ".");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }

    }


    // ///////////////////////////////
    /* HOST CERTIFICATE OPERATIONS */
    // ///////////////////////////////
    public HostCertificateRecord requestHostCertificate(String callerGridId, HostCertificateRequest req)
        throws DorianInternalFault, InvalidHostCertificateRequestFault, InvalidHostCertificateFault,
        PermissionDeniedFault {
        GridUser caller = getUser(callerGridId);
        verifyActiveUser(caller);
        long id = hostManager.requestHostCertifcate(callerGridId, req);
        HostCertificateRecord record = null;
        if (this.conf.autoHostCertificateApproval()) {
            record = hostManager.approveHostCertifcate(id);
        } else {
            record = hostManager.getHostCertificateRecord(id);
        }
        return record;
    }


    public HostCertificateRecord[] getHostCertificatesForCaller(String callerGridId) throws DorianInternalFault,
        PermissionDeniedFault {
        GridUser caller = getUser(callerGridId);
        verifyActiveUser(caller);
        List<HostCertificateRecord> list = hostManager.getHostCertificateRecords(callerGridId);
        HostCertificateRecord[] records = new HostCertificateRecord[list.size()];
        for (int i = 0; i < list.size(); i++) {
            records[i] = list.get(i);
        }

        return records;
    }


    public HostCertificateRecord approveHostCertificate(String callerGridId, long recordId) throws DorianInternalFault,
        InvalidHostCertificateFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridId);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        return hostManager.approveHostCertifcate(recordId);
    }


    public HostCertificateRecord[] findHostCertificates(String callerGridId, HostCertificateFilter f)
        throws DorianInternalFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridId);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        List<HostCertificateRecord> list = hostManager.findHostCertificates(f);
        HostCertificateRecord[] records = new HostCertificateRecord[list.size()];
        for (int i = 0; i < list.size(); i++) {
            records[i] = list.get(i);
        }
        return records;
    }


    public void updateHostCertificateRecord(String callerGridId, HostCertificateUpdate update)
        throws DorianInternalFault, InvalidHostCertificateFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridId);
        verifyActiveUser(caller);
        verifyAdminUser(caller);

        // We need to make sure that if the owner changed, that the owner is an
        // active user.
        if (update.getOwner() != null) {
            HostCertificateRecord record = hostManager.getHostCertificateRecord(update.getId());
            if (!record.getOwner().equals(update.getOwner())) {
                try {
                    verifyActiveUser(getUser(update.getOwner()));
                } catch (PermissionDeniedFault f) {
                    InvalidHostCertificateFault fault = new InvalidHostCertificateFault();
                    fault.setFaultString("The owner specified does not exist or is not an active user.");
                    throw fault;
                }
            }
        }

        hostManager.updateHostCertificateRecord(update);
    }


    public HostCertificateRecord renewHostCertificate(String callerGridId, long recordId) throws DorianInternalFault,
        InvalidHostCertificateFault, PermissionDeniedFault {
        GridUser caller = getUser(callerGridId);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        return hostManager.renewHostCertificate(recordId);
    }


    public void publishCRL() {
        if (publishCRL) {
            if ((conf.getGtsPublishCRLList() != null) && (conf.getGtsPublishCRLList().size() > 0)) {
                Runner runner = new Runner() {
                    public void execute() {
                        synchronized (mutex) {
                            List<String> services = conf.getGtsPublishCRLList();
                            try {
                                X509CRL crl = getCRL();
                                gov.nih.nci.cagrid.gts.bean.X509CRL x509 = new gov.nih.nci.cagrid.gts.bean.X509CRL();
                                x509.setCrlEncodedString(CertUtil.writeCRL(crl));
                                String authName = ca.getCACertificate().getSubjectDN().getName();
                                for (int i = 0; i < services.size(); i++) {
                                    String uri = services.get(i);
                                    try {
                                        debug("Publishing CRL to the GTS " + uri);
                                        GTSAdminClient client = new GTSAdminClient(uri, null);
                                        client.updateCRL(authName, x509);
                                        debug("Published CRL to the GTS " + uri);
                                    } catch (Exception ex) {
                                        getLog().error("Error publishing the CRL to the GTS " + uri + "!!!", ex);
                                    }

                                }

                            } catch (Exception e) {
                                getLog().error("Unexpected Error publishing the CRL!!!", e);
                            }
                        }
                    }
                };
                try {
                    threadManager.executeInBackground(runner);
                } catch (Exception t) {
                    t.getMessage();
                }
            }
        }
    }


    public X509CRL getCRL() throws DorianInternalFault {
        Map<Long, CRLEntry> list = new HashMap<Long, CRLEntry>();

        Set<String> users = this.um.getDisabledUsers();
        Iterator<String> itr = users.iterator();
        while (itr.hasNext()) {
            String gid = itr.next();
            List<BigInteger> userCerts = this.userCertificateManager.getActiveCertificates(gid);
            for (int i = 0; i < userCerts.size(); i++) {
                Long sn = userCerts.get(i).longValue();
                if (!list.containsKey(sn)) {
                    list.put(sn, new CRLEntry(userCerts.get(i), CRLReason.PRIVILEGE_WITHDRAWN));
                }
            }

            List<Long> hostCerts = this.hostManager.getHostCertificateRecordsSerialNumbers(gid);
            for (int i = 0; i < hostCerts.size(); i++) {
                if (!list.containsKey(hostCerts.get(i))) {
                    CRLEntry entry = new CRLEntry(BigInteger.valueOf(hostCerts.get(i).longValue()),
                        CRLReason.PRIVILEGE_WITHDRAWN);
                    list.put(hostCerts.get(i), entry);
                }
            }
        }

        List<BigInteger> compromisedUserCerts = this.userCertificateManager.getCompromisedCertificates();
        for (int i = 0; i < compromisedUserCerts.size(); i++) {
            Long sn = compromisedUserCerts.get(i).longValue();
            if (!list.containsKey(sn)) {
                list.put(sn, new CRLEntry(compromisedUserCerts.get(i), CRLReason.PRIVILEGE_WITHDRAWN));
            }
        }

        List<Long> hosts = this.hostManager.getDisabledHostCertificatesSerialNumbers();
        for (int i = 0; i < hosts.size(); i++) {
            if (!list.containsKey(hosts.get(i))) {
                CRLEntry entry = new CRLEntry(BigInteger.valueOf(hosts.get(i).longValue()),
                    CRLReason.PRIVILEGE_WITHDRAWN);
                list.put(hosts.get(i), entry);
            }
        }

        List<Long> blist = this.blackList.getBlackList();

        for (int i = 0; i < blist.size(); i++) {
            if (!list.containsKey(blist.get(i))) {
                CRLEntry entry = new CRLEntry(BigInteger.valueOf(blist.get(i).longValue()),
                    CRLReason.PRIVILEGE_WITHDRAWN);
                list.put(blist.get(i), entry);
            }
        }

        CRLEntry[] entries = new CRLEntry[list.size()];
        Iterator<CRLEntry> itr2 = list.values().iterator();
        int count = 0;
        while (itr2.hasNext()) {
            entries[count] = itr2.next();
            count++;
        }
        try {
            X509CRL crl = ca.getCRL(entries);
            return crl;

        } catch (Exception e) {
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("Unexpected error obtaining the CRL.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addDescription(Utils.getExceptionMessage(e));
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }

    }


    private GridUser getUser(String gridId) throws DorianInternalFault, PermissionDeniedFault {
        try {
            return um.getUser(gridId);
        } catch (InvalidUserFault f) {
            PermissionDeniedFault fault = new PermissionDeniedFault();
            fault.setFaultString("You are not a valid user!!!");
            throw fault;
        }
    }


    private void verifyAdminUser(GridUser usr) throws DorianInternalFault, PermissionDeniedFault {
        try {
            if (administrators.isMember(usr.getGridId())) {
                return;
            } else {
                PermissionDeniedFault fault = new PermissionDeniedFault();
                fault.setFaultString("You are NOT an Administrator!!!");
                throw fault;
            }

        } catch (GroupException e) {
            logError(e.getMessage(), e);
            DorianInternalFault fault = new DorianInternalFault();
            fault
                .setFaultString("An unexpected error occurred in determining if the user is a member of the administrators group.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
    }


    private void verifyActiveUser(GridUser usr) throws DorianInternalFault, PermissionDeniedFault {

        try {
            TrustedIdP idp = this.tm.getTrustedIdPById(usr.getIdPId());

            if (!idp.getStatus().equals(TrustedIdPStatus.Active)) {
                PermissionDeniedFault fault = new PermissionDeniedFault();
                fault.setFaultString("Access for your Identity Provider has been suspended!!!");
                throw fault;
            }
        } catch (InvalidTrustedIdPFault f) {
            PermissionDeniedFault fault = new PermissionDeniedFault();
            fault.setFaultString("Unexpected error in determining your Identity Provider has been suspended!!!");
            throw fault;
        }

        if (!usr.getUserStatus().equals(GridUserStatus.Active)) {
            if (usr.getUserStatus().equals(GridUserStatus.Suspended)) {
                PermissionDeniedFault fault = new PermissionDeniedFault();
                fault.setFaultString("The account has been suspended.");
                throw fault;

            } else if (usr.getUserStatus().equals(GridUserStatus.Rejected)) {
                PermissionDeniedFault fault = new PermissionDeniedFault();
                fault.setFaultString("The request for an account was rejected.");
                throw fault;

            } else if (usr.getUserStatus().equals(GridUserStatus.Pending)) {
                PermissionDeniedFault fault = new PermissionDeniedFault();
                fault.setFaultString("The request for an account has not been reviewed.");
                throw fault;
            } else {
                PermissionDeniedFault fault = new PermissionDeniedFault();
                fault.setFaultString("Unknown Reason");
                throw fault;
            }
        }

    }


    protected UserManager getUserManager() {
        return um;
    }


    private String getAttribute(SAMLAssertion saml, String namespace, String name) throws InvalidAssertionFault {
        Iterator itr = saml.getStatements();
        while (itr.hasNext()) {
            Object o = itr.next();
            if (o instanceof SAMLAttributeStatement) {
                SAMLAttributeStatement att = (SAMLAttributeStatement) o;
                Iterator attItr = att.getAttributes();
                while (attItr.hasNext()) {
                    SAMLAttribute a = (SAMLAttribute) attItr.next();
                    if ((a.getNamespace().equals(namespace)) && (a.getName().equals(name))) {
                        Iterator vals = a.getValues();
                        while (vals.hasNext()) {

                            String val = Utils.clean((String) vals.next());
                            if (val != null) {
                                return val;
                            }
                        }
                    }
                }
            }
        }
        InvalidAssertionFault fault = new InvalidAssertionFault();
        fault.setFaultString("The assertion does not contain the required attribute, " + namespace + ":" + name);
        throw fault;
    }


    private SAMLAuthenticationStatement getAuthenticationStatement(SAMLAssertion saml) throws InvalidAssertionFault {
        Iterator itr = saml.getStatements();
        SAMLAuthenticationStatement auth = null;
        while (itr.hasNext()) {
            Object o = itr.next();
            if (o instanceof SAMLAuthenticationStatement) {
                if (auth != null) {
                    InvalidAssertionFault fault = new InvalidAssertionFault();
                    fault.setFaultString("The assertion specified contained more that one authentication statement.");
                    throw fault;
                }
                auth = (SAMLAuthenticationStatement) o;
            }
        }
        if (auth == null) {
            InvalidAssertionFault fault = new InvalidAssertionFault();
            fault.setFaultString("No authentication statement specified in the assertion provided.");
            throw fault;
        }
        return auth;
    }


    public void clearDatabase() throws DorianInternalFault {
        this.um.clearDatabase();
        this.tm.clearDatabase();
        try {
            this.groupManager.clearDatabase();
        } catch (GroupException e) {
            logError(e.getMessage(), e);
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString("An unexpected error occurred in deleting the groups database.");
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }
        this.userCertificateManager.clearDatabase();
        this.hostManager.clearDatabase();
        this.blackList.clearDatabase();
        try {
            ca.clearCertificateAuthority();
        } catch (CertificateAuthorityFault e) {
            DorianInternalFault fault = new DorianInternalFault();
            fault.setFaultString(e.getFaultString());
            FaultHelper helper = new FaultHelper(fault);
            helper.addFaultCause(e);
            fault = (DorianInternalFault) helper.getFault();
            throw fault;
        }

    }


    public TrustedIdentityProviders getTrustedIdentityProviders() throws DorianInternalFault {

        TrustedIdentityProviders idps = new TrustedIdentityProviders();
        TrustedIdP[] list1 = this.tm.getTrustedIdPs();
        if (list1 != null) {
            List<TrustedIdentityProvider> list2 = new ArrayList<TrustedIdentityProvider>();
            for (int i = 0; i < list1.length; i++) {
                if (list1[i].getStatus().equals(TrustedIdPStatus.Active)) {
                    TrustedIdentityProvider idp = new TrustedIdentityProvider();
                    idp.setName(list1[i].getName());
                    idp.setDisplayName(list1[i].getDisplayName());
                    idp.setAuthenticationServiceURL(list1[i].getAuthenticationServiceURL());
                    idp.setAuthenticationServiceIdentity(list1[i].getAuthenticationServiceIdentity());
                    list2.add(idp);
                }
            }

            TrustedIdentityProvider[] list3 = new TrustedIdentityProvider[list2.size()];
            for (int i = 0; i < list2.size(); i++) {
                list3[i] = list2.get(i);
            }
            idps.setTrustedIdentityProvider(list3);
        }
        return idps;
    }


    public List<UserCertificateRecord> findUserCertificateRecords(String callerIdentity, UserCertificateFilter f)
        throws DorianInternalFault, InvalidUserCertificateFault, PermissionDeniedFault {
        GridUser caller = getUser(callerIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        return this.userCertificateManager.findUserCertificateRecords(f);
    }


    public void updateUserCertificateRecord(String callerIdentity, UserCertificateUpdate update)
        throws DorianInternalFault, InvalidUserCertificateFault, PermissionDeniedFault {
        GridUser caller = getUser(callerIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        this.userCertificateManager.updateUserCertificateRecord(update);
    }


    public void removeUserCertificate(String callerIdentity, long serialNumber)
        throws DorianInternalFault, InvalidUserCertificateFault, PermissionDeniedFault {
        GridUser caller = getUser(callerIdentity);
        verifyActiveUser(caller);
        verifyAdminUser(caller);
        this.userCertificateManager.removeCertificate(serialNumber);
    }
}
