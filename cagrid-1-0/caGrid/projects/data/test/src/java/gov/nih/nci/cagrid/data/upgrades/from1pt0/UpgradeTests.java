package gov.nih.nci.cagrid.data.upgrades.from1pt0;

import gov.nih.nci.cagrid.testing.system.haste.JVMStoryRunner;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/** 
 *  UpgradeTests
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 20, 2007 
 * @version $Id: UpgradeTests.java,v 1.3 2007-12-03 16:27:19 hastings Exp $ 
 */
public class UpgradeTests extends StoryBook {

	protected void stories() {
		addStory(new UpgradeTo1pt2Tests());
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
