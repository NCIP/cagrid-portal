package gov.nih.nci.cagrid.testing.system.haste;

import gov.nih.nci.cagrid.common.StreamGobbler;
import gov.nih.nci.cagrid.testing.system.haste.io.ReportLineCollector;
import gov.nih.nci.cagrid.testing.system.haste.io.ReportWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import junit.framework.TestFailure;
import junit.framework.TestResult;

/*
 * HASTE - High-level Automated System Test Environment
 * Copyright (C) 2002  Atomic Object, LLC.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact Atomic Object:
 * 
 * Atomic Object, LLC.
 * East Building Suite 190
 * 419 Norwood Ave SE
 * Grand Rapids, MI 49506
 * USA
 *
 * info@atomicobject.com
 */

/**
 * JVMStoryRunner facilitates the execution of Stories in their own
 * processes, and thus in their own JVM.  <code>runStoryBook()</code>
 * will break the book into its component Stories, and <code>main()</code>
 * is responsible for parsing the classname (passed in via <code>exec()</code>)
 * and instantiating and running the Story. 
 */
public class JVMStoryRunner {

	/** The class used to execute Stories in a separate process */
	static final String RUNNER_CLASS = "com.atomicobject.haste.framework.JVMStoryRunner";

	// Exit codes
	static final int NORMAL_EXIT = 0;
	static final int FAILED_EXIT = -1;
	static final int ERROR_EXIT = -2;

	/**
	 * When executed standalone, this class accepts the classname
	 * of a Story to execute.
	 */
	public static void main(String args[]) {
		Vector errors = new Vector();
		
		// Parse the classname and attempt to get an instance via default
		// constructor.
		if (args.length != 1) {
			errors.addElement(
					"Expected a classname as the one and only argument to main()");
			reportErrors(errors, "");
			
		} else {
			Story story = null;
			String cname = args[0];
			try {
				story = (Story)(Class.forName(cname).newInstance());
				
			} catch (ClassNotFoundException ex) {
				errors.addElement("Could not get class for [" + cname + "]: "
						+ ex.getMessage());
			} catch (IllegalAccessException ex) {
				errors.addElement("Could not get class for [" + cname + "]: "
						+ ex.getMessage());
			} catch (InstantiationException ex) {
				errors.addElement("Could not get class for [" + cname + "]: "
						+ ex.getMessage());
			} catch (ClassCastException ex) {
				errors.addElement(cname + " is not descended from Story");
			}

			if (story != null) {
				doRun(story);
				
			} else {
				reportErrors(errors, cname);
			}
		}
	}

	static void reportErrors(Vector errors, String title) {
			PrintWriter out =
				new PrintWriter(new OutputStreamWriter(System.out));
			
			printHeader(out, title);
		
			for (int i=0; i < errors.size(); i++) {
				out.println(errors.elementAt(i).toString());
			}
			out.close();
	}
	

	/** Create a test result and execute the Story */
	static protected void doRun(Story story) {
		TestResult result = new TestResult();
		story.run(result);


		// Print a little report, if there were errors or failures
		if (result.errorCount() > 0 || result.failureCount() > 0) {
			// Open the communal report file for appending
			PrintWriter out = new PrintWriter(new ReportWriter(System.out));
			
			printHeader(out, story.getClass().getName());

			if (result.errorCount() > 0) {
				//
				// Print errors
				//
				out.println("This story had an ERROR:");
				TestFailure failure = (TestFailure)result.errors().nextElement();
				out.println(" - " + failure.failedTest() + ": ");
				failure.thrownException().printStackTrace(out);
				
				//
				// Print failures
				//
			} else if (result.failureCount() > 0) {
				out.println("This story FAILED:");
				TestFailure failure= (TestFailure)result.failures().nextElement();
				out.print(" - " + failure.failedTest());
				Throwable t= failure.thrownException();
				if (t.getMessage() != null) {
					out.println(" \"" + t.getMessage()
							+ "\"");
				} else {
					out.println();
					failure.thrownException().printStackTrace(out);
				}
			}
			// close the report file
			out.close();
		} // end report

		// Exit with the proper status
		if (result.errorCount() > 0) {
			System.exit(ERROR_EXIT);
		}
		else if (result.failureCount() > 0) {
			System.exit(FAILED_EXIT);
		} else {
			System.exit(NORMAL_EXIT);
		}
		
	} // end doRun()

