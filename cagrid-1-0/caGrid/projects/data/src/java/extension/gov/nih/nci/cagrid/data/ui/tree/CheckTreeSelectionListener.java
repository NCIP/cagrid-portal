package gov.nih.nci.cagrid.data.ui.tree;

/** 
 *  CheckTreeSelectionListener
 *  Listener for node check / uncheck events
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 21, 2006 
 * @version $Id$ 
 */
public interface CheckTreeSelectionListener {

	public void nodeChecked(CheckTreeSelectionEvent e);
	
	
	public void nodeUnchecked(CheckTreeSelectionEvent e);
}
