package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.ImageIcon;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GridGrouperLookAndFeel extends PortalLookAndFeel {
	public final static ImageIcon getGrouperIcon22x22() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/grouper_logo_22x22.png"));
	}
	
	public final static ImageIcon getGrouperIconNoBackground() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/grouper_logo_no_background.png"));
	}
	
	public final static ImageIcon getGrouperAddIcon22x22() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/grouper_add_22x22.png"));
	}
	
	
	public final static ImageIcon getGrouperRemoveIcon22x22() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/grouper_remove_22x22.png"));
	}
	
	public final static ImageIcon getGridGrouperServicesIcon16x16() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/applications-internet-16x16.png"));
	}
	
	
	
	public final static ImageIcon getGrouperIcon16x16() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/grouper_logo_16x16.png"));
	}
	
	public final static ImageIcon getLoadIcon() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/view-refresh.png"));
	}
	
	public final static ImageIcon getStemIcon() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/chart_organisation.png"));
	}
	
	public final static ImageIcon getCloseTab() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/closeTab.gif"));
	}
}
