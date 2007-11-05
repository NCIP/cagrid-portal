package gov.nih.nci.cagrid.testing.system.deployment;

import gov.nih.nci.cagrid.common.StreamGobbler;
import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;
import org.globus.axis.gsi.GSIConstants;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.jdom.Element;
import org.oasis.wsrf.lifetime.Destroy;
import org.projectmobius.common.XMLUtilities;

import com.counter.CounterPortType;
import com.counter.CreateCounter;
import com.counter.CreateCounterResponse;
import com.counter.service.CounterServiceAddressingLocator;

/** 
 *  TomcatServiceContainer
 *  Service container implementation for tomcat
 * 
 * @author David Ervin
 * 
 * @created Oct 19, 2007 12:01:22 PM
 * @version $Id: TomcatServiceContainer.java,v 1.4 2007-11-05 16:19:58 dervin Exp $ 
 */
public class TomcatServiceContainer extends ServiceContainer {
    
    private static final Logger LOG = Logger.getLogger(ServiceContainer.class);
    
    public static final int CONNECT_ATTEMPTS = 10;
    public static final int CONNECT_ATTEPMT_TIMEOUT = 1; // secconds between attempts
    public static final int SHUTDOWN_WAIT_TIME = 10; // secconds
    public static final int DEPLOY_WAIT_TIME = 120; // secconds

    public static final String ENV_ANT_HOME = "ANT_HOME";
    public static final String ENV_CATALINA_HOME = "CATALINA_HOME";
    public static final String ENV_GLOBUS_LOCATION = "GLOBUS_LOCATION";
    
    public static final String DEPLOY_ANT_TARGET = "deployTomcat";
    
    private Process catalinaProcess;

    public TomcatServiceContainer(ContainerProperties properties) {
        super(properties);
    }


    protected void deploy(File serviceDir, List<String> deployArgs) throws ContainerException {
        String antHome = System.getenv(ENV_ANT_HOME);
        if (antHome == null || antHome.equals("")) {
            throw new ContainerException(ENV_ANT_HOME + " not set");
        }
        File ant = new File(antHome, "bin" + File.separator + "ant");

        List<String> command = new ArrayList<String>();

        // executable to call
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            command.add("cmd");
            command.add("/c");
            command.add(ant.getAbsolutePath() + ".bat");
        } else {
            command.add(ant.getAbsolutePath());
        }
        
        // any arguments
        if (deployArgs != null && deployArgs.size() != 0) {
            command.addAll(deployArgs);
        }

        // target to execute
        command.add(DEPLOY_ANT_TARGET);

        String[] locationEnvironment = new String[]{ENV_CATALINA_HOME + "=" 
            + getProperties().getContainerDirectory().getAbsolutePath()};
        String[] editedEnvironment = editEnvironment(locationEnvironment);
        
        LOG.debug("Command environment:\n");
        for (String e : editedEnvironment) {
            LOG.debug(e);
        }

        String[] commandArray = command.toArray(new String[command.size()]);
        Process deployProcess = null;
        try {
            deployProcess = Runtime.getRuntime().exec(commandArray, editedEnvironment, serviceDir);
            new StreamGobbler(deployProcess.getInputStream(), StreamGobbler.TYPE_OUT).start();
            new StreamGobbler(deployProcess.getErrorStream(), StreamGobbler.TYPE_OUT).start();
        } catch (Exception ex) {
            throw new ContainerException("Error invoking deploy process: " + ex.getMessage(), ex);
        }
        
