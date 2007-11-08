package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.common.StreamGobbler;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.jasciidammit.ConfigurationException;
import org.jasciidammit.JAsciiDammit;
import org.jdom.Element;
import org.jdom.filter.Filter;

/** 
 *  FixXmiExecutor
 *  Executes SDK 3.1 / 3.2 / 3.2.1's fix-xmi ant target
 * 
 * @author David Ervin
 * 
 * @created Oct 29, 2007 2:11:31 PM
 * @version $Id: FixXmiExecutor.java,v 1.5 2007-11-08 21:19:59 dervin Exp $ 
 */
public class FixXmiExecutor {
    public static final Logger LOG = Logger.getLogger(FixXmiExecutor.class);
    
    public static final String ERROR_APOSTROPHE = "’";
    
    // problematic elements in the original XMI
    public static final String DOCTYPE_UML_EA = "<!DOCTYPE XMI SYSTEM \"UML_EA.dtd\">";

    // ant tasks
    public static final String FIX_XMI_TASK = "fix-xmi";
    public static final String COMPILE_GENERATOR_TASK = "compile-generator";
    
    // properties for the ant tasks
    public static final String MODEL_DIR_PROPERTY = "dir.model";
    public static final String MODEL_FILENAME_PROPERTY = "model_filename";
    public static final String FIXED_MODEL_FILENAME_PROPERTY = "fixed_filename";
    public static final String PREPROCESSOR_PROPERTY = "xmi_preprocessor";
    
    // the EA xmi preprocessor class
    public static final String EA_XMI_PREPROCESSOR = 
        "gov.nih.nci.codegen.core.util.EAXMIPreprocessor";
    
