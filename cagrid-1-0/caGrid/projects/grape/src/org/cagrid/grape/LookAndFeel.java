package org.cagrid.grape;

import java.awt.Color;

import javax.swing.ImageIcon;

import org.cagrid.grape.utils.IconUtils;

public class LookAndFeel {

	public final static ImageIcon getPreferencesIcon() {
		return IconUtils.loadIcon("/modify22x22.png");
	}
	
	public final static ImageIcon getPreferencesTreeIcon() {
		return IconUtils.loadIcon("/modify16x16.png");
	}
	
	public final static ImageIcon getConfigurationGroupIcon() {
		return IconUtils.loadIcon("/document-open16x16.png");
	}
	
	public final static ImageIcon getConfigurationPropertyIcon() {
		return IconUtils.loadIcon("/document-properties16x16.png");
	}


	public final static ImageIcon getCascadeIcon() {
		return IconUtils.loadIcon("/Cascade.gif");
	}

	public final static ImageIcon getTileIcon() {
		return IconUtils.loadIcon("/TileVertical.gif");
	}
	
	public final static ImageIcon getAboutIcon() {
		return IconUtils.loadIcon("/Inform.gif");
	}
	
	public final static ImageIcon getAddIcon() {
		return IconUtils.loadIcon("/list-add.png");
	}
	
	public final static ImageIcon getRemoveIcon() {
		return IconUtils.loadIcon("/list-remove.png");
	}
	
	public final static ImageIcon getLogoNoText() {
		return IconUtils.loadIcon("/caGrid-notext.gif");
	}
	
	public final static ImageIcon getLogoNoText22x22() {
		return IconUtils.loadIcon("/caGrid-icon-22x22.gif");
	}
	
	public final static Color getPanelLabelColor() {
		float[] vals = new float[3];
		Color.RGBtoHSB(62, 109, 181, vals);
		return Color.getHSBColor(vals[0], vals[1], vals[2]);
	}

	public final static Color getTableSelectTextColor() {
		return Color.WHITE;
	}

	public final static Color getTableRowColor() {
		return getLightBlue();
	}
	
	public final static Color getLightGray() {
		float[] vals = new float[3];
		Color.RGBtoHSB(211, 211, 211, vals);
		return Color.getHSBColor(vals[0], vals[1], vals[2]);
	}
	
	public final static Color getDarkGray() {
		float[] vals = new float[3];
		Color.RGBtoHSB(180, 180, 180, vals);
		return Color.getHSBColor(vals[0], vals[1], vals[2]);
	}
	
	
	public final static Color getLightBlue() {
		float[] vals = new float[3];
		Color.RGBtoHSB(183, 201, 227, vals);
		return Color.getHSBColor(vals[0], vals[1], vals[2]);
	}

	public final static Color getDarkBlue() {
		float[] vals = new float[3];
		Color.RGBtoHSB(78, 111, 160, vals);
		return Color.getHSBColor(vals[0], vals[1], vals[2]);
	}

	public final static Color getTableSelectColor() {
		return getDarkBlue();
	}

}
