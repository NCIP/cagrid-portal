package gov.nih.nci.cagrid.data.ui.browser;

import java.util.EventObject;

/** 
 *  AdditionalJarsEvent
 *  Event fired when additional jars change in the class browser panel
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 12, 2006 
 * @version $Id$ 
 */
public class AdditionalJarsChangedEvent extends EventObject {

	public AdditionalJarsChangedEvent(ClassBrowserPanel source) {
		super(source);
	}
}
