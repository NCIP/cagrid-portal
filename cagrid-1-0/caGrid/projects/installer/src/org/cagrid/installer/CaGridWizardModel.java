package org.cagrid.installer;

import javax.swing.JComponent;

import org.pietschy.wizard.OverviewProvider;
import org.pietschy.wizard.models.MultiPathModel;
import org.pietschy.wizard.models.Path;

public class CaGridWizardModel extends MultiPathModel implements OverviewProvider {

	public CaGridWizardModel(Path arg0) {
		super(arg0);
	}

	public JComponent getOverviewComponent() {
		return new CaGridOverviewPanel();
	}
	

}
