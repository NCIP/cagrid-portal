package gov.nci.nih.cagrid.tests.core.util;

import gov.nih.nci.cagrid.introduce.common.AntTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;


/**
  *  AntUtils
  *  Utilities to fire off ant commands
  * 
  * @author David Ervin
  * 
  * @created Nov 6, 2007 12:52:56 PM
  * @version $Id: AntUtils.java,v 1.10 2007-11-06 21:06:24 dervin Exp $
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


    /**
     * @deprecated This code is a MESS! use getAntCommand();
     * @param dir
     * @param buildFile
     * @param target
     * @param sysProps
     * @param envp
     * @throws IOException
     * @throws InterruptedException
     */
    public static void runAnt(File dir, String buildFile, String target, Properties sysProps, String[] envp)
        throws IOException, InterruptedException {
        // build command
        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add(getAntCommand());

        // add system properties
        if (sysProps != null) {
            Enumeration keys = sysProps.keys();
            while (keys.hasMoreElements()) {
                String name = (String) keys.nextElement();
                String value = sysProps.getProperty(name);
                if (!OSUtils.isWindows()) {
                    value = value.replaceAll(" ", "\\\\ ");
                }
                cmd.add("\"-D" + name + "=" + value + "\"");
            }
        }

        // add build file
        if (buildFile != null) {
            cmd.add("-f");
            cmd.add(buildFile);
        }

        // add target
        if (target != null) {
            cmd.add(target);
        }

        // run ant
        Process p = Runtime.getRuntime().exec(cmd.toArray(new String[0]), envp, dir);
        // track stdout and stderr
        StringBuffer stdout = new StringBuffer();
        StringBuffer stderr = new StringBuffer();
        new IOThread(p.getInputStream(), System.out, stdout).start();
        new IOThread(p.getErrorStream(), System.err, stderr).start();

        // wait and return
        int result = p.waitFor();
        if (stdout.indexOf("BUILD FAILED") != -1 || stderr.indexOf("BUILD FAILED") != -1
            || stdout.indexOf("Build failed") != -1 || stderr.indexOf("Build failed") != -1) {
            System.err.println(stderr);
            System.out.println(stdout);
            throw new IOException("ant command '" + target + "' failed");
        }
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
