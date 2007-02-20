package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.atomicobject.haste.framework.JVMStoryRunner;
import com.atomicobject.haste.framework.StoryBook;

/** 
 *  UpgradeTests
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeTests.java,v 1.1 2007-02-20 21:03:17 dervin Exp $ 
 */
public class UpgradeTests extends StoryBook {

	protected void stories() {
		addStory(new UpgradeTo1pt1Tests());
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			PrintWriter out = new PrintWriter(
					new OutputStreamWriter(System.out));
			StoryBook book = new UpgradeTests();
			book.printDocument(out);
		} else {
			JVMStoryRunner.runStoryBook(new UpgradeTests());
		}
	}
}
