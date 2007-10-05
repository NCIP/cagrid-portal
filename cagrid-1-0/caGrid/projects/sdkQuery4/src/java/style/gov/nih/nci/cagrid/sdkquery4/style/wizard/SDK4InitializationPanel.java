package gov.nih.nci.cagrid.sdkquery4.style.wizard;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.style.sdkstyle.wizard.CoreDsIntroPanel;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** 
 *  SDK4InitializationPanel
 *  Panel to initialize the SDK 4 wizard
 * 
 * @author David Ervin
 * 
 * @created Oct 5, 2007 2:08:34 PM
 * @version $Id: SDK4InitializationPanel.java,v 1.2 2007-10-05 19:41:22 dervin Exp $ 
 */
public class SDK4InitializationPanel extends CoreDsIntroPanel {
    
    public static final String SDK4_QUERY_PROCESSOR_CLASSNAME = SDK4QueryProcessor.class.getName();

    public SDK4InitializationPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
        super(extensionDescription, info);
    }


    protected void setLibrariesAndProcessor() {
        // shouldn't have to explicitly copy any libraries into the service to support
        // the SDK 4 query processor, since style libraries get copied into the
        // service's lib directory at creation time.
        
        // add the libraries to the service's additional libs list
        File styleLibDir = new File(ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath()
            + File.separator + "data" + File.separator + "styles" + File.separator + "cacore4" + File.separator + "lib");
        try {
            Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
            AdditionalLibraries libs = data.getAdditionalLibraries();
            if (libs == null) {
                libs = new AdditionalLibraries();
                data.setAdditionalLibraries(libs);
            }
            Set jarNames = new HashSet();
            if (libs.getJarName() != null) {
                Collections.addAll(jarNames, (Object[]) libs.getJarName());
            }
            // list the libraries
            File[] jars = styleLibDir.listFiles(new FileFilters.JarFileFilter());
            for (File jar : jars) {
                jarNames.add(jar.getName());
            }
            String[] names = new String[jarNames.size()];
            jarNames.toArray(names);
            libs.setJarName(names);
            // store the modified list
            ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error adding the library to the service information", ex);
            return;
        }
        // add the query processor class name as a service property
        CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
            DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, SDK4_QUERY_PROCESSOR_CLASSNAME, false);
        /* fix this for sdk 4 serializer
        // set the serializer and deserializer properties
        getBitBucket().put(SchemaTypesPanel.TYPE_SERIALIZER_CLASS_PROPERTY, 
            SDK32SerializerFactory.class.getName());
        getBitBucket().put(SchemaTypesPanel.TYPE_DESERIALIZER_CLASS_PROPERTY,
            SDK32DeserializerFactory.class.getName());
        */
    }
}
