package org.cagrid.grape;

import javax.swing.ImageIcon;

import org.cagrid.grape.utils.IconUtils;

public class LookAndFeel {

	public final static ImageIcon getPreferencesIcon() {
		return IconUtils.loadIcon("/modify22x22.png");
	}
	
	public final static ImageIcon getPreferencesTreeIcon() {
		return IconUtils.loadIcon("/modify16x16.png");
	}

	public final static ImageIcon getCascadeIcon() {
		return IconUtils.loadIcon("/Cascade.gif");
	}

	public final static ImageIcon getTileIcon() {
		return IconUtils.loadIcon("/TileVertical.gif");
	}

}
