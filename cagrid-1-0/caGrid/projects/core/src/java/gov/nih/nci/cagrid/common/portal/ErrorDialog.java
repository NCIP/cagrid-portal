package gov.nih.nci.cagrid.common.portal;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  PortalErrorDialog
 *  Dialog for displaying / queueing up errors and detail messages
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 2, 2006 
 * @version $Id$ 
 */
public class ErrorDialog extends JDialog {
	
	private static Frame ownerFrame = null;
	private static Vector errors = null;
	private static ErrorDialog dialog = null;
	private static String lastFileLocation = null;
	
	private JList errorList = null;
	private JScrollPane errorScrollPane = null;
	private JTextArea detailTextArea = null;
	private JScrollPane detailScrollPane = null;
	private JButton clearButton = null;
	private JPanel mainPanel = null;
	private JButton hideDialogButton = null;
	private JButton logErrorsButton = null;
	private JPanel buttonPanel = null;
	private JSplitPane errorsSplitPane = null;

	private ErrorDialog(Frame parentFrame) {
		super(parentFrame);
		initialize();
	}
		

	private void initialize() {
		setTitle("Errors");
		this.setContentPane(getMainPanel());
		pack();
	}
	
	
	public static void setOwnerFrame(Frame frame) {
		ownerFrame = frame;
	}
	
	
	private static Frame getOwnerFrame() {
		if (ownerFrame == null) {
			return PortalResourceManager.getInstance().getGridPortal();
		}
		return ownerFrame;
	}
	
	
	private static void addError(final String error, final String detail) {
		if (dialog == null) {
			dialog = new ErrorDialog(getOwnerFrame());	
		}
		Runnable r = new Runnable() {
			public void run() {
				dialog.setAlwaysOnTop(true);
				ErrorContainer container = new ErrorContainer(error, detail);
				if (errors == null) {
					errors = new Vector();
				}
				errors.add(container);
				dialog.getErrorList().setListData(errors);
				if (!dialog.isVisible()) {
					dialog.setModal(true);
					dialog.pack();
					dialog.setSize(500, 450);
					// attempt to center the dialog
					centerDialog();
					dialog.setVisible(true);
				}
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	
	public static void showErrorDialog(Exception ex) {
		String message = ex.getMessage();
		if (message == null) {
			message = ex.getClass().getName();
		}
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		String detail = writer.getBuffer().toString();
		addError(message, detail);
	}
	
	
	public static void showErrorDialog(String error) {
		addError(error, "");
	}
	
	
	public static void showErrorDialog(String error, String detail) {
		addError(error, detail);
	}
	
	
	public static void showErrorDialog(String error, String[] detail) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < detail.length; i++) {
			builder.append(detail[i]).append("\n");
		}
		addError(error, builder.toString());
	}
	
	
	public static void showErrorDialog(String message, Exception ex) {
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		String detail = writer.getBuffer().toString();
		addError(message, detail);
	}

	
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getErrorList() {
		if (errorList == null) {
			errorList = new JList();
			errorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			errorList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					int index = errorList.getSelectedIndex();
					if (index != -1) {
						ErrorContainer cont = (ErrorContainer) getErrorList().getSelectedValue();
						getDetailTextArea().setText(cont.getDetail());
					}
				}
			});
			if (errors != null) {
				errorList.setListData(errors);
			}
		}
		return errorList;
	}

	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getErrorScrollPane() {
		if (errorScrollPane == null) {
			errorScrollPane = new JScrollPane();
			errorScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Errors", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			errorScrollPane.setViewportView(getErrorList());
			errorScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return errorScrollPane;
	}
	

	/**
	 * This method initializes jTextArea   
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getDetailTextArea() {
		if (detailTextArea == null) {
			detailTextArea = new JTextArea();
			detailTextArea.setEditable(false);
			detailTextArea.setWrapStyleWord(true);
			detailTextArea.setLineWrap(true);
		}
		return detailTextArea;
	}
	

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getDetailScrollPane() {
		if (detailScrollPane == null) {
			detailScrollPane = new JScrollPane();
			detailScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			detailScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			detailScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Detail", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			detailScrollPane.setViewportView(getDetailTextArea());			
		}
		return detailScrollPane;
	}
	

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = new JButton();
			clearButton.setText("Clear");
			clearButton.setToolTipText("Clears the dialog of any errors and closes it");
			clearButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					errors.clear();
					dispose();
				}
			});
		}
		return clearButton;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getErrorsSplitPane(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints1);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getHideDialogButton() {
		if (hideDialogButton == null) {
			hideDialogButton = new JButton();
			hideDialogButton.setToolTipText("Simply hides the dialog, preserving all displayed errors");
			hideDialogButton.setText("Hide");
			hideDialogButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return hideDialogButton;
	}
	
	
	private JButton getLogErrorsButton() {
		if (logErrorsButton == null) {
			logErrorsButton = new JButton();
			logErrorsButton.setText("Log Errors");
			logErrorsButton.setToolTipText("Allows saving the error dialog's contents to disk");
			logErrorsButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					saveLogFile();
				}
			});
		}
		return logErrorsButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getLogErrorsButton(), gridBagConstraints1);
			buttonPanel.add(getHideDialogButton(), gridBagConstraints2);
			buttonPanel.add(getClearButton(), gridBagConstraints3);
		}
		return buttonPanel;
	}
	
	
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getErrorsSplitPane() {
		if (errorsSplitPane == null) {
			errorsSplitPane = new JSplitPane();
			errorsSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			// errorsSplitPane.setResizeWeight(0.3D);
			errorsSplitPane.setTopComponent(getErrorScrollPane());
			errorsSplitPane.setBottomComponent(getDetailScrollPane());
			errorsSplitPane.setOneTouchExpandable(true);
		}
		return errorsSplitPane;
	}
	
	
	private static void centerDialog() {
		// Determine the new location of the window
		Frame owner = getOwnerFrame();
		if (owner != null) {
			int w = owner.getSize().width;
			int h = owner.getSize().height;
			int x = owner.getLocationOnScreen().x;
			int y = owner.getLocationOnScreen().y;
			Dimension dim = dialog.getSize();
			dialog.setLocation(w / 2 + x - dim.width / 2, h / 2 + y - dim.height / 2);			
		}
	}
	
	
	private void saveLogFile() {
		String nl = System.getProperty("line.separator");
		JFileChooser chooser = new JFileChooser(lastFileLocation);
		int choice = chooser.showSaveDialog(dialog);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			lastFileLocation = file.getAbsolutePath();
			StringBuilder text = new StringBuilder();
			synchronized (getErrorList()) {
				for (int i = 0; i < getErrorList().getModel().getSize(); i++) {
					ErrorContainer err = (ErrorContainer) getErrorList().getModel().getElementAt(i);
					text.append(err.getMessage()).append(nl);
					if (err.getDetail() != null) {
						String[] details = err.getDetail().split("\n");
						for (int j = 0; j < details.length; j++) {
							text.append("\t").append(details[j]).append(nl);
						}
					}
					if (i + 1 < getErrorList().getModel().getSize()) {
					    text.append("---- ---- ---- ----").append(nl);
                    }
				}
			}
			try {
				FileWriter writer = new FileWriter(file);
				writer.write(text.toString());
				writer.flush();
				writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				showErrorDialog(ex);
			}
		}
	}


	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("HELLO THERE");
		frame.setSize(new Dimension(400,400));
		frame.setVisible(true);
		ErrorDialog.setOwnerFrame(frame);
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(5000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			ErrorDialog.showErrorDialog(new Exception("Oh Noes!"));		
		}
	}
	
	
	private static class ErrorContainer {
		private String message;
		private String detail;
		
		public ErrorContainer(String message, String detail) {
			this.message = message;
			this.detail = detail;
		}
		
		
		public String getMessage() {
			return message;
		}
		
		
		public String getDetail() {
			return detail;
		}
		
		
		public String toString() {
			return this.getMessage();
		}
	}
}
