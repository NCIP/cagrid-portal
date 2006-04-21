package gov.nih.nci.cagrid.data.ui.types;

/** 
 *  TypeSelectionListener
 *  Listener for type selection events
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 21, 2006 
 * @version $Id$ 
 */
public interface TypeSelectionListener {

	public void typeSelectionAdded(TypeSelectionEvent e);
	
	
	public void typeSelectionRemoved(TypeSelectionEvent e);
}
