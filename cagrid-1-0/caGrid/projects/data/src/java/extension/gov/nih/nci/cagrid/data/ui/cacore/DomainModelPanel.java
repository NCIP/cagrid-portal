package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.ui.types.NamespaceUtils;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/** 
 *  DomainModelPanel
 *  Panel to allow selection / generation of a domain model for the service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 25, 2006 
 * @version $Id$ 
 */
public class DomainModelPanel extends AbstractWizardPanel {
	
	private static Project lastSelectedProject = null;

	private JRadioButton noDomainModelRadioButton = null;
	private JRadioButton fromFileRadioButton = null;
	private JRadioButton fromCaDsrRadioButton = null;
	private JPanel dmSourcePanel = null;
	private JLabel fileLabel = null;
	private JTextField fileTextField = null;
	private JButton browseButton = null;
	private JPanel dmFilePanel = null;
	private CaDSRBrowserPanel caDsrBrowser = null;
	private JList selectedPackagesList = null;
	private JScrollPane selectedPackagesScrollPane = null;
	private JButton addPackageButton = null;
	private JButton removePackageButton = null;
	private JPanel packageButtonsPanel = null;
	private JPanel caDsrPanel = null;
	
	public DomainModelPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}


	public void update() {
		
	}


	public String getPanelTitle() {
		return "Domain Model Selection";
	}

	public String getPanelShortName() {
		return "Model";
	}
	
	
	private void initialize() {
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.gridx = 0;
        gridBagConstraints13.gridwidth = 2;
        gridBagConstraints13.weightx = 1.0D;
        gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints13.gridy = 1;
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.gridx = 1;
        gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints12.gridy = 0;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(623,298));
        this.add(getDmSourcePanel(), gridBagConstraints11);
        this.add(getDmFilePanel(), gridBagConstraints12);
        this.add(getCaDsrPanel(), gridBagConstraints13);		
	}


	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getNoDomainModelRadioButton() {
		if (noDomainModelRadioButton == null) {
			noDomainModelRadioButton = new JRadioButton();
			noDomainModelRadioButton.setText("Use No Domain Model");
			noDomainModelRadioButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (noDomainModelRadioButton.isSelected()) {
						CacoreWizardUtils.setContainerEnabled(getDmFilePanel(), false);
						CacoreWizardUtils.setContainerEnabled(getCaDsrPanel(), false);
					}
				}
			});
		}
		return noDomainModelRadioButton;
	}


	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getFromFileRadioButton() {
		if (fromFileRadioButton == null) {
			fromFileRadioButton = new JRadioButton();
			fromFileRadioButton.setText("Domain Model From File");
			fromFileRadioButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (fromFileRadioButton.isSelected()) {
						CacoreWizardUtils.setContainerEnabled(getDmFilePanel(), true);
						CacoreWizardUtils.setContainerEnabled(getCaDsrPanel(), false);
					}
				}
			});
		}
		return fromFileRadioButton;
	}


	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getFromCaDsrRadioButton() {
		if (fromCaDsrRadioButton == null) {
			fromCaDsrRadioButton = new JRadioButton();
			fromCaDsrRadioButton.setText("Generate From caDSR");
			fromCaDsrRadioButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (fromCaDsrRadioButton.isSelected()) {
						CacoreWizardUtils.setContainerEnabled(getDmFilePanel(), false);
						CacoreWizardUtils.setContainerEnabled(getCaDsrPanel(), true);
					}
				}
			});
		}
		return fromCaDsrRadioButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDmSourcePanel() {
		if (dmSourcePanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			dmSourcePanel = new JPanel();
			dmSourcePanel.setLayout(new GridBagLayout());
			dmSourcePanel.add(getNoDomainModelRadioButton(), gridBagConstraints);
			dmSourcePanel.add(getFromFileRadioButton(), gridBagConstraints1);
			dmSourcePanel.add(getFromCaDsrRadioButton(), gridBagConstraints2);
			ButtonGroup group = new ButtonGroup();
			group.add(getNoDomainModelRadioButton());
			group.add(getFromFileRadioButton());
			group.add(getFromCaDsrRadioButton());
			group.setSelected(getNoDomainModelRadioButton().getModel(), true);
		}
		return dmSourcePanel;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getFileLabel() {
		if (fileLabel == null) {
			fileLabel = new JLabel();
			fileLabel.setText("Domain Model File:");
		}
		return fileLabel;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFileTextField() {
		if (fileTextField == null) {
			fileTextField = new JTextField();
		}
		return fileTextField;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBrowseButton() {
		if (browseButton == null) {
			browseButton = new JButton();
			browseButton.setText("Browse");
			browseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String lastDir = (String) getBitBucket().get(CacoreWizardUtils.LAST_DIRECTORY_KEY);
					JFileChooser chooser = new JFileChooser(lastDir);
					chooser.setFileFilter(FileFilters.XML_FILTER);
					chooser.setMultiSelectionEnabled(false);
					int choice = chooser.showOpenDialog(DomainModelPanel.this);
					if (choice == JFileChooser.APPROVE_OPTION) {
						File selectedFile = chooser.getSelectedFile();
						getBitBucket().put(CacoreWizardUtils.LAST_DIRECTORY_KEY, selectedFile.getAbsolutePath());
						String serviceEtcDir = CacoreWizardUtils.getServiceLibDir(getServiceInformation()) + File.separator + "etc";
						File outputFile = new File(serviceEtcDir + File.separator + selectedFile.getName());
						try {
							Utils.copyFile(selectedFile, outputFile);
							Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
							CadsrInformation info = data.getCadsrInformation();
							if (info == null) {
								info = new CadsrInformation();
							}
							info.setSuppliedDomainModel(outputFile.getName());
							data.setCadsrInformation(info);
							ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
						} catch (Exception ex) {
							ex.printStackTrace();
							String[] error = {
								"Error copying selected file to service:",
								ex.getMessage()
							};
							JOptionPane.showMessageDialog(DomainModelPanel.this, error, "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return browseButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDmFilePanel() {
		if (dmFilePanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 0;
			dmFilePanel = new JPanel();
			dmFilePanel.setLayout(new GridBagLayout());
			dmFilePanel.add(getFileLabel(), gridBagConstraints3);
			dmFilePanel.add(getFileTextField(), gridBagConstraints4);
			dmFilePanel.add(getBrowseButton(), gridBagConstraints5);
		}
		return dmFilePanel;
	}
	
	
	private CaDSRBrowserPanel getCaDsrBrowser() {
		if (caDsrBrowser == null) {
			caDsrBrowser = new CaDSRBrowserPanel(true, false);
		}
		return caDsrBrowser;
	}


	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getSelectedPackagesList() {
		if (selectedPackagesList == null) {
			selectedPackagesList = new JList();
		}
		return selectedPackagesList;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSelectedPackagesScrollPane() {
		if (selectedPackagesScrollPane == null) {
			selectedPackagesScrollPane = new JScrollPane();
			selectedPackagesScrollPane.setViewportView(getSelectedPackagesList());
			selectedPackagesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Selected Packages", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		}
		return selectedPackagesScrollPane;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddPackageButton() {
		if (addPackageButton == null) {
			addPackageButton = new JButton();
			addPackageButton.setText("Add Package");
			addPackageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// get the selected package
					Project proj = getCaDsrBrowser().getSelectedProject();
					if (lastSelectedProject != null) {
						// verify the project is still the same
						if (!lastSelectedProject.getLongName().equals(proj.getLongName()) ||
							!lastSelectedProject.getVersion().equals(proj.getVersion())) {
							// they don't match...
							String[] message = {
								"The selected project is not the same as the project",
								"to which the previously selected packages belong.",
								"To use this project, all currently selected packages",
								"must be removed from the domain model.",
								"Procede?"
							};
							int choice = JOptionPane.showConfirmDialog(
								DomainModelPanel.this, message, "Project Conflict", JOptionPane.YES_NO_OPTION);
							if (choice == JOptionPane.YES_OPTION) {
								// remove all packages from list, set the last selected project
											
							} else {
								// user selected no, so bail out
								return;
							}
						}
					}
					lastSelectedProject = proj;
					UMLPackageMetadata pack = getCaDsrBrowser().getSelectedPackage();
					addUmlPackage(pack);
				}
			});
		}
		return addPackageButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemovePackageButton() {
		if (removePackageButton == null) {
			removePackageButton = new JButton();
			removePackageButton.setText("Remove");
			removePackageButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String[] names = (String[]) getSelectedPackagesList().getSelectedValues();
					removeUmlPackages(names);
				}
			});
		}
		return removePackageButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPackageButtonsPanel() {
		if (packageButtonsPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.gridy = 0;
			packageButtonsPanel = new JPanel();
			packageButtonsPanel.setLayout(new GridBagLayout());
			packageButtonsPanel.add(getAddPackageButton(), gridBagConstraints6);
			packageButtonsPanel.add(getRemovePackageButton(), gridBagConstraints7);
		}
		return packageButtonsPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCaDsrPanel() {
		if (caDsrPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.weighty = 1.0;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridheight = 2;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 0;
			caDsrPanel = new JPanel();
			caDsrPanel.setLayout(new GridBagLayout());
			caDsrPanel.add(getCaDsrBrowser(), gridBagConstraints8);
			caDsrPanel.add(getPackageButtonsPanel(), gridBagConstraints9);
			caDsrPanel.add(getSelectedPackagesScrollPane(), gridBagConstraints10);
		}
		return caDsrPanel;
	}
	
	
	private void addUmlPackage(UMLPackageMetadata pack) {
		// add the package name to the list
		String[] names = new String[getSelectedPackagesList().getModel().getSize() + 1];
		for (int i = 0; i < getSelectedPackagesList().getModel().getSize(); i++) {
			names[i] = (String) getSelectedPackagesList().getModel().getElementAt(i);
		}
		names[names.length - 1] = pack.getName();
		getSelectedPackagesList().setListData(names);
		// add the package to the cadsr information in extension data
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CadsrInformation info = data.getCadsrInformation();
			if (info == null) {
				info = new CadsrInformation();
			}
			// create cadsr package for the new metadata package
			CadsrPackage newPackage = new CadsrPackage();
			newPackage.setName(pack.getName());
			newPackage.setSelectedClass(getClassNamesFromPackage(lastSelectedProject, pack));
			newPackage.setMappedNamespace(NamespaceUtils.createNamespaceString(lastSelectedProject, pack));			
			CadsrPackage[] packages = info.getPackages();
			if (packages == null) {
				packages = new CadsrPackage[] {newPackage};
			} else {
				packages = (CadsrPackage[]) Utils.appendToArray(packages, newPackage);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			String[] error = {
				"Error storing the new package information:",
				ex.getMessage()
			};
			JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private String[] getClassNamesFromPackage(Project proj, UMLPackageMetadata pack) throws Exception {
		CaDSRServiceClient client = new CaDSRServiceClient(getCaDsrBrowser().getCadsr().getText());
		UMLClassMetadata[] classMdArray = client.findClassesInPackage(proj, pack.getName());
		String[] names = new String[classMdArray.length];
		for (int i = 0; i < classMdArray.length; i++) {
			names[i] = classMdArray[i].getName();
		}
		return names;
	}
	
	
	private void removeUmlPackages(String[] packageNames) {
		// change the gui
		Set selected = new HashSet();
		Collections.addAll(selected, packageNames);
		Vector remaining = new Vector();
		for (int i = 0; i < getSelectedPackagesList().getModel().getSize(); i++) {
			String name = (String) getSelectedPackagesList().getModel().getElementAt(i);
			if (!selected.contains(name)) {
				remaining.add(name);
			}
		}
		getSelectedPackagesList().setListData(remaining);
		// if everything has been removed, also remove the last selected project
		if (remaining.size() == 0) {
			lastSelectedProject = null;
		}
		// change the data model
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CadsrInformation information = data.getCadsrInformation();
			CadsrPackage[] packages = information.getPackages();
			List remainingPackages = new ArrayList();
			for (int i = 0; i < packages.length; i++) {
				if (!selected.contains(packages[i].getName())) {
					remainingPackages.add(packages[i]);
				}
			}
			CadsrPackage[] remainingPackagesArray = new CadsrPackage[remainingPackages.size()];
			remainingPackages.toArray(remainingPackagesArray);
			information.setPackages(remainingPackagesArray);
			ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
		} catch (Exception ex) {
			ex.printStackTrace();
			String[] error = {
				"Error removing the selected packages from the model:",
				ex.getMessage()
			};
			JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