	/**
	 * Print a header for reporting
	 */
	static void printHeader(PrintWriter out, String title) {
		out.println(ReportWriter.getLineSep()+"---------------------------------------------");
		out.println(title + ReportWriter.getLineSep());
	}
		
	/**
	 * Given a StoryBook, execute and report on the success of
	 * each Story within.  Each story is to be executed as a
	 * separate process, which allows each Story to execute within
	 * its very own JVM, minimizing static interaction between Stories.
	 * @param StoryBook The StoryBook object to execute.
	 */
	public static void runStoryBook(StoryBook book) {
		runStoryBook(book, System.out);
	}


	/**
	 * Given a StoryBook, execute and report on the success of
	 * each Story within.  Each story is to be executed as a
	 * separate process, which allows each Story to execute within
	 * its very own JVM, minimizing static interaction between Stories.
	 * @param StoryBook The StoryBook object to execute.
	 * @param out The output stream to do "console" reporting on
	 */	
	public static void runStoryBook(StoryBook book, OutputStream out) {
		PrintWriter outWriter = new PrintWriter(new OutputStreamWriter(out));
		
		
		Vector allStories = book.getStories();
		Vector failedStories = new Vector();
		Vector errorStories = new Vector();

		
		// Run each story in a separate JVM
		Story story;
		String className;
		Process proc = null;
		ReportLineCollector collector = null;
		StringBuffer reportBuffer = new StringBuffer();
		
		for (int i=0; i < allStories.size(); i++) {

			story = (Story)allStories.elementAt(i);
			className = story.getClass().getName();
			// Execute the Story runner class
			// and redirect stdout and stderr
			try {
				proc = Runtime.getRuntime().exec(new String[] {
					"java",	"-cp", System.getProperty("java.class.path"),
					RUNNER_CLASS, className });
				
				InputStream in = proc.getInputStream();
				collector = new ReportLineCollector(in);
				InputStream err = proc.getErrorStream();
				StreamGobbler gobbler1 = new StreamGobbler(collector,"TEST",out);
				StreamGobbler gobbler2 = new StreamGobbler(err,"TEST",System.err);
				
			} catch (IOException ex) {
				System.err.println("JVMStoryRunner: IOException while executing"
						+ " child process: " + ex.getMessage());
				ex.printStackTrace();				
			}

			// Wait on the process and get its exit value.
			int exitVal = 0; // presume normal exit
			try {
				exitVal = proc.waitFor();

				if (exitVal == FAILED_EXIT) {
					// Story failed for some reason
					failedStories.addElement(story);
				} else if (exitVal == ERROR_EXIT) {
					// Story erred
					errorStories.addElement(story);
				}

				// build up our buffer of reporting output
				reportBuffer.append(collector.getReportLines().toString());
				
			} catch (InterruptedException ex) {
				System.err.println("JVMStoryRunner: Interrupted while waiting for"
						+ " child process: " + ex.getMessage());
				ex.printStackTrace();
			}


		} // while

		// All stories have run.
			
		// Dump the communal StringBuffer to the output stream
		try {
//			try { Thread.sleep(2000);} catch(Exception ex) {}
			outWriter.flush();
			outWriter.println(ReportWriter.getLineSep()+" *** REPORT ***"+ReportWriter.getLineSep());
			outWriter.flush();
		
			InputStream reportIn = new ByteArrayInputStream(reportBuffer.toString().getBytes());
			StreamGobbler gobbler = new StreamGobbler(reportIn,"TEST",out);
			outWriter.flush();  
			out.flush();
			
		} catch (IOException ex) {
			System.err.println("JVMStoryRunner: failed to copy the report file to stdout: " + ex.getMessage());
			ex.printStackTrace();
		}
		
		// Report stats
		if (failedStories.size() == 0 &&
				errorStories.size() ==0) {
			outWriter.println();
			outWriter.print("OK");
			outWriter.println (" (" + allStories.size() + " stories)");

		} else {
			outWriter.println();
			outWriter.println("FAILURES!!!");
			outWriter.println();
			outWriter.println("Ran " + allStories.size() + " stories total.");
			outWriter.println("  failures: " + failedStories.size()); 
			outWriter.println("  errors:   " + errorStories.size());
		}
		outWriter.flush();
	}

}









