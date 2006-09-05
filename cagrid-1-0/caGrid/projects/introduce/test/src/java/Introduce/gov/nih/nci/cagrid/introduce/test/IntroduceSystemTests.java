package gov.nih.nci.cagrid.introduce.test;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.atomicobject.haste.framework.JVMStoryRunner;
import com.atomicobject.haste.framework.StoryBook;

public class IntroduceSystemTests extends StoryBook {

	protected void stories() {
		addStory(new SyncToolsTest());
	}

	/**
	 * Convenience method for executing this StoryBook from the command line.
	 */
	public static void main(String args[]) {
		if (args.length > 0) {
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(System.out));
			StoryBook book = new IntroduceSystemTests();
			book.printDocument(out);
		} else {
			JVMStoryRunner.runStoryBook(new IntroduceSystemTests());
		}
	}
}
