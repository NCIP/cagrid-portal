package gov.nci.nih.cagrid.tests.core.util;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.axis.gsi.GSIConstants;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.oasis.wsrf.lifetime.Destroy;

import com.counter.CounterPortType;
import com.counter.CreateCounter;
import com.counter.CreateCounterResponse;
import com.counter.service.CounterServiceAddressingLocator;


public class GlobusHelper {

    private boolean secure;
    private File securityDescriptor;
    private Integer port;

    private File tmpDir;
    private File tmpGlobusLocation;
    private Process globusProcess;
    private Throwable isGlobusRunningException;


    public GlobusHelper() {
        this(false, null);
    }


    public GlobusHelper(File tmpDir) {
        this(false, tmpDir);
    }


    public GlobusHelper(boolean secure) {
        this(secure, null);
    }


    public GlobusHelper(boolean secure, File tmpDir) {
        super();
        this.secure = secure;
        this.tmpDir = tmpDir;
    }


    public synchronized void createTempGlobus() throws IOException {
        // get globus location
        String globusLocation = System.getenv("GLOBUS_LOCATION");
        if (globusLocation == null || globusLocation.equals("")) {
            throw new IllegalArgumentException("GLOBUS_LOCATION not set");
        }

        // create tmp globus location
        this.tmpGlobusLocation = FileUtils.createTempDir("Globus", "dir", this.tmpDir);

        // copy globus to tmp location
        FileUtils.copyRecursive(new File(globusLocation), this.tmpGlobusLocation, null);
    }


    public synchronized void deployService(File serviceDir) throws IOException, InterruptedException {
        deployService(serviceDir, "deployGlobus");
    }


    public synchronized void deployService(File serviceDir, String target) throws IOException, InterruptedException {
        String antHome = System.getenv("ANT_HOME");
        if (antHome == null || antHome.equals("")) {
            throw new IllegalArgumentException("ANT_HOME not set");
        }
        File ant = new File(antHome, "bin" + File.separator + "ant");

        String[] cmd = new String[]{ant.toString(), "-Dservice.deployment.host=\"localhost\"", "deployGlobus"};
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            cmd = new String[]{"cmd", "/c", ant + ".bat", "-Dservice.deployment.host=\"localhost\"", target,};
        }
        String[] envp = new String[]{"GLOBUS_LOCATION=" + this.tmpGlobusLocation.toString(),};
        envp = EnvUtils.overrideEnv(envp);

        Process p = Runtime.getRuntime().exec(cmd, envp, serviceDir);
        new StdIOThread(p.getInputStream()).start();
        new StdIOThread(p.getErrorStream()).start();
        p.waitFor();

