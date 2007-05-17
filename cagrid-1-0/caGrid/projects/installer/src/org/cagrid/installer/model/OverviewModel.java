/**
 * 
 */
package org.cagrid.installer.model;

import javax.swing.JComponent;

import org.pietschy.wizard.OverviewProvider;
import org.pietschy.wizard.models.MultiPathModel;
import org.pietschy.wizard.models.Path;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class OverviewModel extends MultiPathModel implements OverviewProvider {

	private JComponent overviewComponent; 
	
	/**
	 * @param firstPath
	 */
	public OverviewModel(Path firstPath) {
		super(firstPath);
	}

	/* (non-Javadoc)
	 * @see org.pietschy.wizard.OverviewProvider#getOverviewComponent()
	 */
	public JComponent getOverviewComponent() {
		return this.overviewComponent;
	}
	
	public void setOverviewComponent(JComponent overviewComponent){
		this.overviewComponent = overviewComponent;
	}

}
