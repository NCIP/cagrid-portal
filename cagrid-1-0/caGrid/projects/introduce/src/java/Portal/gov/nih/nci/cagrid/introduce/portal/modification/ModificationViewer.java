package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.Archive;
import gov.nih.nci.cagrid.introduce.SyncTools;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: ModificationViewer.java,v 1.18 2005-12-14 02:23:08 hastings Exp $
 */
public class ModificationViewer extends GridPortalBaseFrame {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancel = null;

	private JPanel selectPanel = null;

	private MethodsTable methodsTable = null;

	private JScrollPane jScrollPane = null;

	private File methodsDirectory = null;

	private Document methodsDocument = null;

	private Properties serviceProperties = null;

	private JButton addMethodButton = null;

	private JButton saveButton = null;

	private JComponent me;

	private static File defaultMethodsDir = new File(System
			.getProperty("user.dir"));

	private JButton removeButton = null;

	private JButton modifyButton = null; // @jve:decl-index=0:

	private JPanel contentButtonPanel = null;

	private JButton undoButton = null;

	private boolean dirty = false;

	private JPanel progressPanel = null;

	/**
	 * This is the default constructor
	 */
	public ModificationViewer() {
		super();
		this.me = this;
		chooseService();
		initialize();
	}

	public ModificationViewer(File methodsDirectory) {
		super();
		this.me = this;
		this.methodsDirectory = methodsDirectory;
		initialize();
	}

