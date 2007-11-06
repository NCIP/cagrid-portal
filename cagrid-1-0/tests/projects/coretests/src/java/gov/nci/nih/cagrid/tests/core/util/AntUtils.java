package gov.nci.nih.cagrid.tests.core.util;

import gov.nih.nci.cagrid.introduce.common.AntTools;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;


/**
  *  AntUtils
  *  Utilities to fire off ant commands
  * 
  * @author David Ervin
  * 
  * @created Nov 6, 2007 12:52:56 PM
  * @version $Id: AntUtils.java,v 1.11 2007-11-06 21:27:30 dervin Exp $
 */
public class AntUtils {
    public static String getAntCommand() {
        // ant home
        String ant = System.getenv("ANT_HOME");
        if (ant == null) {
            throw new IllegalArgumentException("ANT_HOME not set");
        }

        // ant home/bin
        if (!ant.endsWith(File.separator)) {
            ant += File.separator;
        }
        ant += "bin" + File.separator;

        // ant home/bin/ant
        if (OSUtils.isWindows()) {
            ant += "ant.bat";
        } else {
            ant += "ant";
        }

        if (!new File(ant).exists()) {
            throw new IllegalArgumentException(ant + " does not exist");
        }
        return ant;
    }
    
    
    public static String getAntCommand(File baseDir, String target) throws Exception {
        return getAntCommand(baseDir, target, null);
    }
    
    
    public static String getAntCommand(File baseDir, String target, Properties systemProps) throws Exception {
        String command = AntTools.getAntCommand(target, baseDir.getAbsolutePath());
        // add system properties
        if (systemProps != null) {
            Enumeration keys = systemProps.keys();
            while (keys.hasMoreElements()) {
                String name = (String) keys.nextElement();
                String value = systemProps.getProperty(name);
                if (!OSUtils.isWindows()) {
                    value = value.replaceAll(" ", "\\\\ ");
                }
                String propPart = " \"-D" + name + "=" + value + "\"";
                command += propPart;
            }
        }
        
        return command;
    }
}
