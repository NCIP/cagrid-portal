package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.ImageIcon;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
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
				.getResource("/grouper_logo_22x22.png"));
	}
	
	
	public final static ImageIcon getGrouperRemoveIcon22x22() {
		return new javax.swing.ImageIcon(GridGrouperLookAndFeel.class
				.getResource("/grid_grouper_remove_22x22.png"));
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
}
