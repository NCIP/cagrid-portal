package gov.nih.nci.cagrid.data.system.enumeration;

import gov.nih.nci.cagrid.testing.system.haste.JVMStoryRunner;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/** 
 *  EnumerationDataServiceSystemTests
 *  Test for an enumeration supporting data service invocation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: EnumerationDataServiceSystemTests.java,v 1.2 2007-12-03 16:27:19 hastings Exp $ 
 */
public class EnumerationDataServiceSystemTests extends StoryBook {

	protected void stories() {
		addStory(new EnumerationSystemTests());
	}


	public static void main(String[] args) {
		if (args.length > 0) {
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(System.out));
			StoryBook book = new EnumerationDataServiceSystemTests();
			book.printDocument(out);
		} else {
			JVMStoryRunner.runStoryBook(new EnumerationDataServiceSystemTests());
		}
	}
}
