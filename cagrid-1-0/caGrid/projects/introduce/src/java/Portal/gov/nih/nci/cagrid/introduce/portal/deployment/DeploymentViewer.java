package gov.nih.nci.cagrid.introduce.portal.deployment;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;


/**
 * CreationViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class DeploymentViewer extends GridPortalBaseFrame {

	private static final String GLOBUS = "GLOBUS_LOCATION";

	private static final String TOMCAT = "CATALINA_HOME";

	private JPanel deployPropertiesPanel = null;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JButton deployButton = null;

	private File serviceDirectory;

	private ServiceDescription introService;

	Properties deployProperties;

	private JPanel deploymentTypePanel = null;

	private JComboBox deploymentTypeSelector = null;

	private JPanel servicePropertiesPanel = null;

	private JScrollPane servicePropertiesScrollPane = null;


	/**
	 * This method initializes
	 */
	public DeploymentViewer() {
		super();
		createChooserThread().start();
	}


	private Thread createChooserThread() {
		Thread th = new Thread(new Runnable() {

			public void run() {
				try {
					chooseService();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (serviceDirectory == null) {
					DeploymentViewer.this.dispose();
					return;
				}
				if (serviceDirectory.exists() && serviceDirectory.canRead()) {
					try {
						initialize();
					} catch (Exception e) {
						ErrorDialog.showErrorDialog("Error initializing the deployment: "
							+ e.getMessage());
						// JOptionPane.showMessageDialog(DeploymentViewer.this, "Error initializing the deployment: "
						// 	+ e.getMessage());
						DeploymentViewer.this.dispose();
					}
				} else {
					ErrorDialog.showErrorDialog("Directory "
						+ serviceDirectory.getAbsolutePath() + " does not seem to be an introduce service");
					// JOptionPane.showMessageDialog(DeploymentViewer.this, "Directory "
					// 	+ serviceDirectory.getAbsolutePath() + " does not seem to be an introduce service");
					DeploymentViewer.this.dispose();
				}
			}

		});
		return th;
	}


	private void chooseService() throws Exception {
		String dir = ResourceManager.promptDir(this, null);
		if (dir != null) {
			this.serviceDirectory = new File(dir);
		}
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() throws Exception {
		this.setContentPane(getMainPanel());
		this.setFrameIcon(IntroduceLookAndFeel.getDeployIcon());
		this.setTitle("Deploy Grid Service");

		File deployPropertiesFile = new File(serviceDirectory.getAbsolutePath() + File.separator + "deploy.properties");
		deployProperties = new Properties();
		try {
			deployProperties.load(new FileInputStream(deployPropertiesFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.introService = (ServiceDescription) Utils.deserializeDocument(this.serviceDirectory.getAbsolutePath()
			+ File.separator + "introduce.xml", ServiceDescription.class);
		if (introService.getIntroduceVersion() == null
			|| !introService.getIntroduceVersion().equals(IntroduceConstants.INTRODUCE_VERSION)) {
			throw new Exception("Introduce version in project does not match version provided by Introduce Toolkit ( "
				+ IntroduceConstants.INTRODUCE_VERSION + " ): " + introService.getIntroduceVersion());
		}

		// load up the deploy properties;
		Enumeration keys = deployProperties.keys();
		this.setSize(new java.awt.Dimension(298, 280));
		int i = 0;
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			this.addTextField(this.getDeployPropertiesPanel(), key, deployProperties.getProperty(key), i++, true);
		}

		// load up the service properties
		if (introService.getServiceProperties() != null && introService.getServiceProperties().getProperty() != null) {
			for (i = 0; i < introService.getServiceProperties().getProperty().length; i++) {
				ServicePropertiesProperty prop = introService.getServiceProperties().getProperty(i);
				this.addTextField(this.getServicePropertiesPanel(), prop.getKey(), prop.getValue(), i, true);

			}
		}

		pack();

	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDeployPropertiesPanel() {
		if (deployPropertiesPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 3;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridwidth = 2;
			gridBagConstraints10.weighty = 1.0D;
			gridBagConstraints10.gridx = 1;
			deployPropertiesPanel = new JPanel();
			deployPropertiesPanel.setLayout(new GridBagLayout());
			deployPropertiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Deployment Properties",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 3;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
		}
		return deployPropertiesPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 2;
			gridBagConstraints13.weightx = 1.0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 3;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints1.weighty = 0.0D;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridheight = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints.weightx = 0.0D;
			gridBagConstraints.weighty = 0.0D;
			gridBagConstraints.gridwidth = 1;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getDeployPropertiesPanel(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints1);
			mainPanel.add(getDeploymentTypePanel(), gridBagConstraints11);
			mainPanel.add(getServicePropertiesScrollPane(), gridBagConstraints13);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getDeployButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDeployButton() {
		if (deployButton == null) {
			deployButton = new JButton();
			deployButton.setText("Deploy");
			deployButton.setIcon(IntroduceLookAndFeel.getDeployIcon());
			deployButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					BusyDialogRunnable r = new BusyDialogRunnable(PortalResourceManager.getInstance().getGridPortal(),
						"Deployment") {

						public void process() {
							setProgressText("setting introduce resource properties...");

							try {
								ResourceManager.setProperty(ResourceManager.LAST_DEPLOYMENT,
									(String) deploymentTypeSelector.getSelectedItem());
							} catch (Exception e1) {
								e1.printStackTrace();
							}

							setProgressText("writing deployment property file");

							Enumeration keys = deployProperties.keys();
							while (keys.hasMoreElements()) {
								String key = (String) keys.nextElement();
								String value = getTextFieldValue(key);
								deployProperties.setProperty(key, value);
							}

							try {
								deployProperties
									.store(new FileOutputStream(new File(serviceDirectory.getAbsolutePath()
										+ File.separator + "deploy.properties")),
										"introduce service deployment properties");
							} catch (FileNotFoundException ex) {
								ex.printStackTrace();
								setErrorMessage("Error: " + ex.getMessage());
							} catch (IOException ex) {
								ex.printStackTrace();
								setErrorMessage("Error: " + ex.getMessage());
							}

							Properties serviceProps = new Properties();
							// load up the service properties
							if (introService.getServiceProperties() != null
								&& introService.getServiceProperties().getProperty() != null) {
								for (int i = 0; i < introService.getServiceProperties().getProperty().length; i++) {
									ServicePropertiesProperty prop = introService.getServiceProperties().getProperty(i);
									serviceProps.put(prop.getKey(), getTextFieldValue(prop.getKey()));
								}
							}

							try {
								serviceProps.store(new FileOutputStream(new File(serviceDirectory.getAbsolutePath()
									+ File.separator + IntroduceConstants.INTRODUCE_SERVICE_PROPERTIES)),
									"service deployment properties");
							} catch (FileNotFoundException ex) {
								ex.printStackTrace();
								setErrorMessage("Error: " + ex.getMessage());
							} catch (IOException ex) {
								ex.printStackTrace();
								setErrorMessage("Error: " + ex.getMessage());
							}

							setProgressText("deploying");

							try {
								String cmd = "";
								if (((String) getDeploymentTypeSelector().getSelectedItem()).equals(GLOBUS)) {
									cmd = CommonTools.getAntDeployGlobusCommand(serviceDirectory.getAbsolutePath());
								} else {
									cmd = CommonTools.getAntDeployTomcatCommand(serviceDirectory.getAbsolutePath());
								}
								Process p = CommonTools.createAndOutputProcess(cmd);
								p.waitFor();
								if (p.exitValue() != 0) {
									setErrorMessage("Error deploying service!");
								}
							} catch (Exception ex) {
								setErrorMessage("Error deploying service! " + ex.getMessage());
								ex.printStackTrace();
							}
							dispose();

						}
					};
					Thread th = new Thread(r);
					th.start();
				}
			});
		}

		return deployButton;
	}


	/**
	 * This method initializes deploymetnTypePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDeploymentTypePanel() {
		if (deploymentTypePanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.weightx = 1.0;
			deploymentTypePanel = new JPanel();
			deploymentTypePanel.setLayout(new GridBagLayout());
			deploymentTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Deployment Location",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			deploymentTypePanel.add(getDeploymentTypeSelector(), gridBagConstraints2);
		}
		return deploymentTypePanel;
	}


	/**
	 * This method initializes deploymentTypeSelector
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getDeploymentTypeSelector() {
		if (deploymentTypeSelector == null) {
			deploymentTypeSelector = new JComboBox();
			deploymentTypeSelector.addItem(TOMCAT);
			deploymentTypeSelector.addItem(GLOBUS);
			try {
				if (ResourceManager.getProperty(ResourceManager.LAST_DEPLOYMENT) != null) {
					deploymentTypeSelector
						.setSelectedItem(ResourceManager.getProperty(ResourceManager.LAST_DEPLOYMENT));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return deploymentTypeSelector;
	}


	/**
	 * This method initializes servicePropertiesPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getServicePropertiesPanel() {
		if (servicePropertiesPanel == null) {
			servicePropertiesPanel = new JPanel();
			servicePropertiesPanel.setLayout(new GridBagLayout());
		}
		return servicePropertiesPanel;
	}


	/**
	 * This method initializes servicePropertiesScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getServicePropertiesScrollPane() {
		if (servicePropertiesScrollPane == null) {
			servicePropertiesScrollPane = new JScrollPane();
			servicePropertiesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			servicePropertiesScrollPane.setViewportView(getServicePropertiesPanel());
			servicePropertiesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Service Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		}
		return servicePropertiesScrollPane;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
