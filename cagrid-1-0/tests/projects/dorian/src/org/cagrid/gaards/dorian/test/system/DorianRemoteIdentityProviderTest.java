package org.cagrid.gaards.dorian.test.system;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.steps.CopyServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeleteServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.story.ServiceStoryBase;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.common.AuthenticationProfile;
import org.cagrid.gaards.authentication.faults.InvalidCredentialFault;
import org.cagrid.gaards.authentication.test.AuthenticationProperties;
import org.cagrid.gaards.authentication.test.system.steps.AuthenticationStep;
import org.cagrid.gaards.authentication.test.system.steps.InvalidAuthentication;
import org.cagrid.gaards.authentication.test.system.steps.SuccessfullAuthentication;
import org.cagrid.gaards.authentication.test.system.steps.ValidateSupportedAuthenticationProfilesStep;
import org.cagrid.gaards.dorian.federation.AutoApprovalAutoRenewalPolicy;
import org.cagrid.gaards.dorian.federation.GridUserStatus;
import org.cagrid.gaards.dorian.federation.TrustedIdPStatus;
import org.cagrid.gaards.dorian.idp.Application;
import org.cagrid.gaards.dorian.idp.CountryCode;
import org.cagrid.gaards.dorian.idp.IdPUserRole;
import org.cagrid.gaards.dorian.idp.IdPUserStatus;
import org.cagrid.gaards.dorian.idp.StateCode;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.dorian.test.system.steps.ChangeLocalUserPasswordStep;
import org.cagrid.gaards.dorian.test.system.steps.CleanupDorianStep;
import org.cagrid.gaards.dorian.test.system.steps.ConfigureGlobusToTrustDorianStep;
import org.cagrid.gaards.dorian.test.system.steps.CopyConfigurationStep;
import org.cagrid.gaards.dorian.test.system.steps.FindGridUserStep;
import org.cagrid.gaards.dorian.test.system.steps.FindLocalUserStep;
import org.cagrid.gaards.dorian.test.system.steps.GetAsserionSigningCertificateStep;
import org.cagrid.gaards.dorian.test.system.steps.InvalidGridCredentialRequest;
import org.cagrid.gaards.dorian.test.system.steps.RegisterUserWithDorianIdentityProviderStep;
import org.cagrid.gaards.dorian.test.system.steps.GridCredentialRequestStep;
import org.cagrid.gaards.dorian.test.system.steps.SuccessfullGridCredentialRequest;
import org.cagrid.gaards.dorian.test.system.steps.UpdateGridUserStatusStep;
import org.cagrid.gaards.dorian.test.system.steps.UpdateLocalUserStatusStep;
import org.cagrid.gaards.dorian.test.system.steps.VerifyTrustedIdPStep;


public class DorianRemoteIdentityProviderTest extends ServiceStoryBase {

    private File dorianConfiguration;
    private File dorianProperties;
    private File authenticationServiceConfiguration;
    private AuthenticationProperties authenticationProperties;
    private File authenticationTempService;
    private File dorianTempService;
    private ConfigureGlobusToTrustDorianStep trust;


    public DorianRemoteIdentityProviderTest(ServiceContainer container, File dorianConfiguration,
        File dorianProperties, File authenticationServiceConfiguration) {
        super(container);
        this.dorianConfiguration = dorianConfiguration;
        this.dorianProperties = dorianProperties;
        this.authenticationServiceConfiguration = authenticationServiceConfiguration;
    }


    @Override
    public String getName() {
        return "Dorian Remote Identity Provider System Test";
    }


    public String getDescription() {
        return "Dorian Remote Identity Provider System Test";
    }


