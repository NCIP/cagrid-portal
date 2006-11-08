package gov.nih.nci.cagrid.data.system;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.atomicobject.haste.framework.JVMStoryRunner;
import com.atomicobject.haste.framework.StoryBook;

/** 
 *  DataServiceSystemTests
 *  Test for data service invocation
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 7, 2006 
 * @version $Id: DataServiceSystemTests.java,v 1.1 2006-11-08 18:09:38 dervin Exp $ 
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
