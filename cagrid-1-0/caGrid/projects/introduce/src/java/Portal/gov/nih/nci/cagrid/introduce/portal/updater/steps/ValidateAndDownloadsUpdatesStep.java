package gov.nih.nci.cagrid.introduce.portal.updater.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.software.SoftwareType;
import gov.nih.nci.cagrid.introduce.portal.updater.steps.updatetree.UpdateTree;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.jdom.Document;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class ValidateAndDownloadsUpdatesStep extends PanelWizardStep {
	
	private SoftwareType software = null;

	private JPanel descriptionPanel = null;

	private Thread workerThread = null; // @jve:decl-index=0:

	private JPanel busyPanel = null;

	private JProgressBar busyProgressBar = null;

	private JButton startButton = null;

	private JTextField updateSiteTextField = null;

	private JLabel updateSiteLabel = null;

	private JLabel statusLabel = null;

	private JPanel updatesPanel = null;

	private JScrollPane updatesScrollPane = null;

	private UpdateTree updatesTree = null;

	/**
	 * This method initializes
	 */
	public ValidateAndDownloadsUpdatesStep() {
		super("Check For Updates",
				"Looking for update on the Introduce project server.");
		initialize();
	}

	public void applyState() throws InvalidStateException {

	}

	protected void checkForUpdates() throws MalformedURLException, IOException,
			MobiusException, Exception {
		URL url = null;
		url = new URL(getUpdateSiteTextField().getText());
		URLConnection connection = url.openConnection();
		InputStream stream = connection.getInputStream();
		org.w3c.dom.Document doc = XMLUtils.newDocument(stream);
		this.software = (SoftwareType)ObjectDeserializer.toObject(doc.getDocumentElement(), SoftwareType.class);
		stream.close();
		this.getUpdatesTree().update(software);
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridheight = 2;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 0.0D;
		gridBagConstraints.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(342, 270));
		this.add(getDescriptionPanel(), gridBagConstraints1);
	}

	/**
	 * This method initializes descriptionPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.weighty = 0.2D;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.gridwidth = 2;
			gridBagConstraints8.gridheight = 2;
			gridBagConstraints8.gridy = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.insets = new Insets(20, 20, 20, 20);
			gridBagConstraints7.gridy = 2;
			statusLabel = new JLabel();
			statusLabel.setText("");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			updateSiteLabel = new JLabel();
			updateSiteLabel.setText("Update Site: ");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 1;
			descriptionPanel = new JPanel();
			descriptionPanel.setLayout(new GridBagLayout());
			descriptionPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
			descriptionPanel.add(getUpdateSiteTextField(), gridBagConstraints3);
			descriptionPanel.add(updateSiteLabel, gridBagConstraints6);
			descriptionPanel.add(statusLabel, gridBagConstraints7);
			descriptionPanel.add(getUpdatesPanel(), gridBagConstraints8);
			descriptionPanel.add(getBusyPanel(), gridBagConstraints2);
		}
		return descriptionPanel;
	}

	/**
	 * This method initializes busyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBusyPanel() {
		if (busyPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridy = 0;
			busyPanel = new JPanel();
			busyPanel.setLayout(new GridBagLayout());
			busyPanel.add(getBusyProgressBar(), gridBagConstraints4);
			busyPanel.add(getStartButton(), gridBagConstraints5);
		}
		return busyPanel;
	}

	/**
	 * This method initializes busyProgressBar
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getBusyProgressBar() {
		if (busyProgressBar == null) {
			busyProgressBar = new JProgressBar();
			busyProgressBar.setPreferredSize(new Dimension(148, 16));
		}
		return busyProgressBar;
	}

	/**
	 * This method initializes startButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStartButton() {
		if (startButton == null) {
			startButton = new JButton();
			startButton.setText("Look For Updates");
			startButton.setPreferredSize(new Dimension(150, 16));
			startButton.setFont(new Font("Dialog", Font.BOLD, 10));
			startButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getStartButton().setEnabled(false);
					Thread th = new Thread(new Runnable() {

						public void run() {
							getBusyProgressBar().setIndeterminate(true);
							setComplete(true);
							statusLabel
									.setText("Updates found.  Press Next to view and select updates.");
							try {
								checkForUpdates();
							} catch (MalformedURLException e) {
								statusLabel
										.setText("ERROR: Malformed update site URL!");
								e.printStackTrace();
							} catch (IOException e) {
								statusLabel
										.setText("ERROR: Unable to connect or read from update site!");
								e.printStackTrace();
							} catch (MobiusException e) {
								statusLabel
										.setText("ERROR: Update site information is corupt");
								e.printStackTrace();
							} catch (Exception e) {
								statusLabel
										.setText("ERROR: Undetermined Exception");
								e.printStackTrace();
							}
							getBusyProgressBar().setIndeterminate(false);
						}

					});
					th.start();
				}
			});
		}
		return startButton;
	}

	/**
	 * This method initializes updateSiteTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getUpdateSiteTextField() {
		if (updateSiteTextField == null) {
			updateSiteTextField = new JTextField();
			updateSiteTextField
					.setText("http://bmi.osu.edu/~hastings/introduce/software/software.xml");
		}
		return updateSiteTextField;
	}

	/**
	 * This method initializes updatesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getUpdatesPanel() {
		if (updatesPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.gridx = 0;
			updatesPanel = new JPanel();
			updatesPanel.setLayout(new GridBagLayout());
			updatesPanel.add(getUpdatesScrollPane(), gridBagConstraints9);
		}
		return updatesPanel;
	}

	/**
	 * This method initializes updatesScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getUpdatesScrollPane() {
		if (updatesScrollPane == null) {
			updatesScrollPane = new JScrollPane();
			updatesScrollPane.setViewportView(getUpdatesTree());
		}
		return updatesScrollPane;
	}

	/**
	 * This method initializes updatesTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private UpdateTree getUpdatesTree() {
		if (updatesTree == null) {
			updatesTree = new UpdateTree();
		}
		return updatesTree;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
