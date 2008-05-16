package org.cagrid.data.test.system;

import gov.nih.nci.cagrid.testing.system.haste.JVMStoryRunner;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/** 
 *  DataServiceSystemTests
 *  Test for data service invocation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: DataServiceSystemTests.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public class DataServiceSystemTests extends StoryBook {

	protected void stories() {
		addStory(new SystemTests());
	}


	public static void main(String[] args) {
		if (args.length > 0) {
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(System.out));
			StoryBook book = new DataServiceSystemTests();
			book.printDocument(out);
		} else {
			JVMStoryRunner.runStoryBook(new DataServiceSystemTests());
		}
	}
}
