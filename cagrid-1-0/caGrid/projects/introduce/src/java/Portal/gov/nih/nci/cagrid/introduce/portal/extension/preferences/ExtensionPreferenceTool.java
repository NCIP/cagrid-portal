package gov.nih.nci.cagrid.introduce.portal.extension.preferences;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.lang.reflect.Constructor;

public class ExtensionPreferenceTool {
    

    public static ExtensionsPreferencesConfigurationPanel getExtensionsPreferencesConfigurationPanel(
        String extensionName) throws Exception {
        ExtensionDescription extensionDesc = ExtensionsLoader.getInstance().getExtension(extensionName);
        if ((extensionDesc != null) && (extensionDesc.getExtensionPreferencesPanel() != null)
            && !extensionDesc.getExtensionPreferencesPanel().equals("")) {
            Class c = Class.forName(extensionDesc.getExtensionPreferencesPanel());
            Constructor con = c.getConstructor(new Class[]{ExtensionDescription.class});
            Object obj = con.newInstance(new Object[]{extensionDesc});
            return (ExtensionsPreferencesConfigurationPanel) obj;
        }
        return null;
    }


}
