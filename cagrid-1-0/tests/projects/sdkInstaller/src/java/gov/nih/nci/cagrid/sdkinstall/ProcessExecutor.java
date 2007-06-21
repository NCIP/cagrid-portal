package gov.nih.nci.cagrid.sdkinstall;

import gov.nih.nci.cagrid.common.StreamGobbler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
  *  ProcessExecutor
  *  Utility to execute and manage a process
  * 
  * @author David Ervin
  * 
  * @created Jun 20, 2007 10:55:05 AM
  * @version $Id: ProcessExecutor.java,v 1.2 2007-06-21 16:23:18 dervin Exp $
 */
public class ProcessExecutor {
    private String command;
    private File workingDir;
    private Process process;
    private ByteArrayOutputStream stdOutStream;
    private ByteArrayOutputStream stdErrStream;
    
    public ProcessExecutor(String cmd, File workingDir) {
        this.command = cmd;
        this.workingDir = workingDir;
        stdOutStream = new ByteArrayOutputStream();
        stdErrStream = new ByteArrayOutputStream();
    }
    
    
    public void exec() throws IOException {
        process = Runtime.getRuntime().exec(command, null, workingDir);
        new StreamGobbler(process.getInputStream(), 
            StreamGobbler.TYPE_OUT, stdOutStream).start();
        new StreamGobbler(process.getErrorStream(), 
            StreamGobbler.TYPE_ERR, stdErrStream).start();
    }
    
    
    public void waitForProcess() throws InterruptedException {
        process.waitFor();
    }
    
    
    public boolean processIsRunning() {
        if (process == null) {
            return false;
        }
        try {
            process.exitValue();
            return true;
        } catch (IllegalThreadStateException ex) {
            return false;
        }
    }
    
    
    public int getProcessExitValue() {
        return process.exitValue();
    }
    
    
    public String getStdOut() {
        synchronized (stdOutStream) {
            return stdOutStream.toString();
        }
    }
    
    
    public String getStdErr() {
        synchronized (stdErrStream) {
            return stdErrStream.toString();
        }
    }
}