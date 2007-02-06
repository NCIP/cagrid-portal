package gov.nih.nci.cagrid.introduce.portal.updater.steps;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.jdom.Document;
import org.pietschy.wizard.PanelWizardStep;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;


public class CheckForUpdatesStep extends PanelWizardStep {

	private JPanel descriptionPanel = null;

	private Thread workerThread = null; // @jve:decl-index=0:

	private JPanel busyPanel = null;

	private JProgressBar busyProgressBar = null;

	private JButton startButton = null;

	private JTextField updateSiteTextField = null;

	private JLabel updateSiteLabel = null;

	private JLabel statusLabel = null;


	/**
	 * This method initializes
	 */
	public CheckForUpdatesStep() {
		super();
		initialize();
	}


	protected void checkForUpdates() throws MalformedURLException, IOException, MobiusException, Exception {
		URL url = null;
		url = new URL(getUpdateSiteTextField().getText());
		URLConnection connection = url.openConnection();
		InputStream stream = connection.getInputStream();
		Document doc = null;
		doc = XMLUtilities.streamToDocument(stream);
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints2.weighty = 0.2D;
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 0.0D;
		gridBagConstraints.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(263, 161));
		this.add(getDescriptionPanel(), gridBagConstraints1);

		this.add(getBusyPanel(), gridBagConstraints2);
	}


	/**
	 * This method initializes descriptionPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
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
			startButton.setText("Start");
			startButton.setPreferredSize(new Dimension(57, 16));
			startButton.setFont(new Font("Dialog", Font.BOLD, 10));
			startButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getStartButton().setEnabled(false);
					Thread th = new Thread(new Runnable() {

						public void run() {
							getBusyProgressBar().setIndeterminate(true);
							setComplete(true);
							statusLabel.setText("Updates found.  Press Next to view and select updates.");
							try {
								checkForUpdates();
							} catch (MalformedURLException e) {
								statusLabel.setText("ERROR: Malformed update site URL!");
								e.printStackTrace();
							} catch (IOException e) {
								statusLabel.setText("ERROR: Unable to connect or read from update site!");
								e.printStackTrace();
							} catch (MobiusException e) {
								statusLabel.setText("ERROR: Update site information is corupt");
								e.printStackTrace();
							} catch (Exception e) {
								statusLabel.setText("ERROR: Undetermined Exception");
								e.printStackTrace();
							}
							setComplete(false);
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
			updateSiteTextField.setText("http://bmi.osu.edu/~hastings/updates/updates.xml");
		}
		return updateSiteTextField;
	}
}
