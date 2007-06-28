/**
 * 
 */
package org.cagrid.installer.steps;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class InstallationCompleteStep extends PanelWizardStep {

	private CaGridInstallerModel model;
	private JTextPane textPane;
	
	/**
	 * 
	 */
	public InstallationCompleteStep() {
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InstallationCompleteStep(String name, String summary) {
		super(name, summary);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public InstallationCompleteStep(String name, String summary, Icon icon) {
		super(name, summary, icon);
	}
	
	public void init(WizardModel m){
		this.model = (CaGridInstallerModel)m;
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(263, 161));
		
		this.textPane = new JTextPane();
		InputMap inputMap = this.textPane.getInputMap();
		KeyStroke keyStroke = KeyStroke.getKeyStroke("control C");
		inputMap.put(keyStroke, DefaultEditorKit.copyAction);
		this.add(textPane, gridBagConstraints);
		
		setComplete(true);	
	}
	
	public void prepare(){
		StringBuilder sb = new StringBuilder();

		sb.append("caGrid installation is complete. ");
		
		sb.append("Please remember to set the following environment variables:\n");
		sb.append("\t").append("ANT_HOME=").append(this.model.getState().get(Constants.ANT_HOME)).append("\n");
		sb.append("\t").append("GLOBUS_LOCATION=").append(this.model.getState().get(Constants.GLOBUS_HOME)).append("\n");
		sb.append("\t").append("CATALINA_HOME=").append(this.model.getState().get(Constants.TOMCAT_HOME)).append("\n");
		
		this.textPane.setText(sb.toString());
	}

}
