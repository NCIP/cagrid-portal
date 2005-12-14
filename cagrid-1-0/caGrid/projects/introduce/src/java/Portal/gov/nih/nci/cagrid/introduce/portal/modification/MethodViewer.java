package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdom.Attribute;
import org.jdom.Element;
import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;
import javax.swing.JTabbedPane;
import java.awt.Insets;

/**
 * MethodViewer TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 24, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class MethodViewer extends GridPortalBaseFrame {

	Element method;

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

	private JButton gmeButton = null;

	private JLabel methodLabel = null;

	private JComboBox security = null;

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

	private JPanel securityPanel = null;

	private JPanel exceptionInputButtonPanel = null;

	private JLabel methodSecurityStyle = null;

	public MethodViewer(Element method, File schemaDir, MethodsTable table,
			int selectedRow) {
		this.method = method;
		this.schemaDir = schemaDir;
		this.methodsTable = table;
		this.currentRow = selectedRow;
		this.setTitle("Modify Method");
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		this.setSize(687, 622);
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
			outputTypejScrollPane
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
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
	private JTable getOutputTypeTable() {
		if (outputTypeTable == null) {
			outputTypeTable = new OutputTypeTable(this.method, this.schemaDir);
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
			inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Input Parameters",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					IntroduceLookAndFeel.getPanelLabelColor()));
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
			GridBagConstraints gridBagContraints9 = new GridBagConstraints();
			gridBagContraints9.gridx = 0;
			gridBagContraints9.weightx = 1.0D;
			gridBagContraints9.weighty = 1.0D;
			gridBagContraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagContraints9.gridheight = 1;
			gridBagContraints9.gridy = 0;
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
			outputTypePanel.add(getOutputTypejScrollPane(), gridBagContraints9);
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
					method.setAttribute("name", getNameField().getText());
					String secure = (String) getSecurity().getSelectedItem();
					method.setAttribute("secure", String.valueOf(secure));
					methodsTable.changeMethodName(currentRow, getNameField()
							.getText());
					Element inputs = method.getChild("inputs", method
							.getNamespace());
					inputs.removeChildren("input", method.getNamespace());
					for (int i = 0; i < getInputParamTable().getRowCount(); i++) {
						Element input = new Element("input", method
								.getNamespace());
						String className = ((String) getInputParamTable()
								.getValueAt(i, 0));
						String name = ((String) getInputParamTable()
								.getValueAt(i, 1));
						String namespace = ((String) getInputParamTable()
								.getValueAt(i, 2));
						String type = ((String) getInputParamTable()
								.getValueAt(i, 3));
						String location = ((String) getInputParamTable()
								.getValueAt(i, 4));

						if (className != null && !className.equals("")) {
							input.setAttribute("className", className);
							if (className.indexOf("[]") > -1) {
								input.setAttribute("minOccurs", "0");
								input.setAttribute("maxOccurs", "unbounded");
							}
						}
						if (name != null && !name.equals("")) {
							input.setAttribute("name", name);
						}
						if (namespace != null && !namespace.equals("")) {
							input.setAttribute("namespace", namespace);
						}
						if (type != null && !type.equals("")) {
							input.setAttribute("type", type);
						}
						if (location != null && !location.equals("")) {
							input.setAttribute("location", location);
						}

						inputs.addContent(input);
					}

					// process exceptions
					Element exceptions = method.getChild("exceptions", method
							.getNamespace());
					exceptions.removeChildren("exception", method
							.getNamespace());
					for (int i = 0; i < getExceptionsTable().getRowCount(); i++) {
						Element exception = new Element("exception", method
								.getNamespace());
						String className = null;
						String name = ((String) getExceptionsTable()
								.getValueAt(i, 0));
						String namespace = null;
						String type = null;
						String location = null;

						if (className != null && !className.equals("")) {
							exception.setAttribute("className", className);
							if (className.indexOf("[]") > -1) {
								exception.setAttribute("minOccurs", "0");
								exception
										.setAttribute("maxOccurs", "unbounded");
							}
						}
						if (name != null && !name.equals("")) {
							exception.setAttribute("name", name);
						}
						if (namespace != null && !namespace.equals("")) {
							exception.setAttribute("namespace", namespace);
						}
						if (type != null && !type.equals("")) {
							exception.setAttribute("type", type);
						}
						if (location != null && !location.equals("")) {
							exception.setAttribute("location", location);
						}

						exceptions.addContent(exception);
					}

					// now process the output
					Element oldOutput = method.getChild("output", method
							.getNamespace());
					method.removeContent(oldOutput);
					Element output = new Element("output", method
							.getNamespace());
					String className = ((String) getOutputTypeTable()
							.getValueAt(0, 0));
					String namespace = ((String) getOutputTypeTable()
							.getValueAt(0, 1));
					String type = ((String) getOutputTypeTable().getValueAt(0,
							2));
					String location = ((String) getOutputTypeTable()
							.getValueAt(0, 3));

					if (className != null && !className.equals("")) {
						output.setAttribute("className", className);
						if (className.indexOf("[]") > -1) {
							output.setAttribute("minOccurs", "0");
							output.setAttribute("maxOccurs", "unbounded");
						}
					}
					if (namespace != null && !namespace.equals("")) {
						output.setAttribute("namespace", namespace);
					}
					if (type != null && !type.equals("")) {
						output.setAttribute("type", type);
					}
					if (location != null && !location.equals("")) {
						output.setAttribute("location", location);
					}

					method.addContent(1, output);

					dispose();
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
							final Element input = new Element("input", method
									.getNamespace());
							method.getChild("inputs", method.getNamespace())
									.addContent(
											method.getChild("inputs",
													method.getNamespace())
													.getChildren().size(),
											input);
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
					if ((row < 0)
							|| (row >= getInputParamTable().getRowCount())) {
						PortalUtils
								.showErrorMessage("Please select a parameter to remove.");
						return;
					}
					((DefaultTableModel) getInputParamTable().getModel())
							.removeRow(getInputParamTable().getSelectedRow());
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
	private JButton getGmeButton() {
		if (gmeButton == null) {
			gmeButton = new JButton();
			gmeButton.setText("Edit With GME");
			gmeButton.setIcon(IntroduceLookAndFeel.getMobiusIcon());
			gmeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					performModify(e);
				}
			});
		}
		return gmeButton;
	}

	public void performModify(java.awt.event.ActionEvent e) {

		int row = getInputParamTable().getSelectedRow();
		if ((row < 0) || (row >= getInputParamTable().getRowCount())) {
			PortalUtils.showErrorMessage("Please select a parameter to edit.");
			return;
		}
		Vector v = (Vector) getInputParamTable().getValueAt(
				getInputParamTable().getSelectedRow(), 6);
		PortalResourceManager.getInstance().getGridPortal()
				.addGridPortalComponent(
						new GMEParameterConfigurationComponent(v, schemaDir,
								true));
	}

	/**
	 * This method initializes security
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getSecurity() {
		if (security == null) {
			security = new JComboBox();
			security.addItem("NONE");
			security.addItem("INTEGRITY");
			security.addItem("PRIVACY");
			security.addItem("EITHER");
			Attribute secureAtt = method.getAttribute("secure");
			if (secureAtt != null) {
				security.setSelectedItem(secureAtt.getValue());
			} else {
				security.setSelectedItem("NONE");
			}

		}
		return security;
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
			inputButtonPanel.add(getGmeButton(), null);
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
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
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
			addExceptionButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.out.println("actionPerformed()");
							final Element exception = new Element("exception",
									method.getNamespace());
							method
									.getChild("exceptions",
											method.getNamespace()).addContent(
											method.getChild("exceptions",
													method.getNamespace())
													.getChildren().size(),
											exception);
							getExceptionsTable().addRow(exception);
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
							System.out.println("actionPerformed()");
							// TODO Auto-generated Event stub actionPerformed()
							int row = getExceptionsTable().getSelectedRow();
							if ((row < 0)
									|| (row >= getExceptionsTable()
											.getRowCount())) {
								PortalUtils
										.showErrorMessage("Please select an exception to remove.");
								return;
							}
							((DefaultTableModel) getExceptionsTable()
									.getModel()).removeRow(getExceptionsTable()
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
			tabbedPanel.addTab("Input/Output", null, getMethodPanel(), null);
			tabbedPanel.addTab("Faults", null, getExceptionsPanel(), null);
			tabbedPanel.addTab("Security", null, getSecurityPanel(), null);
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
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.weighty = 0.0D;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.weighty = 1.0D;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridy = 2;
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
		}
		return methodPanel;
	}

	/**
	 * This method initializes securityPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSecurityPanel() {
		if (securityPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 0;
			methodSecurityStyle = new JLabel();
			methodSecurityStyle.setText("Method Level Security Style");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridx = 1;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.insets = new java.awt.Insets(2,2,2,2);
			securityPanel = new JPanel();
			securityPanel.setLayout(new GridBagLayout());
			securityPanel.add(getSecurity(), gridBagConstraints13);
			securityPanel.add(methodSecurityStyle, gridBagConstraints15);
		}
		return securityPanel;
	}

	/**
	 * This method initializes exceptionInputButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getExceptionInputButtonPanel() {
		if (exceptionInputButtonPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new java.awt.Insets(5,3,5,5);
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new java.awt.Insets(5,5,5,2);
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
} // @jve:decl-index=0:visual-constraint="10,10"