    protected Vector<Step> steps() {
        Vector<Step> steps = new Vector<Step>();
        try {
            steps.add(new UnpackContainerStep(getContainer()));
            steps.add(new CopyConfigurationStep(dorianTempService, this.dorianConfiguration, this.dorianProperties));
            steps.add(new org.cagrid.gaards.authentication.test.system.steps.CopyConfigurationStep(
                authenticationTempService, this.authenticationServiceConfiguration, this.authenticationProperties
                    .getPropertiesFile()));
            steps.add(new DeployServiceStep(getContainer(), this.dorianTempService.getAbsolutePath()));
            steps.add(new DeployServiceStep(getContainer(), this.authenticationTempService.getAbsolutePath()));
            trust = new ConfigureGlobusToTrustDorianStep(getContainer());
            steps.add(trust);

            steps.add(new StartContainerStep(getContainer()));

            GetAsserionSigningCertificateStep signingCertStep = new GetAsserionSigningCertificateStep(getContainer());
            steps.add(signingCertStep);

            String dorianURL = getContainer().getContainerBaseURI().toString() + "cagrid/Dorian";
            String asURL = getContainer().getContainerBaseURI().toString() + "cagrid/AuthenticationService";
            
            
            //Test Get supported authentication types
            Set<QName> dorianExpectedProfiles = new HashSet<QName>();
            dorianExpectedProfiles.add(AuthenticationProfile.BASIC_AUTHENTICATION);
            steps.add(new ValidateSupportedAuthenticationProfilesStep(dorianURL, dorianExpectedProfiles));

            SuccessfullAuthentication dorianSuccess = new SuccessfullAuthentication("dorian", "Mr.", "Administrator",
                "dorian@dorian.org", signingCertStep);

            // Test Successful authentication
            BasicAuthentication cred = new BasicAuthentication();
            cred.setUserId("dorian");
            cred.setPassword("DorianAdmin$1");
            AuthenticationStep adminAuth = new AuthenticationStep(dorianURL, dorianSuccess, cred);
            steps.add(adminAuth);

            // Get Admin's Grid Credentials

            GridCredentialRequestStep admin = new GridCredentialRequestStep(dorianURL, adminAuth,
                new SuccessfullGridCredentialRequest());
            steps.add(admin);

            Set<QName> asExpectedProfiles = new HashSet<QName>();
            asExpectedProfiles.add(AuthenticationProfile.BASIC_AUTHENTICATION);
            steps.add(new ValidateSupportedAuthenticationProfilesStep(
                    asURL, asExpectedProfiles));

            SuccessfullAuthentication success = new SuccessfullAuthentication(
                    "jdoe", "John", "Doe", "jdoe@doe.com", authenticationProperties
                            .getSigningCertificate());

            // Test Successful authentication
            BasicAuthentication asUser = new BasicAuthentication();
            asUser.setUserId("jdoe");
            asUser.setPassword("password");
            steps.add(new AuthenticationStep(asURL, success, asUser));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return steps;
    }


    protected boolean storySetUp() throws Throwable {
        this.authenticationProperties = new AuthenticationProperties();
        this.dorianTempService = new File("tmp/dorian");
        this.authenticationTempService = new File("tmp/authentication-service");
        File dorianLocation = new File("../../../caGrid/projects/dorian");
        CopyServiceStep copyService = new CopyServiceStep(dorianLocation, dorianTempService);
        copyService.runStep();

        File asLocation = new File("../../../caGrid/projects/authentication-service");
        CopyServiceStep copyService2 = new CopyServiceStep(asLocation, authenticationTempService);
        copyService2.runStep();
        return true;
    }


    protected void storyTearDown() throws Throwable {

        try {
            this.authenticationProperties.cleanup();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            if (this.authenticationTempService != null) {
                new DeleteServiceStep(authenticationTempService).runStep();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            if (this.dorianTempService != null) {
                new DeleteServiceStep(dorianTempService).runStep();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        StopContainerStep step2 = new StopContainerStep(getContainer());
        try {
            step2.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        CleanupDorianStep cleanup = new CleanupDorianStep(getContainer(), trust);
        try {
            cleanup.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        DestroyContainerStep step3 = new DestroyContainerStep(getContainer());
        try {
            step3.runStep();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
