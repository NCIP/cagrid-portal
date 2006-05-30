package gov.nih.nci.cagrid.introduce.portal.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.ModificationViewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

	public static final String SCHEMA_DIR = "schema";

	private static String DEFAULT_NAME = "HelloWorld";

	private static String DEFAULT_JAVA_PACKAGE = "gov.nih.nci.cagrid";

	private static String DEFAULT_NAMESPACE = "http://cagrid.nci.nih.gov/HelloWorld";

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

	private JComboBox serviceStyleSeletor = null;

	private JPanel extensionsPanel = null;

	private JButton addExtensionButton = null;

	private JButton removeExtensionButton = null;

	private JScrollPane extensionsScrollPane = null;

	private ExtensionsTable extensionsTable = null;

	private JPanel extensionsTablePanel = null;

	private JLabel upExtensionLabel = null;

	private JLabel downExtensionLabel = null;

	public CreationViewer() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getMainPanel());
		this.setContentPane(getMainPanel());
		this.setFrameIcon(IntroduceLookAndFeel.getCreateServiceIcon());
		this.setTitle("Create Grid Service");
	}

	/**
	 * This method initializes jPanel
	 */
	private JPanel getInputPanel() {
		if (inputPanel == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 5;
			gridBagConstraints17.weightx = 0.0;
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints17.gridwidth = 3;
			gridBagConstraints17.weighty = 1.0D;
			gridBagConstraints17.gridx = 0;
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
			namespaceLabel.setText("Namespace");
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
			inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Create Grid Service",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					PortalLookAndFeel.getPanelLabelColor()));
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
			inputPanel.add(getExtensionsPanel(), gridBagConstraints17);
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
			createButton.setIcon(IntroduceLookAndFeel.getCreateServiceIcon());
			createButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					createService();
				}
			});
		}

		return createButton;
	}

	/**
	 * This method initializes serviceStyleSeletor
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getServiceStyleSeletor() {
		if (serviceStyleSeletor == null) {
			serviceStyleSeletor = new JComboBox();
			serviceStyleSeletor.addItem("NONE");

			List extensionDescriptors = ExtensionsLoader.getInstance()
					.getServiceExtensions();
			for (int i = 0; i < extensionDescriptors.size(); i++) {
				ServiceExtensionDescriptionType ex = (ServiceExtensionDescriptionType) extensionDescriptors
						.get(i);
				serviceStyleSeletor.addItem(ex.getDisplayName());
			}
		}
		return serviceStyleSeletor;
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
						String previous = getDir().getText();
						String location = ResourceManager.promptDir(
								CreationViewer.this, previous);
						if (location != null && location.length() > 0) {
							getDir().setText(location);
						} else {
							getDir().setText(previous);
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
			servicePackage.setText((DEFAULT_JAVA_PACKAGE).toLowerCase());
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

	/**
	 * This method initializes closeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setIcon(PortalLookAndFeel.getCloseIcon());
			closeButton.setText("Cancel");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return closeButton;
	}

	private void createService() {
		int doIdeleteResult = JOptionPane.OK_OPTION;
		final File dirFile = new File(getDir().getText());
		if (dirFile.exists() && dirFile.list().length != 0) {
			doIdeleteResult = JOptionPane
					.showConfirmDialog(
							this,
							"The creation directory is not empty.  All information in the directory will be lost.",
							"Confirm Overwrite", JOptionPane.YES_NO_OPTION);
		}

		if (doIdeleteResult == JOptionPane.OK_OPTION) {
			BusyDialogRunnable r = new BusyDialogRunnable(PortalResourceManager
					.getInstance().getGridPortal(), "Creating") {
				public void process() {
					try {
						if (dirFile.exists()) {
							setProgressText("deleting existing directory");
							boolean deleted = Utils.deleteDir(dirFile);
							if (!deleted) {
								setErrorMessage("Unable to delete creation directory");
								return;
							}
						}

						setProgressText("caching directory location");

						setProgressText("validating");
						String serviceName = getService().getText();
						String dirName = getDir().getText();
						String packageName = getServicePackage().getText();
						String serviceNsDomain = getNamespaceDomain().getText();
						// String templateFilename =
						// getMethodsTemplateFile().getText();
						if (!CommonTools.isValidServiceName(serviceName)) {
							setErrorMessage("Service Name is not valid.  Service name must be a java compatible class name. (" + CommonTools.ALLOWED_JAVA_NAME_REGEX + ")");
							return;
						}

						setProgressText("purging old archives");
						ResourceManager.purgeArchives(serviceName);

						// only supporting one for now.....
						String serviceExtensions = "";
						for (int i = 0; i < getExtensionsTable().getRowCount(); i++) {
							ServiceExtensionDescriptionType edt = ExtensionsLoader
									.getInstance()
									.getServiceExtensionByDisplayName(
											getExtensionsTable().getRowData(i));
							serviceExtensions += edt.getName();
							if (i < getExtensionsTable().getRowCount() - 1) {
								serviceExtensions += ",";
							}
						}
						setProgressText("creating");
						String cmd = CommonTools.getAntSkeletonCreationCommand(
								".", serviceName, dirName, packageName,
								serviceNsDomain, serviceExtensions);
						Process p = CommonTools.createAndOutputProcess(cmd);
						p.waitFor();
						if (p.exitValue() != 0) {
							setErrorMessage("Error creating new service!");
						}

						setProgressText("running extension viewers");
						Properties properties = new Properties();
						properties
								.load(new FileInputStream(
										getDir().getText()
												+ File.separator
												+ IntroduceConstants.INTRODUCE_PROPERTIES_FILE));
						ServiceDescription introService = (ServiceDescription) Utils
								.deserializeDocument(
										getDir().getText()
												+ File.separator
												+ IntroduceConstants.INTRODUCE_XML_FILE,
										ServiceDescription.class);
						ServiceInformation info = new ServiceInformation(
								introService, properties, new File(getDir()
										.getText()));
						if (!serviceExtensions.equals("")) {
							JDialog extDialog = ExtensionTools
									.getCreationUIDialog(serviceExtensions,
											info);
							if (extDialog != null) {
								extDialog.setVisible(true);
							}
						}
						
						setProgressText("post creation");
						cmd = CommonTools.getAntSkeletonPostCreationCommand(
								".", serviceName, dirName, packageName,
								serviceNsDomain, serviceExtensions);
						p = CommonTools.createAndOutputProcess(cmd);
						p.waitFor();
						if (p.exitValue() != 0) {
							setErrorMessage("Error post creating new service!");
						}

						setProgressText("building");
						cmd = CommonTools.getAntCompileCommand(dirName);
						p = CommonTools.createAndOutputProcess(cmd);
						p.waitFor();
						if (p.exitValue() == 0) {
							PortalResourceManager.getInstance().getGridPortal()
									.addGridPortalComponent(
											new ModificationViewer(new File(
													dirName)));
							dispose();
						} else {
							setErrorMessage("Error creating new service!");
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						setErrorMessage("Error: " + ex.getMessage());
					}
				}
			};

			Thread th = new Thread(r);
			th.start();

		}
	}

	/**
	 * This method initializes extensionsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExtensionsPanel() {
		if (extensionsPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 1;
			gridBagConstraints13.gridy = 0;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints20.gridwidth = 3;
			gridBagConstraints20.gridy = 1;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints19.gridheight = 2;
			gridBagConstraints19.gridy = 2;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 2;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.gridy = 0;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			extensionsPanel = new JPanel();
			extensionsPanel.setLayout(new GridBagLayout());
			extensionsPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Service Extensions",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, IntroduceLookAndFeel
											.getPanelLabelColor()));
			extensionsPanel.add(getServiceStyleSeletor(), gridBagConstraints15);
			extensionsPanel.add(getRemoveExtensionButton(),
					gridBagConstraints16);
			extensionsPanel.add(getExtensionsTable(), gridBagConstraints19);
			extensionsPanel.add(getAddExtensionButton(), gridBagConstraints13);
			extensionsPanel.add(getExtensionsTableionsTablePanel(),
					gridBagConstraints20);
		}
		return extensionsPanel;
	}

	/**
	 * This method initializes addExtensionButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddExtensionButton() {
		if (addExtensionButton == null) {
			addExtensionButton = new JButton();
			addExtensionButton.setText("Add");
			addExtensionButton.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (!((String) serviceStyleSeletor.getSelectedItem())
							.equals("NONE")) {
						getExtensionsTable().addRow(
								(String) serviceStyleSeletor.getSelectedItem());
					}
				}

			});
		}
		return addExtensionButton;
	}

	/**
	 * This method initializes removeExtensionButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveExtensionButton() {
		if (removeExtensionButton == null) {
			removeExtensionButton = new JButton();
			removeExtensionButton.setText("Remove");
			removeExtensionButton.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					try {
						getExtensionsTable().removeSelectedRow();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			});
		}
		return removeExtensionButton;
	}

	/**
	 * This method initializes extensionsScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getExtensionsScrollPane() {
		if (extensionsScrollPane == null) {
			extensionsScrollPane = new JScrollPane();
			extensionsScrollPane.setViewportView(getExtensionsTable());
		}
		return extensionsScrollPane;
	}

	/**
	 * This method initializes extensionsTable
	 * 
	 * @return javax.swing.JTable
	 */
	private ExtensionsTable getExtensionsTable() {
		if (extensionsTable == null) {
			extensionsTable = new ExtensionsTable();
			extensionsTable.setPreferredSize(new java.awt.Dimension(100,150));
		}
		return extensionsTable;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExtensionsTableionsTablePanel() {
		if (extensionsTablePanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.SOUTHWEST;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints21.gridy = 0;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints14.gridy = 1;
			downExtensionLabel = new JLabel();
			downExtensionLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					try {
						getExtensionsTable().moveSelectedRowDown();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			downExtensionLabel.setIcon(IntroduceLookAndFeel.getDownIcon());
			upExtensionLabel = new JLabel();
			upExtensionLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					try {
						getExtensionsTable().moveSelectedRowUp();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			upExtensionLabel.setIcon(IntroduceLookAndFeel.getUpIcon());
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.weighty = 1.0;
			gridBagConstraints18.gridheight = 2;
			gridBagConstraints18.gridx = 0;
			extensionsTablePanel = new JPanel();
			extensionsTablePanel.setLayout(new GridBagLayout());
			extensionsTablePanel.add(getExtensionsScrollPane(),
					gridBagConstraints18);
			extensionsTablePanel.add(upExtensionLabel, gridBagConstraints21);
			extensionsTablePanel.add(downExtensionLabel, gridBagConstraints14);
		}
		return extensionsTablePanel;
	}
}
