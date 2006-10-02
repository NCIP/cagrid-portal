package gov.nih.nci.cagrid.common.portal;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
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
	
	private JList errorList = null;
	private JScrollPane errorScrollPane = null;
	private JTextArea detailTextArea = null;
	private JScrollPane detailScrollPane = null;
	private JButton okButton = null;
	private JPanel mainPanel = null;
	private JButton dismissButton = null;
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
	
	
	private static void addError(String error, String detail) {
		if (dialog == null) {
			dialog = new ErrorDialog(getOwnerFrame());	
		}
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
			if (getOwnerFrame() == PortalResourceManager.getInstance().getGridPortal()
				&& PortalResourceManager.getInstance().getGridPortal() != null) {
				centerDialog();
			}
			dialog.setVisible(true);
		}
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
	
	
	public static void showErrorDialog(String error, String[] detail) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < detail.length; i++) {
			builder.append(detail).append("\n");
		}
		addError(error, builder.toString());
	}
	
	
	public static void showErrorDialog(final String message, Exception ex) {
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		String detail = writer.getBuffer().toString();
		addError(message, detail);
	}
	
	
	private final static void centerDialog() {
		// Determine the new location of the window
		int w = PortalResourceManager.getInstance().getGridPortal().getSize().width;
		int h = PortalResourceManager.getInstance().getGridPortal().getSize().height;
		int x = PortalResourceManager.getInstance().getGridPortal().getLocationOnScreen().x;
		int y = PortalResourceManager.getInstance().getGridPortal().getLocationOnScreen().y;
		Dimension dim = dialog.getSize();
		dialog.setLocation(w / 2 + x - dim.width / 2, h / 2 + y - dim.height / 2);
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
					System.out.println("SELECTION: " + index);
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
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.setToolTipText("Clears the dialog of any errors and dismisses it");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					errors.clear();
					dispose();
				}
			});
		}
		return okButton;
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
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
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
	private JButton getDismissButton() {
		if (dismissButton == null) {
			dismissButton = new JButton();
			dismissButton.setToolTipText("Simply hides the dialog, preserving errors");
			dismissButton.setText("Dismiss");
			dismissButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return dismissButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getDismissButton(), gridBagConstraints2);
			buttonPanel.add(getOkButton(), gridBagConstraints3);
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


	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ErrorDialog.showErrorDialog(new Exception("Oh Noes!"));
				}
			});			
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