    private FixXmiExecutor() {
        // prevent instantiation
    }
    
    
    /**
     * Runs the SDK's fix-xmi target against the specified model
     * @param originalModel
     *      The file containing the original XMI model from EA
     * @param sdkDir
     *      The caCORE SDK base directory
     * @return
     *      The file containing the 'fixed' model
     */
    public static File fixEaXmiModel(File originalModel, File sdkDir) throws IOException,
        InterruptedException {
        File cleanModelFile = cleanXmi(originalModel);
        StringBuilder command = new StringBuilder();
        // get the base ant command
        command.append(getAntCall(sdkDir.getAbsolutePath())).append(" ");
        // add properties and their values
        command.append("-D").append(MODEL_DIR_PROPERTY)
            .append("=").append(cleanModelFile.getAbsoluteFile().getParent()).append(" ");
        command.append("-D").append(MODEL_FILENAME_PROPERTY)
            .append("=").append(cleanModelFile.getName()).append(" ");
        command.append("-D").append(FIXED_MODEL_FILENAME_PROPERTY)
            .append("=").append("fixed_").append(originalModel.getName()).append(" ");
        command.append("-D").append(PREPROCESSOR_PROPERTY)
            .append("=").append(EA_XMI_PREPROCESSOR);
        // execute the command
        System.out.println("Executing " + command.toString());
        Process proc = Runtime.getRuntime().exec(command.toString());
        /* streams to LOG
        new StreamGobbler(proc.getInputStream(), StreamGobbler.TYPE_OUT,
            LOG, Priority.DEBUG).start();
        new StreamGobbler(proc.getErrorStream(), StreamGobbler.TYPE_ERR,
            LOG, Priority.DEBUG).start();
        */
        // Streams to out
        new StreamGobbler(proc.getInputStream(), StreamGobbler.TYPE_OUT, System.out).start();
        new StreamGobbler(proc.getErrorStream(), StreamGobbler.TYPE_ERR, System.err).start();
        System.out.println("Waiting");
        proc.waitFor();
        if (proc.exitValue() == 0) {
            return new File(originalModel.getParent() + File.separator + "fixed_" + originalModel.getName());
        } else {
            throw new RuntimeException("Error executing fix-xmi command:\n" + command.toString());
        }
    }
    
    
    private static String getAntCall(String buildFileDir) {
        String os = System.getProperty("os.name").toLowerCase();
        StringBuilder cmd = new StringBuilder();
        if (os.indexOf("windows") >= 0) {
            cmd.append("java.exe ");
            cmd.append("-classpath \"").append(getAntLauncherJarLocation(System.getProperty("java.class.path")));
            cmd.append("\" org.apache.tools.ant.launch.Launcher -buildfile \"").append(buildFileDir);
            cmd.append(File.separator).append("build.xml\"");
        } else {
            // escape out the spaces.....
            buildFileDir = buildFileDir.replaceAll("\\s", "\\ ");
            cmd.append("java ");
            cmd.append("-classpath ").append(getAntLauncherJarLocation(System.getProperty("java.class.path")));
            cmd.append(" org.apache.tools.ant.launch.Launcher -buildfile ").append(buildFileDir);
            cmd.append(File.separator).append("build.xml");
        }
        // add targets
        cmd.append(" ")/*.append(COMPILE_GENERATOR_TASK).append(" ")*/.append(FIX_XMI_TASK);
        return cmd.toString();
    }
    
    
    private static String getAntLauncherJarLocation(String path) {
        StringTokenizer pathTokenizer = new StringTokenizer(path, File.pathSeparator);
        while (pathTokenizer.hasMoreTokens()) {
            String pathElement = pathTokenizer.nextToken();
            if ((pathElement.indexOf("ant-launcher") != -1) && pathElement.endsWith(".jar")) {
                return pathElement;
            }
        }
        return null;
    }
    
    
    private static File cleanXmi(File originalXmi) throws IOException {
        System.out.println("Clean XMI");
        File cleanedFile = new File(originalXmi.getParentFile(), "cleaned_" + originalXmi.getName());
        StringBuffer xmiContents = Utils.fileToStringBuffer(originalXmi);
        cleanSmartquotes(xmiContents);
        cleanDoctype(xmiContents);
        cleanTaggedValues(xmiContents);
        Utils.stringBufferToFile(xmiContents, cleanedFile.getAbsolutePath());
        return cleanedFile;
    }
    
    
    private static void cleanSmartquotes(StringBuffer xmiContents) throws IOException {
        String raw = xmiContents.toString();
        raw = raw.replace(ERROR_APOSTROPHE, "'");
        try {
            raw = new JAsciiDammit().translate(raw);
            xmiContents.delete(0, xmiContents.length());
            xmiContents.append(raw);
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
            IOException ioe = new IOException(ex.getMessage());
            ioe.initCause(ex);
            throw ioe;
        }
    }
    
    
    private static void cleanDoctype(StringBuffer xmiContents) {
        int start = xmiContents.indexOf(DOCTYPE_UML_EA); 
        if (start != -1) {
            System.out.println("OFFENDING DOCTYPE ELEMENT FOUND");
            xmiContents.delete(start, start + DOCTYPE_UML_EA.length());
        }
    }
    
    
    private static void cleanTaggedValues(StringBuffer xmiContents) throws IOException {
        // UML namespace == xmlns:UML="omg.org/UML1.3"
        Element xmiElement = null;
        try {
            xmiElement = XMLUtilities.stringToDocument(xmiContents.toString()).getRootElement();
        } catch (Exception ex) {
            IOException ioe = new IOException(ex.getMessage());
            ioe.initCause(ex);
            throw ioe;
        }
        // iterate everything, looking for <UML:TaggedValue ../>
        Filter taggedValueFilter = new Filter() {
            public boolean matches(Object obj) {
                if (obj instanceof Element) {
                    Element elem = (Element) obj;
                    if (elem.getName().equals("TaggedValue")) {
                        return elem.getAttribute("value") == null;
                    }
                }
                return false;
            }
        };
        Iterator<Element> badTaggedValues = xmiElement.getDescendants(taggedValueFilter);
        List<Element> removeElements = new LinkedList<Element>();
        while (badTaggedValues.hasNext()) {
            removeElements.add(badTaggedValues.next());
        }
        for (Element removeMe : removeElements) {
            removeMe.detach();
        }
        
        System.out.println("Removed " + removeElements.size() + " TaggedValues with no 'value' attribute");
        String cleanXmi = null;
        try {
            cleanXmi = XMLUtilities.formatXML(XMLUtilities.elementToString(xmiElement));
        } catch (Exception ex) {
            IOException ioe = new IOException(ex.getMessage());
            ioe.initCause(ex);
            throw ioe;
        }
        xmiContents.delete(0, xmiContents.length());
        xmiContents.append(cleanXmi);
    }
}
