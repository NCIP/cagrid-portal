package gov.nih.nci.cagrid.introduce.portal.modification.services.methods;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.security.MethodSecurityPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespaceTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeTreeNode;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.xml.namespace.QName;

import org.apache.axis.utils.JavaUtils;
import org.projectmobius.portal.GridPortalBaseFrame;

/**
 * MethodViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class MethodViewer extends GridPortalBaseFrame {

	MethodType method;

	private JPanel mainPanel = null;

	private JScrollPane inputParamScrollPanel = null;

	private InputParametersTable inputParamTable = null;

	private JScrollPane outputTypejScrollPane = null;

	private OutputTypeTable outputTypeTable = null;

	private JPanel inputPanel = null;

	private JPanel outputTypePanel = null;

	private JPanel buttonPanel = null;

	private JButton doneButton = null;

	private JButton addInputParamButton = null;

	private JPanel namePanel = null;

	private JTextField nameField = null;

	private JButton removeButton = null;

	private JLabel methodLabel = null;

	private JPanel inputButtonPanel = null;

	private JButton cancelButton = null;

	private JPanel exceptionsPanel = null;

	private JScrollPane exceptionScrollPane = null;

	private JPanel exceptionInputPanel = null;

	private ExceptionsTable exceptionsTable = null;

	private JButton addExceptionButton = null;

	private JButton removeExceptionButton = null;

	private JTabbedPane tabbedPanel = null;

	private JPanel methodPanel = null;

	private JPanel securityContainerPanel = null;

	private ServiceInformation info;

	private JTabbedPane configureTabbedPane = null;

	private JTextField exceptionEditText = null;

	private JPanel inputNamespacesPanel = null;

	private JScrollPane inputNamespaceScrollPane = null;

	private NamespacesJTree inputNamespaceTypesJTree = null;

	private JPanel methodPropertiesPanel = null;

	private JPanel outputNamespacePanel = null;

	private JScrollPane outputNamespacesTypeScrollPane = null;

	private NamespacesJTree outputNamespacesJTree = null;

	private JPanel outputTypesTablePanel = null;

	private JPanel inputTypesTablePanel = null;

	private JButton setOutputButton = null;

	private JPanel inputTableControlsPanel = null;

	private JLabel upButtonLabel = null;

	private JLabel downButtonLabel = null;

	private JPanel inputTableControlsPanel1 = null;

	private JLabel upLabel = null;

	private JLabel downLabel = null;

	private JButton clearOutputTypeButton = null;

	private JLabel faultNameLabel = null;

	private JPanel exceptionsInputButtonPanel = null;

	private JPanel importInformationPanel = null;

	private JLabel serviceName = null;

	private JLabel operationNameLabel = null;

	private JLabel wsdlFileLabel = null;

	private JLabel namespaceLabel = null;

	private JTextField namespaceTextField = null;

	private JTextField serviceNameTextField = null;

	private JTextField operationNameTextField = null;

	private JTextField wsdlFileTextField = null;

	private JCheckBox isImportedCheckBox = null;

	private JLabel isProvidedLabel = null;

	private JCheckBox isProvidedCheckBox = null;

	private JLabel providerClassLabel = null;

	private JTextField providerClassTextField = null;

	private JLabel packageNameLabel = null;

	private JTextField packageNameTextField = null;

	public MethodViewer(MethodType method, ServiceInformation info) {
		this.info = info;
		this.method = method;
		// this.methodsTable = table;
		// this.currentRow = selectedRow;
		this.setTitle("Modify Method");
		initialize();
	}

	private void initialize() {
		this.setContentPane(getMainPanel());
		this.setTitle("Build/Modify Operation");
		this.setFrameIcon(IntroduceLookAndFeel.getModifyIcon());
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.weighty = 0.0D;
			gridBagConstraints20.weightx = 1.0D;
			gridBagConstraints20.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints20.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 1;
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
			mainPanel.add(getMethodPropertiesPanel(), gridBagConstraints20);
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
			inputParamScrollPanel
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			inputParamScrollPanel
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
			outputTypejScrollPane
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			outputTypejScrollPane
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			outputTypejScrollPane.setViewportView(getOutputTypeTable());
		}
		return outputTypejScrollPane;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private OutputTypeTable getOutputTypeTable() {
		if (outputTypeTable == null) {
			outputTypeTable = new OutputTypeTable(this.method);
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
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridx = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			inputPanel = new JPanel();
			inputPanel.setLayout(new GridBagLayout());
			inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Input Parameters",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					IntroduceLookAndFeel.getPanelLabelColor()));
			inputPanel.add(getInputNamespacesPanel(), gridBagConstraints7);
			inputPanel.add(getInputTypesTablePanel(), gridBagConstraints8);
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
			gridBagConstraints16.gridx = 1;
			gridBagConstraints16.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints16.weighty = 1.0D;
			gridBagConstraints16.weightx = 1.0D;
			gridBagConstraints16.gridy = 0;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints17.weightx = 1.0D;
			gridBagConstraints17.weighty = 1.0D;
			gridBagConstraints17.gridy = 0;
			outputTypePanel = new JPanel();
			outputTypePanel.setLayout(new GridBagLayout());
			outputTypePanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Output Type",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, IntroduceLookAndFeel
											.getPanelLabelColor()));
			outputTypePanel
					.add(getOutputNamespacePanel(), gridBagConstraints17);
			outputTypePanel.add(getOutputTypesTablePanel(),
					gridBagConstraints16);
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
			doneButton = new JButton(IntroduceLookAndFeel.getDoneIcon());
			doneButton.setText("Done");
			doneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// First process the inputs
					boolean valid = true;
					String message = "";

					try {
						method.setName(getNameField().getText());

						method
								.setMethodSecurity(((MethodSecurityPanel) securityContainerPanel)
										.getMethodSecurity());

						// methodsTable.refreshRowFromMethodType(currentRow);

						// process the inputs
						MethodTypeInputs inputs = new MethodTypeInputs();
						MethodTypeInputsInput[] inputsA = new MethodTypeInputsInput[getInputParamTable()
								.getRowCount()];

						List usedNames = new ArrayList();
						for (int i = 0; i < getInputParamTable().getRowCount(); i++) {
							MethodTypeInputsInput input = getInputParamTable()
									.getRowData(i);
							// validate the input param
							if (usedNames.contains(input.getName())) {
								valid = false;
								message = "Method "
										+ method.getName()
										+ " contains more that one parameter named "
										+ input.getName();
							}
							usedNames.add(input.getName());
							if (!JavaUtils.isJavaId(input.getName())) {
								valid = false;
								message = "Parameter name must be a valid java identifier: Method: "
										+ method.getName()
										+ " param: "
										+ input.getName();
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
							MethodTypeExceptionsException exception = getExceptionsTable()
									.getRowData(i);
							exceptionsA[i] = exception;
						}
						exceptions.setException(exceptionsA);
						method.setExceptions(exceptions);

						// now process the output
						MethodTypeOutput output = getOutputTypeTable()
								.getRowData(0);
						method.setOutput(output);

						if (getIsImportedCheckBox().isSelected()) {
							// process the import information
							method.setIsImported(true);
							if (getIsImportedCheckBox().isSelected()) {
								MethodTypeImportInformation importInfo = new MethodTypeImportInformation();
								importInfo.setNamespace(getNamespaceTextField()
										.getText());
								importInfo
										.setPortTypeName(getServiceNameTextField()
												.getText());
								importInfo
										.setPackageName(getPackageNameTextField()
												.getText());
								importInfo
										.setOperationName(getOperationNameTextField()
												.getText());
								importInfo.setWsdlFile(getWsdlFileTextField()
										.getText());
								importInfo
										.setIsProvided(getIsProvidedCheckBox()
												.isSelected());
								importInfo
										.setProviderClass(getProviderClassTextField()
												.getText());
								method.setImportInformation(importInfo);
							}
						}

					} catch (Exception ex) {
						PortalUtils.showErrorMessage(ex);
					}
					if (!valid) {
						JOptionPane.showMessageDialog(MethodViewer.this,
								message);
					} else {
						dispose();
					}
				}

			});
		}
		return doneButton;
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
			addInputParamButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (getInputNamespaceTypesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
								NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getInputNamespaceTypesJTree()
										.getCurrentNode().getParent())
										.getUserObject());
								SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getInputNamespaceTypesJTree()
										.getCurrentNode()).getUserObject());
								MethodTypeInputsInput input = new MethodTypeInputsInput();
								input.setQName(new QName(nt.getNamespace(), st
										.getType()));
								input.setIsArray(false);
								input.setName(JavaUtils.xmlNameToJava(st
										.getType()));
								getInputParamTable().addRow(input);
							} else {
								JOptionPane.showMessageDialog(
										MethodViewer.this,
										"Please select a type to add");
							}
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
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			gridBagConstraints110.gridx = 2;
			gridBagConstraints110.insets = new java.awt.Insets(0, 30, 0, 0);
			gridBagConstraints110.gridy = 0;
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
			namePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Method Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					IntroduceLookAndFeel.getPanelLabelColor()));
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			namePanel.add(getNameField(), gridBagConstraints2);
			namePanel.add(methodLabel, gridBagConstraints);
			namePanel.add(getIsImportedCheckBox(), gridBagConstraints110);
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
			nameField.setText(method.getName());
			// nameField.setText(methodsTable.getSelectedMethodType().getName());
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
					try {
						getInputParamTable().removeSelectedRow();
					} catch (Exception ex) {
						PortalUtils
								.showErrorMessage("Please select an input parameter to Remove");
					}
				}
			});
		}
		return removeButton;
	}

	public void performModify(java.awt.event.ActionEvent e) {
		try {
			// getInputParamTable().modifySelectedRow(getDiscoveryPanel().createInput());
		} catch (Exception e1) {
			PortalUtils.showErrorMessage("Please select a parameter to edit.");
		}
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
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.weightx = 0.0D;
			gridBagConstraints12.weighty = 0.0D;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weightx = 2.0D;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			exceptionsPanel = new JPanel();
			exceptionsPanel.setLayout(new GridBagLayout());
			exceptionsPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Faults",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, IntroduceLookAndFeel
											.getPanelLabelColor()));
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
			exceptionScrollPane
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 1;
			gridBagConstraints27.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints27.gridheight = 2;
			gridBagConstraints27.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.SOUTHWEST;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 0;
			faultNameLabel = new JLabel();
			faultNameLabel.setText("Fault Name:");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 1;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 10);
			gridBagConstraints15.gridheight = 1;
			exceptionInputPanel = new JPanel();
			exceptionInputPanel.setLayout(new GridBagLayout());
			exceptionInputPanel.add(getExceptionEditText(),
					gridBagConstraints15);
			exceptionInputPanel.add(faultNameLabel, gridBagConstraints3);
			exceptionInputPanel.add(getExceptionsInputButtonPanel(),
					gridBagConstraints27);
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
			addExceptionButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							getExceptionsTable().addRow(
									getExceptionEditText().getText());
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
			removeExceptionButton = new JButton(IntroduceLookAndFeel
					.getRemoveIcon());
			removeExceptionButton.setText("Remove");
			removeExceptionButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							try {
								getExceptionsTable().removeSelectedRow();
							} catch (Exception ex) {
								PortalUtils
										.showErrorMessage("Please select an exception to Remove");
							}
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
			tabbedPanel
					.addTab("Method Signature", null, getMethodPanel(), null);
			tabbedPanel.addTab("Security", null, getSecurityContainerPanel(),
					null);
			tabbedPanel.addTab("Import Information", null,
					getImportInformationPanel(), null);
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
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			methodPanel = new JPanel();
			methodPanel.setLayout(new GridBagLayout());
			methodPanel.add(getConfigureTabbedPane(), gridBagConstraints1);
		}
		return methodPanel;
	}

	/**
	 * This method initializes securityContainerPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecurityContainerPanel() {
		if (securityContainerPanel == null) {
			securityContainerPanel = new MethodSecurityPanel(info.getServices()
					.getService(0).getServiceSecurity(), this.method
					.getMethodSecurity());
			securityContainerPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Method Level Security Configuration",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel
							.getPanelLabelColor()));
		}
		return securityContainerPanel;
	}

	/**
	 * This method initializes configureTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getConfigureTabbedPane() {
		if (configureTabbedPane == null) {
			configureTabbedPane = new JTabbedPane();
			configureTabbedPane.addTab("Inputs", null, getInputPanel(), null);
			configureTabbedPane.addTab("Output", null, getOutputTypePanel(),
					null);
			configureTabbedPane.addTab("Faults", null, getExceptionsPanel(),
					null);

		}
		return configureTabbedPane;
	}

	/**
	 * This method initializes exceptionEditText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getExceptionEditText() {
		if (exceptionEditText == null) {
			exceptionEditText = new JTextField();
		}
		return exceptionEditText;
	}

	/**
	 * This method initializes namespacesPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInputNamespacesPanel() {
		if (inputNamespacesPanel == null) {
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints19.weighty = 1.0;
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridy = 0;
			gridBagConstraints19.insets = new java.awt.Insets(0, 0, 0, 0);
			gridBagConstraints19.weightx = 1.0;
			inputNamespacesPanel = new JPanel();
			inputNamespacesPanel.setLayout(new GridBagLayout());
			inputNamespacesPanel.add(getInputNamespaceScrollPane(),
					gridBagConstraints19);
		}
		return inputNamespacesPanel;
	}

	/**
	 * This method initializes namespaceScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getInputNamespaceScrollPane() {
		if (inputNamespaceScrollPane == null) {
			inputNamespaceScrollPane = new JScrollPane();
			inputNamespaceScrollPane
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			inputNamespaceScrollPane
					.setViewportView(getInputNamespaceTypesJTree());
		}
		return inputNamespaceScrollPane;
	}

	/**
	 * This method initializes namespaceTypesJTree
	 * 
	 * @return javax.swing.JTree
	 */
	private NamespacesJTree getInputNamespaceTypesJTree() {
		if (inputNamespaceTypesJTree == null) {
			inputNamespaceTypesJTree = new NamespacesJTree(
					info.getNamespaces(), true);
			inputNamespaceTypesJTree.addMouseListener(new MouseListener() {

				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						if (getInputNamespaceTypesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
							NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getInputNamespaceTypesJTree()
									.getCurrentNode().getParent())
									.getUserObject());
							SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getInputNamespaceTypesJTree()
									.getCurrentNode()).getUserObject());
							MethodTypeInputsInput input = new MethodTypeInputsInput();
							input.setQName(new QName(nt.getNamespace(), st
									.getType()));
							input.setIsArray(false);
							input
									.setName(JavaUtils.xmlNameToJava(st
											.getType()));
							getInputParamTable().addRow(input);
						}
					}

				}

			});
		}
		return inputNamespaceTypesJTree;
	}

	/**
	 * This method initializes methodPropertiesPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMethodPropertiesPanel() {
		if (methodPropertiesPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.weightx = 1.0D;
			gridBagConstraints13.weighty = 0.0D;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			methodPropertiesPanel = new JPanel();
			methodPropertiesPanel.setLayout(new GridBagLayout());
			methodPropertiesPanel.add(getNamePanel(), gridBagConstraints13);
		}
		return methodPropertiesPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOutputNamespacePanel() {
		if (outputNamespacePanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.weightx = 1.0;
			outputNamespacePanel = new JPanel();
			outputNamespacePanel.setLayout(new GridBagLayout());
			outputNamespacePanel.add(getOutputNamespacesTypeScrollPane(),
					gridBagConstraints6);
		}
		return outputNamespacePanel;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getOutputNamespacesTypeScrollPane() {
		if (outputNamespacesTypeScrollPane == null) {
			outputNamespacesTypeScrollPane = new JScrollPane();
			outputNamespacesTypeScrollPane
					.setViewportView(getOutputNamespacesJTree());
		}
		return outputNamespacesTypeScrollPane;
	}

	/**
	 * This method initializes outputNamespacesJTree
	 * 
	 * @return javax.swing.JTree
	 */
	private NamespacesJTree getOutputNamespacesJTree() {
		if (outputNamespacesJTree == null) {
			outputNamespacesJTree = new NamespacesJTree(info.getNamespaces(),
					true);
			outputNamespacesJTree.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					if (e.getClickCount() == 2) {
						if (getOutputNamespacesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
							NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getOutputNamespacesJTree()
									.getCurrentNode().getParent())
									.getUserObject());
							SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getOutputNamespacesJTree()
									.getCurrentNode()).getUserObject());
							MethodTypeOutput output = new MethodTypeOutput();
							output.setQName(new QName(nt.getNamespace(), st
									.getType()));
							output.setIsArray(false);
							try {
								getOutputTypeTable().modifyRow(0, output);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}

				}

				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

			});
		}
		return outputNamespacesJTree;
	}

	/**
	 * This method initializes outputTypesTablePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOutputTypesTablePanel() {
		if (outputTypesTablePanel == null) {
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 1;
			gridBagConstraints26.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints26.gridy = 0;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints23.gridy = 0;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.gridy = 1;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.weighty = 1.0;
			gridBagConstraints22.gridwidth = 3;
			gridBagConstraints22.insets = new java.awt.Insets(2, 2, 2, 2);
			outputTypesTablePanel = new JPanel();
			outputTypesTablePanel.setLayout(new GridBagLayout());
			outputTypesTablePanel.add(getOutputTypejScrollPane(),
					gridBagConstraints22);
			outputTypesTablePanel.add(getSetOutputButton(),
					gridBagConstraints23);
			outputTypesTablePanel.add(getClearOutputTypeButton(),
					gridBagConstraints26);
		}
		return outputTypesTablePanel;
	}

	/**
	 * This method initializes inputTypesTablePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInputTypesTablePanel() {
		if (inputTypesTablePanel == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.fill = java.awt.GridBagConstraints.VERTICAL;
			gridBagConstraints18.gridy = 0;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridwidth = 2;
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.gridy = 1;
			gridBagConstraints14.weightx = 0.0D;
			gridBagConstraints14.weighty = 0.0D;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 0;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.weighty = 1.0;
			gridBagConstraints21.insets = new java.awt.Insets(0, 0, 0, 0);
			inputTypesTablePanel = new JPanel();
			inputTypesTablePanel.setLayout(new GridBagLayout());
			inputTypesTablePanel.add(getInputParamScrollPanel(),
					gridBagConstraints21);
			inputTypesTablePanel.add(getInputButtonPanel(),
					gridBagConstraints14);
			inputTypesTablePanel.add(getInputTableControlsPanel1(),
					gridBagConstraints18);
		}
		return inputTypesTablePanel;
	}

	/**
	 * This method initializes setOutputButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSetOutputButton() {
		if (setOutputButton == null) {
			setOutputButton = new JButton();
			setOutputButton.setText("Set Output Type");
			setOutputButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (getOutputNamespacesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
								NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getOutputNamespacesJTree()
										.getCurrentNode().getParent())
										.getUserObject());
								SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getOutputNamespacesJTree()
										.getCurrentNode()).getUserObject());
								MethodTypeOutput output = new MethodTypeOutput();
								output.setQName(new QName(nt.getNamespace(), st
										.getType()));
								output.setIsArray(false);
								try {
									getOutputTypeTable().modifyRow(0, output);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					});
		}
		return setOutputButton;
	}

	/**
	 * This method initializes inputTableControlsPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInputTableControlsPanel1() {
		if (inputTableControlsPanel1 == null) {
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 0;
			gridBagConstraints25.gridy = 0;
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 0;
			gridBagConstraints24.gridy = 1;
			downLabel = new JLabel();
			downLabel.setText("");
			downLabel.setIcon(IntroduceLookAndFeel.getDownIcon());
			downLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					try {
						getInputParamTable().moveSelectedRowDown();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			});
			upLabel = new JLabel();
			upLabel.setText("");
			upLabel.setIcon(IntroduceLookAndFeel.getUpIcon());
			upLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					try {
						getInputParamTable().moveSelectedRowUp();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			});
			inputTableControlsPanel1 = new JPanel();
			inputTableControlsPanel1.setLayout(new GridBagLayout());
			inputTableControlsPanel1.add(upLabel, gridBagConstraints25);
			inputTableControlsPanel1.add(downLabel, gridBagConstraints24);
		}
		return inputTableControlsPanel1;
	}

	/**
	 * This method initializes clearOutputTypeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getClearOutputTypeButton() {
		if (clearOutputTypeButton == null) {
			clearOutputTypeButton = new JButton();
			clearOutputTypeButton.setText("Clear Output Type");
			clearOutputTypeButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							MethodTypeOutput output = new MethodTypeOutput();
							output.setQName(new QName("", "void"));
							try {
								getOutputTypeTable().modifyRow(0, output);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});
		}
		return clearOutputTypeButton;
	}

	/**
	 * This method initializes exceptionsInputButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getExceptionsInputButtonPanel() {
		if (exceptionsInputButtonPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.gridx = 0;
			exceptionsInputButtonPanel = new JPanel();
			exceptionsInputButtonPanel.setLayout(new GridBagLayout());
			exceptionsInputButtonPanel.add(getAddExceptionButton(),
					gridBagConstraints4);
			exceptionsInputButtonPanel.add(getRemoveExceptionButton(),
					gridBagConstraints5);
		}
		return exceptionsInputButtonPanel;
	}

	/**
	 * This method initializes importInformationPanel
	 * 
	 * @ret p providerClassNameLabel.setText("JLabel");
	 *      gridBagConstraints38.insets = new java.awt.Insets(2,2,2,2);
	 *      importInformationPanel.add(getProviderClassTextField(),
	 *      gridBagConstraints38); providerClassLabel.setText("Provider Cl
	 *      importInformationPanel.add(providerClassNameLabel,
	 *      gridBagConstraints40); ass"); gridBagConstraints38.fill =
	 *      java.awt.GridBagConstraints.HORIZONTAL; gridBagConstraints38.insets =
	 *      new java.awt.Insets(2,2,2,2);
	 *      importInformationPanel.add(providerClassLabel,
	 *      gridBagConstraints38);
	 *      importInformationPanel.add(getProviderClassTextField(),
	 *      gridBagConstraints40); roviderClassLabel.setText("Provider Class");
	 *      gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
	 *      gridBagConstraints41.insets = new java.awt.Insets(2,2,2,2);
	 *      importInformationPanel.add(providerClassLabel,
	 *      gridBagConstraints41); gridBagConstraints40.insets = new
	 *      java.awt.Insets(2,2,2,2);
	 *      importInformationPanel.add(getProviderClassTextField(),
	 *      gridBagConstraints40); urn javax.swing.JPanel
	 */
	private JPanel getImportInformationPanel() {
		if (importInformationPanel == null) {
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 1;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints41.gridx = 1;
			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.gridx = 0;
			gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints40.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints40.gridy = 1;
			packageNameLabel = new JLabel();
			packageNameLabel.setText("Package Name");
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints39.gridy = 6;
			gridBagConstraints39.weightx = 1.0;
			gridBagConstraints39.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints39.gridx = 1;
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.gridx = 0;
			gridBagConstraints38.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints38.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints38.gridy = 6;
			providerClassLabel = new JLabel();
			providerClassLabel.setText("Provider Class");
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.gridx = 1;
			gridBagConstraints37.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints37.gridy = 5;
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.gridx = 0;
			gridBagConstraints36.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints36.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints36.gridy = 5;
			isProvidedLabel = new JLabel();
			isProvidedLabel.setText("Is Provided");
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints35.gridy = 4;
			gridBagConstraints35.weightx = 1.0;
			gridBagConstraints35.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints35.gridx = 1;
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints34.gridy = 3;
			gridBagConstraints34.weightx = 1.0;
			gridBagConstraints34.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints34.gridx = 1;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints33.gridy = 2;
			gridBagConstraints33.weightx = 1.0;
			gridBagConstraints33.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints33.gridx = 1;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints32.gridy = 0;
			gridBagConstraints32.weightx = 1.0;
			gridBagConstraints32.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints32.gridx = 1;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints31.gridy = 0;
			namespaceLabel = new JLabel();
			namespaceLabel.setText("Namepace");
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.gridx = 0;
			gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridy = 4;
			wsdlFileLabel = new JLabel();
			wsdlFileLabel.setText("WSDL File");
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints29.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints29.gridy = 3;
			operationNameLabel = new JLabel();
			operationNameLabel.setText("Operation");
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 0;
			gridBagConstraints28.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints28.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints28.gridy = 2;
			serviceName = new JLabel();
			serviceName.setText("PortType");
			importInformationPanel = new JPanel();
			importInformationPanel.setLayout(new GridBagLayout());
			importInformationPanel.add(serviceName, gridBagConstraints28);
			importInformationPanel
					.add(operationNameLabel, gridBagConstraints29);
			importInformationPanel.add(wsdlFileLabel, gridBagConstraints30);
			importInformationPanel.add(namespaceLabel, gridBagConstraints31);
			importInformationPanel.add(getNamespaceTextField(),
					gridBagConstraints32);
			importInformationPanel.add(getServiceNameTextField(),
					gridBagConstraints33);
			importInformationPanel.add(getOperationNameTextField(),
					gridBagConstraints34);
			importInformationPanel.add(getWsdlFileTextField(),
					gridBagConstraints35);
			importInformationPanel.add(isProvidedLabel, gridBagConstraints36);
			importInformationPanel.add(getIsProvidedCheckBox(),
					gridBagConstraints37);
			importInformationPanel
					.add(providerClassLabel, gridBagConstraints38);
			importInformationPanel.add(getProviderClassTextField(),
					gridBagConstraints39);
			importInformationPanel.add(packageNameLabel, gridBagConstraints40);
			importInformationPanel.add(getPackageNameTextField(),
					gridBagConstraints41);
		}
		return importInformationPanel;
	}

	/**
	 * This method initializes namespaceTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNamespaceTextField() {
		if (namespaceTextField == null) {
			namespaceTextField = new JTextField();
			if (method.getImportInformation() != null) {
				namespaceTextField.setText(method.getImportInformation()
						.getNamespace());
			}
		}
		return namespaceTextField;
	}

	/**
	 * This method initializes serviceNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getServiceNameTextField() {
		if (serviceNameTextField == null) {
			serviceNameTextField = new JTextField();
			if (method.getImportInformation() != null) {
				serviceNameTextField.setText(method.getImportInformation()
						.getPortTypeName());
			}
		}
		return serviceNameTextField;
	}

	/**
	 * This method initializes operationNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getOperationNameTextField() {
		if (operationNameTextField == null) {
			operationNameTextField = new JTextField();
		}
		if (method.getImportInformation() != null) {
			operationNameTextField.setText(method.getImportInformation()
					.getOperationName());
		}
		return operationNameTextField;
	}

	/**
	 * This method initializes wsdlFileTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getWsdlFileTextField() {
		if (wsdlFileTextField == null) {
			wsdlFileTextField = new JTextField();
			if (method.getImportInformation() != null) {
				wsdlFileTextField.setText(method.getImportInformation()
						.getWsdlFile());
			}
		}
		return wsdlFileTextField;
	}

	/**
	 * This method initializes isImportedCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsImportedCheckBox() {
		if (isImportedCheckBox == null) {
			isImportedCheckBox = new JCheckBox();
			isImportedCheckBox.setText("imported");
			isImportedCheckBox.setSelected(method.isIsImported());
		}
		return isImportedCheckBox;
	}

	/**
	 * This method initializes isProvidedCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsProvidedCheckBox() {
		if (isProvidedCheckBox == null) {
			isProvidedCheckBox = new JCheckBox();
			if (method.getImportInformation() != null) {
				isProvidedCheckBox.setSelected(method.getImportInformation()
						.isIsProvided());
			}
		}
		return isProvidedCheckBox;
	}

	/**
	 * This method initializes providerClassTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getProviderClassTextField() {
		if (providerClassTextField == null) {
			providerClassTextField = new JTextField();
			if (method.getImportInformation() != null) {
				providerClassTextField.setText(method.getImportInformation()
						.getProviderClass());
			}
		}
		return providerClassTextField;
	}

	/**
	 * This method initializes packageNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getPackageNameTextField() {
		if (packageNameTextField == null) {
			packageNameTextField = new JTextField();
			if (method.getImportInformation() != null) {
				packageNameTextField.setText(method.getImportInformation()
						.getPackageName());
			}
		}
		return packageNameTextField;
	}
} // @jve:decl-index=0:visual-constraint="4,12"