        final Process finalDeploy = deployProcess;
        FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {
            public Boolean call() {
                try {
                    LOG.debug("Waiting for deploy process");
                    finalDeploy.waitFor();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return Boolean.FALSE;
                }
                return Boolean.valueOf(finalDeploy.exitValue() == 0);
            }
        });
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(future);
        
        boolean success = false;
        try {
            success = future.get(DEPLOY_WAIT_TIME, TimeUnit.SECONDS).booleanValue();
        } catch (Exception ex) {
            throw new ContainerException("Error deploying service: " + ex.getMessage(), ex);
        } finally {
            future.cancel(true);
            deployProcess.destroy();
        }
        
        executor.shutdownNow();
        deployProcess = null;
        
        if (!success) {
            throw new ContainerException("Deployment ant command failed: " + deployProcess.exitValue());
        }
    }


    protected void shutdown() throws ContainerException {
        if (catalinaProcess == null) {
            // no tomcat, no problem
            return;
        }
        
        String shutdown = getProperties().getContainerDirectory().getAbsolutePath() + File.separator + "bin" + File.separator + "catalina";
        
        List<String> command = new ArrayList<String>();

        // executable to call
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            command.add("cmd");
            command.add("/c");
            command.add(shutdown + ".bat stop");
        } else {
            command.add(shutdown + " stop");
        }
        
        String[] locationEnvironment = new String[]{ENV_CATALINA_HOME + "=" 
            + getProperties().getContainerDirectory().getAbsolutePath()};
        String[] editedEnvironment = editEnvironment(locationEnvironment);
        
        LOG.debug("Command environment:\n");
        for (String e : editedEnvironment) {
            LOG.debug(e);
        }

        String[] commandArray = command.toArray(new String[command.size()]);
        Process shutdownProcess = null;
        try {
            shutdownProcess = Runtime.getRuntime().exec(commandArray, editedEnvironment, 
                getProperties().getContainerDirectory());
            new StreamGobbler(shutdownProcess.getInputStream(), StreamGobbler.TYPE_OUT).start();
            new StreamGobbler(shutdownProcess.getErrorStream(), StreamGobbler.TYPE_OUT).start();
        } catch (Exception ex) {
            throw new ContainerException("Error invoking startup process: " + ex.getMessage(), ex);
        }
        
        final Process finalShutdownProcess = shutdownProcess;

        FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {
            public Boolean call() {
                try {
                    LOG.debug("Waiting for shutdown process");
                    finalShutdownProcess.waitFor();
                    LOG.debug("Waiting for catalina process");
                    catalinaProcess.waitFor();
                    LOG.debug("Done waiting");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return Boolean.FALSE;
                } finally {
                    finalShutdownProcess.destroy();
                    catalinaProcess.destroy();
                }
                return Boolean.valueOf(finalShutdownProcess.exitValue() == 0);
            }
        });
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(future);
        
        boolean success = false;
        try {
            success = future.get(SHUTDOWN_WAIT_TIME, TimeUnit.SECONDS).booleanValue();
        } catch (Exception ex) {
            throw new ContainerException("Error shutting down container: " + ex.getMessage(), ex);
        } finally {
            LOG.debug("Shutdown task complete, destroying processes");
            future.cancel(true);
            shutdownProcess.destroy();
            catalinaProcess.destroy();
        }
        
        executor.shutdownNow();
        
        shutdownProcess = null;
        catalinaProcess = null;
        
        if (!success) {
            throw new ContainerException("Shutdown command failed: " + finalShutdownProcess.exitValue());
        }
    }


    protected void startup() throws ContainerException {
        try {
            setServerPort();
        } catch (Exception ex) {
            throw new ContainerException("Error setting server port: " + ex.getMessage(), ex);
        }
        
        String startup = getProperties().getContainerDirectory().getAbsolutePath() 
            + File.separator + "bin" + File.separator + "catalina";
        
        List<String> command = new ArrayList<String>();

        // executable to call
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            command.add("cmd");
            command.add("/c");
            command.add(startup + ".bat run");
        } else {
            command.add(startup + " run");
        }
        
        String[] locationEnvironment = new String[]{ENV_CATALINA_HOME + "=" 
            + getProperties().getContainerDirectory().getAbsolutePath()};
        String[] editedEnvironment = editEnvironment(locationEnvironment);
        
        LOG.debug("Command environment:\n");
        for (String e : editedEnvironment) {
            LOG.debug(e);
        }

        String[] commandArray = command.toArray(new String[command.size()]);
        try {
            catalinaProcess = Runtime.getRuntime().exec(commandArray, editedEnvironment, 
                getProperties().getContainerDirectory());
            new StreamGobbler(catalinaProcess.getInputStream(), StreamGobbler.TYPE_OUT).start();
            new StreamGobbler(catalinaProcess.getErrorStream(), StreamGobbler.TYPE_OUT).start();
        } catch (Exception ex) {
            throw new ContainerException("Error invoking startup process: " + ex.getMessage(), ex);
        }
        
        // start checking for running
        Exception testException = null;
        sleep(2000);
        boolean running = false;
        for (int i = 0; !running && i < CONNECT_ATTEMPTS; i++) {
            LOG.debug("Connection attempt " + (i + 1));            
            try {
                running = isGlobusRunningCounter();
            } catch (Exception ex) {
                testException = ex;
            }
            sleep(CONNECT_ATTEPMT_TIMEOUT * 1000L);
        }
        if (!running) {
            if (testException != null) {
                throw new ContainerException("Error starting Tomcat: " + testException.getMessage(), testException);
            } else {
                throw new ContainerException("Tomcat non responsive after " + CONNECT_ATTEMPTS + " attempts to connect");
            }
        }
    }
    
    
    // ---------------
    // Helpers
    // ---------------
    
    
    private String[] editEnvironment(String[] edits) {
        Map<String, String> envm = new HashMap<String, String>(System.getenv());
        for (String element : edits) {
            String[] envVar = element.split("=");
            envm.put(envVar[0], envVar[1]);
        }
        String[] environment = new String[envm.size()];
        Iterator<String> keys = envm.keySet().iterator();
        int i = 0;
        while (keys.hasNext()) {
            String key = keys.next();
            environment[i++] = key + "=" + envm.get(key);
        }
        return environment;
    }
    
    
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * Checks that Globus is running by hitting the counter service
     * @return
     *      true if the container service could be contacted
     */
    protected synchronized boolean isGlobusRunningCounter() throws IOException, ServiceException {
        org.globus.axis.util.Util.registerTransport();
        CounterServiceAddressingLocator locator = new CounterServiceAddressingLocator();
        String globusLocation = System.getenv(ENV_GLOBUS_LOCATION);
        EngineConfiguration engineConfig = 
            new FileProvider(globusLocation + File.separator + "client-config.wsdd");
        // TODO: do we even need this?
        locator.setEngine(new AxisClient(engineConfig));

        String url = getContainerBaseURI().toString() + "CounterService";
        LOG.debug("Connecting to counter at " + url);

        CounterPortType counter = locator.getCounterPortTypePort(new EndpointReferenceType(new Address(url)));
        setAnonymous((Stub) counter);

        CreateCounterResponse response = counter.createCounter(new CreateCounter());
        EndpointReferenceType endpoint = response.getEndpointReference();
        counter = locator.getCounterPortTypePort(endpoint);
        setAnonymous((Stub) counter);
        ((Stub) counter).setTimeout(1000);
        counter.add(0);
        counter.destroy(new Destroy());
        return true;
    }
    
    
    private static void setAnonymous(Stub stub) {
        stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS, Boolean.TRUE);
        stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, NoAuthorization.getInstance());
        stub._setProperty(GSIConstants.GSI_AUTHORIZATION, org.globus.gsi.gssapi.auth.NoAuthorization.getInstance());
    }
    
    
    private void setServerPort() throws Exception {
        Integer port = getProperties().getPortPreference().getPort();
        File serverConfigFile = new File(getProperties().getContainerDirectory(),
            "conf" + File.separator + "server.xml");
        Element configRoot = XMLUtilities.fileNameToDocument(serverConfigFile.getAbsolutePath()).getRootElement();
        for (Element serviceElement : (List<Element>) configRoot.getChildren("Service", configRoot.getNamespace())) {
            if (serviceElement.getAttributeValue("name").equals("Catalina")) {
                for (Element connectorElement : 
                    (List<Element>) serviceElement.getChildren("Connector", configRoot.getNamespace())) {
                    boolean connectorFound = false;
                    if (getProperties().isSecure()) {
                        if (connectorElement.getAttributeValue("port").equals("8443")
                            && connectorElement.getAttributeValue("sslProtocol").equals("TLS")) {
                            connectorFound = true;
                        }
                    } else {
                        if (connectorElement.getAttributeValue("port").equals("8080")) {
                            connectorFound = true;
                        }
                    }
                    if (connectorFound) {
                        connectorElement.setAttribute("port", port.toString());
                        break;
                    }
                }
                break;
            }
        }
        String xml = XMLUtilities.formatXML(XMLUtilities.elementToString(configRoot));
        Utils.stringBufferToFile(new StringBuffer(xml), serverConfigFile.getAbsolutePath());
    }
}
