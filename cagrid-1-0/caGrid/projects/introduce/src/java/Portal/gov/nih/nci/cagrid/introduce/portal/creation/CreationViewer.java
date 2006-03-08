package gov.nih.nci.cagrid.introduce.portal.creation;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.ModificationViewer;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;


/**
 * CreationViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class CreationViewer extends GridPortalComponent {

	private static String DEFAULT_NAME = "HelloWorld";

	private static String DEFAULT_JAVA_PACKAGE = "gov.nih.nci.cagrid";

	private static String DEFAULT_NAMESPACE = "http://cagrid.nci.nih.gov";

	private JPanel inputPanel = null;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JButton createButton = null;

	private JLabel serviceLabel = null;

	private JTextField service = null;

	private JLabel destinationLabel = null;

	private JTextField dir = null;

	private JButton dirButton = null;

	private JLabel packageLabel = null;

	private JTextField servicePackage = null;

	private JLabel namespaceLabel = null;

	private JTextField namespaceDomain = null;

	private JButton closeButton = null;

	private JTextField serviceTemplate = null;

	private JButton serviceTemplateButton = null;

	private JPanel templatePanel = null;

	private JLabel serviceTemplateJLabel = null;

	private JLabel serviceStyleLabel = null;

	private JComboBox serviceStyleSeletor = null;

	private Component me;


	/**
	 * This method initializes
	 */
	public CreationViewer() {
		super();
		me = this;
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getMainPanel());
		this.setSize(469, 446);
		this.setFrameIcon(IntroduceLookAndFeel.getCreateIcon());
		this.setTitle("Create Grid Service");

	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInputPanel() {
		if (inputPanel == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 5;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.gridwidth = 2;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 5;
			serviceStyleLabel = new JLabel();
			serviceStyleLabel.setText("Service Style");
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints19.gridy = 7;
			gridBagConstraints19.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints19.weightx = 1.0D;
			gridBagConstraints19.weighty = 1.0D;
			gridBagConstraints19.gridwidth = 3;
			gridBagConstraints19.gridx = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 4;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridwidth = 2;
			gridBagConstraints12.weighty = 1.0D;
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridy = 4;
			gridBagConstraints11.insets = new java.awt.Insets(0, 0, 0, 0);
			gridBagConstraints11.gridx = 0;
			namespaceLabel = new JLabel();
			namespaceLabel.setText("Namespace Domain");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 3;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridwidth = 2;
			gridBagConstraints10.weighty = 1.0D;
			gridBagConstraints10.gridx = 1;
			inputPanel = new JPanel();
			inputPanel.setLayout(new GridBagLayout());
			inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Create Grid Service",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			packageLabel = new JLabel();
			packageLabel.setText("Package");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 3;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.gridwidth = 2;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 0;
			destinationLabel = new JLabel();
			destinationLabel.setText("Creation Directory");
			destinationLabel.setName("Destination Directory");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridwidth = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.weighty = 1.0D;
			gridBagConstraints2.weightx = 1.0;
			inputPanel.add(destinationLabel, gridBagConstraints5);
			inputPanel.add(getDirButton(), gridBagConstraints6);
			inputPanel.add(packageLabel, gridBagConstraints7);
			serviceLabel = new JLabel();
			serviceLabel.setText("Service Name");
			inputPanel.add(getService(), gridBagConstraints2);
			inputPanel.add(getDir(), gridBagConstraints3);
			inputPanel.add(getServicePackage(), gridBagConstraints8);
			inputPanel.add(getNamespaceDomain(), gridBagConstraints12);
			inputPanel.add(serviceLabel, gridBagConstraints4);
			inputPanel.add(namespaceLabel, gridBagConstraints11);
			inputPanel.add(getTemplatePanel(), gridBagConstraints19);
			inputPanel.add(serviceStyleLabel, gridBagConstraints13);
			inputPanel.add(getServiceStyleSeletor(), gridBagConstraints16);
			serviceStyleLabel.setEnabled(false);
		}
		return inputPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints1.gridheight = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridwidth = 1;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getInputPanel(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints1);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getCreateButton(), null);
			buttonPanel.add(getCloseButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCreateButton() {
		if (createButton == null) {
			createButton = new JButton();
			createButton.setText("Create");
			createButton.setIcon(IntroduceLookAndFeel.getCreateIcon());
			createButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					int doIdeleteResult = JOptionPane.OK_OPTION;
					final File dirFile = new File(getDir().getText());
					if (dirFile.exists() && dirFile.list().length != 0) {
						doIdeleteResult = JOptionPane.showConfirmDialog(me,
							"The creation directory is not empty.  All information in the directory will be lost.");
					}

					if (doIdeleteResult == JOptionPane.OK_OPTION) {
						final int finalDoIdeleteResult = doIdeleteResult;
						BusyDialogRunnable r = new BusyDialogRunnable(PortalResourceManager.getInstance()
							.getGridPortal(), "Creating") {

							public void process() {
								try {
									if (finalDoIdeleteResult == JOptionPane.OK_OPTION) {
										if (dirFile.exists()) {
											setProgressText("deleting existing directory");
											boolean deleted = Utils.deleteDir(dirFile);
											if (!deleted) {
												JOptionPane.showMessageDialog(CreationViewer.this,
													"Unable to delete creation directory");
												return;
											}
										}
									} else {
										return;
									}

									setProgressText("caching directory location");

									setProgressText("validating");
									if (service.getText().length() > 0) {
										if (!service.getText().matches("[A-Z]++[A-Za-z0-9\\_\\$]*")) {
											PortalUtils
												.showMessage("Service Name can only contain [A-Z]++[A-Za-z0-9\\_\\$]*");
											return;
										}
										if (service.getText().substring(0, 1).toLowerCase().equals(
											service.getText().substring(0, 1))) {
											PortalUtils
												.showMessage("Service Name cannnot start with lower case letters.");
											return;
										}
									} else {
										PortalUtils.showMessage("Service Name cannot be empty.");
										return;
									}
									setProgressText("creating");
									String cmd = CommonTools.getAntSkeletonCreationCommand(".", service.getText(), dir
										.getText(), servicePackage.getText(), namespaceDomain.getText());
									Process p = CommonTools.createAndOutputProcess(cmd);
									p.waitFor();
									if (p.exitValue() != 0) {
										PortalUtils.showErrorMessage("Error creating new service!");
									}
									if (serviceTemplate.getText().length() > 0) {
										if (serviceTemplate.getText().length() > 0) {
											StringBuffer file = Utils.fileToStringBuffer(new File(serviceTemplate
												.getText()));
											Utils.stringBufferToFile(file, dir.getText() + File.separator
												+ "introduce.xml");
										}

										setProgressText("resynchronizing using templates");
										SyncTools sync = new SyncTools(new File(dir.getText()));
										sync.sync();
									}
									setProgressText("building");
									cmd = CommonTools.getAntAllCommand(dir.getText());
									p = CommonTools.createAndOutputProcess(cmd);
									p.waitFor();
									if (p.exitValue() == 0) {
										PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
											new ModificationViewer(new File(dir.getText())));
										dispose();
									} else {
										PortalUtils.showErrorMessage("Error creating new service!");
									}
									
									setProgressText("purging old archives");
									ResourceManager.purgeArchives(service.getText());
									
									//create the archive
									long id = System.currentTimeMillis();
									Properties props = new Properties();
									props.load(new FileInputStream(dir.getText() + File.separator + "introduce.properties"));
									props.setProperty("introduce.skeleton.timestamp", String.valueOf(id));
									props.store(
										new FileOutputStream(dir.getText() + File.separator + "introduce.properties"),
										"Introduce Properties");
									setProgressText("creating a new archive");
									ResourceManager.createArchive(String.valueOf(id), service.getText(), dir.getText());
									
								} catch (Exception ex) {
									ex.printStackTrace();
									PortalUtils.showErrorMessage(ex.getMessage());
									dispose();
								}

							}

						};

						Thread th = new Thread(r);
						th.start();
					}
				}
			});
		}

		return createButton;
	}


	/**
	 * This method initializes service
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getService() {
		if (service == null) {
			service = new JTextField();
			service.setText(DEFAULT_NAME);
		}
		return service;
	}


	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDir() {
		if (dir == null) {
			dir = new JTextField();
			dir.setText(DEFAULT_NAME);
		}
		return dir;
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDirButton() {
		if (dirButton == null) {
			dirButton = new JButton();
			dirButton.setText("Browse");
			dirButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						String previous = dir.getText();
						String location = ResourceManager.promptDir(me, previous);
						if (location != null && location.length() > 0) {
							dir.setText(location);
						} else {
							dir.setText(previous);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
		return dirButton;
	}


	/**
	 * This method initializes servicePackage
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getServicePackage() {
		if (servicePackage == null) {
			servicePackage = new JTextField();
			servicePackage.setText((DEFAULT_JAVA_PACKAGE + "." + DEFAULT_NAME).toLowerCase());
		}
		return servicePackage;
	}


	/**
	 * This method initializes namespaceDomain
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNamespaceDomain() {
		if (namespaceDomain == null) {
			namespaceDomain = new JTextField();
			namespaceDomain.setText(DEFAULT_NAMESPACE);
		}
		return namespaceDomain;
	}


	private String promptFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select Attribute File");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return "";
	}


	/**
	 * This method initializes closeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setIcon(IntroduceLookAndFeel.getCloseIcon());
			closeButton.setText("Cancel");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return closeButton;
	}


	/**
	 * This method initializes methodsTemplateFile
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getMethodsTemplateFile() {
		if (serviceTemplate == null) {
			serviceTemplate = new JTextField();
			serviceTemplate.setEditable(false);
			serviceTemplate.setEnabled(false);
		}
		return serviceTemplate;
	}


	/**
	 * This method initializes methodsTemplateButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getServiceTemplateButton() {
		if (serviceTemplateButton == null) {
			serviceTemplateButton = new JButton();
			serviceTemplateButton.setText("Browse");
			serviceTemplateButton.setEnabled(false);
			serviceTemplateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					serviceTemplate.setText(promptFile());
				}
			});
		}
		return serviceTemplateButton;
	}


	/**
	 * This method initializes templatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTemplatePanel() {
		if (templatePanel == null) {
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.gridy = 0;
			serviceTemplateJLabel = new JLabel();
			serviceTemplateJLabel.setText("Service Template File");
			serviceTemplateJLabel.setEnabled(false);
			serviceTemplateJLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.gridx = 3;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14.gridx = 2;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			templatePanel = new JPanel();
			templatePanel.setLayout(new GridBagLayout());
			templatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Service Template Options",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			templatePanel.setEnabled(false);
			templatePanel.add(getMethodsTemplateFile(), gridBagConstraints14);
			templatePanel.add(getServiceTemplateButton(), gridBagConstraints15);
			templatePanel.add(serviceTemplateJLabel, gridBagConstraints20);
		}
		return templatePanel;
	}


	/**
	 * This method initializes serviceStyleSeletor
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getServiceStyleSeletor() {
		if (serviceStyleSeletor == null) {
			serviceStyleSeletor = new JComboBox();
			serviceStyleSeletor.setEnabled(false);
			serviceStyleSeletor.addItem("NONE");
			serviceStyleSeletor.addItem("ANALYTICAL");
			serviceStyleSeletor.addItem("DATA");
			serviceStyleSeletor.addItem("CUSTOM");
			serviceStyleSeletor.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (serviceStyleSeletor.getSelectedItem().equals("CUSTOM")) {
						serviceTemplateJLabel.setEnabled(true);
						serviceTemplateButton.setEnabled(true);
					} else {
						serviceTemplateJLabel.setEnabled(false);
						serviceTemplateButton.setEnabled(false);
						if (serviceStyleSeletor.getSelectedItem().equals("ANALYTICAL")) {
							serviceTemplate.setText("templates" + File.separator + "analyticalIntroduce.xml");
						} else if (serviceStyleSeletor.getSelectedItem().equals("DATA")) {
							serviceTemplate.setText("templates" + File.separator + "dataIntroduce.xml");
						} else {
							serviceTemplate.setText("");
						}
					}
				}
			});
		}
		return serviceStyleSeletor;
	}


	public static void main(String[] args) {
		System.out.println();

	}

} // @jve:decl-index=0:visual-constraint="10,4"
