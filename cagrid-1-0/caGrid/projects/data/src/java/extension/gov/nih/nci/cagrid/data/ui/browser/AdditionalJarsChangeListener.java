package gov.nih.nci.cagrid.data.ui.browser;

import java.util.EventListener;

/** 
 *  AdditionalJarsChangeListener
 *  Listens for additional jars changed events
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 12, 2006 
 * @version $Id$ 
 */
public interface AdditionalJarsChangeListener extends EventListener {

	public void additionalJarsChanged(AdditionalJarsChangedEvent e);
}
