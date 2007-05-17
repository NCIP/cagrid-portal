package org.cagrid.installer.steps;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;

import org.pietschy.wizard.PanelWizardStep;

public class PathSelectorStep extends PanelWizardStep {

	
	private Map<String,JCheckBox> checkBoxes = new LinkedHashMap<String,JCheckBox>();
	

	/**
	 * This method initializes 
	 * 
	 */
	public PathSelectorStep(List<String> paths) {
		super();
		
		int gridX = 0;
		int gridY = 0;
		setLayout(new GridBagLayout());
		setSize(new Dimension(getWidth(), getHeight()));
		for(String path : paths){
			JCheckBox checkBox = newJCheckBox(path);
			this.checkBoxes.put(path, checkBox);
			GridBagConstraints cons = new GridBagConstraints();
			cons.gridx = gridX;
			cons.gridy = gridY++;
			add(checkBox, cons);
		}
		
		this.setComplete(true);
	}
	
	protected JCheckBox newJCheckBox(String pathName) {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setText(pathName);
		checkBox.setSelected(false);
		return checkBox;
	}
	
	public boolean isPathSelected(String pathName){

		JCheckBox checkBox = this.checkBoxes.get(pathName);
		if(checkBox == null){
			throw new IllegalArgumentException("No checkbox found for path '" + pathName + "'");
		}
		System.out.println("Is " + pathName + " selected? " + checkBox.isSelected());
		return checkBox.isSelected();
	}



}
