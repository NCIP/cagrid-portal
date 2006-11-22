package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfigProperty;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/** 
 *  CsmConfigPanel
 *  Panel to configure CSM security (Using the caCORE SDK ClientSession) for the caCORE service
 *  NOTE: this class is presently unused, but will have a place when support for 
 *  caCORE SDK version 3.2 is pushed through caGrid Data Services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 30, 2006 
 * @version $Id$ 
 */
public class CsmConfigPanel extends AbstractWizardPanel {
	public static final String APPLICATION_SERVICE_URL = "appserviceUrl";
	public static final String USE_CSM_FLAG = "useCsmSecurity";
	public static final String CSM_CONTEXT_NAME = "csmContextName";
	
	public static final String PERFORMANCE_WARNING = 
		"NOTE:\n" +
		"Enabling CSM security for the caCORE SDK data source will cause " + 
		"significant performance degredation when multiple clients connect " +
		"to the data service.  Due to a bug in the caCORE SDK API, all calls " +
		"to the query method when CSM is enabled must be synchronized, and " +
		"cannot be executed in parallel.";
	
	private JCheckBox useCsmCheckBox = null;
	private JLabel csmContextLabel = null;
	private JTextField csmContextTextField = null;
	private JCheckBox useAppserviceUrlCheckBox = null;
	private JPanel configPanel = null;
	private JTextArea performanceWarningTextArea = null;
	private JScrollPane performanceWarningScrollPane = null;

	public CsmConfigPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}
	
	
	private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.gridx = 0;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.weightx = 1.0D;
        gridBagConstraints7.gridy = 2;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints6.gridy = 1;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(622,132));
        this.add(getUseCsmCheckBox(), gridBagConstraints6);
        this.add(getConfigPanel(), gridBagConstraints7);
        this.add(getPerformanceWarningScrollPane(), gridBagConstraints);
		
	}
	

	public void update() {
		// load data into gui
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CQLProcessorConfigProperty[] props = data.getCQLProcessorConfig().getProperty();
			for (int i = 0; props != null && i < props.length; i++) {
				String propName = props[i].getName();
				if (propName.equals(USE_CSM_FLAG)) {
					boolean selected = Boolean.valueOf(props[i].getValue()).booleanValue();
					getUseCsmCheckBox().setSelected(selected);
				} else if (propName.equals(CSM_CONTEXT_NAME)) {
					String contextName = props[i].getValue();
					getCsmContextTextField().setText(contextName);
					String appserviceUrl = null;
					for (int j = 0; j < props.length; j++) {
						if (props[j].getName().equals(APPLICATION_SERVICE_URL)) {
							appserviceUrl = props[j].getValue();
							break;
						}
					}
					getUseAppserviceUrlCheckBox().setSelected(contextName.equals(appserviceUrl));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error loading data for CSM", ex);
		}
	}


	public String getPanelTitle() {
		return "Configure CSM Security Options";
	}


	public String getPanelShortName() {
		return "CSM Security";
	}


	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getUseCsmCheckBox() {
		if (useCsmCheckBox == null) {
			useCsmCheckBox = new JCheckBox();
			useCsmCheckBox.setText("Use CSM Security");
			useCsmCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					PortalUtils.setContainerEnabled(getConfigPanel(), useCsmCheckBox.isSelected());
					// set the use CSM property in the extension data useCsmSecurity
					try {
						Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
						CQLProcessorConfigProperty[] props = data.getCQLProcessorConfig().getProperty();
						boolean propertyFound = false;
						for (int i = 0; props != null && i < props.length; i++) {
							if (props[i].getName().equals(USE_CSM_FLAG)) {
								props[i].setValue(String.valueOf(useCsmCheckBox.isSelected()));
								propertyFound = true;
								break;
							}
						}
						if (!propertyFound) {
							CQLProcessorConfigProperty csmProp = new CQLProcessorConfigProperty(
								USE_CSM_FLAG, String.valueOf(useCsmCheckBox.isSelected()));
							if (props == null) {
								props = new CQLProcessorConfigProperty[] {csmProp};
							} else {
								props = (CQLProcessorConfigProperty[]) Utils.appendToArray(props, csmProp);
							}
						}
						data.getCQLProcessorConfig().setProperty(props);
						ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
					} catch (Exception ex) {
						ex.printStackTrace();
						ErrorDialog.showErrorDialog("Error storing use CSM property", ex);
					}
				}
			});
		}
		return useCsmCheckBox;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getCsmContextLabel() {
		if (csmContextLabel == null) {
			csmContextLabel = new JLabel();
			csmContextLabel.setText("CSM Context Name:");
		}
		return csmContextLabel;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCsmContextTextField() {
		if (csmContextTextField == null) {
			csmContextTextField = new JTextField();
		}
		return csmContextTextField;
	}


	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getUseAppserviceUrlCheckBox() {
		if (useAppserviceUrlCheckBox == null) {
			useAppserviceUrlCheckBox = new JCheckBox();
			useAppserviceUrlCheckBox.setText("Use Application Service URL");
			useAppserviceUrlCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean selected = useAppserviceUrlCheckBox.isSelected();
					getCsmContextTextField().setEnabled(!selected);
					getCsmContextTextField().setEditable(!selected);
					if (selected) {
						try {
							Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
							CQLProcessorConfigProperty[] props = data.getCQLProcessorConfig().getProperty();
							for (int i = 0; props != null && i < props.length; i++) {
								if (props[i].getName().equals(APPLICATION_SERVICE_URL)) {
									getCsmContextTextField().setText(props[i].getValue());
									break;
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							ErrorDialog.showErrorDialog("Error getting application service URL", ex);
						}
					}
				}
			});
		}
		return useAppserviceUrlCheckBox;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getConfigPanel() {
		if (configPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 0;
			configPanel = new JPanel();
			configPanel.setLayout(new GridBagLayout());
			configPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Configuration Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			configPanel.add(getCsmContextLabel(), gridBagConstraints1);
			configPanel.add(getCsmContextTextField(), gridBagConstraints3);
			configPanel.add(getUseAppserviceUrlCheckBox(), gridBagConstraints5);
		}
		return configPanel;
	}


	/**
	 * This method initializes performanceWarningTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getPerformanceWarningTextArea() {
		if (performanceWarningTextArea == null) {
			performanceWarningTextArea = new JTextArea();
			performanceWarningTextArea.setLineWrap(true);
			performanceWarningTextArea.setWrapStyleWord(true);
			performanceWarningTextArea.setEditable(false);
			performanceWarningTextArea.setText(PERFORMANCE_WARNING);
		}
		return performanceWarningTextArea;
	}


	/**
	 * This method initializes performanceWarningScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getPerformanceWarningScrollPane() {
		if (performanceWarningScrollPane == null) {
			performanceWarningScrollPane = new JScrollPane();
			performanceWarningScrollPane.setBorder(BorderFactory.createTitledBorder(
				null, "Note", TitledBorder.DEFAULT_JUSTIFICATION, 
				TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			performanceWarningScrollPane.setViewportView(getPerformanceWarningTextArea());
		}
		return performanceWarningScrollPane;
	}
}
