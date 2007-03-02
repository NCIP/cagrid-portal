package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.ui.GroupSelectionListener;
import gov.nih.nci.cagrid.data.ui.NotifyingButtonGroup;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;


/**
 * CoreDsIntroPanel Simple panel to show the user some information about what
 * they're getting into for developing a caGrid data service backended by the
 * cacore SDK
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @created Sep 25, 2006
 * @version $Id$
 */
public class CoreDsIntroPanel extends AbstractWizardPanel {
	// constants to select which caCORE version is used
	public static final String CACORE_VERSION_PROPERTY = "cacoreVersion";
	public static final String CACORE_31_VERSION = "cacore31";
	public static final String CACORE_32_VERSION = "cacore32";

	// constants for the 3.1 version of the SDK Query Processor
	public static final String SDK_31_QUERY_LIB = ".." + File.separator + "sdkQuery" + File.separator + "build"
		+ File.separator + "lib" + File.separator + "caGrid-1.0-sdkQuery.jar";
	public static final String SDK_31_QUERY_PROCESSOR = "gov.nih.nci.cagrid.data.cql.cacore.HQLCoreQueryProcessor";

	// constants for the 3.2 version of the SDK Query Processor
	public static final String SDK_32_QUERY_LIB = ".." + File.separator + "sdkQuery32" + File.separator + "build"
		+ File.separator + "lib" + File.separator + "caGrid-1.0-sdkQuery32.jar";
	public static final String SDK_32_QUERY_PROCESSOR = "gov.nih.nci.cagrid.data.sdk32query.HQLCoreQueryProcessor";

	/**
	 * This is the text message shown to the user to let them know what the
	 * wizard does and what it expects from them. A better idea may be to load
	 * this from a text file so if it changes, we dont need to recompile.
	 */
	private static String INFO_TEXT = "This wizard will guide you through a simplified process to create a caGrid Data Service "
		+ "using a caCORE SDK backend as your data source.  In the following screens, you will be "
		+ "required to provide your generated client.jar, along with the URL of the data source to "
		+ "have the data service connect to.  Your domain model should be registered in the caDSR "
		+ "for proper generation of a Domain Model metadata extract, or you can provide this file "
		+ "yourself.  Finally, the schemas for all data types in your model need to be added, "
		+ "either from the GME, or from your local file system.";

	private JLabel wizardLabel = null;
	private JTextArea infoTextArea = null;
	private JScrollPane infoTextScrollPane = null;
	private JPanel versionSelectionPanel = null;
	private JRadioButton sdk31RadioButton = null;
	private JRadioButton sdk32RadioButton = null;


