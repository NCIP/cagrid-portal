package gov.nih.nci.cagrid.common.portal;

import gov.nih.nci.cagrid.introduce.portal.AnalyticalLookAndFeel;

import java.awt.Color;

import javax.swing.ImageIcon;

public class PortalLookAndFeel {
	 public final static Color getPanelLabelColor(){
		 	float[] vals = new float[3];
			Color.RGBtoHSB(62,109,181,vals);
			return Color.getHSBColor(vals[0],vals[1],vals[2]);
		 }
	 
	 public final static ImageIcon getCloseIcon(){
		 	return new javax.swing.ImageIcon( PortalLookAndFeel.class.getResource("/Delete.gif"));
		 }
	 
	 public final static ImageIcon getSelectIcon(){
		 	return new javax.swing.ImageIcon( PortalLookAndFeel.class.getResource("/Check.gif"));
		 }
	 

	 public final static ImageIcon getEditIcon(){
		 	return new javax.swing.ImageIcon(PortalLookAndFeel.class.getResource("/Draw.gif"));
		 }

	 public final static ImageIcon getInformIcon(){
	 	return new javax.swing.ImageIcon(AnalyticalLookAndFeel.class.getResource("/Inform.gif"));
	 }
	 
	 public final static ImageIcon getAddIcon(){
	 	return new javax.swing.ImageIcon(AnalyticalLookAndFeel.class.getResource("/Plus20X20.gif"));
	 }
	 
	 public final static ImageIcon getRemoveIcon(){
	 	return new javax.swing.ImageIcon(AnalyticalLookAndFeel.class.getResource("/Minus.gif"));
	 }
	 
	 public final static ImageIcon getQueryIcon(){
	 	return new javax.swing.ImageIcon(AnalyticalLookAndFeel.class.getResource("/Magnify.gif"));
	 }
}
