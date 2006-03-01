package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.modification.cadsr.CADSRMetadataConfigurationComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.gme.GMEMetadataConfigurationComponent;
import gov.nih.nci.cagrid.introduce.portal.security.ServiceSecurityPanel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.xml.namespace.QName;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: ModificationViewer.java,v 1.71 2006-03-01 16:53:29 hastings Exp $
 */
public class ModificationViewer extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel operationsPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancel = null;

	private JPanel selectPanel = null;

	private MethodsTable methodsTable = null;

	private JScrollPane jScrollPane = null;

	private File methodsDirectory = null;

	private ServiceDescription introService;

	private Properties serviceProperties = null;

	private JButton addMethodButton = null;

	private JButton saveButton = null;

	private JButton removeButton = null;

	private JButton modifyButton = null; // @jve:decl-index=0:

	private JPanel operationsButtonPanel = null;

	private JButton undoButton = null;

	private boolean dirty = false;

	private JTabbedPane contentTabbedPane = null;

	private JPanel metadataPanel = null;

	private JScrollPane metadataScrollPane = null;

	private MetadataTable metadataTable = null;

	private JPanel metadataButtonsPanel = null;

	private JButton addMetadataButton = null;

	private JButton removeMetadataButton = null;

	private JButton modifyMetadataButton = null;

	private ServiceSecurityPanel securityPanel = null;

	private ServiceSecurity lastServiceSecurity;

	private JLabel serviceNameLabel = null;

	private JTextField serviceName = null;

	private JLabel packageLabel = null;

	private JTextField packageName = null;

	private JLabel lastSavedLabel = null;

	private JTextField lastSaved = null;

	private JLabel saveLocationLabel = null;

	private JTextField saveLocation = null;


	/**
	 * This is the default constructor
	 */
	public ModificationViewer() {
		super();
		// throw a thread out so that i can make sure that if the chooser is
		// canceled i can dispose of this frame
		Thread th = createChooserThread();
		th.start();
	}


	public ModificationViewer(File methodsDirectory) {
		super();
		this.methodsDirectory = methodsDirectory;
		initialize();

	}


	private Thread createChooserThread() {
		Thread th = new Thread(new Runnable() {

			public void run() {
				try {
					chooseService();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (methodsDirectory == null) {
					ModificationViewer.this.dispose();
					return;
				}
				File file = new File(methodsDirectory.getAbsolutePath() + File.separator + "introduce.xml");
				if (file.exists() && file.canRead()) {
					initialize();
				} else {
					JOptionPane.showMessageDialog(ModificationViewer.this, "Directory "
						+ methodsDirectory.getAbsolutePath() + " does not seem to be an introduce service");
					ModificationViewer.this.dispose();
				}
			}

		});
		return th;
	}


	private void loadServiceProps() {
		try {
			serviceProperties = new Properties();
			serviceProperties.load(new FileInputStream(this.methodsDirectory.getAbsolutePath() + File.separator
				+ "introduce.properties"));
			serviceProperties.setProperty("introduce.skeleton.destination.dir", methodsDirectory.getAbsolutePath());
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
	}


	private void chooseService() throws Exception {
		String dir = ResourceManager.promptDir(this, null);
		if (dir != null) {
			this.methodsDirectory = new File(dir);
		}
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		if (this.methodsDirectory != null) {
			try {
				this.introService = (ServiceDescription) Utils.deserializeDocument(this.methodsDirectory
					.getAbsolutePath()
					+ File.separator + "introduce.xml", ServiceDescription.class);
				loadServiceProps();
				this.lastServiceSecurity = this.introService.getServiceSecurity();
				if (this.lastServiceSecurity == null) {
					this.lastServiceSecurity = new ServiceSecurity();
				}
				this.setSize(500, 400);
				this.setContentPane(getJContentPane());
				this.setTitle("Modify Service Interface");
				this.setFrameIcon(IntroduceLookAndFeel.getModifyIcon());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 1;
			gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints13.weightx = 1.0;
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
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
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
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getSelectPanel(), gridBagConstraints3);
			mainPanel.add(getContentTabbedPane(), gridBagConstraints13);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing. gridBagConstraints41.gridx = 1; JPanel
	 */
	private JPanel getMethodsPanel() {
		if (operationsPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 3;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			operationsPanel = new JPanel();
			operationsPanel.setLayout(new GridBagLayout());
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			operationsPanel.add(getJScrollPane(), gridBagConstraints4);
			operationsPanel.add(getMethodsButtonPanel(), gridBagConstraints);
		}
		return operationsPanel;
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
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints24.gridy = 1;
			gridBagConstraints24.weightx = 1.0;
			gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints24.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints24.gridx = 3;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 2;
			gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints23.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints23.gridy = 1;
			saveLocationLabel = new JLabel();
			saveLocationLabel.setText("Location");
			saveLocationLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.gridy = 1;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints22.gridx = 1;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridy = 1;
			lastSavedLabel = new JLabel();
			lastSavedLabel.setText("Last Saved");
			lastSavedLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.gridy = 0;
			gridBagConstraints20.weightx = 1.0;
			gridBagConstraints20.gridx = 3;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 2;
			gridBagConstraints19.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints19.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints19.gridy = 0;
			packageLabel = new JLabel();
			packageLabel.setText("Package");
			packageLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints18.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints18.gridy = 0;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints17.weightx = 1.0;
			serviceNameLabel = new JLabel();
			serviceNameLabel.setText("Service Name");
			serviceNameLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
			selectPanel = new JPanel();
			selectPanel.setLayout(new GridBagLayout());
			selectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Service Properties",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			selectPanel.add(serviceNameLabel, gridBagConstraints18);
			selectPanel.add(getServiceName(), gridBagConstraints17);
			selectPanel.add(packageLabel, gridBagConstraints19);
			selectPanel.add(getPackageName(), gridBagConstraints20);
			selectPanel.add(lastSavedLabel, gridBagConstraints21);
			selectPanel.add(getLastSaved(), gridBagConstraints22);
			selectPanel.add(saveLocationLabel, gridBagConstraints23);
			selectPanel.add(getSaveLocation(), gridBagConstraints24);
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
			methodsTable = new MethodsTable(introService.getMethods(), this.methodsDirectory, this.serviceProperties);
			methodsTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						dirty = true;
						performMethodModify();
					}
				}
			});
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
			addMethodButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dirty = true;
					MethodType method = new MethodType();
					method.setName("newMethod");
					MethodTypeOutput output = new MethodTypeOutput();
					output.setClassName("void");
					method.setOutput(output);

					// add new method to array in bean
					// this seems to be a wierd way be adding things....
					MethodType[] newMethods;
					int newLength = 0;
					if (introService.getMethods() != null && introService.getMethods().getMethod() != null) {
						newLength = introService.getMethods().getMethod().length + 1;
						newMethods = new MethodType[newLength];
						System.arraycopy(introService.getMethods().getMethod(), 0, newMethods, 0, introService
							.getMethods().getMethod().length);
					} else {
						newLength = 1;
						newMethods = new MethodType[newLength];
					}
					newMethods[newLength - 1] = method;
					introService.getMethods().setMethod(newMethods);

					getMethodsTable().addRow(method);

					performMethodModify();
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
					final int confirmed = JOptionPane.showConfirmDialog(ModificationViewer.this,
						"Are you sure you want to save?");
					if (confirmed == JOptionPane.OK_OPTION) {
						try {
							resetMethodSecurityIfServiceSecurityChanged();
						} catch (Exception ex) {
							PortalUtils.showErrorMessage(ex);
						}

						BusyDialogRunnable r = new BusyDialogRunnable(PortalResourceManager.getInstance()
							.getGridPortal(), "Save") {

							public void process() {
								try {

									if (confirmed == JOptionPane.OK_OPTION) {
										setProgressText("editing service metadata object");
										// walk the methods table and create the
										// new MethodsType array
										MethodType[] methodsArray = new MethodType[methodsTable.getRowCount()];
										for (int i = 0; i < methodsArray.length; i++) {
											MethodType methodInstance = methodsTable.getMethodType(i);
											methodsArray[i] = methodInstance;
										}
										MethodsType methods = new MethodsType();
										methods.setMethod(methodsArray);
										introService.setMethods(methods);

										// walk the metadata table and create
										// the
										// new ServiceMetadataType array
										MetadataType[] metadataArray = new MetadataType[metadataTable.getRowCount()];
										for (int i = 0; i < metadataArray.length; i++) {
											String packageName = (String) metadataTable.getValueAt(i, 0);
											String className = (String) metadataTable.getValueAt(i, 1);
											String namespace = (String) metadataTable.getValueAt(i, 2);
											String type = (String) metadataTable.getValueAt(i, 3);
											String location = (String) metadataTable.getValueAt(i, 4);
											String populateFromFile = (String) metadataTable.getValueAt(i, 5);
											String register = (String) metadataTable.getValueAt(i, 6);
											String qnameNS = (String) metadataTable.getValueAt(i, 7);
											String qnameName = (String) metadataTable.getValueAt(i, 8);

											MetadataType metadata = new MetadataType();
											if (packageName != null && !packageName.equals("")) {
												metadata.setPackageName(packageName);
											}
											if (className != null && !className.equals("")) {
												metadata.setClassName(className);
											}
											if (namespace != null && !namespace.equals("")) {
												metadata.setNamespace(namespace);
											}
											if (type != null && !type.equals("")) {
												metadata.setType(type);
											}
											if (location != null && !location.equals("")) {
												metadata.setLocation(location);
											}
											if (populateFromFile != null && !populateFromFile.equals("")) {
												metadata.setPopulateFromFile(Boolean.valueOf(populateFromFile)
													.booleanValue());
											}
											if (register != null && !register.equals("")) {
												metadata.setRegister(Boolean.valueOf(register).booleanValue());
											}
											if (qnameNS != null && !qnameNS.equals("") && qnameName != null
												&& !qnameName.equals("")) {
												QName qn = new QName(qnameNS, qnameName);
												metadata.setQName(qn);
											}
											metadataArray[i] = metadata;

										}

										MetadataListType serviceMetadataList = new MetadataListType();
										serviceMetadataList.setMetadata(metadataArray);
										introService.setMetadataList(serviceMetadataList);
										introService.setServiceSecurity(securityPanel.getServiceSecurity());
										// check the methods to make sure they
										// are
										// valid.......

										// save the metadata and methods and
										// then
										// call the resync and build
										setProgressText("writting service document");
										Utils.serializeDocument(methodsDirectory.getAbsolutePath() + File.separator
											+ "introduce.xml", introService, new QName(
											"gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));
										setProgressText("sychronizing skeleton");
										// call the sync tools
										SyncTools sync = new SyncTools(methodsDirectory);
										sync.sync();
										setProgressText("rebuilding skeleton");
										String cmd = CommonTools.getAntCommand("clean all", methodsDirectory
											.getAbsolutePath());
										Process p = CommonTools.createAndOutputProcess(cmd);
										p.waitFor();
										if(p.exitValue()!=0){
											JOptionPane.showMessageDialog(ModificationViewer.this,"Error: Unable to rebuild the skeleton");
											return;
										}
										dirty = false;
										setProgressText("loading service properties");
										loadServiceProps();
										setLastSaved(serviceProperties.getProperty("introduce.skeleton.timestamp"));
										this.setProgressText("");
									}
								} catch (Exception e1) {
									e1.printStackTrace();
									JOptionPane.showMessageDialog(ModificationViewer.this,"Error: " + e1.getMessage());
									return;
								}
							}

						};

						Thread th = new Thread(r);
						th.start();
					}
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
						PortalUtils.showErrorMessage("Please select a method to remove.");
						return;
					}
					int oldSelectedRow = getMethodsTable().getSelectedRow();
					getMethodsTable().removeRow(oldSelectedRow);
					if (oldSelectedRow == 0) {
						oldSelectedRow++;
					}
					if (getMethodsTable().getRowCount() > 0) {
						getMethodsTable().setRowSelectionInterval(oldSelectedRow - 1, oldSelectedRow - 1);
					}
				}
			});
		}
		return removeButton;
	}


	public void performMetadataModify() {

		int row = getMetadataTable().getSelectedRow();
		if ((row < 0) || (row >= getMetadataTable().getRowCount())) {
			PortalUtils.showErrorMessage("Please select a metadata type to modify.");
			return;
		}

		Vector v = (Vector) getMetadataTable().getValueAt(getMetadataTable().getSelectedRow(), 9);
		IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
			IntroducePortalConf.RESOURCE);
		if (conf.getDiscoveryType().equals(IntroducePortalConf.GME_DISCOVERY)) {
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
				new GMEMetadataConfigurationComponent(v, new File(methodsDirectory.getAbsolutePath() + File.separator
					+ "schema" + File.separator + serviceProperties.getProperty("introduce.skeleton.service.name"))));
		} else if (conf.getDiscoveryType().equals(IntroducePortalConf.CADSR_DISCOVERY)) {
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
				new CADSRMetadataConfigurationComponent(v, new File(methodsDirectory.getAbsolutePath() + File.separator
					+ "schema" + File.separator + serviceProperties.getProperty("introduce.skeleton.service.name"))));
		}
	}


	private void resetMethodSecurityIfServiceSecurityChanged() throws Exception {
		if (!lastServiceSecurity.equals(securityPanel.getServiceSecurity())) {
			MethodsType mt = this.introService.getMethods();
			if (mt != null) {
				lastServiceSecurity = securityPanel.getServiceSecurity();
				PortalUtils
					.showMessage("Service security configuration changed, resetting all method security configurations.");
				MethodType[] methods = mt.getMethod();
				if (methods != null) {
					for (int i = 0; i < methods.length; i++) {
						methods[i].setMethodSecurity(null);
					}
				}
			}
		}
	}


	public void performMethodModify() {
		try {
			this.resetMethodSecurityIfServiceSecurityChanged();
		} catch (Exception e) {
			PortalUtils.showErrorMessage(e);
			return;
		}
		MethodType method = getMethodsTable().getSelectedMethodType();
		if (method == null) {
			PortalUtils.showErrorMessage("Please select a method to modify.");
			return;
		}
		PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
			new MethodViewer(method, lastServiceSecurity, new File(methodsDirectory.getAbsolutePath() + File.separator
				+ "schema" + File.separator + serviceProperties.getProperty("introduce.skeleton.service.name")),
				getMethodsTable(), getMethodsTable().getSelectedRow()));
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
					performMethodModify();
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
	private JPanel getMethodsButtonPanel() {
		if (operationsButtonPanel == null) {
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
			operationsButtonPanel = new JPanel();
			operationsButtonPanel.setLayout(new GridBagLayout());
			operationsButtonPanel.add(getAddMethodButton(), gridBagConstraints5);
			operationsButtonPanel.add(getModifyButton(), gridBagConstraints6);
			operationsButtonPanel.add(getRemoveButton(), gridBagConstraints7);
		}
		return operationsButtonPanel;
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
					BusyDialogRunnable r = new BusyDialogRunnable(PortalResourceManager.getInstance().getGridPortal(),
						"Undo") {

						public void process() {
							System.out.println("Loading in last known save for this project");
							try {
								if (!dirty) {
									setProgressText("restoring from local cache");
									ResourceManager.restoreLatest(serviceProperties
										.getProperty("introduce.skeleton.timestamp"), serviceProperties
										.getProperty("introduce.skeleton.service.name"), serviceProperties
										.getProperty("introduce.skeleton.destination.dir"));
								}
								dispose();
								PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(
									new ModificationViewer(methodsDirectory));
							} catch (Exception e1) {
								//e1.printStackTrace();
								JOptionPane.showMessageDialog(ModificationViewer.this,"Unable to roll back, there may be no older versions available");
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
	 * This method initializes contentTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getContentTabbedPane() {
		if (contentTabbedPane == null) {
			contentTabbedPane = new JTabbedPane();
			contentTabbedPane.addTab("Operations", null, getMethodsPanel(), null);
			contentTabbedPane.addTab("Metadata", null, getMetadataPanel(), null);
			// diable the metadata tab if they've specified not to sync metadata
			MetadataListType metadataList = this.introService.getMetadataList();
			if (metadataList != null && metadataList.getSynchronizeMetadataFramework() != null
				&& !metadataList.getSynchronizeMetadataFramework().booleanValue()) {
				// Disable the tab
				contentTabbedPane.setEnabledAt(contentTabbedPane.getTabCount() - 1, false);
			}

			contentTabbedPane.addTab("Security", null, getSecurityPanel(), null);
		}
		return contentTabbedPane;
	}


	/**
	 * This method initializes metadataPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMetadataPanel() {
		if (metadataPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			metadataPanel = new JPanel();
			metadataPanel.setLayout(new GridBagLayout());
			metadataPanel.add(getMetadataScrollPane(), gridBagConstraints1);
			metadataPanel.add(getMetadataButtonsPanel(), gridBagConstraints12);
		}
		return metadataPanel;
	}


	/**
	 * This method initializes metadataScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getMetadataScrollPane() {
		if (metadataScrollPane == null) {
			metadataScrollPane = new JScrollPane();
			metadataScrollPane.setViewportView(getMetadataTable());
		}
		return metadataScrollPane;
	}


	/**
	 * This method initializes metadataTable
	 */
	private MetadataTable getMetadataTable() {
		if (metadataTable == null) {
			metadataTable = new MetadataTable(introService.getMetadataList());
			metadataTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						dirty = true;
						performMetadataModify();
					}
				}
			});
		}
		return metadataTable;
	}


	/**
	 * This method initializes metadataButtonsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMetadataButtonsPanel() {
		if (metadataButtonsPanel == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.gridy = 2;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints15.gridx = 0;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints14.gridy = 1;
			metadataButtonsPanel = new JPanel();
			metadataButtonsPanel.setLayout(new GridBagLayout());
			metadataButtonsPanel.add(getAddMetadataButton(), gridBagConstraints15);
			metadataButtonsPanel.add(getRemoveMetadataButton(), gridBagConstraints14);
			metadataButtonsPanel.add(getModifyMetadataButton(), gridBagConstraints16);
		}
		return metadataButtonsPanel;
	}


	/**
	 * This method initializes addMetadataButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddMetadataButton() {
		if (addMetadataButton == null) {
			addMetadataButton = new JButton();
			addMetadataButton.setText("Add");
			addMetadataButton.setToolTipText("add service metadata");
			addMetadataButton.setIcon(IntroduceLookAndFeel.getAddIcon());
			addMetadataButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dirty = true;
					MetadataType metadata = new MetadataType();

					// add new metadata to array in bean
					// this seems to be a wierd way be adding things....
					MetadataType[] metadatas;
					int newLength = 0;
					if (introService.getMetadataList() != null && introService.getMetadataList().getMetadata() != null) {
						newLength = introService.getMetadataList().getMetadata().length + 1;
						metadatas = new MetadataType[newLength];
						System.arraycopy(introService.getMetadataList().getMetadata(), 0, metadatas, 0, introService
							.getMetadataList().getMetadata().length);
					} else {
						newLength = 1;
						metadatas = new MetadataType[newLength];
					}
					metadatas[newLength - 1] = metadata;
					introService.getMetadataList().setMetadata(metadatas);

					getMetadataTable().addRow(metadata);

					performMetadataModify();
				}
			});
		}
		return addMetadataButton;
	}


	/**
	 * This method initializes removeMetadataButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveMetadataButton() {
		if (removeMetadataButton == null) {
			removeMetadataButton = new JButton();
			removeMetadataButton.setText("Remove");
			removeMetadataButton.setToolTipText("remove service metadata");
			removeMetadataButton.setIcon(IntroduceLookAndFeel.getRemoveIcon());
			removeMetadataButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dirty = true;
					int row = getMetadataTable().getSelectedRow();
					if ((row < 0) || (row >= getMetadataTable().getRowCount())) {
						PortalUtils.showErrorMessage("Please select a metdata type to remove.");
						return;
					}
					getMetadataTable().removeRow(getMetadataTable().getSelectedRow());
				}
			});
		}
		return removeMetadataButton;
	}


	/**
	 * This method initializes modifyMetadataButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getModifyMetadataButton() {
		if (modifyMetadataButton == null) {
			modifyMetadataButton = new JButton();
			modifyMetadataButton.setText("Modify");
			modifyMetadataButton.setToolTipText("modify selected service medata");
			modifyMetadataButton.setIcon(IntroduceLookAndFeel.getModifyIcon());
			modifyMetadataButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					performMetadataModify();
				}
			});
		}
		return modifyMetadataButton;
	}


	/**
	 * This method initializes securityPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private ServiceSecurityPanel getSecurityPanel() {
		if (securityPanel == null) {
			try {
				securityPanel = new ServiceSecurityPanel(introService.getServiceSecurity());
			} catch (Exception e) {
				PortalUtils.showErrorMessage(e);
			}
		}
		return securityPanel;
	}


	/**
	 * This method initializes serviceName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getServiceName() {
		if (serviceName == null) {
			serviceName = new JTextField();
			serviceName.setEditable(false);
			serviceName.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
			serviceName.setText(serviceProperties.getProperty("introduce.skeleton.service.name"));
		}
		return serviceName;
	}


	/**
	 * This method initializes packageName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getPackageName() {
		if (packageName == null) {
			packageName = new JTextField();
			packageName.setText(serviceProperties.getProperty("introduce.skeleton.package"));
			packageName.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
			packageName.setEditable(false);
		}
		return packageName;
	}


	/**
	 * This method initializes lastSaved
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getLastSaved() {
		if (lastSaved == null) {
			lastSaved = new JTextField();
			lastSaved.setEditable(false);
			lastSaved.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
			setLastSaved(serviceProperties.getProperty("introduce.skeleton.timestamp"));
		}
		return lastSaved;
	}
	
	
	private void setLastSaved(String savedDate){
		Date date;
		if (savedDate.equals("0")) {
			date = new Date();
		} else {
			date = new Date(Long.parseLong(savedDate));
		}
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		lastSaved.setText(formatter.format(date));
	}


	/**
	 * This method initializes location
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSaveLocation() {
		if (saveLocation == null) {
			saveLocation = new JTextField();
			saveLocation.setText(methodsDirectory.getAbsolutePath());
			saveLocation.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
			saveLocation.setEditable(false);
		}
		return saveLocation;
	}
} // @jve:decl-index=0:visual-constraint="6,9"
