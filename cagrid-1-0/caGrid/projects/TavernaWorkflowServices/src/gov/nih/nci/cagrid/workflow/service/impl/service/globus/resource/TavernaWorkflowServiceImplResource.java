package gov.nih.nci.cagrid.workflow.service.impl.service.globus.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.factory.service.TavernaWorkflowServiceConfiguration;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;

import workflowmanagementfactoryservice.StartInputType;
import workflowmanagementfactoryservice.WMSInputType;
import workflowmanagementfactoryservice.WMSOutputType;
import workflowmanagementfactoryservice.WorkflowOutputType;
import workflowmanagementfactoryservice.WorkflowStatusType;


/** 
 * The implementation of this TavernaWorkflowServiceImplResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */


public class TavernaWorkflowServiceImplResource extends TavernaWorkflowServiceImplResourceBase {

	private String scuflDoc = null;
	private static String[] outputDoc = null;
	private String[] inputDoc = null;
	private String baseDir = null;

	private String tempDir = null;
	private String workflowName = null;

	private static WorkflowStatusType workflowStatus = WorkflowStatusType.Pending;
	private static TavernaWorkflowServiceConfiguration config = null;

	private class WorkflowExecutionThread extends Thread {
		
		private String[] args = null;
		public WorkflowExecutionThread (String[] args)
		{
			this.args = args;
		}
		public void run()
		{	
			
			String tavernaDir = config.getTavernaDir();
			String repository = config.getBaseRepositoryDir();
			ProcessBuilder builder = new ProcessBuilder(this.args);
			builder.directory(new File(tavernaDir + "/target/classes"));

			Map<String, String> environment = builder.environment();
			
		    String classpath = tavernaDir + "/target/classes";
		    
		    // lisfOfJars is a method that returns all the jars from Taverna repository in CLASSPATH format (: seperated). 
		    classpath = classpath + listOfJars(repository);
		    environment.put("CLASSPATH", classpath);
			
			Iterator it = environment.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry pairs = (Map.Entry) it.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());
			}			
			try {
				Process process;
				process = builder.start();
				InputStream is = process.getInputStream();

				InputStreamReader isr = new InputStreamReader(is);

				BufferedReader br = new BufferedReader(isr);
				String line;
				System.out.printf("Output of running %s is: ", 
						Arrays.toString(args));
				boolean finished = false;
				String output = "";
				while ((line = br.readLine()) != null) {
					if(finished == true)
					{
						System.out.println(line);
						//this.setOutputDoc(new String[] {line});
						output = output + line;
						workflowStatus = WorkflowStatusType.Done;
					}
					if(line.equals("Finished!"))		
						finished = true;
				}
				String[] temp = output.split(":::");
					System.out.println("\nOUTPUT:\n" + temp[1]);
				setOutputDoc(new String[] {temp[1]});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}

