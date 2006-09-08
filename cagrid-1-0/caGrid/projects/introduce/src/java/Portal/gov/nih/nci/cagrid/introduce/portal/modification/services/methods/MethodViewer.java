package gov.nih.nci.cagrid.introduce.portal.modification.services.methods;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.modification.security.MethodSecurityPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespaceTypeTreeNode;
import gov.nih.nci.cagrid.introduce.portal.modification.types.NamespacesJTree;
import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeTreeNode;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
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

	public class ExceptionHolder implements Comparable {
		boolean isCreated;

		QName qname;


		public ExceptionHolder(QName qname, boolean isCreated) {
			this.qname = qname;
			this.isCreated = isCreated;
		}


		public int compareTo(Object arg0) {
			return this.qname.toString().compareTo(((QName) arg0).toString());
		}


		public String toString() {
			return qname.toString();
		}

	}

	private MethodType method;

	private JPanel mainPanel = null;

	private JScrollPane inputParamScrollPanel = null;

	private InputParametersTable inputParamTable = null;

	private JScrollPane outputTypejScrollPane = null;

	private OutputTypeTable outputTypeTable = null;

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

	private SpecificServiceInformation info;

	private JTabbedPane configureTabbedPane = null;

	private JComboBox exceptionJComboBox = null;

	private JPanel inputNamespacesPanel = null;

	private JScrollPane inputNamespaceScrollPane = null;

	private NamespacesJTree inputNamespaceTypesJTree = null;

	private JPanel methodPropertiesPanel = null;

	private JPanel outputNamespacePanel = null;

	private JScrollPane outputNamespacesTypeScrollPane = null;

	private NamespacesJTree outputNamespacesJTree = null;

	private JPanel outputTypesTablePanel = null;

	private JPanel inputTypesTablePanel = null;

	private JPanel inputTableControlsPanel = null;

	private JLabel upLabel = null;

	private JLabel downLabel = null;

	private JButton clearOutputTypeButton = null;

	private JPanel exceptionsInputButtonPanel = null;

	private JPanel importInformationPanel = null;

	private JLabel serviceName = null;

	private JLabel wsdlFileLabel = null;

	private JLabel namespaceLabel = null;

	private JTextField namespaceTextField = null;

	private JTextField serviceNameTextField = null;

	private JTextField wsdlFileTextField = null;

	private JCheckBox isImportedCheckBox = null;

	private JLabel packageNameLabel = null;

	private JTextField packageNameTextField = null;

	private JCheckBox isProvidedCheckBox = null;

	private JScrollPane servicesTypeScrollPane = null;

	private ServicesTable servicesTypeTable = null;

	private JTextField providerClassnameTextField = null;

	private JPanel providerInformationPanel = null;

	private JLabel providerClassnameLabel = null;

	private JSplitPane inputParamsSplitPane = null;

	private JSplitPane outputTypeSplitPane = null;

	private JPanel messagePanel = null;

	private JLabel inputLabel = null;

	private JLabel outputLabel = null;

	private JTextField inputMessageNamespaceTextField = null;

	private JTextField outputMessageNamespaceTextField = null;

	private JCheckBox messagesCheckBox = null;

	private JTextField inputMessageNameTextField = null;

	private JTextField outputMessageNameTextField = null;

	private JPanel createFaultPanel = null;

	private JTextField newFaultNameTextField = null;

	private JButton createFaultButton = null;

	private JLabel faultTypeNameLabel = null;

	private JLabel existingExceptionLabel = null;

	private JSplitPane exceptionsPanelSplitPane = null;

	private JScrollPane exceptionNamespacesScrollPane = null;

	private NamespacesJTree namespacesJTree = null;

	private JPanel faultsFromTypesPanel = null;

	private JButton addFaultFromTypeButton = null;

	private JPanel removeFaultPanel = null;


	public MethodViewer(MethodType method, SpecificServiceInformation info) {
		this.info = info;
		this.method = method;
		this.setTitle("Modify Method");
		initialize();
	}


	private void initialize() {
		this.setContentPane(getMainPanel());
		this.setTitle("Build/Modify Operation");
		this.setSize(new java.awt.Dimension(683, 469));
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
			inputParamScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			inputParamScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
			outputTypejScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			outputTypejScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
	public JButton getDoneButton() {
		if (doneButton == null) {
			doneButton = new JButton(IntroduceLookAndFeel.getDoneIcon());
			doneButton.setText("Done");
			doneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// First process the inputs
					boolean valid = true;
					String message = "";

					List usedNames = new ArrayList();
					MethodsType methodsType = info.getService().getMethods();
					if (methodsType != null) {
						MethodType methods[] = methodsType.getMethod();
						if (methods != null) {
							for (int j = 0; j < methods.length; j++) {
								MethodType tmethod = methods[j];
								if (!usedNames.contains(tmethod.getName())) {
									usedNames.add(tmethod.getName());
								} else {
									valid = false;
									message = "Method name is not unique: " + tmethod.getName();
								}
							}
						}
					}

					if (!method.getName().equals(getNameField().getText())) {
						if (usedNames.contains(getNameField().getText())) {
							valid = false;
							message = "Method name is not unique: " + getNameField().getText();
						}
					}

					try {
						method.setName(getNameField().getText());

						method.setMethodSecurity(((MethodSecurityPanel) securityContainerPanel).getMethodSecurity());

						// process the inputs
						MethodTypeInputs inputs = new MethodTypeInputs();
						MethodTypeInputsInput[] inputsA = new MethodTypeInputsInput[getInputParamTable().getRowCount()];

						usedNames = new ArrayList();
						for (int i = 0; i < getInputParamTable().getRowCount(); i++) {
							MethodTypeInputsInput input = getInputParamTable().getRowData(i);
							// validate the input param
							if (usedNames.contains(input.getName())) {
								valid = false;
								message = "Method " + method.getName() + " contains more that one parameter named "
									+ input.getName();
							}
							usedNames.add(input.getName());
							if (!JavaUtils.isJavaId(input.getName())) {
								valid = false;
								message = "Parameter name must be a valid java identifier: Method: " + method.getName()
									+ " param: " + input.getName();
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
							MethodTypeExceptionsException exception = getExceptionsTable().getRowData(i);
							exceptionsA[i] = exception;
						}
						exceptions.setException(exceptionsA);
						method.setExceptions(exceptions);

						// now process the output
						MethodTypeOutput output = getOutputTypeTable().getRowData(0);
						method.setOutput(output);

						if (getIsImportedCheckBox().isSelected() && getIsProvidedCheckBox().isSelected()) {
							method.setIsProvided(true);
							MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
							pi.setProviderClass(getProviderClassnameTextField().getText());
							method.setProviderInformation(pi);
						} else {
							method.setIsProvided(false);
						}

						if (getIsImportedCheckBox().isSelected()) {
							// process the import information
							method.setIsImported(true);
							if (getIsImportedCheckBox().isSelected()) {
								MethodTypeImportInformation importInfo = new MethodTypeImportInformation();
								importInfo.setNamespace(getNamespaceTextField().getText());
								importInfo.setPortTypeName(getServiceNameTextField().getText());
								importInfo.setPackageName(getPackageNameTextField().getText());
								importInfo.setWsdlFile(getWsdlFileTextField().getText());
								if (!getInputMessageNamespaceTextField().getText().equals("")
									&& !getInputMessageNameTextField().getText().equals("")) {
									importInfo.setInputMessage(new QName(getInputMessageNamespaceTextField().getText(),
										getInputMessageNameTextField().getText()));
								}
								if (!getOutputMessageNamespaceTextField().getText().equals("")
									&& !getOutputMessageNameTextField().getText().equals("")) {
									importInfo.setOutputMessage(new QName(getOutputMessageNamespaceTextField()
										.getText(), getOutputMessageNameTextField().getText()));
								}
								method.setImportInformation(importInfo);
							}
						} else {
							method.setIsImported(false);
						}

					} catch (Exception ex) {
						// PortalUtils.showErrorMessage(ex);
						ErrorDialog.showErrorDialog(ex);
					}
					if (!valid) {
						JOptionPane.showMessageDialog(MethodViewer.this, message);
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
			addInputParamButton = new JButton(PortalLookAndFeel.getAddIcon());
			addInputParamButton.setText("Add");
			addInputParamButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getInputNamespaceTypesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
						NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getInputNamespaceTypesJTree()
							.getCurrentNode().getParent()).getUserObject());
						SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getInputNamespaceTypesJTree()
							.getCurrentNode()).getUserObject());
						MethodTypeInputsInput input = new MethodTypeInputsInput();
						input.setQName(new QName(nt.getNamespace(), st.getType()));
						input.setIsArray(false);
						input.setName(JavaUtils.xmlNameToJava(st.getType()));
						getInputParamTable().addRow(input);
					} else {
						JOptionPane.showMessageDialog(MethodViewer.this, "Please select a type to add");
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
			gridBagConstraints.gridheight = 2;
			gridBagConstraints.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			namePanel = new JPanel();
			namePanel.setLayout(new GridBagLayout());
			namePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Method Properties",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridheight = 2;
			gridBagConstraints2.gridwidth = 1;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
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
			removeButton = new JButton(PortalLookAndFeel.getRemoveIcon());
			removeButton.setText("Remove");
			removeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						getInputParamTable().removeSelectedRow();
					} catch (Exception ex) {
						PortalUtils.showErrorMessage("Please select an input parameter to Remove");
					}
				}
			});
		}
		return removeButton;
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
			cancelButton.setIcon(PortalLookAndFeel.getCloseIcon());
		}
		return cancelButton;
	}


	/**
	 * This method initializes exceptionsPanel
	 * 
	 * @return jav gridBagConstraints50.gridy = 2;
	 *         exceptionsPanel.add(getExceptionInputPanel(),
	 *         gridBagConstraints50); gridBagConstraints49.fill =
	 *         java.awt.GridBagConstraints.BOTH;
	 *         exceptionsPanel.add(getCreateFaultPanel(), gridBagConstraints49);
	 *         gridBagConstraints49.gridy = 3;
	 *         exceptionsPanel.add(getFaultsFromTypesPanel(),
	 *         gridBagConstraints52); gridBagConstraints49.gridy = 2;
	 *         gridBagConstraints50.gridy = 3; gridBagConstraints50.fill =
	 *         java.awt.GridBagConstraints.BOTH;
	 *         exceptionsPanel.add(getCreateFaultPanel(), gridBagConstraints50);
	 *         gridBagConstraints49.fill = java.awt.GridBagConstraints.BOTH;
	 *         exceptionsPanel.add(getExceptionInputPanel(),
	 *         gridBagConstraints49); gridBagConstraints52.fill =
	 *         java.awt.GridBagConstraints.BOTH;
	 *         exceptionsPanel.add(getFaultsFromTypesPanel(),
	 *         gridBagConstraints52);
	 *         exceptionsPanel.add(getExceptionInputPanel(),
	 *         gridBagConstraints12); exceptionsPanel.add(getCreateFaultPanel(),
	 *         gridBagConstraints46); ax.swing.JPanel
	 */
	private JPanel getExceptionsPanel() {
		if (exceptionsPanel == null) {
			GridBagConstraints gridBagConstraints49 = new GridBagConstraints();
			gridBagConstraints49.gridx = 1;
			gridBagConstraints49.gridheight = 3;
			gridBagConstraints49.gridy = 1;
			GridBagConstraints gridBagConstraints46 = new GridBagConstraints();
			gridBagConstraints46.gridx = 0;
			gridBagConstraints46.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints46.weightx = 1.0D;
			gridBagConstraints46.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.gridx = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints12.gridy = 3;
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.weightx = 1.0;
			exceptionsPanel = new JPanel();
			exceptionsPanel.setLayout(new GridBagLayout());
			exceptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Faults",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			exceptionsPanel.add(getExceptionsPanelSplitPane(), gridBagConstraints3);
			exceptionsPanel.add(getCreateFaultPanel(), gridBagConstraints12);
			exceptionsPanel.add(getExceptionInputPanel(), gridBagConstraints11);
			exceptionsPanel.add(getFaultsFromTypesPanel(), gridBagConstraints46);
			exceptionsPanel.add(getRemoveFaultPanel(), gridBagConstraints49);

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
			exceptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.gridx = 2;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.gridx = 0;
			gridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.insets = new java.awt.Insets(2, 2, 2, 10);
			gridBagConstraints51.gridy = 0;
			existingExceptionLabel = new JLabel();
			existingExceptionLabel.setText("Used Faults:");
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 2;
			gridBagConstraints27.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints27.gridheight = 2;
			gridBagConstraints27.gridy = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridx = 1;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints15.gridheight = 1;
			exceptionInputPanel = new JPanel();
			exceptionInputPanel.setLayout(new GridBagLayout());
			exceptionInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Choose Used Fault",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			exceptionInputPanel.add(getExceptionJComboBox(), gridBagConstraints15);
			exceptionInputPanel.add(getExceptionsInputButtonPanel(), gridBagConstraints27);
			exceptionInputPanel.add(existingExceptionLabel, gridBagConstraints51);
			exceptionInputPanel.add(getAddExceptionButton(), gridBagConstraints4);
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
			exceptionsTable = new ExceptionsTable(this.method, this.info.getService());
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
			addExceptionButton = new JButton(PortalLookAndFeel.getAddIcon());
			addExceptionButton.setText("Add Used Fault");
			addExceptionButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ExceptionHolder exceptionHolder = null;
					if (getExceptionJComboBox().getSelectedItem() != null) {
						exceptionHolder = (ExceptionHolder) getExceptionJComboBox().getSelectedItem();
					}
					if (exceptionHolder != null) {
						// parse qname string into qname

						for (int i = 0; i < getExceptionsTable().getRowCount(); i++) {
							MethodTypeExceptionsException exception = null;
							try {
								exception = getExceptionsTable().getRowData(i);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							if (exception != null && exception.getQname() != null
								&& exception.getQname().equals(exceptionHolder.qname)) {
								JOptionPane.showMessageDialog(MethodViewer.this, "Exception (" + exceptionHolder
									+ ") already thrown by method.");
								return;
							}
						}
						getExceptionsTable().addRow(exceptionHolder.qname, exceptionHolder.isCreated);
					} else {
						JOptionPane.showMessageDialog(MethodViewer.this, "Please select an exception first!");
					}
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
			removeExceptionButton = new JButton(PortalLookAndFeel.getRemoveIcon());
			removeExceptionButton.setText("Remove");
			removeExceptionButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						getExceptionsTable().removeSelectedRow();
					} catch (Exception ex) {
						PortalUtils.showErrorMessage("Please select an exception to Remove");
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
			tabbedPanel.addTab("Method Signature", null, getMethodPanel(), null);
			tabbedPanel.addTab("Security", null, getSecurityContainerPanel(), null);
			tabbedPanel.addTab("Import Information", null, getImportInformationPanel(), null);
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
			securityContainerPanel = new MethodSecurityPanel(info.getService().getServiceSecurity(), this.method
				.getMethodSecurity());
			securityContainerPanel.setBorder(BorderFactory.createTitledBorder(null,
				"Method Level Security Configuration", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
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
			configureTabbedPane.addTab("Inputs", null, getInputParamsSplitPane(), null);
			configureTabbedPane.addTab("Output", null, getOutputTypeSplitPane(), null);
			configureTabbedPane.addTab("Faults", null, getExceptionsPanel(), null);
		}
		return configureTabbedPane;
	}


	/**
	 * This method initializes exceptionEditText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JComboBox getExceptionJComboBox() {
		if (exceptionJComboBox == null) {
			exceptionJComboBox = new JComboBox();
			// populate with currently used exception names
			ServiceType[] service = this.info.getServices().getService();
			SortedSet exceptionNameSet = new TreeSet();
			for (int namespaceI = 0; namespaceI < info.getNamespaces().getNamespace().length; namespaceI++) {
				NamespaceType namespace = info.getNamespaces().getNamespace(namespaceI);

			}
			if (service != null) {
				for (int i = 0; i < service.length; i++) {
					MethodsType methodsType = service[i].getMethods();
					if (methodsType != null) {
						MethodType methods[] = methodsType.getMethod();
						if (methods != null) {
							for (int j = 0; j < methods.length; j++) {
								MethodTypeExceptions exceptionsType = methods[j].getExceptions();
								if (exceptionsType != null) {
									MethodTypeExceptionsException[] exceptions = exceptionsType.getException();
									if (exceptions != null) {
										for (int e = 0; e < exceptions.length; e++) {
											if (exceptions[e].getQname() != null) {
												exceptionNameSet
													.add(new ExceptionHolder(exceptions[e].getQname(), true));
											} else {
												exceptionNameSet.add(new ExceptionHolder(new QName(info.getService()
													.getNamespace()
													+ "/types", exceptions[e].getName()), false));
											}
										}
									}
								}
							}
						}
					}
				}
			}
			for (Iterator iter = exceptionNameSet.iterator(); iter.hasNext();) {
				exceptionJComboBox.addItem(iter.next());
			}

		}
		return exceptionJComboBox;
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
			inputNamespacesPanel.add(getInputNamespaceScrollPane(), gridBagConstraints19);
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
			inputNamespaceScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			inputNamespaceScrollPane.setViewportView(getInputNamespaceTypesJTree());
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
			inputNamespaceTypesJTree = new NamespacesJTree(info.getNamespaces(), true);
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
								.getCurrentNode().getParent()).getUserObject());
							SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getInputNamespaceTypesJTree()
								.getCurrentNode()).getUserObject());
							MethodTypeInputsInput input = new MethodTypeInputsInput();
							input.setQName(new QName(nt.getNamespace(), st.getType()));
							input.setIsArray(false);
							input.setName(JavaUtils.xmlNameToJava(st.getType()));
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
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints29.weighty = 1.0;
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.gridy = 3;
			gridBagConstraints29.weightx = 1.0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridheight = 3;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.weightx = 1.0;
			outputNamespacePanel = new JPanel();
			outputNamespacePanel.setLayout(new GridBagLayout());
			outputNamespacePanel.add(getOutputNamespacesTypeScrollPane(), gridBagConstraints6);
			outputNamespacePanel.add(getServicesTypeScrollPane(), gridBagConstraints29);
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
			outputNamespacesTypeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Types",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			outputNamespacesTypeScrollPane.setViewportView(getOutputNamespacesJTree());
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
			outputNamespacesJTree = new NamespacesJTree(info.getNamespaces(), true);
			outputNamespacesJTree.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					if (e.getClickCount() == 2) {
						if (getOutputNamespacesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
							NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getOutputNamespacesJTree()
								.getCurrentNode().getParent()).getUserObject());
							SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getOutputNamespacesJTree()
								.getCurrentNode()).getUserObject());
							MethodTypeOutput output = new MethodTypeOutput();
							output.setQName(new QName(nt.getNamespace(), st.getType()));
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
			gridBagConstraints26.gridx = 0;
			gridBagConstraints26.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints26.gridy = 0;
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
			outputTypesTablePanel.add(getOutputTypejScrollPane(), gridBagConstraints22);
			outputTypesTablePanel.add(getClearOutputTypeButton(), gridBagConstraints26);
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
			inputTypesTablePanel.add(getInputParamScrollPanel(), gridBagConstraints21);
			inputTypesTablePanel.add(getInputButtonPanel(), gridBagConstraints14);
			inputTypesTablePanel.add(getInputTableControlsPanel(), gridBagConstraints18);
		}
		return inputTypesTablePanel;
	}


	/**
	 * This method initializes inputTableControlsPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInputTableControlsPanel() {
		if (inputTableControlsPanel == null) {
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
			inputTableControlsPanel = new JPanel();
			inputTableControlsPanel.setLayout(new GridBagLayout());
			inputTableControlsPanel.add(upLabel, gridBagConstraints25);
			inputTableControlsPanel.add(downLabel, gridBagConstraints24);
		}
		return inputTableControlsPanel;
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
			clearOutputTypeButton.addActionListener(new java.awt.event.ActionListener() {
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
			exceptionsInputButtonPanel = new JPanel();
			exceptionsInputButtonPanel.setLayout(new GridBagLayout());
		}
		return exceptionsInputButtonPanel;
	}


	/**
	 * Yabba dabba do
	 * 
	 * @return
	 */
	private JPanel getImportInformationPanel() {
		if (importInformationPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridy = 5;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints7.gridwidth = 2;
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.gridx = 0;
			gridBagConstraints36.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints36.gridwidth = 2;
			gridBagConstraints36.gridy = 6;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints23.gridy = 0;
			gridBagConstraints23.gridx = 1;
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
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints35.gridy = 4;
			gridBagConstraints35.weightx = 1.0;
			gridBagConstraints35.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints35.gridx = 1;
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
			importInformationPanel.add(wsdlFileLabel, gridBagConstraints30);
			importInformationPanel.add(namespaceLabel, gridBagConstraints31);
			importInformationPanel.add(getNamespaceTextField(), gridBagConstraints32);
			importInformationPanel.add(getServiceNameTextField(), gridBagConstraints33);
			importInformationPanel.add(getWsdlFileTextField(), gridBagConstraints35);
			importInformationPanel.add(packageNameLabel, gridBagConstraints40);
			importInformationPanel.add(getPackageNameTextField(), gridBagConstraints41);
			importInformationPanel.add(getMessagePanel(), gridBagConstraints7);
			importInformationPanel.add(getProviderInformationPanel(), gridBagConstraints36);
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
				namespaceTextField.setText(method.getImportInformation().getNamespace());
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
				serviceNameTextField.setText(method.getImportInformation().getPortTypeName());
			}
		}
		return serviceNameTextField;
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
				wsdlFileTextField.setText(method.getImportInformation().getWsdlFile());
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
			if (isImportedCheckBox.isSelected()) {
				getTabbedPanel().setEnabledAt(2, true);
			} else {
				getTabbedPanel().setEnabledAt(2, false);
				if (getTabbedPanel().getSelectedIndex() == 2) {
					getTabbedPanel().setSelectedIndex(0);
				}
			}
			isImportedCheckBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (isImportedCheckBox.isSelected()) {
						getTabbedPanel().setEnabledAt(2, true);
					} else {
						getTabbedPanel().setEnabledAt(2, false);
						if (getTabbedPanel().getSelectedIndex() == 2) {
							getTabbedPanel().setSelectedIndex(0);
						}
					}
				}

			});
		}
		return isImportedCheckBox;
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
				packageNameTextField.setText(method.getImportInformation().getPackageName());
			}
		}
		return packageNameTextField;
	}


	/**
	 * This method initializes isProvidedCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsProvidedCheckBox() {
		if (isProvidedCheckBox == null) {
			isProvidedCheckBox = new JCheckBox();
			isProvidedCheckBox.setText("Provided");
			isProvidedCheckBox.setSelected(method.isIsProvided());
			isProvidedCheckBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (isProvidedCheckBox.isSelected()) {
						getProviderClassnameTextField().setEnabled(true);
						getProviderClassnameTextField().setEditable(true);
					} else {
						getProviderClassnameTextField().setEnabled(false);
						getProviderClassnameTextField().setEditable(false);
					}
				}

			});
		}
		return isProvidedCheckBox;
	}


	/**
	 * This method initializes servicesTypeScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getServicesTypeScrollPane() {
		if (servicesTypeScrollPane == null) {
			servicesTypeScrollPane = new JScrollPane();
			servicesTypeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Client Handle Types",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			servicesTypeScrollPane.setViewportView(getServicesTypeTable());
		}
		return servicesTypeScrollPane;
	}


	/**
	 * This method initializes servicesTypeTable
	 * 
	 * @return javax.swing.JTable
	 */
	private ServicesTable getServicesTypeTable() {
		if (servicesTypeTable == null) {
			servicesTypeTable = new ServicesTable(info.getServices());
			servicesTypeTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (e.getClickCount() == 2) {

						// set the epr type as this outputType
						MethodTypeOutput output = new MethodTypeOutput();
						try {
							output.setQName(new QName(getServicesTypeTable().getSelectedRowData().getNamespace()
								+ "/types", getServicesTypeTable().getSelectedRowData().getName() + "Reference"));
							output.setIsArray(false);
							output.setIsClientHandle(new Boolean(true));
							output.setClientHandleClass(getServicesTypeTable().getSelectedRowData().getPackageName()
								+ "." + "client" + "." + getServicesTypeTable().getSelectedRowData().getName()
								+ "Client");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							getOutputTypeTable().modifyRow(0, output);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

			});
		}
		return servicesTypeTable;
	}


	/**
	 * This method initializes providerClassnameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getProviderClassnameTextField() {
		if (providerClassnameTextField == null) {
			providerClassnameTextField = new JTextField();
			if (method.getProviderInformation() != null && method.getProviderInformation().getProviderClass() != null) {
				providerClassnameTextField.setText(method.getProviderInformation().getProviderClass());
			}
		}
		return providerClassnameTextField;
	}


	/**
	 * This method initializes providerInformationPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProviderInformationPanel() {
		if (providerInformationPanel == null) {
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.gridx = 0;
			gridBagConstraints34.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints34.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints34.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints34.gridy = 1;
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.gridx = 1;
			gridBagConstraints37.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints37.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints37.weightx = 1.0D;
			gridBagConstraints37.gridy = 1;
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.gridx = 0;
			gridBagConstraints38.gridwidth = 2;
			gridBagConstraints38.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints38.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints38.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints38.weightx = 0.0D;
			gridBagConstraints38.gridy = 0;
			providerClassnameLabel = new JLabel();
			providerClassnameLabel.setText("Provider Classname");
			providerInformationPanel = new JPanel();
			providerInformationPanel.setLayout(new GridBagLayout());
			providerInformationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Provider Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			providerInformationPanel.add(getIsProvidedCheckBox(), gridBagConstraints38);
			providerInformationPanel.add(providerClassnameLabel, gridBagConstraints34);
			providerInformationPanel.add(getProviderClassnameTextField(), gridBagConstraints37);
		}
		return providerInformationPanel;
	}


	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getInputParamsSplitPane() {
		if (inputParamsSplitPane == null) {
			inputParamsSplitPane = new JSplitPane();
			inputParamsSplitPane.setSize(new java.awt.Dimension(173, 68));
			inputParamsSplitPane.setOneTouchExpandable(true);
			inputParamsSplitPane.setLeftComponent(getInputNamespacesPanel());
			inputParamsSplitPane.setRightComponent(getInputTypesTablePanel());
			inputParamsSplitPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Input Parameters",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		}
		return inputParamsSplitPane;
	}


	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getOutputTypeSplitPane() {
		if (outputTypeSplitPane == null) {
			outputTypeSplitPane = new JSplitPane();
			outputTypeSplitPane.setSize(new java.awt.Dimension(192, 90));
			outputTypeSplitPane.setOneTouchExpandable(true);
			outputTypeSplitPane.setLeftComponent(getOutputNamespacePanel());
			outputTypeSplitPane.setRightComponent(getOutputTypesTablePanel());
			outputTypeSplitPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Output Type",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		}
		return outputTypeSplitPane;
	}


	/**
	 * This method initializes messagePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMessagePanel() {
		if (messagePanel == null) {
			GridBagConstraints gridBagConstraints44 = new GridBagConstraints();
			gridBagConstraints44.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints44.gridy = 2;
			gridBagConstraints44.weightx = 1.0;
			gridBagConstraints44.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints44.gridx = 2;
			GridBagConstraints gridBagConstraints43 = new GridBagConstraints();
			gridBagConstraints43.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints43.gridy = 1;
			gridBagConstraints43.weightx = 1.0;
			gridBagConstraints43.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints43.gridx = 2;
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.gridx = 0;
			gridBagConstraints42.gridy = 0;
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints39.gridy = 2;
			gridBagConstraints39.weightx = 1.0;
			gridBagConstraints39.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints39.gridx = 1;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 1;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.gridy = 2;
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridx = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridx = 0;
			outputLabel = new JLabel();
			outputLabel.setText("Output Message QName");
			inputLabel = new JLabel();
			inputLabel.setText("Input Message QName");
			messagePanel = new JPanel();
			messagePanel.setLayout(new GridBagLayout());
			messagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Messages",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			messagePanel.add(inputLabel, gridBagConstraints8);
			messagePanel.add(outputLabel, gridBagConstraints16);
			messagePanel.add(getInputMessageNamespaceTextField(), gridBagConstraints17);
			messagePanel.add(getOutputMessageNamespaceTextField(), gridBagConstraints39);
			messagePanel.add(getMessagesCheckBox(), gridBagConstraints42);
			messagePanel.add(getInputMessageNameTextField(), gridBagConstraints43);
			messagePanel.add(getOutputMessageNameTextField(), gridBagConstraints44);
		}
		return messagePanel;
	}


	/**
	 * This method initializes inputMessageTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getInputMessageNamespaceTextField() {
		if (inputMessageNamespaceTextField == null) {
			inputMessageNamespaceTextField = new JTextField();
			if (method.isIsImported()) {
				inputMessageNamespaceTextField.setText(method.getImportInformation().getInputMessage()
					.getNamespaceURI());
			}
		}
		return inputMessageNamespaceTextField;
	}


	/**
	 * This method initializes ouputMessageTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getOutputMessageNamespaceTextField() {
		if (outputMessageNamespaceTextField == null) {
			outputMessageNamespaceTextField = new JTextField();
			if (method.isIsImported()) {
				outputMessageNamespaceTextField.setText(method.getImportInformation().getOutputMessage()
					.getNamespaceURI());
			}
		}
		return outputMessageNamespaceTextField;
	}


	/**
	 * This method initializes messagesCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getMessagesCheckBox() {
		if (messagesCheckBox == null) {
			messagesCheckBox = new JCheckBox();
			messagesCheckBox.setText("customize message imports");
			messagesCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (messagesCheckBox.isSelected()) {
						getInputMessageNamespaceTextField().setEnabled(true);
						getInputMessageNamespaceTextField().setEditable(true);
						getInputMessageNameTextField().setEnabled(true);
						getInputMessageNameTextField().setEditable(true);
						getOutputMessageNamespaceTextField().setEnabled(true);
						getOutputMessageNamespaceTextField().setEditable(true);
						getOutputMessageNameTextField().setEnabled(true);
						getOutputMessageNameTextField().setEditable(true);
					} else {
						getInputMessageNamespaceTextField().setEnabled(false);
						getInputMessageNamespaceTextField().setEditable(false);
						getInputMessageNameTextField().setEnabled(false);
						getInputMessageNameTextField().setEditable(false);
						getOutputMessageNamespaceTextField().setEnabled(false);
						getOutputMessageNamespaceTextField().setEditable(false);
						getOutputMessageNameTextField().setEnabled(false);
						getOutputMessageNameTextField().setEditable(false);
					}
				}
			});

			getInputMessageNamespaceTextField().setEnabled(false);
			getInputMessageNamespaceTextField().setEditable(false);
			getInputMessageNameTextField().setEnabled(false);
			getInputMessageNameTextField().setEditable(false);
			getOutputMessageNamespaceTextField().setEnabled(false);
			getOutputMessageNamespaceTextField().setEditable(false);
			getOutputMessageNameTextField().setEnabled(false);
			getOutputMessageNameTextField().setEditable(false);

		}
		return messagesCheckBox;
	}


	/**
	 * This method initializes inputMessageNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getInputMessageNameTextField() {
		if (inputMessageNameTextField == null) {
			inputMessageNameTextField = new JTextField();
			if (method.isIsImported()) {
				inputMessageNameTextField.setText(method.getImportInformation().getInputMessage().getLocalPart());
			}
		}
		return inputMessageNameTextField;
	}


	/**
	 * This method initializes outputMessageNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getOutputMessageNameTextField() {
		if (outputMessageNameTextField == null) {
			outputMessageNameTextField = new JTextField();
			if (method.isIsImported()) {
				outputMessageNameTextField.setText(method.getImportInformation().getOutputMessage().getLocalPart());
			}
		}
		return outputMessageNameTextField;
	}


	/**
	 * This method initializes createFaultPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCreateFaultPanel() {
		if (createFaultPanel == null) {
			GridBagConstraints gridBagConstraints48 = new GridBagConstraints();
			gridBagConstraints48.gridx = 1;
			gridBagConstraints48.gridy = 0;
			faultTypeNameLabel = new JLabel();
			faultTypeNameLabel.setText("Fault Type Name:");
			GridBagConstraints gridBagConstraints47 = new GridBagConstraints();
			gridBagConstraints47.gridx = 3;
			gridBagConstraints47.gridy = 0;
			GridBagConstraints gridBagConstraints45 = new GridBagConstraints();
			gridBagConstraints45.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints45.gridx = 2;
			gridBagConstraints45.gridy = 0;
			gridBagConstraints45.weightx = 1.0;
			gridBagConstraints45.insets = new java.awt.Insets(5, 5, 5, 5);
			createFaultPanel = new JPanel();
			createFaultPanel.setLayout(new GridBagLayout());
			createFaultPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Create New Service Faults",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			createFaultPanel.add(getNewFaultNameTextField(), gridBagConstraints45);
			createFaultPanel.add(getCreateFaultButton(), gridBagConstraints47);
			createFaultPanel.add(faultTypeNameLabel, gridBagConstraints48);
		}
		return createFaultPanel;
	}


	/**
	 * This method initializes newFaultNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNewFaultNameTextField() {
		if (newFaultNameTextField == null) {
			newFaultNameTextField = new JTextField();
		}
		return newFaultNameTextField;
	}


	/**
	 * This method initializes createFaultButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCreateFaultButton() {
		if (createFaultButton == null) {
			createFaultButton = new JButton(IntroduceLookAndFeel.getAddIcon());
			createFaultButton.setText("Add New Fault");
			createFaultButton
				.setToolTipText("Creates a new fault under this services types namespace and adds it to the list of available faults.");

			createFaultButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					QName exceptionQName = null;
					if (CommonTools.isValidServiceName(getNewFaultNameTextField().getText())) {
						exceptionQName = new QName(info.getService().getNamespace() + "/types",
							getNewFaultNameTextField().getText());
					} else {
						JOptionPane.showMessageDialog(MethodViewer.this, "Invalid Exception Name("
							+ getNewFaultNameTextField() + "):  Exception must be a valid java indentifier.");
						return;
					}
					ExceptionHolder holder = new ExceptionHolder(exceptionQName, false);
					getExceptionJComboBox().addItem(holder);
					getExceptionsTable().addRow(holder.qname, holder.isCreated);

				}
			});
		}
		return createFaultButton;
	}


	/**
	 * This method initializes exceptionsPanelSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getExceptionsPanelSplitPane() {
		if (exceptionsPanelSplitPane == null) {
			exceptionsPanelSplitPane = new JSplitPane();
			exceptionsPanelSplitPane.setRightComponent(getExceptionScrollPane());
			exceptionsPanelSplitPane.setLeftComponent(getExceptionNamespacesScrollPane());
			exceptionsPanelSplitPane.setDividerLocation(0.40);
		}
		return exceptionsPanelSplitPane;
	}


	/**
	 * This method initializes exceptionNamespacesScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getExceptionNamespacesScrollPane() {
		if (exceptionNamespacesScrollPane == null) {
			exceptionNamespacesScrollPane = new JScrollPane();
			exceptionNamespacesScrollPane.setViewportView(getNamespacesJTree());
		}
		return exceptionNamespacesScrollPane;
	}


	/**
	 * This method initializes namespacesJTree
	 * 
	 * @return javax.swing.JTree
	 */
	private NamespacesJTree getNamespacesJTree() {
		if (namespacesJTree == null) {
			namespacesJTree = new NamespacesJTree(info.getNamespaces(), false);
		}
		return namespacesJTree;
	}


	/**
	 * This method initializes faultsFromTypesPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFaultsFromTypesPanel() {
		if (faultsFromTypesPanel == null) {
			faultsFromTypesPanel = new JPanel();
			faultsFromTypesPanel.setLayout(new GridBagLayout());
			faultsFromTypesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Add Fault From Types",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			faultsFromTypesPanel.add(getAddFaultFromTypeButton(), new GridBagConstraints());
		}
		return faultsFromTypesPanel;
	}


	/**
	 * This method initializes addFaultFromTypeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddFaultFromTypeButton() {
		if (addFaultFromTypeButton == null) {
			addFaultFromTypeButton = new JButton(IntroduceLookAndFeel.getAddIcon());
			addFaultFromTypeButton.setText("Add From Type");
			addFaultFromTypeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getNamespacesJTree().getCurrentNode() instanceof SchemaElementTypeTreeNode) {
						NamespaceType nt = ((NamespaceType) ((NamespaceTypeTreeNode) getNamespacesJTree()
							.getCurrentNode().getParent()).getUserObject());
						SchemaElementType st = ((SchemaElementType) ((SchemaElementTypeTreeNode) getNamespacesJTree()
							.getCurrentNode()).getUserObject());
						if (CommonTools.validateIsFaultType(nt, st, new File(info.getBaseDirectory().getAbsolutePath()
							+ File.separator + "schema" + File.separator + info.getServices().getService(0).getName()))) {
							QName qname = new QName(nt.getNamespace(), st.getType());
							ExceptionHolder holder = new ExceptionHolder(qname, true);
							getExceptionJComboBox().addItem(holder);
							getExceptionsTable().addRow(holder.qname, holder.isCreated);
						} else {
							JOptionPane.showMessageDialog(MethodViewer.this, "Type does not appear to extend from {http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd}BaseFaultType");
						}
					} else {
						JOptionPane.showMessageDialog(MethodViewer.this, "Please select a type to add");
					}
				}
			});
		}
		return addFaultFromTypeButton;
	}


	/**
	 * This method initializes removeFaultPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRemoveFaultPanel() {
		if (removeFaultPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = -1;
			gridBagConstraints5.gridy = -1;
			gridBagConstraints5.gridheight = 2;
			removeFaultPanel = new JPanel();
			removeFaultPanel.setLayout(new GridBagLayout());
			removeFaultPanel.add(getRemoveExceptionButton(), gridBagConstraints5);
		}
		return removeFaultPanel;
	}

} // @jve:decl-index=0:visual-constraint="4,12"

