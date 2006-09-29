package gov.nih.nci.cagrid.data.ui.cacore;

/** 
 *  ButtonEnableListener
 *  Listener to enable / disable buttons
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 29, 2006 
 * @version $Id$ 
 */
public interface ButtonEnableListener {

	public void setNextEnabled(boolean enable);
	
	
	public void setPrevEnabled(boolean enable);
}
