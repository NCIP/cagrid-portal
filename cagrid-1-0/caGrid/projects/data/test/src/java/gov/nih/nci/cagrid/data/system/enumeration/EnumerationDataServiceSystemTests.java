package gov.nih.nci.cagrid.data.system.enumeration;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.atomicobject.haste.framework.JVMStoryRunner;
import com.atomicobject.haste.framework.StoryBook;

/** 
 *  EnumerationDataServiceSystemTests
 *  Test for an enumeration supporting data service invocation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: EnumerationDataServiceSystemTests.java,v 1.1 2006-12-18 14:48:47 dervin Exp $ 
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
