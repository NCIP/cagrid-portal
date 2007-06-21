/**
 * 
 */
package org.cagrid.installer.steps;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JTextPane;

import org.pietschy.wizard.PanelWizardStep;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DisplayMessageStep extends PanelWizardStep {
	
	public DisplayMessageStep(String name, String description, String message){
		super(name, description);
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(263, 161));
		
		JTextPane textPane = new JTextPane();
		textPane.setText(message);
		this.add(textPane, gridBagConstraints);
		
		setComplete(true);
	}

}
