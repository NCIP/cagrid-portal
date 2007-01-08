package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.JarUtilities;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.utilities.CastorMappingUtil;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/** 
 *  ClientJarPanel
 *  Panel asking the user to supply a client.jar for their caCORE SDK data source,
 *  and providing an oppertunity to add any additional jars they may need.
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 25, 2006 
 * @version $Id$ 
 */
public class ClientJarPanel extends AbstractWizardPanel {

	private JList additionalJarsList = null;
	private JScrollPane additionalJarsScrollPane = null;
	private JButton addJarButton = null;
	private JButton removeButton = null;
	private JPanel additionalJarButtonsPanel = null;
	private JPanel requiredJarsPanel = null;
	
	public ClientJarPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}
	
	
	private void initialize() {
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.weightx = 1.0D;
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.gridy = 1;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints2.weightx = 1.0D;
        gridBagConstraints2.weighty = 1.0D;
        gridBagConstraints2.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(351,220));
        this.add(getRequiredJarsPanel(), gridBagConstraints2);
	}


	public void update() {
		// load any existing additional jars into the list
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			AdditionalLibraries libs = data.getAdditionalLibraries();
			if (libs != null && libs.getJarName() != null) {
				getAdditionalJarsList().setListData(libs.getJarName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error loading extension data", ex);
		}
		// see if the castor mapping file exists
		File castorMapping = new File(CastorMappingUtil.getCustomCastorMappingFileName(getServiceInformation()));
		setNextEnabled(castorMapping.exists());
	}


	public String getPanelTitle() {
		return "Select caCORE SDK client.jar";
	}


	public String getPanelShortName() {
		return "client.jar";
	}


	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getAdditionalJarsList() {
		if (additionalJarsList == null) {
			additionalJarsList = new JList();
		}
		return additionalJarsList;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getAdditionalJarsScrollPane() {
		if (additionalJarsScrollPane == null) {
			additionalJarsScrollPane = new JScrollPane();
			additionalJarsScrollPane.setViewportView(getAdditionalJarsList());
			additionalJarsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return additionalJarsScrollPane;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddJarButton() {
		if (addJarButton == null) {
			addJarButton = new JButton();
			addJarButton.setText("Add Jars");
			addJarButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String[] selectedJarNames = null;
					try {
						selectedJarNames = ResourceManager.promptMultiFiles(
							ClientJarPanel.this, null, FileFilters.JAR_FILTER);
					} catch (IOException ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error getting filenames", ex);
					}
					if (selectedJarNames != null) {
						// copy the libs in to the service dir
						String libDir = getServiceLibDir();
						String[] jarNames = new String[selectedJarNames.length];
						try {
							for (int i = 0; i < selectedJarNames.length; i++) {
								File inJarFile = new File(selectedJarNames[i]);
								File outJarFile = new File(libDir + File.separator + inJarFile.getName());
								jarNames[i] = inJarFile.getName();
								Utils.copyFile(inJarFile, outJarFile);
							}
							// attempt to extract a castor mapping file
							extractCastorMapping(selectedJarNames);
							// add the new libraries to the service extension data
							Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
							AdditionalLibraries libs = data.getAdditionalLibraries();
							if (libs == null) {
								libs = new AdditionalLibraries();
							}
							String[] additionalLibNames = libs.getJarName();
							if (additionalLibNames == null) {
								additionalLibNames = jarNames;
							} else {
								additionalLibNames = (String[]) Utils.concatenateArrays(
									String.class, jarNames, additionalLibNames);
							}
							libs.setJarName(additionalLibNames);
							data.setAdditionalLibraries(libs);
							ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
							// sort the lib names and display them
							Arrays.sort(additionalLibNames);
							getAdditionalJarsList().setListData(additionalLibNames);
						} catch (Exception ex) {
							ex.printStackTrace();
							ErrorDialog.showErrorDialog("Error copying jar file", ex);
						}
					}
				}
			});
		}
		return addJarButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveButton() {
		if (removeButton == null) {
			removeButton = new JButton();
			removeButton.setText("Remove");
			removeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// get the selected libraries
					Set selectedLibs = new HashSet();
					Collections.addAll(selectedLibs, getAdditionalJarsList().getSelectedValues());
					if (selectedLibs.size() == 0) {
						JOptionPane.showMessageDialog(ClientJarPanel.this, "Please select libraries to remove.");
						return;
					}
					// delete the libraries from the file system
					String libDir = getServiceLibDir();
					Iterator selectedIter = selectedLibs.iterator();
					while (selectedIter.hasNext()) {
						File delme = new File(libDir + File.separator + selectedIter.next().toString());
						delme.delete();
					}
					// make a list of libraries to keep
					String[] cleanedLibs = 
						new String[getAdditionalJarsList().getModel().getSize() - selectedLibs.size()];
					int cleanIndex = 0;
					for (int i = 0; i < getAdditionalJarsList().getModel().getSize(); i++) {
						String name = (String) getAdditionalJarsList().getModel().getElementAt(i);
						if (!selectedLibs.contains(name)) {
							cleanedLibs[cleanIndex] = name;
							cleanIndex++;
						}
					}
					// show the kept libraries in the gui
					getAdditionalJarsList().setListData(cleanedLibs);
					try {
						// store the new jars information in the service extension data
						Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
						AdditionalLibraries libs = data.getAdditionalLibraries();
						if (libs == null) {
							libs = new AdditionalLibraries();
						}
						libs.setJarName(cleanedLibs);
						data.setAdditionalLibraries(libs);
						ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
					} catch (Exception ex) {
						ErrorDialog.showErrorDialog("Error removing libraries from service", ex);
					}					
				}
			});
		}
		return removeButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAdditionalJarButtonsPanel() {
		if (additionalJarButtonsPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridy = 0;
			additionalJarButtonsPanel = new JPanel();
			additionalJarButtonsPanel.setLayout(new GridBagLayout());
			additionalJarButtonsPanel.add(getAddJarButton(), gridBagConstraints4);
			additionalJarButtonsPanel.add(getRemoveButton(), gridBagConstraints5);
		}
		return additionalJarButtonsPanel;
	}
	
	
	private String getServiceLibDir() {
		return CacoreWizardUtils.getServiceBaseDir(getServiceInformation()) + File.separator + "lib";
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRequiredJarsPanel() {
		if (requiredJarsPanel == null) {
			requiredJarsPanel = new JPanel();
			requiredJarsPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints6.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.gridx = 0;
			requiredJarsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Required Jar Files", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			requiredJarsPanel.add(getAdditionalJarsScrollPane(), gridBagConstraints3);
			requiredJarsPanel.add(getAdditionalJarButtonsPanel(), gridBagConstraints6);
		}
		return requiredJarsPanel;
	}
	
	
	private void extractCastorMapping(String[] jarNames) throws IOException {
		for (int i = 0; i < jarNames.length; i++) {
			JarFile jarFile = new JarFile(jarNames[i]);
			StringBuffer mappingFile = JarUtilities.getFileContents(
				jarFile, DataServiceConstants.CACORE_CASTOR_MAPPING_FILE);
			if (mappingFile != null) {
				// copy the mapping file to the service's source dir + base package name
				String mappingOut = CastorMappingUtil.getCustomCastorMappingFileName(getServiceInformation());
				Utils.stringBufferToFile(mappingFile, mappingOut);
				setNextEnabled(true);
				break;
			}
		}
	}
}
