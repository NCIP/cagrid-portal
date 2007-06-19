package gov.nih.nci.cagrid.sdkinstall;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/** 
 *  JBossTwiddler
 *  Pings JBoss's Twiddle utility to extract (hopefully) useful information
 * 
 * @author David Ervin
 * 
 * @created Jun 19, 2007 1:57:14 PM
 * @version $Id: JBossTwiddler.java,v 1.1 2007-06-19 19:58:05 dervin Exp $ 
 */
public class JBossTwiddler {
    public static final String CHECK_JBOSS_STARTED = "get \"jboss.system:type=Server\" Started";
    public static final String JBOSS_STARTED_VALUE = "Started=true";
    
    private File jbossDir;

    public JBossTwiddler(File jbossDir) {
        this.jbossDir = jbossDir;
    }
    
    
    public boolean isJBossRunning() throws Exception {
        String command = getTwiddlerBaseCommand();
        command += " " + CHECK_JBOSS_STARTED;
        final ProcessExecutor executor = new ProcessExecutor(command, 
            new File(jbossDir.getAbsolutePath() + File.separator + "bin"));
        Callable processCallable = new Callable() {
            public Boolean call() throws IOException, InterruptedException {
                executor.exec();
                executor.waitForProcess();
                String stdOut = executor.getStdOut().trim();
                return Boolean.valueOf(stdOut.equals(JBOSS_STARTED_VALUE));
            }
        };
        FutureTask<Boolean> processTask = new FutureTask(processCallable);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.submit(processTask);
        return processTask.get().booleanValue();
    }
    
    
    private String getTwiddlerBaseCommand() {
        File jbossBinDir = new File(jbossDir.getAbsolutePath() + File.separator + "bin");
        String baseCommand = jbossBinDir.getAbsolutePath() + File.separator + "twiddle";
        if (isWindows()) {
            return baseCommand + ".bat";
        }
        return baseCommand + ".sh";
    }
    
    
    private boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().indexOf("windows") != -1;
    }
    
    
    private static class ProcessExecutor {
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
            new StreamRedirector(process.getInputStream(), stdOutStream).start();
            new StreamRedirector(process.getErrorStream(), stdErrStream).start();
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
}
