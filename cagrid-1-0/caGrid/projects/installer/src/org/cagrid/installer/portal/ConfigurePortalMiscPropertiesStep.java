package org.cagrid.installer.portal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.steps.options.PasswordPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.pietschy.wizard.WizardModel;

import javax.swing.*;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ConfigurePortalMiscPropertiesStep  extends PropertyConfigurationStep {
    private static final Log logger = LogFactory
            .getLog(ConfigurePortalMiscPropertiesStep.class);

    public ConfigurePortalMiscPropertiesStep() {
    }

    public ConfigurePortalMiscPropertiesStep(String name, String description) {
        super(name, description);
    }

    public ConfigurePortalMiscPropertiesStep(String name, String description, Icon icon) {
        super(name, description, icon);
    }

    @Override
    public void init(WizardModel m) {
        this.model = (CaGridInstallerModel) m;

        addOptions();

        super.init(m);//To change body of overridden methods use File | Settings | File Templates.
    }

    protected void addOptions() {

        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORTAL_GOOGLE_MAP_KEY, model
                        .getMessage("portal.google.map.key"), model
                        .getProperty(Constants.PORTAL_GOOGLE_MAP_KEY,
                        ""), true));

        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORTAL_GEOCODER_APP_ID, model
                        .getMessage("portal.geocoder.app.id"), model
                        .getProperty(Constants.PORTAL_GEOCODER_APP_ID,
                        ""), true));

        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORTAL_SECURITY_ENCRYPTION_KEY, model
                        .getMessage("portal.security.encryption.key"), model
                        .getProperty(Constants.PORTAL_SECURITY_ENCRYPTION_KEY,
                        "yadda12345678901234567890"), false));

        getOptions().add(
                new FilePropertyConfigurationOption(
                        Constants.LIFERAY_KEYSTORE_PATH, model
                        .getMessage("liferay.keystore.path"), model
                        .getProperty(Constants.LIFERAY_KEYSTORE_PATH,
                        model.getProperty(Constants.PORTAL_INSTALL_DIR_PATH) + "/portal-liferay"), true));

        getOptions().add(
                new PasswordPropertyConfigurationOption(
                        Constants.LIFERAY_KEYSTORE_PASSWORD, model
                        .getMessage("liferay.keystore.password"), model
                        .getProperty(Constants.LIFERAY_KEYSTORE_PASSWORD,
                        "portal"), true));

        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORATL_HTTP_REDIRECT_PORT, model
                        .getMessage("portal.http.redirect.port"), model
                        .getProperty(Constants.PORATL_HTTP_REDIRECT_PORT,
                        "8080"), true));

        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORATL_HTTPS_REDIRECT_PORT, model
                        .getMessage("portal.http.redirect.port"), model
                        .getProperty(Constants.PORATL_HTTPS_REDIRECT_PORT,
                        "8443"), true));

        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORTAL_ADMIN_EMAIL, model
                        .getMessage("portal.admin.email"), model
                        .getProperty(Constants.PORTAL_ADMIN_EMAIL,
                        "ncicb@pop.nci.nih.gov"), true));



    }

    @Override
    public void prepare() {
        super.prepare();//To change body of overridden methods use File | Settings | File Templates.

        //need to do this otherwise null value is set when panel is initialized
        JTextField keystorePath = (JTextField)getOption(Constants.LIFERAY_KEYSTORE_PATH);
        keystorePath.setText(this.model.getProperty(Constants.PORTAL_INSTALL_DIR_PATH) + "/portal-liferay");

    }
}
