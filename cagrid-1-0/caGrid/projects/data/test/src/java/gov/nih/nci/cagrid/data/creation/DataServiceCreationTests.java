package gov.nih.nci.cagrid.data.creation;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.atomicobject.haste.framework.JVMStoryRunner;
import com.atomicobject.haste.framework.StoryBook;

/** 
 *  DataServiceCreationTests
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006 
 * @version $Id$ 
 */
public class DataServiceCreationTests extends StoryBook {
	
	protected void stories() {
		addStory(new CreationTests());
	}


	public static void main(String[] args) {
		if (args.length > 0) {
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(System.out));
			StoryBook book = new DataServiceCreationTests();
			book.printDocument(out);
		} else {
			JVMStoryRunner.runStoryBook(new DataServiceCreationTests());
		}
	}
}