	public TavernaWorkflowServiceImplResource() {
		try {
			config = TavernaWorkflowServiceConfiguration.getConfiguration();
			this.setBaseDir(config.getBaseRepositoryDir());

			if(config.getTavernaDir() == null)
			{
				throw new RemoteException("tavernaDir is not set the services.properties file"); 
			}


			System.out.println("basedir:" + baseDir);

			if (this.getBaseDir().equals("null"))
			{
				if(System.getProperty("os.type").startsWith("Windows"))
				{
					this.setBaseDir(System.getProperty("user.home")+ "\\Application Data" + "\\Taverna-1.7.1\\");
				}
				else
				{
					this.setBaseDir(System.getProperty("user.home")+ "\\Application Data" + "\\Taverna-1.7.1\\");
				}
			}

			//			if (this.getBaseDir().equals("$home/Library/Application Support/Taverna-1.7.1/repository/"))
			//			{
			//				this.setBaseDir(this.getBaseDir().replaceAll("\\$home", System.getenv("HOME")));
			//				//baseDir = baseDir.replaceAll("\\$home", System.getenv("HOME"));
			//			}
			//System.out.println("New Resource Created: " + this.getResourceKey().toString());

			System.out.println("\n\nThe Taverna Basedir is set to: " + baseDir);
			System.out.println("NOTE: Please set the Taverna base directly correctly. This can be set in the service.properties file of service code.\n\n");

			tempDir = System.getProperty("java.io.tmpdir");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String[] getInputDoc() {
		return inputDoc;
	}

	public void setInputDoc(String[] inputDoc) {
		this.inputDoc = inputDoc;
	}

	public static String[] getOutputDoc() {
		return outputDoc;
	}

	public static void setOutputDoc(String[] workflowOuput) {
		outputDoc = workflowOuput;
	}

	public String getScuflDoc() {
		return scuflDoc;
	}

	public void setScuflDoc(String scuflDoc) {
		this.scuflDoc = scuflDoc;
	}



	public void createWorkflow(WMSInputType wMSInputElement)
	{
		try {

			String [] keys = this.getResourceKey().toString().trim().split("TavernaWorkflowServiceImplResultsKey=");
			System.out.println("Workflow NAME :" + wMSInputElement.getWorkflowName());
			this.setWorkflowName(wMSInputElement.getWorkflowName());

			String scuflDocTemp = this.getTempDir() + "/" + keys[1] + "--workflow.xml";
			Utils.stringBufferToFile(new StringBuffer(wMSInputElement.getScuflDoc()), scuflDocTemp);
			this.setScuflDoc(scuflDocTemp);

			//String output = this.getTempDir() + keys[1] + "--output.xml";
			//this.setOutputDoc(output);



		} catch (Exception e){
			this.workflowStatus = WorkflowStatusType.Failed;
			e.printStackTrace();
			System.exit(1);
		}

	}

	public WorkflowStatusType start (StartInputType startInput) {

		String [] keys = this.getResourceKey().toString().trim().split("TavernaWorkflowServiceImplResultsKey=");
		try {
			
			int inputPorts = 0;
			
			if(startInput != null)
			{
				System.out.println("InputFile provided for the workflow.");
				String[] inputs = startInput.getInputArgs();
				for (int i=0; i < inputs.length; i++)
				{
					String inputFile = this.getTempDir() + "/" + keys[1] + "-input-" + i + ".xml";					
					Utils.stringBufferToFile(new StringBuffer(inputs[i]), inputFile);
					System.out.println("Input file " + i + " : " + inputFile);
				}
				this.setInputDoc(inputs);
				inputPorts = this.getInputDoc().length;
			}

			/*			String [] args = { "-workflow", this.getScuflDoc(), 
					"-outputdoc", this.getOutputDoc(),
					"-basedir", this.getBaseDir(),
					"-inputdoc", this.getInputDoc()	};*/


			//The first argument is the scuflDoc string, and remaining are the input strings.
			//String [] args = { this.getScuflDoc(), "blah", "and blah"	};
			String [] args = new String[inputPorts + 3];

			args[0] = "java";
//			args[1] = "net.sf.taverna.t2.examples.execution.ExecuteWorkflow";
			args[1] = "gov.nih.nci.cagrid.workflow.factory.taverna.ExecuteWorkflow";
			args[2] = this.getScuflDoc();
			for(int i = 0; i < inputPorts; i++)
			{
				args[i+3] = this.getInputDoc()[i];
			}

			workflowStatus = WorkflowStatusType.Active;
			WorkflowExecutionThread executor = new WorkflowExecutionThread(args);
			executor.start();

			/*	
			System.out.println("In Service resource: \n");
			System.out.println("Launching WorkflowLauncher: \n");

			String [] results = new ExecuteWorkflow().run(args);
			this.setOutputDoc(results);

			// Need to return the WorkflowStatusType with status as "Done" */

			//this.workflowStatus = WorkflowStatusType.Done;

		} catch (Exception e) {
			e.printStackTrace();
			this.workflowStatus = WorkflowStatusType.Failed;
		}
		return this.workflowStatus;
	}

	public WorkflowOutputType getWorkflowOutput()
	{
		WorkflowOutputType workflowOuputElement = new WorkflowOutputType();
		try {
			//if( (new File(this.getOutputDoc()).exists()) && (this.workflowStatus.equals(WorkflowStatusType.Done)))
			if( this.workflowStatus.equals(WorkflowStatusType.Done) )
			{
				//workflowOuputElement.setOutputFile(Utils.fileToStringBuffer( new File(this.getOutputDoc())).toString());
				// Currently, the results are stored as array of string holding the outputs.
				// Ideally, the output files should be created for each output in the "start" operation,
				//	 and then access those files here, converte them into string buffers and then store them below.

				workflowOuputElement.setOutputFile(this.getOutputDoc());
			}
			else
			{
				throw new RemoteException("Either the Workflow execution is not Completed or Failed.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return workflowOuputElement;
	}


	public WorkflowStatusType getStatus()
	{
		return this.workflowStatus;
	}
	
	//****** Following two methods is get a list of all the jars from the Taverna repository******
	//******  These jars are used to add to the classpath in process builder.				******
	//****** Methods: listFilesAsArray() and listFiles
	
	public String listOfJars(String directory)
	{
		File dir = new File(directory);
	    Collection<File> children = listFiles(dir, null, true);		    
	    File[] fileArray = new File[children.size()];
		children.toArray(fileArray);
	    
		String classpath = "";
	    if (children == null) {
	    	System.out.println("Error : Taverna repository is Empty.");
	    } else {
	        for (int i=0; i<fileArray.length; i++) {
	            // Get filename of file or directory
	            String filename = fileArray[i].toString();
	            if(filename.endsWith(".jar"))
	            {
	            	System.out.println(i+1 + " : " + filename);
	            	classpath = classpath + ":" + filename;
	            }
	        }
	    }
    	System.out.println(classpath);
	    return classpath;
	}

	public static Collection<File> listFiles(
	// Java4: public static Collection listFiles(
			File directory,
			FilenameFilter filter,
			boolean recurse)
	{
		// List of files / directories
		Vector<File> files = new Vector<File>();
	// Java4: Vector files = new Vector();
		
		// Get files / directories in the directory
		File[] entries = directory.listFiles();
		
		// Go over entries
		for (File entry : entries)
		{
	// Java4: for (int f = 0; f < files.length; f++) {
	// Java4: 	File entry = (File) files[f];

			// If there is no filter or the filter accepts the 
			// file / directory, add it to the list
			if (filter == null || filter.accept(directory, entry.getName()))
			{
				files.add(entry);
			}
			
			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory())
			{
				files.addAll(listFiles(entry, filter, recurse));
			}
		}
		return files;		
		// Return collection of files
	}


}
