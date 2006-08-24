package gov.nih.nci.cagrid.common.portal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.projectmobius.portal.PortalResourceManager;

/** 
 *  ErrorDialog
 *  Modal error dialog which can append messages if it's already showing instread of freezing your GUI
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 24, 2006 
 * @version $Id$ 
 */
public class ErrorDialog extends JDialog {
	public static final String ERROR_TITLE = "Error";
	
	private static ErrorDialog dialog = null;
	
	private JButton okButton = null;
	private JPanel mainPanel = null;
	private JTextPane errorEditorPane = null;
	private JScrollPane errorScrollPane = null;
	
	public static void showErrorDialog(Exception ex) {
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		showErrorDialog(writer.getBuffer().toString());
	}
	
	
	public static void showErrorDialog(final String[] message) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < message.length; i++) {
			builder.append(message).append("\n");
		}
		showErrorDialog(builder.toString());
	}
	
	
	public static void showErrorDialog(final String message, Exception ex) {
		StringWriter writer = new StringWriter();
		writer.write(message);
		writer.write("\n");
		ex.printStackTrace(new PrintWriter(writer));
		showErrorDialog(writer.getBuffer().toString());
	}
	

	public static void showErrorDialog(final String message) {
		if (dialog == null) {
			dialog = new ErrorDialog();
		}
		Runnable messageRecorder = new Runnable() {
			public void run() {
				Document doc = dialog.getErrorEditorPane().getDocument();
				try {
					if (doc.getLength() != 0) {
						doc.insertString(doc.getLength(), "------------\n", new SimpleAttributeSet());
					}
					SimpleAttributeSet errorAttributes = new SimpleAttributeSet();
					StyleConstants.setForeground(errorAttributes, Color.RED);
					StyleConstants.setBold(errorAttributes, true);
					doc.insertString(doc.getLength(), message + "\n", errorAttributes);
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
				// move the scroll bar to the bottom
				dialog.getErrorScrollPane().getVerticalScrollBar().setValue(
					dialog.getErrorScrollPane().getVerticalScrollBar().getMaximum());
			}
		};
		SwingUtilities.invokeLater(messageRecorder);
		dialog.setVisible(true);
		dialog.setModal(true);
		centerDialog();
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
	
	
	private ErrorDialog() {
		super();
		initialize();
	}
	
	
	private void initialize() {
		setTitle(ERROR_TITLE);
        this.setContentPane(getMainPanel());
        setSize(300, 400);
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
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Document doc = dialog.getErrorEditorPane().getDocument();
					try {
						doc.remove(0, doc.getLength());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					setModal(false);
					setVisible(false);
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
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(6,6,6,6);
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getErrorScrollPane(), gridBagConstraints);
			mainPanel.add(getOkButton(), gridBagConstraints1);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	private JTextPane getErrorEditorPane() {
		if (errorEditorPane == null) {
			errorEditorPane = new JTextPane();
			errorEditorPane.setEditable(false);
		}
		return errorEditorPane;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getErrorScrollPane() {
		if (errorScrollPane == null) {
			errorScrollPane = new JScrollPane();
			errorScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			errorScrollPane.setViewportView(getErrorEditorPane());
		}
		return errorScrollPane;
	}
}