	public CoreDsIntroPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}


	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.insets = new Insets(5, 10, 10, 10);
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.anchor = GridBagConstraints.NORTH;
		gridBagConstraints11.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints1.gridheight = 2;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.insets = new Insets(10, 10, 5, 10);
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(608, 301));
		this.add(getWizardLabel(), gridBagConstraints);
		this.add(getInfoTextScrollPane(), gridBagConstraints1);
		this.add(getVersionSelectionPanel(), gridBagConstraints11);
		groupSdkVersionButtons();
	}


	public void update() {
		String sdkVersion = (String) getBitBucket().get(CACORE_VERSION_PROPERTY);
		if (sdkVersion != null) {
			if (sdkVersion.equals(CACORE_31_VERSION)) {
				getSdk31RadioButton().setSelected(true);
			} else if (sdkVersion.equals(CACORE_32_VERSION)) {
				getSdk32RadioButton().setSelected(true);
			} else {
				// wtf?
				ErrorDialog.showErrorDialog("Error setting cacore version to " + sdkVersion);
			}
		}
		setLibrariesAndProcessor();
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
			Icon icon = new ImageIcon(getClass().getResource("/resources/sdk3.gif"));
			wizardLabel.setIcon(icon);
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
	 * Copies the caCORE SDK query processor jar into the data service and sets
	 * the query processor class property
	 */
	private void setLibrariesAndProcessor() {
		String libName = null;
		String qpClassName = null;
		String deleteLib = null;
		if (getSdk31RadioButton().isSelected()) {
			libName = SDK_31_QUERY_LIB;
			qpClassName = SDK_31_QUERY_PROCESSOR;
			deleteLib = new File(SDK_32_QUERY_LIB).getName();
		} else {
			libName = SDK_32_QUERY_LIB;
			qpClassName = SDK_32_QUERY_PROCESSOR;
			deleteLib = new File(SDK_31_QUERY_LIB).getName();
		}
		// get the path to the SDK Query project
		File sdkQuery = new File(libName);
		if (!sdkQuery.exists()) {
			String[] error = {"The SDK Query project does not exist or has not",
					"yet been built.  Please build this project first!"};
			ErrorDialog.showErrorDialog("SDK Query Library Not Found", error);
		} else {
			// delete the old query processor library
			File oldSdkQuery = new File(CacoreWizardUtils.getServiceBaseDir(getServiceInformation()) + File.separator
				+ "lib" + File.separator + deleteLib);
			if (oldSdkQuery.exists()) {
				oldSdkQuery.delete();
			}
			// copy the library to the service's lib dir
			File sdkQueryOut = new File(CacoreWizardUtils.getServiceBaseDir(getServiceInformation()) + File.separator
				+ "lib" + File.separator + sdkQuery.getName());
			try {
				Utils.copyFile(sdkQuery, sdkQueryOut);
			} catch (Exception ex) {
				ex.printStackTrace();
				ErrorDialog.showErrorDialog("Error copying the required query processor library", ex);
				return;
			}
			// add the library to the service's additional libs list
			try {
				Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
				AdditionalLibraries libs = data.getAdditionalLibraries();
				if (libs == null) {
					libs = new AdditionalLibraries();
					data.setAdditionalLibraries(libs);
				}
				Set jarNames = new HashSet();
				if (libs.getJarName() != null) {
					Collections.addAll(jarNames, libs.getJarName());
				}
				// remove the old sdk QP library
				jarNames.remove(oldSdkQuery.getName());
				// add the new library
				jarNames.add(sdkQuery.getName());
				String[] names = new String[jarNames.size()];
				jarNames.toArray(names);
				libs.setJarName(names);
				// store the modified list
				ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
			} catch (Exception ex) {
				ex.printStackTrace();
				ErrorDialog.showErrorDialog("Error adding the library to the service information", ex);
				return;
			}
			// add the query processor class name as a service property
			CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
				DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, qpClassName, false, "");
		}
	}


	/**
	 * This method initializes versionSelectionPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getVersionSelectionPanel() {
		if (versionSelectionPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.gridy = 0;
			versionSelectionPanel = new JPanel();
			versionSelectionPanel.setLayout(new GridBagLayout());
			versionSelectionPanel.add(getSdk31RadioButton(), gridBagConstraints2);
			versionSelectionPanel.add(getSdk32RadioButton(), gridBagConstraints3);
		}
		return versionSelectionPanel;
	}


	/**
	 * This method initializes sdk31RadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getSdk31RadioButton() {
		if (sdk31RadioButton == null) {
			sdk31RadioButton = new JRadioButton();
			sdk31RadioButton.setText("SDK Version 3.1");
		}
		return sdk31RadioButton;
	}


	/**
	 * This method initializes sdk32RadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getSdk32RadioButton() {
		if (sdk32RadioButton == null) {
			sdk32RadioButton = new JRadioButton();
			sdk32RadioButton.setText("SDK Version 3.2");
		}
		return sdk32RadioButton;
	}


	private void groupSdkVersionButtons() {
		NotifyingButtonGroup group = new NotifyingButtonGroup();
		group.add(getSdk31RadioButton());
		group.add(getSdk32RadioButton());
		group.addGroupSelectionListener(new GroupSelectionListener() {
			public void selectionChanged(final ButtonModel previousSelection, final ButtonModel currentSelection) {
				if (currentSelection == getSdk31RadioButton().getModel()) {
					getBitBucket().put(CACORE_VERSION_PROPERTY, CACORE_31_VERSION);
				} else {
					getBitBucket().put(CACORE_VERSION_PROPERTY, CACORE_32_VERSION);
				}
				setLibrariesAndProcessor();
			}
		});
		group.setSelected(getSdk31RadioButton().getModel(), true);
	}
}
