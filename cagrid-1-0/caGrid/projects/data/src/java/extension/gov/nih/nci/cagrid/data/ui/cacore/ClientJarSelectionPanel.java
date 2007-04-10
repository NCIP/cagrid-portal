package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.utilities.CastorMappingUtil;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

/** 
 *  ClientJarSelectionPanel
 *  Panel to select a client jar file and its dependencies
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Jan 8, 2007 
 * @version $Id: ClientJarSelectionPanel.java,v 1.5 2007-04-10 14:30:28 hastings Exp $ 
 */
public class ClientJarSelectionPanel extends AbstractWizardPanel {
	
	private JLabel qpJarLabel = null;
	private JTextField qpJarTextField = null;
	private JLabel clientJarLabel = null;
	private JTextField clientJarTextField = null;
	private JButton clientJarBrowseButton = null;
	private JLabel dependsLabel = null;
	private JList dependsList = null;
	private JScrollPane dependsScrollPane = null;
	private JButton addDependButton = null;
	private JButton removeDependButton = null;
	
	
	public ClientJarSelectionPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}
	

	private void initialize() {
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.anchor = GridBagConstraints.NORTH;
        gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints3.gridx = 2;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints1.weighty = 1.0D;
        gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints8.gridx = 2;
        gridBagConstraints8.gridy = 1;
        gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.gridy = 1;
        gridBagConstraints7.weightx = 1.0;
        gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.gridy = 1;
        gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.gridy = 0;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(452, 204));
        this.add(getQpJarLabel(), gridBagConstraints4);
        this.add(getQpJarTextField(), gridBagConstraints5);
        this.add(getClientJarLabel(), gridBagConstraints6);
        this.add(getClientJarTextField(), gridBagConstraints7);
        this.add(getClientJarBrowseButton(), gridBagConstraints8);
        this.add(getDependsLabel(), gridBagConstraints);
        this.add(getDependsScrollPane(), gridBagConstraints1);
        this.add(getAddDependButton(), gridBagConstraints2);
        this.add(getRemoveDependButton(), gridBagConstraints3);
	}


	public String getPanelShortName() {
		return "Jar Selection";
	}


	public String getPanelTitle() {
		return "Client jar and dependency selection"; 
	}


	public void update() {
		String clientJarName = getClientJarTextField().getText(); 
		setNextEnabled(clientJarName != null && clientJarName.length() != 0);
		// verify the sdk query library has been copied into the service
		File sdkQueryLib = null;
		String sdkVersion = (String) getBitBucket().get(CoreDsIntroPanel.CACORE_VERSION_PROPERTY);
		if (sdkVersion != null) {
			if (sdkVersion.equals(CoreDsIntroPanel.CACORE_31_VERSION)) {
				String sdkQueryLibName = new File(CoreDsIntroPanel.SDK_31_QUERY_LIB).getName();
				sdkQueryLib = new File(getServiceLibDir() + File.separator + sdkQueryLibName);
			} else if (sdkVersion.equals(CoreDsIntroPanel.CACORE_32_VERSION)) {
				String sdkQueryLibName = new File(CoreDsIntroPanel.SDK_32_QUERY_LIB).getName();
				sdkQueryLib = new File(getServiceLibDir() + File.separator + sdkQueryLibName);
			} else {
				ErrorDialog.showErrorDialog("No SDK version could be determined!");
			}
		}
		
		if (sdkQueryLib.exists()) {
			getQpJarTextField().setText(sdkQueryLib.getName());
		} else {
			getQpJarTextField().setText("ERROR: LIBRARY NOT FOUND");
		}
		// any 'additional libs' added previously should show up
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			AdditionalLibraries additionalLibs = data.getAdditionalLibraries();
			if (additionalLibs != null && additionalLibs.getJarName() != null) {
				String[] jarNames = additionalLibs.getJarName();
				Vector dependJars = new Vector();
				for (int i = 0; i < jarNames.length; i++) {
					if (!jarNames[i].equals(sdkQueryLib.getName()) 
						&& !jarNames[i].equals(getClientJarTextField().getText())) {
						dependJars.add(jarNames[i]);
					}
				}
				Collections.sort(dependJars);
				getDependsList().setListData(dependJars);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error updating UI", ex);
		}
	}


	/**
	 * This method initializes qpJarLabel
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getQpJarLabel() {
		if (qpJarLabel == null) {
			qpJarLabel = new JLabel();
			qpJarLabel.setText("Query Processor Jar:");
		}
		return qpJarLabel;
	}


	/**
	 * This method initializes qpJarTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getQpJarTextField() {
		if (qpJarTextField == null) {
			qpJarTextField = new JTextField();
			qpJarTextField.setToolTipText(
				"The jar file containing the SDK query processor.  This is not editable.");
			qpJarTextField.setEditable(false);
		}
		return qpJarTextField;
	}


	/**
	 * This method initializes clientJarLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getClientJarLabel() {
		if (clientJarLabel == null) {
			clientJarLabel = new JLabel();
			clientJarLabel.setText("Client Jar:");
		}
		return clientJarLabel;
	}


	/**
	 * This method initializes clientJarTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getClientJarTextField() {
		if (clientJarTextField == null) {
			clientJarTextField = new JTextField();
			clientJarTextField.setEditable(false);
			clientJarTextField.setToolTipText(
				"The client jar file for the SDK data source.");
		}
		return clientJarTextField;
	}


	/**
	 * This method initializes clientJarBrowseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClientJarBrowseButton() {
		if (clientJarBrowseButton == null) {
			clientJarBrowseButton = new JButton();
			clientJarBrowseButton.setText("Browse");
			clientJarBrowseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectClientJar();
				}
			});
		}
		return clientJarBrowseButton;
	}


	/**
	 * This method initializes dependsLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getDependsLabel() {
		if (dependsLabel == null) {
			dependsLabel = new JLabel();
			dependsLabel.setText("Dependencies:");
		}
		return dependsLabel;
	}


	/**
	 * This method initializes dependsList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getDependsList() {
		if (dependsList == null) {
			dependsList = new JList();
			dependsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return dependsList;
	}


	/**
	 * This method initializes dependsScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getDependsScrollPane() {
		if (dependsScrollPane == null) {
			dependsScrollPane = new JScrollPane();
			dependsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			dependsScrollPane.setViewportView(getDependsList());
		}
		return dependsScrollPane;
	}


	/**
	 * This method initializes addDependButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddDependButton() {
		if (addDependButton == null) {
			addDependButton = new JButton();
			addDependButton.setText("Add");
			addDependButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addDependJars();
				}
			});
		}
		return addDependButton;
	}


	/**
	 * This method initializes removeDependButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveDependButton() {
		if (removeDependButton == null) {
			removeDependButton = new JButton();
			removeDependButton.setText("Remove");
			removeDependButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					removeDependJars();
				}
			});
		}
		return removeDependButton;
	}
	
	
	private void selectClientJar() {
		String filename = null;
		try {
			filename = ResourceManager.promptFile(null, FileFilters.JAR_FILTER);
		} catch (IOException ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error selecting jar file", ex);
		}
		
		if (filename != null) {
			try {
				JarFile jar = new JarFile(filename);
				// verify the file is a client.jar with a castor mapping file
				StringBuffer mappingFile = JarUtilities.getFileContents(
					jar, DataServiceConstants.CACORE_CASTOR_MAPPING_FILE);
				if (mappingFile != null) {
					if (getClientJarTextField().getText().length() != 0) {
						// delete the old jar named in the client jar text field					
						File oldLib = new File(getServiceLibDir() + File.separator + getClientJarTextField().getText());
						oldLib.delete();
					}
					// copy the file into the service's lib directory
					File inLib = new File(filename);
					File outLib = new File(getServiceLibDir() + File.separator + inLib.getName());
					Utils.copyFile(inLib, outLib);
					// copy the mapping file to the service's source dir + base package name
					String mappingOut = CastorMappingUtil.getCustomCastorMappingFileName(getServiceInformation());
					Utils.stringBufferToFile(mappingFile, mappingOut);
					// set the text of the client jar field
					getClientJarTextField().setText(outLib.getName());
					setNextEnabled(true);
					storeLibrariesInExtensionData();
				} else {
					ErrorDialog.showErrorDialog("Selected file does not appear to be an SDK client");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				ErrorDialog.showErrorDialog("Error processing client jar!", ex);
			}
		}
	}
	
	
	private void addDependJars() {
		// get the jar selection
		String[] selection = null;
		try {
			selection = ResourceManager.promptMultiFiles(null, FileFilters.JAR_FILTER);
		} catch (IOException ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error selecting files", ex);
		}
		
		if (selection != null) {
			Vector allLibs = new Vector();
			for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
				allLibs.add(getDependsList().getModel().getElementAt(i));
			}
			for (int i = 0; i < selection.length; i++) {
				File inFile = new File(selection[i]);
				// ignore anything that is a repeat jar
				if (shouldAddJar(inFile.getName())) {
					File outFile = new File(getServiceLibDir() + File.separator + inFile.getName());
					try {
						Utils.copyFile(inFile, outFile);
						allLibs.add(outFile.getName());
					} catch (IOException ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error copying library " + inFile.getAbsolutePath(), ex);
					}					
				}
			}
			Collections.sort(allLibs);
			getDependsList().setListData(allLibs);
			storeLibrariesInExtensionData();
		}
	}
	
	
	private boolean shouldAddJar(String jarName) {
		Set usedNames = new HashSet();
		usedNames.add(getQpJarTextField().getText());
		usedNames.add(getClientJarTextField().getText());
		for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
			usedNames.add(getDependsList().getModel().getElementAt(i));
		}
		return !usedNames.contains(jarName);
	}
	
	
	private void removeDependJars() {
		// seperate wheat from the chaff
		Set deleteItems = new HashSet();
		Collections.addAll(deleteItems, getDependsList().getSelectedValues());
		Vector remainingItems = new Vector(getDependsList().getModel().getSize());
		for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
			String libName = (String) getDependsList().getModel().getElementAt(i);
			if (!deleteItems.contains(libName)) {
				remainingItems.add(libName);
			}
		}
		// gather the wheat into the barn
		Collections.sort(remainingItems);
		getDependsList().setListData(remainingItems);
		// burn the chaff in the firey furnace
		Iterator deleteItemIter = deleteItems.iterator();
		while (deleteItemIter.hasNext()) {
			File libFile = new File(getServiceLibDir() + File.separator + (String) deleteItemIter.next());
			libFile.delete();
		}
		storeLibrariesInExtensionData();
	}
	
	
	private void storeLibrariesInExtensionData() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			AdditionalLibraries libs = data.getAdditionalLibraries();
			if (libs == null) {
				libs = new AdditionalLibraries();
				data.setAdditionalLibraries(libs);
			}
			String[] jarNames = new String[getDependsList().getModel().getSize() + 2];
			for (int i = 0; i < getDependsList().getModel().getSize(); i++) {
				jarNames[i] = (String) getDependsList().getModel().getElementAt(i);
			}
			jarNames[jarNames.length - 2] = getQpJarTextField().getText();
			jarNames[jarNames.length - 1] = new File(getClientJarTextField().getText()).getName();
			libs.setJarName(jarNames);
			ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error storing jar information", ex);
		}

	}
	
	
	private String getServiceLibDir() {
		return CacoreWizardUtils.getServiceBaseDir(getServiceInformation()) + File.separator + "lib";
	}
}
