package org.cagrid.gaards.dorian.service.upgrader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.cagrid.gaards.dorian.ca.DBCertificateAuthority;
import org.cagrid.gaards.dorian.ca.EracomCertificateAuthority;
import org.cagrid.gaards.dorian.ca.EracomWrappingCertificateAuthority;
import org.cagrid.gaards.dorian.federation.TrustedIdPManager;
import org.cagrid.gaards.dorian.idp.PasswordSecurityManager;
import org.cagrid.gaards.dorian.service.PropertyManager;
import org.cagrid.tools.database.Database;


public class Upgrade1_2To1_3 extends Upgrade {

    public String getStartingVersion() {
        return PropertyManager.DORIAN_VERSION_1_2;
    }


    public String getUpgradedVersion() {
        return PropertyManager.DORIAN_VERSION_1_3;
    }


    private void upgradeTrustedIdentityProviders(boolean trialRun) throws Exception {
        Database db = getBeanUtils().getDatabase();
        TrustedIdPManager idp = getBeanUtils().getTrustedIdPManager();
        if (!trialRun) {
            idp.buildDatabase();
        }

        boolean hasDisplayName = false;
        boolean hasAuthenticationServiceURL = false;
        boolean hasAuthenticationServiceIdentity = false;

        Connection c = null;
        try {
            c = db.getConnection();
            PreparedStatement s = c.prepareStatement("show COLUMNS FROM " + TrustedIdPManager.TRUST_MANAGER_TABLE);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equals(TrustedIdPManager.DISPLAY_NAME_FIELD)) {
                    hasDisplayName = true;
                }
                if (rs.getString(1).equals(TrustedIdPManager.AUTHENTICATION_SERVICE_URL_FIELD)) {
                    hasAuthenticationServiceURL = true;
                }
                if (rs.getString(1).equals(TrustedIdPManager.AUTHENTICATION_SERVICE_IDENTITY_FIELD)) {
                    hasAuthenticationServiceIdentity = true;
                }

            }
            rs.close();
            s.close();

            if (!hasDisplayName) {
                if (!trialRun) {
                    s = c.prepareStatement("ALTER TABLE " + TrustedIdPManager.TRUST_MANAGER_TABLE + " ADD "
                        + TrustedIdPManager.DISPLAY_NAME_FIELD + " TEXT NOT NULL");
                    s.execute();
                    s.close();
                }
            }

            if (!hasAuthenticationServiceURL) {
                if (!trialRun) {
                    s = c.prepareStatement("ALTER TABLE " + TrustedIdPManager.TRUST_MANAGER_TABLE + " ADD "
                        + TrustedIdPManager.AUTHENTICATION_SERVICE_URL_FIELD + " TEXT NOT NULL");
                    s.execute();
                    s.close();
                }
            }

            if (!hasAuthenticationServiceIdentity) {
                if (!trialRun) {
                    s = c.prepareStatement("ALTER TABLE " + TrustedIdPManager.TRUST_MANAGER_TABLE + " ADD "
                        + TrustedIdPManager.AUTHENTICATION_SERVICE_IDENTITY_FIELD + " TEXT NOT NULL");
                    s.execute();
                    s.close();
                }
            }

            if (!trialRun) {
                s = c.prepareStatement("ALTER TABLE " + TrustedIdPManager.TRUST_MANAGER_TABLE + " MODIFY "
                    + TrustedIdPManager.IDP_SUBJECT_FIELD + " TEXT NOT NULL");
                s.execute();
                s.close();
            }
            
            if (!trialRun) {
                s = c.prepareStatement("ALTER TABLE " + TrustedIdPManager.POLICY_CLASS_FIELD + " MODIFY "
                    + TrustedIdPManager.POLICY_CLASS_FIELD + " TEXT NOT NULL");
                s.execute();
                s.close();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            db.releaseConnection(c);
        }
    }


    private void upgradePasswordSecurity(boolean trialRun) throws Exception {
        Database db = getBeanUtils().getDatabase();
        PasswordSecurityManager psm = new PasswordSecurityManager(db, getBeanUtils().getIdentityProviderProperties()
            .getPasswordSecurityPolicy());
        if (!trialRun) {
            psm.buildDatabase();
        }

        boolean hasPasswordSalt = false;
        boolean hasEncryptionAlgorithm = false;

        Connection c = null;
        try {
            c = db.getConnection();
            PreparedStatement s = c.prepareStatement("show COLUMNS FROM " + PasswordSecurityManager.TABLE);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equals(PasswordSecurityManager.DIGEST_SALT)) {
                    hasPasswordSalt = true;
                }
                if (rs.getString(1).equals(PasswordSecurityManager.DIGEST_ALGORITHM)) {
                    hasEncryptionAlgorithm = true;
                }

            }
            rs.close();
            s.close();

            if (!hasPasswordSalt) {
                if (!trialRun) {
                    s = c.prepareStatement("ALTER TABLE " + PasswordSecurityManager.TABLE + " ADD "
                        + PasswordSecurityManager.DIGEST_SALT + " VARCHAR(255)");
                    s.execute();
                    s.close();
                }
            }

            if (!hasEncryptionAlgorithm) {
                if (!trialRun) {
                    s = c.prepareStatement("ALTER TABLE " + PasswordSecurityManager.TABLE + " ADD "
                        + PasswordSecurityManager.DIGEST_ALGORITHM + " VARCHAR(25)");
                    s.execute();
                    s.close();
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            db.releaseConnection(c);
        }
    }


    private void upgradeCertificateAuthority(PropertyManager pm, boolean trialRun) throws Exception {
        String caType = pm.getCertificateAuthorityType();
        String newCAType = null;
        if (caType.equals("DBCA")) {
            newCAType = DBCertificateAuthority.class.getName();
        } else if (caType.equals("Eracom")) {
            newCAType = EracomWrappingCertificateAuthority.class.getName();
        } else if (caType.equals("EracomHybrid")) {
            newCAType = EracomCertificateAuthority.class.getName();
        }
        if (newCAType != null) {
            if (!trialRun) {
                pm.setCertificateAuthorityType(newCAType);
            }
        } else {
            throw new Exception("Could not determine how to upgrade the Certificate Authority Type " + caType + ".");
        }

    }


    public void upgrade(boolean trialRun) throws Exception {
        Database db = getBeanUtils().getDatabase();
        db.createDatabaseIfNeeded();
        PropertyManager pm = new PropertyManager(db);
        if (pm.getVersion().equals(PropertyManager.DORIAN_VERSION_1_2)) {
            if (!trialRun) {
                pm.setVersion(PropertyManager.DORIAN_VERSION_1_3);
            }
            upgradeCertificateAuthority(pm, trialRun);
            upgradePasswordSecurity(trialRun);
            upgradeTrustedIdentityProviders(trialRun);
        } else {
            if (!trialRun) {
                throw new Exception("Failed to run upgrader " + getClass().getName()
                    + " the version of Dorian you are running is not " + PropertyManager.DORIAN_VERSION_1_2 + ".");
            }
        }

    }
}
