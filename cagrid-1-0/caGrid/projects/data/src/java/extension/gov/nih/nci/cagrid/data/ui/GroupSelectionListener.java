package gov.nih.nci.cagrid.data.ui;

import javax.swing.ButtonModel;

/** 
 *  GroupSelectionListener
 *  Listens for selections of buttons in a group
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 27, 2006 
 * @version $Id: GroupSelectionListener.java,v 1.1 2006-11-27 16:06:41 dervin Exp $ 
 */
public interface GroupSelectionListener {

	/**
	 * Called when the selection of buttons in a group changes
	 * 
	 * @param previousSelection
	 * 		The previously selected button.  This will be null if no button was previously selected
	 * @param currentSelection
	 * 		The currently selected button.
	 */
	public void selectionChanged(final ButtonModel previousSelection, final ButtonModel currentSelection);
}
