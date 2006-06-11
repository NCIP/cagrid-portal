/*
 * Created on Jun 8, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.AntUtils;
import gov.nci.nih.cagrid.tests.core.util.FileUtils;
import gov.nci.nih.cagrid.tests.core.util.IntroduceServiceInfo;
import gov.nci.nih.cagrid.tests.core.util.SourceUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.atomicobject.haste.framework.Step;

public class CreateServiceStep
	extends Step
{
	private File introduceDir;
	private File serviceDir;
	private String serviceName;
	private String pkg;
	private String namespace;
	private File serviceXmlDescriptor;
	private File[] schemas;
	private File implFile;
	private File[] jars;
	
	public CreateServiceStep(File introduceDir, File testDir, File tmpDir) 
		throws ParserConfigurationException, SAXException, IOException
	{
		super();
		
		// set serviceXmlDescriptor, serviceName, pkg, and namespace
		this.serviceXmlDescriptor = new File(testDir, IntroduceServiceInfo.INTRODUCE_SERVICEXML_FILENAME);
		IntroduceServiceInfo serviceInfo = new IntroduceServiceInfo(this.serviceXmlDescriptor);
		this.serviceName = serviceInfo.getServiceName();
		this.namespace = serviceInfo.getNamespace();
		this.pkg = serviceInfo.getPackageName();
		
		// set serviceDir
		this.serviceDir = new File(tmpDir, serviceName);
		this.serviceDir.mkdirs();
		
		// set schemas
		this.schemas = new File(testDir, "schema").listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().endsWith(".xsd");
			}
		});
		
		// set implFile
		this.implFile = new File(testDir, "src" + File.separator + serviceName + "Impl");
		
		// set libJars
		this.jars = new File(testDir, "lib").listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().endsWith(".jar");
			}
		});		
	}
	
	public CreateServiceStep(
		File introduceDir, File serviceDir, String serviceName, String pkg, String namespace, 
		File serviceXmlDescriptor, File[] schemas, File implFile, File[] jars
	) {
		super();
		
		this.introduceDir = introduceDir;
		this.serviceDir = serviceDir;
		this.serviceName = serviceName;
		this.pkg = pkg;
		this.namespace = namespace;
		this.serviceXmlDescriptor = serviceXmlDescriptor;
		this.schemas = schemas;
		this.implFile = implFile;
		this.jars = jars;
	}
	
	public void runStep() 
		throws IOException, InterruptedException, ParserConfigurationException, SAXException
	{
		// create skeleton
		createSkeleton();
		
		// copy schemas
		File schemaDir = new File(serviceDir, "schema" + File.separator + serviceName);
		schemaDir.mkdirs();
		for (File schema : schemas) {
			FileUtils.copy(schema, new File(schemaDir, schema.getName()));
		}
		
		// copy interface
		FileUtils.copy(serviceXmlDescriptor, new File(serviceDir, IntroduceServiceInfo.INTRODUCE_SERVICEXML_FILENAME));
		
		// copy jars
		File libDir = new File(serviceDir, "lib");
		libDir.mkdirs();
		for (File jar : jars) {
			FileUtils.copy(jar, new File(libDir, jar.getName()));
		}

		// synchronize
		synchronizeSkeleton();
		
		// add implementation
		addImplementation();
		
		// rebuild
		buildSkeleton();
	}
	
	private void addImplementation() 
		throws ParserConfigurationException, SAXException, IOException
	{
		String targetPath = pkg.replace('.', File.pathSeparatorChar);		
		File targetJava = new File(serviceDir, "src" + targetPath + "service" + serviceName + "Impl.java");
		
		// add method impl
		IntroduceServiceInfo info = new IntroduceServiceInfo(serviceXmlDescriptor);
		for (String methodName : info.getMethodNames()) {
			SourceUtils.modifyImpl(implFile, targetJava, methodName);
		}
		
		// add constructor impl
		SourceUtils.modifyImpl(implFile, targetJava, serviceName);
	}
	
	private void createSkeleton() 
		throws IOException, InterruptedException
	{
		//String cmd = "ant -D=BasicAnalyticalService -Dintroduce.skeleton.destination.dir=BasicAnalyticalService -Dintroduce.skeleton.package=edu.duke.test -Dintroduce.skeleton.package.dir=edu/duke/test -Dintroduce.skeleton.namespace.domain=http://cagrid.nci.nih.gov \"-Dintroduce.skeleton.extensions=\" createService";
		
		// create properties
		Properties sysProps = new Properties();
		sysProps.setProperty("introduce.skeleton.service.name", serviceName);
		sysProps.setProperty("introduce.skeleton.destination.dir", serviceDir.toString());
		sysProps.setProperty("introduce.skeleton.package", pkg);
		sysProps.setProperty("introduce.skeleton.package.dir", pkg.replace('.', '/'));
		sysProps.setProperty("introduce.skeleton.namespace.domain", namespace);
		sysProps.setProperty("introduce.skeleton.extensions", "");
		
		// invoke ant
		AntUtils.runAnt(introduceDir, null, IntroduceServiceInfo.INTRODUCE_CREATESERVICE_TASK, sysProps, null);
	}
	
	private void synchronizeSkeleton() 
		throws IOException, InterruptedException
	{
		//String cmd = "ant -Dintroduce.skeleton.destination.dir=BasicAnalyticalService resyncService";
		
		// create properties
		Properties sysProps = new Properties();
		sysProps.setProperty("introduce.skeleton.destination.dir", serviceDir.toString());
		
		// invoke ant
		AntUtils.runAnt(introduceDir, null, IntroduceServiceInfo.INTRODUCE_RESYNCSERVICE_TASK, sysProps, null);
	}
	
	private void buildSkeleton() 
		throws IOException, InterruptedException
	{
		// invoke ant
		AntUtils.runAnt(serviceDir, null, null, null, null);
	}

	public File getServiceDir()
	{
		return serviceDir;
	}

	public String getServiceName()
	{
		return serviceName;
	}
}
