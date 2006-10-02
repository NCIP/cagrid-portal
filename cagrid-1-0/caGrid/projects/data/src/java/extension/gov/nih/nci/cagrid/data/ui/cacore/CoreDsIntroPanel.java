package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

/** 
 *  CoreDsIntroPanel
 *  Simple panel to show the user some information about what they're getting into
 *  for developing a caGrid data service backended by the cacore SDK
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 25, 2006 
 * @version $Id$ 
 */
public class CoreDsIntroPanel extends AbstractWizardPanel {
	
	public static final String SDK_QUERY_LIB = "../sdkQuery/build/lib/caGrid-1.0-sdkQuery.jar";
	public static final String SDK_QUERY_PROCESSOR = "gov.nih.nci.cagrid.data.cql.cacore.HQLCoreQueryProcessor";

	/**
	 * This is the text message shown to the user to let them know what the wizard does
	 * and what it expects from them.  A better idea may be to load this from a text file
	 * so if it changes, we dont need to recompile.
	 */
	private static String INFO_TEXT = 
		"This wizard will guide you through a simplified process to create a caGrid Data Service " +
		"using a caCORE SDK backend as your data source.  In the following screens, you will be " +
		"required to provide your generated client.jar, along with the URL of the data source to " +
		"have the data service connect to.  Your domain model should be registered in the caDSR " +
		"for proper generation of a Domain Model metadata extract, or you can provide this file " +
		"yourself.  Finally, the schemas for all data types in your model need to be added, " +
		"either from the GME, or from your local file system.";

	private JLabel wizardLabel = null;
	private JTextArea infoTextArea = null;
	private JScrollPane infoTextScrollPane = null;
	
	private boolean sdkInitDone;


	public CoreDsIntroPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		sdkInitDone = false;
		initialize();
	}
	
	
	private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.insets = new java.awt.Insets(10,10,10,10);
        gridBagConstraints1.gridx = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(427,225));
        this.add(getWizardLabel(), gridBagConstraints);
        this.add(getInfoTextScrollPane(), gridBagConstraints1);
	}


	public void update() {
		initSdkDataService();
	}


	public String getPanelTitle() {
		return "Create caCORE SDK Backended caGrid Data Service";
	}


	public String getPanelShortName() {
		return "Intro";
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getWizardLabel() {
		if (wizardLabel == null) {
			wizardLabel = new JLabel();
			wizardLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD | java.awt.Font.ITALIC, 36));
			wizardLabel.setText("caCORE SDK Wizard");
			wizardLabel.setForeground(PortalLookAndFeel.getPanelLabelColor());
		}
		return wizardLabel;
	}


	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getInfoTextArea() {
		if (infoTextArea == null) {
			infoTextArea = new JTextArea();
			infoTextArea.setLineWrap(true);
			infoTextArea.setEditable(false);
			infoTextArea.setWrapStyleWord(true);
			infoTextArea.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
			infoTextArea.setText(INFO_TEXT);
		}
		return infoTextArea;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getInfoTextScrollPane() {
		if (infoTextScrollPane == null) {
			infoTextScrollPane = new JScrollPane();
			infoTextScrollPane.setBorder(new javax.swing.border.SoftBevelBorder(BevelBorder.LOWERED));
			infoTextScrollPane.setViewportView(getInfoTextArea());
			infoTextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return infoTextScrollPane;
	}
	
	
	/**
	 * Copies the caCORE SDK query processor jar into the data service
	 * and sets the query processor class property
	 */
	private void initSdkDataService() {
		if (!sdkInitDone) {
			// get the path to the SDK Query project
			File sdkQuery = new File(SDK_QUERY_LIB);
			if (!sdkQuery.exists()) {
				String[] error = {
					"The SDK Query project does not exist or has not",
					"yet been built.  Please build this project first!"
				};
				JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				// copy the library to the service's lib dir
				File sdkQueryOut = new File(CacoreWizardUtils.getServiceBaseDir(getServiceInformation())
					+ File.separator + sdkQuery.getName());
				try {
					Utils.copyDirectory(sdkQuery, sdkQueryOut);
				} catch (Exception ex) {
					ex.printStackTrace();
					String[] error = {
						"Error copying the required query processor library:",
						ex.getMessage()
					};
					JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// add the library to the service's additional libs list
				try {
					Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
					AdditionalLibraries libs = data.getAdditionalLibraries();
					if (libs == null) {
						libs = new AdditionalLibraries();
					}
					String[] names = libs.getJarName();
					if (names == null) {
						names = new String[] {sdkQueryOut.getName()};
					} else {
						names = (String[]) Utils.appendToArray(names, sdkQuery.getName());
					}
					libs.setJarName(names);
					data.setAdditionalLibraries(libs);
					ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
				} catch (Exception ex) {
					ex.printStackTrace();
					String[] error = {
						"Error adding the library to the service information:",
						ex.getMessage()
					};
					JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// add the query processor class name
				CommonTools.setServiceProperty(getServiceInformation(), 
					DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, SDK_QUERY_PROCESSOR, false);
				sdkInitDone = true;
			}			
		}
	}
}
