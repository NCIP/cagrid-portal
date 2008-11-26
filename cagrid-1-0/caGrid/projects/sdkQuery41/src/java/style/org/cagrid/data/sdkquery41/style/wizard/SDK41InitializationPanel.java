package org.cagrid.data.sdkquery41.style.wizard;

import java.io.File;

import org.cagrid.data.sdkquery41.processor.SDK41QueryProcessor;
import org.cagrid.data.sdkquery41.style.wizard.config.SDK41InitialConfigurationStep;
import org.cagrid.grape.utils.CompositeErrorDialog;

import gov.nih.nci.cagrid.data.style.sdkstyle.wizard.CoreDsIntroPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;


/**
 * SDK41InitializationPanel First wizard panel to create a caGrid data service
 * backed by caCORE SDK 4.1
 * 
 * @author David
 */
public class SDK41InitializationPanel extends CoreDsIntroPanel {

    public static final String QUERY_PROCESSOR_CLASSNAME = SDK41QueryProcessor.class.getName();

    private SDK41InitialConfigurationStep configuration = null;


    public SDK41InitializationPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
        configuration = new SDK41InitialConfigurationStep(info);
    }


    protected void setLibrariesAndProcessor() {
        // set the query processor and style lib dir on the configuration
        this.configuration.setQueryProcessorClassName(QUERY_PROCESSOR_CLASSNAME);
        File styleLibDir = new File(ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath()
            + File.separator + "data" + File.separator + "styles" + File.separator + "cacore4" + File.separator + "lib");
        this.configuration.setStyleLibDirectory(styleLibDir);
    }


    public void movingNext() {
        try {
            configuration.applyConfiguration();
        } catch (Exception ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error applying configuration", ex.getMessage(), ex);
        }
    }
}
