 package gov.nih.nci.cagrid.testing.system.haste;

import java.util.*;
import junit.framework.Test;
import java.io.PrintWriter;

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
 * StoryBook contains and executes a series of Story objects.
 * This is the system-test-level analogue of JUnit's TestSuite.
 * @version $Revision: 1.1 $
 */
public abstract class StoryBook extends junit.framework.TestSuite {

	/** Default constructor */
	public StoryBook() {
	  super();
		stories();
	}
	
	/**
	 * Convenience method for adding a Story to be executed by this
	 * StoryBook.
	 * @param s The Story to add.
	 */
	public void addStory(Story s) {
		addTest(s);
	}

	/**
	 * Convenience method for adding a StoryBook to be executed.
	 * @param b The StoryBook to add.
	 */ 
	public void addStoryBook(StoryBook b) {
		addTest(b);
	}


	/**
	 * Return a List of all Stories under this StoryBook.
	 * If this StoryBook contains other StoryBooks, they will
	 * be traversed for their Stories.
	 */
	public Vector getStories() {
		Vector stories = new Vector();
		Enumeration tenum = tests();
		Test t;
		while (tenum.hasMoreElements()) {
			t = (Test)tenum.nextElement();
			if (t instanceof Story) {
				// If it's a Story, add it to the list.
				stories.addElement(t);
				
			} else if (t instanceof StoryBook) {
				// Add the stories within the StoryBook
				Vector childStories = ((StoryBook)t).getStories();
				for (int i=0; i < childStories.size(); i++) {
					stories.addElement(childStories.elementAt(i));
				}
				
			} else {
				System.err.println("StoryBook.getStories(): Encountered a Test that"
						+ " was neither a Story nor a StoryBook: " + t);
			}
		}
		return stories;
	}

	/**
	 * Print test coverage documentation to stdout.
	 * To customize this output, override:
	 * <code>
	 * printHeader()
	 * printStory(Story s)
	 * printFooter()
	 * </code>
	 * @param out The stream to write the document to
	 */
	public void printDocument(PrintWriter out) {
		printHeader(out);
		
		Vector stories = getStories();
		Story st;
		for (int i=0; i < stories.size(); i++) {
			st = (Story)stories.elementAt(i);
			printStory(out, st);
			 
		}
//		Iterator storit = getStories().iterator();
//		Story st;
//		while (storit.hasNext()) {
//			st = (Story)storit.next();
//			printStory(out, st);
//		}
		
		printFooter(out);
		out.flush();
	}

	/**
	 * Called by printDocument() just before Stories are printed.	
	 * @param out The stream to write the document to
	 */
	public void printHeader(PrintWriter out) {
		out.println("System Test Coverage\n");
	}
	
	
	/**
	 * Called by printDocument() for each Story.
	 * @param out The stream to write the document to
	 * @param st The Story to print.
	 */
	public void printStory(PrintWriter out, Story st) {
		out.println("<b>" + st.getClass().getName() + ": </b> " +
			st.getDescription() + "\n");
	}
	
	/**
	 * Called by printDocument() after all Stories have been printed.
	 * @param out The stream to write the document to
	 */
	public void printFooter(PrintWriter out) {}

	
	/**
	 * Subclasses of StoryBook use this method to compose
	 * the sequence of Stories and StoryBooks to be executed.
	 * This is done via any number of calls to addStory() and
	 * addStoryBook().
	 * <p>Example of the contents of a <code>stories()</code>
	 * method:
	
	        <pre>
			    addStory( new Story1() );
				addStory( new Story2() );
				addStoryBook( new BookA() );
				addStory( new Story3() );
				addStoryBook( new BookB() ); 
			</pre>
	 */
	protected abstract void stories();
}












