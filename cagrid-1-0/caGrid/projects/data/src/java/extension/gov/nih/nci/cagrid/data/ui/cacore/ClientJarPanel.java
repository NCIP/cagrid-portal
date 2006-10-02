package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfig;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfigProperty;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
	private JLabel serviceUrlLabel = null;
	private JTextField serviceUrlTextField = null;
	private JPanel urlPanel = null;
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
        this.add(getUrlPanel(), gridBagConstraints7);			
	}


	public void update() {
		// TODO Auto-generated method stub
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
					String lastDir = (String) getBitBucket().get(CacoreWizardUtils.LAST_DIRECTORY_KEY);
					JFileChooser chooser = new JFileChooser(lastDir);
					chooser.setFileFilter(FileFilters.JAR_FILTER);
					chooser.setMultiSelectionEnabled(true);
					int choice = chooser.showOpenDialog(ClientJarPanel.this);
					if (choice == JFileChooser.APPROVE_OPTION) {
						File[] selectedJars = chooser.getSelectedFiles();
						getBitBucket().put(CacoreWizardUtils.LAST_DIRECTORY_KEY, selectedJars[0].getAbsolutePath());
						// copy the libs in to the service dir
						String libDir = getServiceLibDir();
						String[] jarNames = new String[selectedJars.length];
						try {
							for (int i = 0; i < selectedJars.length; i++) {
								File outJarFile = new File(libDir + File.separator + selectedJars[i].getName());
								jarNames[i] = selectedJars[i].getName();
								Utils.copyFile(selectedJars[i], outJarFile);
							}
							Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
							AdditionalLibraries libs = data.getAdditionalLibraries();
							if (libs == null) {
								libs = new AdditionalLibraries();
							}
							String[] additionalLibNames = libs.getJarName();
							if (additionalLibNames == null) {
								additionalLibNames = jarNames;
							} else {
								additionalLibNames = (String[]) Utils.concatenateArrays(String.class, jarNames, additionalLibNames);
							}
							libs.setJarName(additionalLibNames);
							data.setAdditionalLibraries(libs);
							ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
							// sort the lib names and display them
							Arrays.sort(additionalLibNames);
							getAdditionalJarsList().setListData(additionalLibNames);
						} catch (Exception ex) {
							ex.printStackTrace();
							String[] error = {
								"Error copying jar file:",
								ex.getMessage()
							};
							JOptionPane.showMessageDialog(ClientJarPanel.this, error, "Error", JOptionPane.ERROR_MESSAGE);
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
						String[] error = {
							"Error removing libraries from service:",
							ex.getMessage()
						};
						JOptionPane.showMessageDialog(ClientJarPanel.this, error, "Error", JOptionPane.ERROR_MESSAGE);
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
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getServiceUrlLabel() {
		if (serviceUrlLabel == null) {
			serviceUrlLabel = new JLabel();
			serviceUrlLabel.setText("Service URL:");
		}
		return serviceUrlLabel;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getServiceUrlTextField() {
		if (serviceUrlTextField == null) {
			serviceUrlTextField = new JTextField();
			serviceUrlTextField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					changeAppServiceUrl();
				}

			    
			    public void removeUpdate(DocumentEvent e) {
			    	changeAppServiceUrl();
			    }
			    

			    public void changedUpdate(DocumentEvent e) {
			    	changeAppServiceUrl();
			    }
			});
		}
		return serviceUrlTextField;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getUrlPanel() {
		if (urlPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			urlPanel = new JPanel();
			urlPanel.setLayout(new GridBagLayout());
			urlPanel.add(getServiceUrlLabel(), gridBagConstraints);
			urlPanel.add(getServiceUrlTextField(), gridBagConstraints1);
		}
		return urlPanel;
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
	
	
	private void changeAppServiceUrl() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CQLProcessorConfig config = data.getCQLProcessorConfig();
			if (config == null) {
				config = new CQLProcessorConfig();
				data.setCQLProcessorConfig(config);
			}
			CQLProcessorConfigProperty[] props = config.getProperty();
			if (props == null || props.length == 0) {
				props = new CQLProcessorConfigProperty[] {
					new CQLProcessorConfigProperty("appserviceUrl", getServiceUrlTextField().getText())};
				config.setProperty(props);
			} else {
				for (int i = 0; i < props.length; i++) {
					if (props[i].getName().equals("appserviceUrl")) {
						props[i].setValue(getServiceUrlTextField().getText());
					}
				}
			}
			ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error setting the application service URL: " + ex.getMessage(), ex);
		}
	}
}
