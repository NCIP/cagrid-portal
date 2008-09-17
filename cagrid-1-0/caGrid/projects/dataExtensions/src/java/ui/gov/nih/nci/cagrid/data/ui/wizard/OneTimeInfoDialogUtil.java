package gov.nih.nci.cagrid.data.ui.wizard;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.swing.JFrame;

import org.cagrid.grape.utils.CompositeErrorDialog;



public class OneTimeInfoDialogUtil {
    
    public static final String DIRECTORY_NAME = "DataServiceWizards";
    public static final String PROPERTIES_FILE_NAME = "oneTimeDialogs.properties";
    
    private static Set<String> shownMessages = null;
    
    /**
     * Shows the info dialog iff the user has not seen it during the current
     * run of the JVM and has not previously checked the box to 
     * "Do Not Show This Message Again"
     * 
     * @param wizardPanelClass
     *      The wizard panel class which is requesting this dialog
     * @param messageId
     *      Some consistent internal intentifier for the message
     * @param message
     *      The message text.  May be multiple lines, separated into
     *      an array of String
     */
    public static void showInfoDialog(
        Class wizardPanelClass, String messageId, String[] message) {
        showInfoDialog(null, wizardPanelClass, messageId, message);
    }
    
    
    public static void showInfoDialog(JFrame parent,
        Class wizardPanelClass, String messageId, String[] message) {
        String internalId = generateMessageId(wizardPanelClass, messageId);
        if (!messageHasBeenShown(internalId) && !shouldNeverShowMessage(internalId)) {
            boolean neverShowAgain = OneTimeInfoDialog.showDialog(parent, message);
            shownMessages.add(internalId);
            storeNeverShowValue(internalId, neverShowAgain);
        }
    }
    
    
    private synchronized static Properties loadDialogProperties() {
        Properties props = new Properties();
        File propertiesFile = getPropertiesFile();
        FileInputStream fis = null;
        try {
            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
            }
            fis = new FileInputStream(propertiesFile);
            props.load(fis);
        } catch (Exception ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog("Error displaying message", ex.getMessage(), ex);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                    // oh well...
                }
            }
        }
        return props;
    }
    
    
    private static File getPropertiesFile() {
        File dataDirectory = new File(Utils.getCaGridUserHome(), DIRECTORY_NAME);
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        File propertiesFile = new File(dataDirectory, PROPERTIES_FILE_NAME);
        if (!propertiesFile.exists()) {
            try {
                propertiesFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                CompositeErrorDialog.showErrorDialog(
                    "Error creating new properties file", ex.getMessage(), ex);
            }
        }
        return propertiesFile;
    }
    
    
    private static String generateMessageId(Class originator, String internalId) {
        return originator.getName() + "::" + internalId;
    }
    
    
    private static boolean messageHasBeenShown(String messageId) {
        if (shownMessages == null) {
            shownMessages = new HashSet<String>();
        }
        return shownMessages.contains(messageId);
    }
    
    
    private static boolean shouldNeverShowMessage(String messageId) {
        Properties props = loadDialogProperties();
        String value = props.getProperty(messageId, Boolean.FALSE.toString());
        return Boolean.parseBoolean(value);
    }
    
    
    private static void storeNeverShowValue(String messageId, boolean value) {
        File propertiesFile = getPropertiesFile();
        Properties props = loadDialogProperties();
        props.setProperty(messageId, String.valueOf(value));
        FileOutputStream propsOutput = null;
        try {
            propsOutput = new FileOutputStream(propertiesFile);
            String message = "Messages shown once and never again";
            props.store(propsOutput, message);
        } catch (IOException ex) {
            ex.printStackTrace();
            CompositeErrorDialog.showErrorDialog(
                "Error storing properties", ex.getMessage(), ex);
        } finally {
            if (propsOutput != null) {
                try {
                    propsOutput.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // we tried...
                }
            }
        }
    }
    
    
    private OneTimeInfoDialogUtil() {
        // prevents instantiation
    }
}
