package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.modification.gme.GMEParameterConfigurationComponent;
import gov.nih.nci.cagrid.introduce.portal.security.MethodSecurityPanel;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;


/**
 * MethodViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 24, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class MethodViewer extends GridPortalBaseFrame {

	MethodType method;

	private JPanel mainPanel = null;

	private JScrollPane inputParamScrollPanel = null;

	private InputParametersTable inputParamTable = null;

	private JScrollPane outputTypejScrollPane = null;

	private JTable outputTypeTable = null;

	private JPanel inputPanel = null;

	private JPanel outputTypePanel = null;

	private JPanel buttonPanel = null;

	private JButton doneButton = null;

	private JButton addInputParamButton = null;

	private File schemaDir;

	private JPanel namePanel = null;

	private JTextField nameField = null;

	private JButton removeButton = null;

	private JButton discoveryButton = null;

	private JLabel methodLabel = null;

	private JPanel inputButtonPanel = null;

	private MethodsTable methodsTable;

	private int currentRow;

	private JButton cancelButton = null;

	private JPanel exceptionsPanel = null;

	private JScrollPane exceptionScrollPane = null;

	private JPanel exceptionInputPanel = null;

	private ExceptionsTable exceptionsTable = null;

	private JButton addExceptionButton = null;

	private JButton removeExceptionButton = null;

	private JTabbedPane tabbedPanel = null;

	private JPanel methodPanel = null;

	private JPanel exceptionInputButtonPanel = null;

	private JPanel securityContainerPanel = null;

	private ServiceSecurity serviceSecurity;

	private GMEParameterConfigurationComponent discoveryPanel = null;

	private JButton outputTypeLoadFromDiscoveryButton = null;


	public MethodViewer(MethodType method, ServiceSecurity serviceSecurity, File schemaDir, MethodsTable table,
		int selectedRow) {
		this.serviceSecurity = serviceSecurity;
		this.method = method;
		this.schemaDir = schemaDir;
		this.methodsTable = table;
		this.currentRow = selectedRow;
		this.setTitle("Modify Method");
		initialize();
	}


	private void initialize() {
		this.setSize(510, 622);
		this.setTitle("Build/Modify Operation");
		this.setContentPane(getMainPanel());
		this.setFrameIcon(IntroduceLookAndFeel.getModifyIcon());
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.weightx = 1.0;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 4;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.weightx = 0.0D;
			gridBagConstraints10.weighty = 0.0D;
			gridBagConstraints10.fill = java.awt.GridBagConstraints.BOTH;
			mainPanel.add(getButtonPanel(), gridBagConstraints10);
			mainPanel.add(getTabbedPanel(), gridBagConstraints9);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getInputParamScrollPanel() {
		if (inputParamScrollPanel == null) {
			inputParamScrollPanel = new JScrollPane();
			inputParamScrollPanel.setViewportView(getInputParamTable());
		}
		return inputParamScrollPanel;
	}


	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private InputParametersTable getInputParamTable() {
		if (inputParamTable == null) {
			inputParamTable = new InputParametersTable(this.method);
		}
		return inputParamTable;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getOutputTypejScrollPane() {
		if (outputTypejScrollPane == null) {
			outputTypejScrollPane = new JScrollPane();
			outputTypejScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			outputTypejScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			outputTypejScrollPane.setViewportView(getOutputTypeTable());
		}
		return outputTypejScrollPane;
	}


	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getOutputTypeTable() {
		if (outputTypeTable == null) {
			outputTypeTable = new OutputTypeTable(getDiscoveryPanel(), this.method, this.schemaDir);
		}
		return outputTypeTable;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInputPanel() {
		if (inputPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.weightx = 0.0D;
			gridBagConstraints7.weighty = 0.0D;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 1;
			gridBagConstraints14.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints14.weightx = 0.0D;
			gridBagConstraints14.weighty = 0.0D;
			gridBagConstraints14.gridx = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			inputPanel = new JPanel();
			inputPanel.setLayout(new GridBagLayout());
			inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Input Parameters",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints8.ipadx = 0;
			gridBagConstraints8.insets = new java.awt.Insets(0, 0, 0, 0);
			gridBagConstraints8.gridwidth = 1;
			inputPanel.add(getInputParamScrollPanel(), gridBagConstraints8);
			inputPanel.add(getInputButtonPanel(), gridBagConstraints14);
		}
		return inputPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOutputTypePanel() {
		if (outputTypePanel == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.gridy = 1;
			GridBagConstraints gridBagContraints9 = new GridBagConstraints();
			gridBagContraints9.gridx = 0;
			gridBagContraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagContraints9.weighty = 1.0D;
			gridBagContraints9.weightx = 1.0D;
			gridBagContraints9.ipady = 0;
			gridBagContraints9.gridy = 0;
			outputTypePanel = new JPanel();
			outputTypePanel.setLayout(new GridBagLayout());
			outputTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Output Type",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			outputTypePanel.add(getOutputTypejScrollPane(), gridBagContraints9);
			outputTypePanel.add(getOutputTypeLoadFromDiscoveryButton(), gridBagConstraints16);
		}
		return outputTypePanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(getDoneButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDoneButton() {
		if (doneButton == null) {
			doneButton = new JButton(IntroduceLookAndFeel.getSelectIcon());
			doneButton.setText("Done");
			doneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// First process the inputs
					try {
						method.setName(getNameField().getText());

						method.setMethodSecurity(((MethodSecurityPanel) securityContainerPanel).getMethodSecurity());

						methodsTable.changeMethodName(currentRow, getNameField().getText());

						MethodTypeInputs inputs = new MethodTypeInputs();
						MethodTypeInputsInput[] inputsA = new MethodTypeInputsInput[getInputParamTable().getRowCount()];

						for (int i = 0; i < getInputParamTable().getRowCount(); i++) {
							MethodTypeInputsInput input = new MethodTypeInputsInput();

							String packageName = ((String) getInputParamTable().getValueAt(i, 0));
							String className = ((String) getInputParamTable().getValueAt(i, 1));
							Boolean isArray = new Boolean((String) getInputParamTable().getValueAt(i, 2));
							String name = ((String) getInputParamTable().getValueAt(i, 3));
							String namespace = ((String) getInputParamTable().getValueAt(i, 4));
							String type = ((String) getInputParamTable().getValueAt(i, 5));
							String location = ((String) getInputParamTable().getValueAt(i, 6));

							if (packageName != null && !packageName.equals("")) {
								input.setPackageName(packageName);
							}
							if (className != null && !className.equals("")) {
								input.setClassName(className);
							}
							if (isArray != null) {
								input.setIsArray(isArray);
							}
							if (name != null && !name.equals("")) {
								input.setName(name);
							}
							if (namespace != null && !namespace.equals("")) {
								input.setNamespace(namespace);
								// cache the needed schemas in the schema dir
								cacheSchema(schemaDir, namespace);
							}
							if (type != null && !type.equals("")) {
								input.setType(type);
							}
							if (location != null && !location.equals("")) {
								input.setLocation(location);
							}

							inputsA[i] = input;
						}

						inputs.setInput(inputsA);
						method.setInputs(inputs);

						// process exceptions
						MethodTypeExceptions exceptions = new MethodTypeExceptions();
						MethodTypeExceptionsException[] exceptionsA = new MethodTypeExceptionsException[getExceptionsTable()
							.getRowCount()];
						for (int i = 0; i < getExceptionsTable().getRowCount(); i++) {
							MethodTypeExceptionsException exception = new MethodTypeExceptionsException();
							String name = ((String) getExceptionsTable().getValueAt(i, 0));
							exception.setName(name);
							exceptionsA[i] = exception;
						}
						exceptions.setException(exceptionsA);
						method.setExceptions(exceptions);

						// now process the output
						MethodTypeOutput output = new MethodTypeOutput();

						String packageName = ((String) getOutputTypeTable().getValueAt(0, 0));
						String className = ((String) getOutputTypeTable().getValueAt(0, 1));
						Boolean isArray = new Boolean(((String) getOutputTypeTable().getValueAt(0, 2)));
						String namespace = ((String) getOutputTypeTable().getValueAt(0, 3));
						String type = ((String) getOutputTypeTable().getValueAt(0, 4));
						String location = ((String) getOutputTypeTable().getValueAt(0, 5));

						if (packageName != null && !packageName.equals("")) {
							output.setPackageName(packageName);
						}
						if (className != null && !className.equals("")) {
							output.setClassName(className);
						}
						if (isArray != null) {
							output.setIsArray(isArray);
						}
						if (namespace != null && !namespace.equals("")) {
							output.setNamespace(namespace);
							// cache the needed schemas in the schema dir
							cacheSchema(schemaDir, namespace);
						}
						if (type != null && !type.equals("")) {
							output.setType(type);
						}
						if (location != null && !location.equals("")) {
							output.setLocation(location);
						}

						method.setOutput(output);

					} catch (Exception ex) {
						PortalUtils.showErrorMessage(ex);
					}
					dispose();
				}

			});
		}
		return doneButton;
	}


	private void cacheSchema(File dir, String namespace) {
		IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
			IntroducePortalConf.RESOURCE);
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		try {
			XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(
				conf.getGME());
			handle.cacheSchema(new Namespace(namespace), dir);
		} catch (MobiusException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(MethodViewer.this,
				"Please check the GME URL and make sure that you have the appropriate credentials!");
		}

	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddInputParamButton() {
		if (addInputParamButton == null) {
			addInputParamButton = new JButton(IntroduceLookAndFeel.getAddIcon());
			addInputParamButton.setText("Add");
			addInputParamButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					final MethodTypeInputsInput input = new MethodTypeInputsInput();
					getInputParamTable().addRow(input);
					performModify(e);

				}
			});
		}
		return addInputParamButton;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNamePanel() {
		if (namePanel == null) {
			methodLabel = new JLabel();
			methodLabel.setText("Method Name");

			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			namePanel = new JPanel();
			namePanel.setLayout(new GridBagLayout());
			namePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Method Properties",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			namePanel.add(getNameField(), gridBagConstraints2);
			namePanel.add(methodLabel, gridBagConstraints);
		}
		return namePanel;
	}


	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNameField() {
		if (nameField == null) {
			nameField = new JTextField();
			nameField.setText(methodsTable.getMethodName(currentRow));
		}
		return nameField;
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
			removeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int row = getInputParamTable().getSelectedRow();
					if ((row < 0) || (row >= getInputParamTable().getRowCount())) {
						PortalUtils.showErrorMessage("Please select a parameter to remove.");
						return;
					}
					int oldSelectedRow = getInputParamTable().getSelectedRow();
					((DefaultTableModel) getInputParamTable().getModel()).removeRow(oldSelectedRow);
					if (oldSelectedRow == 0) {
						oldSelectedRow++;
					}
					if (getInputParamTable().getRowCount() > 0) {
						getInputParamTable().setRowSelectionInterval(oldSelectedRow - 1, oldSelectedRow - 1);
					}
				}
			});
		}
		return removeButton;
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDiscoveryButton() {
		if (discoveryButton == null) {
			discoveryButton = new JButton();
			IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
				IntroducePortalConf.RESOURCE);
			if (conf.getDiscoveryType().equals(IntroducePortalConf.GME_DISCOVERY)) {
				discoveryButton.setText("Populate from GME");
				discoveryButton.setIcon(IntroduceLookAndFeel.getMobiusIcon());
			} else if (conf.getDiscoveryType().equals(IntroducePortalConf.CADSR_DISCOVERY)) {
				discoveryButton.setText("Populate from caDSR");
				discoveryButton.setIcon(IntroduceLookAndFeel.getCADSRIcon());
			}

			discoveryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					performModify(e);
				}
			});
		}
		return discoveryButton;
	}


	public void performModify(java.awt.event.ActionEvent e) {

		int row = getInputParamTable().getSelectedRow();
		if ((row < 0) || (row >= getInputParamTable().getRowCount())) {
			PortalUtils.showErrorMessage("Please select a parameter to edit.");
			return;
		}
		Vector v = (Vector) getInputParamTable().getValueAt(getInputParamTable().getSelectedRow(), 8);
		getDiscoveryPanel().populateRow(v, true);
		paint(getGraphics());
	}


	/**
	 * This method initializes inputButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInputButtonPanel() {
		if (inputButtonPanel == null) {
			inputButtonPanel = new JPanel();
			inputButtonPanel.add(getAddInputParamButton(), null);
			inputButtonPanel.add(getRemoveButton(), null);
			inputButtonPanel.add(getDiscoveryButton(), null);
		}
		return inputButtonPanel;
	}


	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setIcon(IntroduceLookAndFeel.getCloseIcon());
		}
		return cancelButton;
	}


	/**
	 * This method initializes exceptionsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExceptionsPanel() {
		if (exceptionsPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.weightx = 0.0D;
			gridBagConstraints12.weighty = 0.0D;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints12.gridy = 0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weightx = 2.0D;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			exceptionsPanel = new JPanel();
			exceptionsPanel.setLayout(new GridBagLayout());
			exceptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Faults",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			exceptionsPanel.add(getExceptionScrollPane(), gridBagConstraints11);
			exceptionsPanel.add(getExceptionInputPanel(), gridBagConstraints12);
		}
		return exceptionsPanel;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getExceptionScrollPane() {
		if (exceptionScrollPane == null) {
			exceptionScrollPane = new JScrollPane();
			exceptionScrollPane.setViewportView(getExceptionsTable());
		}
		return exceptionScrollPane;
	}


	/**
	 * This method initializes exceptionInputPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExceptionInputPanel() {
		if (exceptionInputPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			exceptionInputPanel = new JPanel();
			exceptionInputPanel.setLayout(new GridBagLayout());
			exceptionInputPanel.add(getExceptionInputButtonPanel(), gridBagConstraints3);
		}
		return exceptionInputPanel;
	}


	/**
	 * This method initializes faultsTable
	 * 
	 * @return javax.swing.JTable
	 */
	private ExceptionsTable getExceptionsTable() {
		if (exceptionsTable == null) {
			exceptionsTable = new ExceptionsTable(this.method);
		}
		return exceptionsTable;
	}


	/**
	 * This method initializes addExceptionButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddExceptionButton() {
		if (addExceptionButton == null) {
			addExceptionButton = new JButton(IntroduceLookAndFeel.getAddIcon());
			addExceptionButton.setText("Add");
			addExceptionButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getExceptionsTable().addRow(new MethodTypeExceptionsException());
				}
			});
		}
		return addExceptionButton;
	}


	/**
	 * This method initializes removeExceptionButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveExceptionButton() {
		if (removeExceptionButton == null) {
			removeExceptionButton = new JButton(IntroduceLookAndFeel.getRemoveIcon());
			removeExceptionButton.setText("Remove");
			removeExceptionButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()");
					int row = getExceptionsTable().getSelectedRow();
					if ((row < 0) || (row >= getExceptionsTable().getRowCount())) {
						PortalUtils.showErrorMessage("Please select an exception to remove.");
						return;
					}
					((DefaultTableModel) getExceptionsTable().getModel()).removeRow(getExceptionsTable()
						.getSelectedRow());
				}
			});
		}
		return removeExceptionButton;
	}


	/**
	 * This method initializes tabbedPanel
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getTabbedPanel() {
		if (tabbedPanel == null) {
			tabbedPanel = new JTabbedPane();
			tabbedPanel.addTab("Method Signature", null, getMethodPanel(), null);
			tabbedPanel.addTab("Security", null, getSecurityContainerPanel(), null);
		}
		return tabbedPanel;
	}


	/**
	 * This method initializes methodPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMethodPanel() {
		if (methodPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints15.gridy = 0;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridy = 4;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.weighty = 0.0D;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.weighty = 1.0D;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridy = 3;
			gridBagConstraints7.weightx = 0.0D;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints7.weighty = 0.0D;
			gridBagConstraints7.ipady = 10;
			gridBagConstraints7.gridx = 0;
			methodPanel = new JPanel();
			methodPanel.setLayout(new GridBagLayout());
			methodPanel.add(getInputPanel(), gridBagConstraints6);
			methodPanel.add(getOutputTypePanel(), gridBagConstraints7);
			methodPanel.add(getNamePanel(), gridBagConstraints1);
			methodPanel.add(getExceptionsPanel(), gridBagConstraints13);
			methodPanel.add(getDiscoveryPanel(), gridBagConstraints15);
		}
		return methodPanel;
	}


	/**
	 * This method initializes exceptionInputButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExceptionInputButtonPanel() {
		if (exceptionInputButtonPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new java.awt.Insets(5, 3, 5, 5);
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 2);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = 0;
			exceptionInputButtonPanel = new JPanel();
			exceptionInputButtonPanel.setLayout(new GridBagLayout());
			exceptionInputButtonPanel.add(getRemoveExceptionButton(), gridBagConstraints4);
			exceptionInputButtonPanel.add(getAddExceptionButton(), gridBagConstraints5);
		}
		return exceptionInputButtonPanel;
	}


	/**
	 * This method initializes securityContainerPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecurityContainerPanel() {
		if (securityContainerPanel == null) {
			securityContainerPanel = new MethodSecurityPanel(this.serviceSecurity, this.method.getMethodSecurity());
			securityContainerPanel.setBorder(BorderFactory.createTitledBorder(null,
				"Method Level Security Configuration", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
		}
		return securityContainerPanel;
	}


	/**
	 * This method initializes discoveryPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private GMEParameterConfigurationComponent getDiscoveryPanel() {
		if (discoveryPanel == null) {
			discoveryPanel = new GMEParameterConfigurationComponent();
		}
		return discoveryPanel;
	}


	/**
	 * This method initializes outputTypeLoadFromDiscoveryButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOutputTypeLoadFromDiscoveryButton() {
		if (outputTypeLoadFromDiscoveryButton == null) {
			outputTypeLoadFromDiscoveryButton = new JButton();
			IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
				IntroducePortalConf.RESOURCE);
			if (conf.getDiscoveryType().equals(IntroducePortalConf.GME_DISCOVERY)) {
				outputTypeLoadFromDiscoveryButton.setText("Populate from GME");
				outputTypeLoadFromDiscoveryButton.setIcon(IntroduceLookAndFeel.getMobiusIcon());
			} else if (conf.getDiscoveryType().equals(IntroducePortalConf.CADSR_DISCOVERY)) {
				outputTypeLoadFromDiscoveryButton.setText("Populate from caDSR");
				outputTypeLoadFromDiscoveryButton.setIcon(IntroduceLookAndFeel.getCADSRIcon());
			}
			outputTypeLoadFromDiscoveryButton.setText("Populate From GME");
			outputTypeLoadFromDiscoveryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Vector v = (Vector) getOutputTypeTable().getValueAt(0, 6);
					getDiscoveryPanel().populateRow(v, false);
					paint(getGraphics());
				}
			});

		}
		return outputTypeLoadFromDiscoveryButton;
	}
} // @jve:decl-index=0:visual-constraint="4,12"