        if (p.exitValue() != 0) {
            throw new IOException("deployService ant command failed: " + p.exitValue());
        }
    }


    public synchronized void startGlobus() throws IOException {
        this.globusProcess = runGlobusCommand("org.globus.wsrf.container.ServiceContainer");

        // make sure it is running
        this.isGlobusRunningException = null;
        sleep(2000);
        for (int i = 0; i < 10; i++) {
            if (isGlobusRunning()) {
                return;
            }
            sleep(1000);
        }
        this.isGlobusRunningException.printStackTrace();
        throw new IOException("could not start Globus");
    }


    private synchronized Process runGlobusCommand(String clName) throws IOException {
        // create globus startup params
        // %_RUNJAVA% -Dlog4j.configuration=container-log4j.properties
        // %LOCAL_OPTS% %GLOBUS_OPTIONS% -classpath %LOCALCLASSPATH%
        // org.globus.bootstrap.Bootstrap
        // org.globus.wsrf.container.ServiceContainer %CMD_LINE_ARGS%
        // C:\Globus4.0.1\bin>"C:\jdk1.5.0_03\bin\java"
        // -Dlog4j.configuration=container-log
        // 4j.properties -DGLOBUS_LOCATION="C:\Globus4.0.1"
        // -Djava.endorsed.dirs="C:\Globus
        // 4.0.1\endorsed" -classpath
        // "C:\Globus4.0.1\lib\bootstrap.jar";"C:\Globus4.0.1\l
        // ib\cog-url.jar";"C:\Globus4.0.1\lib\axis-url.jar"
        // org.globus.bootstrap.Bootstrap
        // org.globus.wsrf.container.ServiceContainer -nosec -debug
        File java = new File(System.getProperty("java.home"), "bin" + File.separator + "java");
        File lib = new File(this.tmpGlobusLocation, "lib");
        String classpath = lib + File.separator + "bootstrap.jar";
        classpath += File.pathSeparator + lib + File.separator + "cog-url.jar";
        classpath += File.pathSeparator + lib + File.separator + "axis-url.jar";

        // build command
        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add(java.toString());
        cmd.add("-Dlog4j.configuration=container-log4j.properties");
        cmd.add("-DGLOBUS_LOCATION=" + this.tmpGlobusLocation);
        cmd.add("-Djava.endorsed.dirs=" + this.tmpGlobusLocation + File.separator + "endorsed");
        cmd.add("-classpath");
        cmd.add(classpath);
        cmd.add("org.globus.bootstrap.Bootstrap");
        cmd.add(clName);
        if (getPort() != null) {
            cmd.add("-p");
            cmd.add(String.valueOf(getPort()));
        }
        if (this.secure && getSecurityDescriptor() != null) {
            cmd.add("-containerDesc");
            cmd.add(getSecurityDescriptor().toString());
        }
        if (!this.secure) {
            cmd.add("-nosec");
        }

        cmd.add("-debug");

        // build environment
        String[] envp = new String[]{"GLOBUS_LOCATION=" + this.tmpGlobusLocation};
        envp = EnvUtils.overrideEnv(envp);

        // start globus
        Process p = Runtime.getRuntime().exec(cmd.toArray(new String[0]), envp, this.tmpGlobusLocation);
        new StdIOThread(p.getInputStream()).start();
        new StdIOThread(p.getErrorStream()).start();
        return p;
    }


    public synchronized boolean isGlobusRunning() {
        return isGlobusRunningCounter();
    }


    public synchronized URI getContainerBaseURI() throws MalformedURIException {
        URI url = new URI("http://localhost:" + getPort() + "/wsrf/services/");
        if (this.secure) {
            url = new URI("https://localhost:" + getPort() + "/wsrf/services/");
        }
        return url;
    }


    public synchronized EndpointReferenceType getServiceEPR(String servicePath) throws MalformedURIException {
        EndpointReferenceType epr = null;

        String url = getContainerBaseURI().toString() + servicePath;
        epr = new EndpointReferenceType(new Address(url));

        return epr;
    }


    protected synchronized boolean isGlobusRunningCounter() {
        try {
            org.globus.axis.util.Util.registerTransport();
            CounterServiceAddressingLocator locator = new CounterServiceAddressingLocator();
            EngineConfiguration engineConfig = new FileProvider(System.getenv("GLOBUS_LOCATION") + File.separator
                + "client-config.wsdd");
            locator.setEngine(new AxisClient(engineConfig));

            String url = getContainerBaseURI().toString() + "CounterService";

            CounterPortType counter = locator.getCounterPortTypePort(new EndpointReferenceType(new Address(url)));
            setAnonymous((Stub) counter);

            CreateCounterResponse response = counter.createCounter(new CreateCounter());
            EndpointReferenceType endpoint = response.getEndpointReference();
            counter = locator.getCounterPortTypePort(endpoint);
            setAnonymous((Stub) counter);
            counter.add(0);
            counter.destroy(new Destroy());
            return true;
        } catch (IOException e) {
            this.isGlobusRunningException = e;
            return false;
        } catch (ServiceException e) {
            this.isGlobusRunningException = e;
            return false;
        }
    }


    private static void setAnonymous(Stub stub) {
        stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS, Boolean.TRUE);
        stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, NoAuthorization.getInstance());
        stub._setProperty(GSIConstants.GSI_AUTHORIZATION, org.globus.gsi.gssapi.auth.NoAuthorization.getInstance());
    }


    public synchronized void stopGlobus() throws IOException {
        if (this.globusProcess == null) {
            return;
        }

        runGlobusCommand("org.globus.wsrf.container.ShutdownClient");
        sleep(2000);

        this.globusProcess.destroy();
        this.globusProcess = null;
    }


    public synchronized void cleanupTempGlobus() {
        if (this.tmpGlobusLocation != null) {
            FileUtils.deleteRecursive(this.tmpGlobusLocation);
        }
    }


    public synchronized File getTempGlobusLocation() {
        return this.tmpGlobusLocation;
    }


    public synchronized File getSecurityDescriptor() {
        return this.securityDescriptor;
    }


    public synchronized void setSecurityDescriptor(File securityDescriptor) {
        this.securityDescriptor = securityDescriptor;
    }


    private static void sleep(long ms) {
        Object sleep = new Object();
        try {
            synchronized (sleep) {
                sleep.wait(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return Returns the secure.
     */
    public boolean isSecure() {
        return this.secure;
    }


    /**
     * @param secure
     *            The secure to set.
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }


    /**
     * @param port
     *            The port to set.
     */
    public void setPort(Integer port) {
        this.port = port;
    }


    /**
     * @return the port
     */
    public synchronized Integer getPort() {
        if (this.port == null) {
            initializePort();
        }
        return this.port;
    }


    private void initializePort() {
        String portProp = System.getProperty("test.globus.port");
        if (portProp != null) {
            this.port = new Integer(portProp);
        }

        int start = Integer.parseInt(System.getProperty(this.getClass().getName() + ".portrange.min", "1025"));
        int end = Integer.parseInt(System.getProperty(this.getClass().getName() + ".portrange.max", "1125"));

        for (int i = start; i <= end; i++) {
            ServerSocket sock = null;
            try {
                sock = new ServerSocket(i);
                this.port = new Integer(i);
                return;
            } catch (Throwable e) {
            } finally {
                if (sock != null) {
                    try {
                        sock.close();
                    } catch (Throwable t) {
                    }
                }
            }
        }
    }
}
