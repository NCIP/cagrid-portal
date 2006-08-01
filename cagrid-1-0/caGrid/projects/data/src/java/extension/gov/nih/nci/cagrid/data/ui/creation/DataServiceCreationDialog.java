package gov.nih.nci.cagrid.data.ui.creation;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.utils.AxisJdomUtils;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.portal.extension.CreationExtensionUIDialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.axis.message.MessageElement;
import org.jdom.Element;

/** 
 *  DataServiceCreationDialog
 *  Dialog for post-creation changes to a data service 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 1, 2006 
 * @version $Id$ 
 */
public class DataServiceCreationDialog extends CreationExtensionUIDialog {

	private JPanel mainPanel = null;
	private JCheckBox wsEnumCheckBox = null;
	private JButton okButton = null;


	public DataServiceCreationDialog(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setWsEnumStatus();
			}
		});
		initialize();
	}
	
	
	private void initialize() {
        this.setTitle("Data Service Configuration");
        this.setSize(new java.awt.Dimension(230,92));
        this.setContentPane(getMainPanel());
		// pack();
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
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getWsEnumCheckBox(), gridBagConstraints);
			mainPanel.add(getOkButton(), gridBagConstraints1);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getWsEnumCheckBox() {
		if (wsEnumCheckBox == null) {
			wsEnumCheckBox = new JCheckBox();
			wsEnumCheckBox.setText("Use WS-Enumeration");
		}
		return wsEnumCheckBox;
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
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setWsEnumStatus();
					dispose();
				}
			});
		}
		return okButton;
	}
	
	
	private void setWsEnumStatus() {
		ExtensionTypeExtensionData data = 
			ExtensionTools.getExtensionData(getExtensionDescription(), getServiceInfo());
		Element wsEnumElem = new Element(DataServiceConstants.WS_ENUM_ENABLED);
		wsEnumElem.setText(String.valueOf(getWsEnumCheckBox().isSelected()));
		try {
			MessageElement wsEnumMessageElement = AxisJdomUtils.fromElement(wsEnumElem);
			ExtensionTools.updateExtensionDataElement(data, wsEnumMessageElement);
		} catch (Exception ex) {
			ex.printStackTrace();
			PortalUtils.showErrorMessage("Error storing configuration", ex);
		}
	}
}
