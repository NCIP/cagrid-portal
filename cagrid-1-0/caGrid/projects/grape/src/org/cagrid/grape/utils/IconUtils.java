package org.cagrid.grape.utils;

import javax.swing.ImageIcon;


public class IconUtils {

	public static ImageIcon loadIcon(String resource) {
		if (resource == null) {
			return null;
		}
		try {
			ImageIcon icon = new ImageIcon(java.awt.Toolkit.getDefaultToolkit().getImage(
				IconUtils.class.getResource(resource)));
			return icon;
		} catch (Exception e) {
			System.out.println("An error occurred loading the icon from the resource " + resource);
		}
		return null;
	}
}
