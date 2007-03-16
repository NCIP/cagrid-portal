package org.cagrid.grape;

import javax.swing.JInternalFrame;

import org.cagrid.grape.utils.IconUtils;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @created Oct 14, 2004
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class ApplicationComponent extends JInternalFrame {

	private ApplicationContext applicationContext;


	public ApplicationComponent(ApplicationContext context) {
		setSize(200, 300);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setResizable(true);
		this.applicationContext = context;
		if (this.applicationContext.getComponent() != null) {
			this.setTitle(this.applicationContext.getComponent().getTitle());
			if (this.applicationContext.getComponent().getIcon() != null) {
				this.setFrameIcon(IconUtils.loadIcon(this.applicationContext.getComponent().getIcon()));
			}
		}
	}


	public ApplicationContext getContext() {
		return applicationContext;
	}

}