	private void loadServiceProps() {
		try {
			serviceProperties = new Properties();
			serviceProperties.load(new FileInputStream(this.methodsDirectory
					.getAbsolutePath()
					+ File.separator + "introduce.properties"));
			serviceProperties.setProperty("introduce.skeleton.destination.dir",
					methodsDirectory.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel label = new JLabel();
		Font f1 = label.getFont();
		f1 = f1.deriveFont(f1.getStyle() ^ Font.BOLD);
		JTextField field = new JTextField();
		Font f2 = field.getFont();
		f2 = f2.deriveFont(f2.getStyle() ^ Font.ITALIC);

		this.addTextField(this.getSelectPanel(), "Timestamp", serviceProperties
				.getProperty("introduce.skeleton.timestamp"), 1, false);
		this.getLabel("Timestamp").setFont(f1);
		this.getTextField("Timestamp").setFont(f2);
		this.addTextField(this.getSelectPanel(), "Service Name",
				serviceProperties
						.getProperty("introduce.skeleton.service.name"), 2,
				false);
		this.getLabel("Service Name").setFont(f1);
		this.getTextField("Service Name").setFont(f2);

		this.addTextField(this.getSelectPanel(), "Location", methodsDirectory
				.getAbsolutePath(), 3, false);
		this.getLabel("Location").setFont(f1);
		this.getTextField("Location").setFont(f2);
		this.addTextField(this.getSelectPanel(), "Package", serviceProperties
				.getProperty("introduce.skeleton.package"), 4, false);
		this.getLabel("Package").setFont(f1);
		this.getTextField("Package").setFont(f2);
		this
				.addTextField(this.getSelectPanel(), "Package Dir",
						serviceProperties
								.getProperty("introduce.skeleton.package.dir"),
						5, false);
		this.getLabel("Package Dir").setFont(f1);
		this.getTextField("Package Dir").setFont(f2);
		this.addTextField(this.getSelectPanel(), "Namespace Domain",
				serviceProperties
						.getProperty("introduce.skeleton.namespace.domain"), 6,
				false);
		this.getLabel("Namespace Domain").setFont(f1);
		this.getTextField("Namespace Domain").setFont(f2);
	}

	private void chooseService() {
		JFileChooser chooser = new JFileChooser(defaultMethodsDir);
		chooser.setDialogTitle("Select Service Skeleton Directory");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(me);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.methodsDirectory = chooser.getSelectedFile();
			defaultMethodsDir = this.methodsDirectory;
		} else {
			return;
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		if (this.methodsDirectory != null) {
			SAXBuilder builder = new SAXBuilder(false);
			try {
				methodsDocument = builder.build(this.methodsDirectory
						.getAbsolutePath()
						+ File.separator + "introduceMethods.xml");

				loadServiceProps();

				// this.getServiceName().setText(serviceProperties.getProperty("introduce.skeleton.service.name"));
				// this.getServiceName().setEnabled(false);
			} catch (JDOMException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		this.setSize(500, 400);
		this.setContentPane(getJContentPane());
		this.setTitle("Modify Service Interface");
		this.setFrameIcon(IntroduceLookAndFeel.getModifyIcon());

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.gridy = 3;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridheight = 0;
			gridBagConstraints11.gridwidth = 0;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 3.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getContentPanel(), gridBagConstraints1);
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getSelectPanel(), gridBagConstraints3);
			mainPanel.add(getProgressPanel(), gridBagConstraints12);
		}
		return mainPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing. gridBagConstraints41.gridx = 1; JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 3;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			contentPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Operations",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									new java.awt.Font("Dialog",
											java.awt.Font.BOLD, 12),
									new java.awt.Color(62, 109, 181)));
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			contentPanel.add(getJScrollPane(), gridBagConstraints4);
			contentPanel.add(getContentButtonPanel(), gridBagConstraints);
		}
		return contentPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints10.gridx = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.gridx = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getUndoButton(), gridBagConstraints8);
			buttonPanel.add(getSaveButton(), gridBagConstraints9);
			buttonPanel.add(getCancel(), gridBagConstraints10);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancel() {
		if (cancel == null) {
			cancel = new JButton(IntroduceLookAndFeel.getCloseIcon());
			cancel.setText("Cancel");
			// cancel.setIcon(GumsLookAndFeel.getCloseIcon());
			cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSelectPanel() {
		if (selectPanel == null) {
			selectPanel = new JPanel();
			selectPanel.setLayout(new GridBagLayout());
			selectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Service Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					IntroduceLookAndFeel.getPanelLabelColor()));
		}
		return selectPanel;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private MethodsTable getMethodsTable() {
		if (methodsTable == null) {
			methodsTable = new MethodsTable(this.methodsDocument,
					this.methodsDirectory, this.serviceProperties);
		}
		return methodsTable;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getMethodsTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddMethodButton() {
		if (addMethodButton == null) {
			addMethodButton = new JButton(IntroduceLookAndFeel.getAddIcon());
			addMethodButton.setText("Add");
			addMethodButton.setToolTipText("add new operation");
			addMethodButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							dirty = true;
							Element method = new Element("method",
									methodsDocument.getRootElement()
											.getNamespace());
							method.setAttribute("name", "newMethod");
							Element inputs = new Element("inputs",
									methodsDocument.getRootElement()
											.getNamespace());
							Element output = new Element("output",
									methodsDocument.getRootElement()
											.getNamespace());
							output.setAttribute("className", "void");
							Element exceptions = new Element("exceptions",
									methodsDocument.getRootElement()
											.getNamespace());
							method.addContent(inputs);
							method.addContent(output);
							method.addContent(exceptions);
							methodsDocument.getRootElement().addContent(method);
							getMethodsTable().addRow(method);
							performModify();
						}
					});
		}
		return addMethodButton;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton(IntroduceLookAndFeel.getSelectIcon());
			saveButton.setText("Save");
			saveButton.setToolTipText("modify and rebuild service");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					final int confirmed = JOptionPane.showConfirmDialog(me,
							"Are you sure you want to save?");
					BusyDialogRunnable r = new BusyDialogRunnable(
							PortalResourceManager.getInstance().getGridPortal(),
							"Save") {

						public void process() {
							try {
								setProgressText("writting methods document");

								if (confirmed == JOptionPane.OK_OPTION) {
									System.out
											.println("Writting service.methods file.");
									FileWriter fw = new FileWriter(new File(
											methodsDirectory.getAbsolutePath()
													+ File.separator
													+ "introduceMethods.xml"));
									XMLOutputter out = new XMLOutputter(Format
											.getPrettyFormat());
									out.output(methodsDocument, fw);

									setProgressText("sychronizing skeleton");
									// call the sync tools
									SyncTools sync = new SyncTools(
											methodsDirectory);
									sync.sync();
									setProgressText("rebuilding skeleton");
									String cmd = CommonTools.getAntCommand(
											"clean all", methodsDirectory
													.getAbsolutePath());
									Process p = CommonTools
											.createAndOutputProcess(cmd);
									p.waitFor();
									dirty = false;
									setProgressText("loading service properties");
									loadServiceProps();
									setProgressText("");
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}

					};
					Thread th = new Thread(r);
					th.start();

				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveButton() {
		if (removeButton == null) {
			removeButton = new JButton(IntroduceLookAndFeel.getRemoveIcon());
			removeButton.setText("Remove");
			removeButton.setToolTipText("remove selected operation");
			removeButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					dirty = true;
					int row = getMethodsTable().getSelectedRow();
					if ((row < 0) || (row >= getMethodsTable().getRowCount())) {
						PortalUtils
								.showErrorMessage("Please select a method to remove.");
						return;
					}
					String methodName = getMethodsTable().getMethodName(
							getMethodsTable().getSelectedRow());
					List methods = methodsDocument.getRootElement()
							.getChildren();
					for (int i = 0; i < methods.size(); i++) {
						Element method = (Element) methods.get(i);
						if (method.getAttributeValue("name").equals(methodName)) {
							methodsDocument.getRootElement().removeContent(
									method);
							break;
						}
					}
					getMethodsTable().removeRow(
							getMethodsTable().getSelectedRow());
				}
			});
		}
		return removeButton;
	}

	public void performModify() {

		int row = getMethodsTable().getSelectedRow();
		if ((row < 0) || (row >= getMethodsTable().getRowCount())) {
			PortalUtils.showErrorMessage("Please select a method to modify.");
			return;
		}

		Element method = (Element) getMethodsTable().getValueAt(
				getMethodsTable().getSelectedRow(), 1);
		PortalResourceManager
				.getInstance()
				.getGridPortal()
				.addGridPortalComponent(
						new MethodViewer(
								method,
								new File(
										methodsDirectory.getAbsolutePath()
												+ File.separator
												+ "schema"
												+ File.separator
												+ serviceProperties
														.getProperty("introduce.skeleton.service.name")),
								getMethodsTable(), getMethodsTable()
										.getSelectedRow()));
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getModifyButton() {
		if (modifyButton == null) {
			modifyButton = new JButton(IntroduceLookAndFeel.getModifyIcon());
			modifyButton.setText("Modify");
			modifyButton.setToolTipText("modify seleted operation");
			modifyButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dirty = true;
					performModify();
				}
			});
		}
		return modifyButton;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentButtonPanel() {
		if (contentButtonPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridx = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			contentButtonPanel = new JPanel();
			contentButtonPanel.setLayout(new GridBagLayout());
			contentButtonPanel.add(getAddMethodButton(), gridBagConstraints5);
			contentButtonPanel.add(getModifyButton(), gridBagConstraints6);
			contentButtonPanel.add(getRemoveButton(), gridBagConstraints7);
		}
		return contentButtonPanel;
	}

	/**
	 * This method initializes undoButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUndoButton() {
		if (undoButton == null) {
			undoButton = new JButton(IntroduceLookAndFeel.getUndoIcon());
			undoButton.setText("Undo");
			undoButton.setToolTipText("roll back to last save state");
			undoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					BusyDialogRunnable r = new BusyDialogRunnable(
							PortalResourceManager.getInstance().getGridPortal(),
							"Undo") {

						public void process() {
							System.out
									.println("Loading in last known save for this project");
							try {
								if (!dirty) {
									setProgressText("restoring from local cache");
									Archive
											.restoreLatest(
													serviceProperties
															.getProperty("introduce.skeleton.timestamp"),
													serviceProperties
															.getProperty("introduce.skeleton.service.name"),
													serviceProperties
															.getProperty("introduce.skeleton.destination.dir"));
								}
								dispose();
								PortalResourceManager.getInstance()
										.getGridPortal()
										.addGridPortalComponent(
												new ModificationViewer(
														methodsDirectory));
							} catch (Exception e1) {
								e1.printStackTrace();
							}

						}

					};
					Thread th = new Thread(r);
					th.start();
				}
			});
		}
		return undoButton;
	}

	/**
	 * This method initializes progressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressPanel() {
		if (progressPanel == null) {
			progressPanel = new JPanel();
			progressPanel.setLayout(new GridBagLayout());
		}
		return progressPanel;
	}
} // @jve:decl-index=0:visual-constraint="6,9"
