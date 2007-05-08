/**
 * 
 */
package gov.nih.nci.cagrid.workflow.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.cagrid.grape.ApplicationComponent;

import javax.swing.JTextField;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Insets;
import java.awt.Color;
import java.io.File;

import javax.swing.SwingConstants;
import org.cagrid.grape.utils.ErrorDialog;

/**
 * @author madduri
 *
 */
public class WorkflowSubmissionGUI extends ApplicationComponent {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	private JLabel jLabel = null;

	private JTextField bpelTextField = null;

	private JButton jButton = null;

	private JTextArea inputTextArea = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JButton submitButton = null;

	private JLabel jLabel3 = null;
	
	private File bpelFile = null;

	private JButton startButton = null;
	
	private WorkflowFactoryServiceClient factoryClient = null;

	/**
	 * This is the default constructor
	 */
	public WorkflowSubmissionGUI() {
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(470, 362);
		this.setTitle("WorkflowSubmissionGUI");
		this.setContentPane(getJContentPane());
		this.setEnabled(true);
		this.setVisible(true);
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints.ipadx = 1;
			gridBagConstraints.ipady = 1;
			gridBagConstraints.weightx = 0.0D;
			gridBagConstraints.weighty = 0.0D;
			gridBagConstraints.gridy = 2;
			jLabel3 = new JLabel();
			jLabel3.setText("Pending");
			jLabel3.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabel3.setForeground(new Color(250, 0, 0));
			jLabel3.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Workflow Status:");
			jLabel2.setBackground(new Color(238, 0, 6));
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Input XML");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints9.ipadx = 1;
			gridBagConstraints9.ipady = 2;
			gridBagConstraints9.anchor = GridBagConstraints.CENTER;
			gridBagConstraints9.weightx = 1.0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.NORTHWEST;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints7.gridx = -1;
			gridBagConstraints7.gridy = -1;
			gridBagConstraints7.weightx = 1.0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.ipadx = 20;
			jLabel = new JLabel();
			jLabel.setText("BPEL File:");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setSize(new Dimension(449, 259));
			jContentPane.add(jLabel, gridBagConstraints6);
			jContentPane.add(getBpelTextField(), gridBagConstraints7);
			jContentPane.add(getJButton(), gridBagConstraints8);
			jContentPane.add(getInputTextArea(), gridBagConstraints9);
			jContentPane.add(jLabel1, gridBagConstraints10);
			jContentPane.add(getSubmitButton(), gridBagConstraints12);
			jContentPane.setEnabled(true);
			jContentPane.setVisible(true);
			jContentPane.add(jLabel2, gridBagConstraints11);
			jContentPane.add(jLabel3, gridBagConstraints);
			jContentPane.add(getStartButton(), gridBagConstraints13);
		}
		return jContentPane;
	}


	/**
	 * This method initializes bpelTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getBpelTextField() {
		if (bpelTextField == null) {
			bpelTextField = new JTextField();
			bpelTextField.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			bpelTextField.setPreferredSize(new Dimension(200, 20));
		}
		return bpelTextField;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setPreferredSize(new Dimension(100, 20));
			jButton.setText("Browse");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					JFrame tempFrame = new JFrame(); // temp frame to open file
					// chooser from
					JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
					chooser.setDialogTitle("Select a BPEL file");
					chooser.setDialogType(JFileChooser.OPEN_DIALOG);
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setMultiSelectionEnabled(false);
					// TODO: FIX THIS
					//chooser.setFileFilter(new XMLFileFilter());
					int choice = chooser.showOpenDialog(tempFrame);
					if (choice == JFileChooser.APPROVE_OPTION) {
						try {
							bpelFile = new File(chooser.getSelectedFile().getAbsolutePath());
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage(), "Error loading file",
								JOptionPane.ERROR_MESSAGE);
						}
					} else {
						System.err.println("No configuration file passed in or selected... exiting.");
						System.exit(1);
					}
					// destroy the temp frame
					tempFrame.dispose();
				}
			});
		}
		return jButton;
	}


	/**
	 * This method initializes inputTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getInputTextArea() {
		if (inputTextArea == null) {
			inputTextArea = new JTextArea();
			inputTextArea.setPreferredSize(new Dimension(200, 200));
			inputTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		return inputTextArea;
	}


	/**
	 * This method initializes submitButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSubmitButton() {
		if (submitButton == null) {
			submitButton = new JButton();
			submitButton.setPreferredSize(new Dimension(100, 20));
			submitButton.setText("Submit");
			submitButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getBpelTextField().getText().trim().equals("")) {
						ErrorDialog.showError("BPEL File cannot be empty");
					}
					 
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return submitButton;
	}
	
	/**
	 * This method initializes startButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getStartButton() {
		if (startButton == null) {
			startButton = new JButton();
			startButton.setPreferredSize(new Dimension(100, 20));
			startButton.setText("Start");
			startButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return startButton;
	}


	public static void main(String args[]) {
		WorkflowSubmissionGUI gui = new WorkflowSubmissionGUI();
		gui.setEnabled(true);
		gui.setVisible(true);
		gui.show();
	}
}  //  @jve:decl-index=0:visual-constraint="181,6"
