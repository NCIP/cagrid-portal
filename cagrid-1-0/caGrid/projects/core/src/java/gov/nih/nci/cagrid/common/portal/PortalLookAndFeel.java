package gov.nih.nci.cagrid.common.portal;

import java.awt.Color;

import javax.swing.ImageIcon;

public class PortalLookAndFeel {
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

	public final static ImageIcon getCloseIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Delete.gif"));
	}

	public final static ImageIcon getSelectIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Check.gif"));
	}

	public final static ImageIcon getEditIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Draw.gif"));
	}

	public final static ImageIcon getInformIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Inform.gif"));
	}

	public final static ImageIcon getAddIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Plus20X20.gif"));
	}

	public final static ImageIcon getRemoveIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Minus.gif"));
	}

	public final static ImageIcon getQueryIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Magnify.gif"));
	}
	
	public final static ImageIcon getSaveIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Save.gif"));
	}
	
	public final static ImageIcon getImportIcon() {
		return new javax.swing.ImageIcon(PortalLookAndFeel.class
				.getResource("/Door.gif"));
	}
}
