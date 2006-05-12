package gov.nih.nci.cagrid.data.ui.browser;

import java.util.EventListener;

/** 
 *  ClassSelectionListener
 *  Listens for class selection events
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 12, 2006 
 * @version $Id$ 
 */
public interface ClassSelectionListener extends EventListener {

	public void classSelectionChanged(ClassSelectionEvent e);
}
