/**
 * 
 */
package gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata;

import gov.nih.nci.cagrid.introduce.portal.extension.ResourcePropertyEditorPanel;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.common.Address;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenterDescription;
import gov.nih.nci.cagrid.metadata.service.Service;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * @author oster
 */
public class ServiceMetadataEditor extends ResourcePropertyEditorPanel {
	private ServiceMetadata serviceMetadata = null;
	private JTabbedPane metadataTabbedPane = null;
	private JPanel centerPanel = null;
	private JPanel servicePanel = null;
	private JPanel centerInfoPanel = null;
	private JTabbedPane centerTabbedPane = null;
	private JPanel centerPOCPanel = null;
	private JPanel centerAddressPanel = null;
	private JPanel centerAdditionalInfoPanel = null;
	private JPanel serviceInfoPanel = null;
	private JTabbedPane serviceTabbedPane = null;
	private JPanel servicePOCPanel = null;
	private JLabel centerDisplayNameLabel = null;
	private JTextField centerDisplayNameTextField = null;
	private JLabel centerShortNameLabel = null;
	private JTextField centerShortNameTextField = null;
	private AddressEditorPanel centerAddressEditorPanel = null;
	private PointsOfContactEditorPanel centerPointsOfContactEditorPanel = null;
	private PointsOfContactEditorPanel servicePointsOfContactEditorPanel = null;
	private JLabel centerDescLabel = null;
	private JTextArea centerDescTextArea = null;
	private JLabel centerURLLabel = null;
	private JTextField centerHomepageTextField = null;
	private JLabel centerRSSLabel = null;
	private JTextField centerRSSTextField = null;
	private JLabel centerImageLabel = null;
	private JTextField centerImageTextField = null;


	public ServiceMetadataEditor(String doc, File schemaFile, File schemaDir) {
		super(doc, schemaFile, schemaDir);
		if (doc != null) {
			try {
				setServiceMetadata(MetadataUtils.deserializeServiceMetadata(new StringReader(doc)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		initialize();
	}


	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(1);
		this.setLayout(gridLayout);
		this.setSize(new java.awt.Dimension(487, 281));
		this.add(getMetadataTabbedPane(), null);

	}


	/**
	 * This method initializes metadataTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getMetadataTabbedPane() {
		if (metadataTabbedPane == null) {
			metadataTabbedPane = new JTabbedPane();
			metadataTabbedPane.addTab("Hosting Center", null, getCenterPanel(),
				"Information on the Hosting Research Center");
			metadataTabbedPane.addTab("Service Information", null, getServicePanel(),
				"Information about the service itself");
		}
		return metadataTabbedPane;
	}


	/**
	 * This method initializes centerPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(getCenterInfoPanel(), java.awt.BorderLayout.NORTH);
			centerPanel.add(getCenterTabbedPane(), java.awt.BorderLayout.CENTER);
		}
		return centerPanel;
	}


	/**
	 * This method initializes servicePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getServicePanel() {
		if (servicePanel == null) {
			servicePanel = new JPanel();
			servicePanel.setLayout(new BorderLayout());
			servicePanel.add(getServiceInfoPanel(), java.awt.BorderLayout.NORTH);
			servicePanel.add(getServiceTabbedPane(), java.awt.BorderLayout.CENTER);
		}
		return servicePanel;
	}


	/**
	 * This method initializes centerInfoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterInfoPanel() {
		if (centerInfoPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 1;
			centerShortNameLabel = new JLabel();
			centerShortNameLabel.setText("Short Name");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.gridx = 0;
			centerDisplayNameLabel = new JLabel();
			centerDisplayNameLabel.setText("Display Name");
			centerInfoPanel = new JPanel();
			centerInfoPanel.setLayout(new GridBagLayout());
			centerInfoPanel.add(centerDisplayNameLabel, gridBagConstraints);
			centerInfoPanel.add(getCenterDisplayNameTextField(), gridBagConstraints1);
			centerInfoPanel.add(centerShortNameLabel, gridBagConstraints2);
			centerInfoPanel.add(getCenterShortNameTextField(), gridBagConstraints11);
		}
		return centerInfoPanel;
	}


	/**
	 * This method initializes centerTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getCenterTabbedPane() {
		if (centerTabbedPane == null) {
			centerTabbedPane = new JTabbedPane();
			centerTabbedPane.addTab("Points of Contact", null, getCenterPOCPanel(), null);
			centerTabbedPane.addTab("Address", null, getCenterAddressPanel(), null);
			centerTabbedPane.addTab("Additional Information", null, getCenterAdditionalInfoPanel(), null);
		}
		return centerTabbedPane;
	}


	/**
	 * This method initializes centerPOCPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterPOCPanel() {
		if (centerPOCPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.ipady = -28;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			centerPOCPanel = new JPanel();
			centerPOCPanel.setLayout(new GridBagLayout());
			centerPOCPanel.add(getCenterPointsOfContactEditorPanel(), gridBagConstraints3);
		}
		return centerPOCPanel;
	}


	/**
	 * This method initializes centerAddressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterAddressPanel() {
		if (centerAddressPanel == null) {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(1);
			centerAddressPanel = new JPanel();
			centerAddressPanel.setLayout(gridLayout1);
			centerAddressPanel.add(getCenterAddressEditorPanel(), null);
		}
		return centerAddressPanel;
	}


	/**
	 * This method initializes centerAdditionalInfoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterAdditionalInfoPanel() {
		if (centerAdditionalInfoPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridy = 2;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints13.gridx = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints12.gridy = 2;
			centerImageLabel = new JLabel();
			centerImageLabel.setText("Image URL");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 3;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints9.gridy = 3;
			centerRSSLabel = new JLabel();
			centerRSSLabel.setText("RSS URL");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints7.gridy = 1;
			centerURLLabel = new JLabel();
			centerURLLabel.setText("Homepage URL");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.gridy = 4;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints5.gridy = 4;
			centerDescLabel = new JLabel();
			centerDescLabel.setText("Description");
			centerAdditionalInfoPanel = new JPanel();
			centerAdditionalInfoPanel.setLayout(new GridBagLayout());
			centerAdditionalInfoPanel.add(centerDescLabel, gridBagConstraints5);
			centerAdditionalInfoPanel.add(getCenterDescTextArea(), gridBagConstraints6);
			centerAdditionalInfoPanel.add(centerURLLabel, gridBagConstraints7);
			centerAdditionalInfoPanel.add(getCenterHomepageTextField(), gridBagConstraints8);
			centerAdditionalInfoPanel.add(centerRSSLabel, gridBagConstraints9);
			centerAdditionalInfoPanel.add(getCenterRSSTextField(), gridBagConstraints10);
			centerAdditionalInfoPanel.add(centerImageLabel, gridBagConstraints12);
			centerAdditionalInfoPanel.add(getCenterImageTextField(), gridBagConstraints13);
		}
		return centerAdditionalInfoPanel;
	}


	/**
	 * This method initializes serviceInfoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getServiceInfoPanel() {
		if (serviceInfoPanel == null) {
			serviceInfoPanel = new JPanel();
		}
		return serviceInfoPanel;
	}


	/**
	 * This method initializes serviceTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getServiceTabbedPane() {
		if (serviceTabbedPane == null) {
			serviceTabbedPane = new JTabbedPane();
			serviceTabbedPane.addTab("Points of Contact", null, getServicePOCPanel(), null);
		}
		return serviceTabbedPane;
	}


	/**
	 * This method initializes servicePOCPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getServicePOCPanel() {
		if (servicePOCPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.gridx = 0;
			servicePOCPanel = new JPanel();
			servicePOCPanel.setLayout(new GridBagLayout());
			servicePOCPanel.add(getServicePointsOfContactEditorPanel(), gridBagConstraints4);
		}
		return servicePOCPanel;
	}


	/**
	 * This method initializes displayNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCenterDisplayNameTextField() {
		if (centerDisplayNameTextField == null) {
			centerDisplayNameTextField = new JTextField();
		}
		return centerDisplayNameTextField;
	}


	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCenterShortNameTextField() {
		if (centerShortNameTextField == null) {
			centerShortNameTextField = new JTextField();
		}
		return centerShortNameTextField;
	}


	/**
	 * This method initializes centerAddressEditorPanel
	 * 
	 * @return gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata.AddressEditorPanel
	 */
	private AddressEditorPanel getCenterAddressEditorPanel() {
		if (centerAddressEditorPanel == null) {
			centerAddressEditorPanel = new AddressEditorPanel();
		}
		return centerAddressEditorPanel;
	}


	/**
	 * This method initializes centerPointsOfContactEditorPanel
	 * 
	 * @return gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata.PointsOfContactEditorPanel
	 */
	private PointsOfContactEditorPanel getCenterPointsOfContactEditorPanel() {
		if (centerPointsOfContactEditorPanel == null) {
			centerPointsOfContactEditorPanel = new PointsOfContactEditorPanel();
		}
		return centerPointsOfContactEditorPanel;
	}


	/**
	 * This method initializes servicePointsOfContactEditorPanel
	 * 
	 * @return gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata.PointsOfContactEditorPanel
	 */
	private PointsOfContactEditorPanel getServicePointsOfContactEditorPanel() {
		if (servicePointsOfContactEditorPanel == null) {
			servicePointsOfContactEditorPanel = new PointsOfContactEditorPanel();
		}
		return servicePointsOfContactEditorPanel;
	}


	/**
	 * This method initializes centerDescjTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getCenterDescTextArea() {
		if (centerDescTextArea == null) {
			centerDescTextArea = new JTextArea();
		}
		return centerDescTextArea;
	}


	/**
	 * This method initializes centerHomepageTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCenterHomepageTextField() {
		if (centerHomepageTextField == null) {
			centerHomepageTextField = new JTextField();
		}
		return centerHomepageTextField;
	}


	/**
	 * This method initializes centerRSSURLTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCenterRSSTextField() {
		if (centerRSSTextField == null) {
			centerRSSTextField = new JTextField();
		}
		return centerRSSTextField;
	}


	/**
	 * This method initializes centerImageTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCenterImageTextField() {
		if (centerImageTextField == null) {
			centerImageTextField = new JTextField();
		}
		return centerImageTextField;
	}


	public static void main(String[] args) {
		JFrame f = new JFrame();
		ServiceMetadataEditor viewer = new ServiceMetadataEditor(null, null, null);

		try {
			JFileChooser fc = new JFileChooser(".");
			fc.showOpenDialog(f);

			ServiceMetadata model = MetadataUtils.deserializeServiceMetadata(new FileReader(fc.getSelectedFile()));
			viewer.setServiceMetadata(model);

			f.getContentPane().add(viewer);
			f.pack();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the serviceMetadata
	 */
	public ServiceMetadata getServiceMetadata() {
		return this.serviceMetadata;
	}


	/**
	 * @param serviceMetadata
	 *            the serviceMetadata to set
	 */
	public void setServiceMetadata(ServiceMetadata serviceMetadata) {
		this.serviceMetadata = serviceMetadata;
		updateView();
	}


	protected void updateView() {
		ResearchCenter hostingResearchCenter = null;
		Service service = null;

		if (this.serviceMetadata != null) {
			if (this.serviceMetadata.getHostingResearchCenter() != null) {
				hostingResearchCenter = this.serviceMetadata.getHostingResearchCenter().getResearchCenter();
			}
			if (this.serviceMetadata.getServiceDescription() != null) {
				service = this.serviceMetadata.getServiceDescription().getService();
			}
		}

		updateCenterView(hostingResearchCenter);
		updateServiceView(service);
	}


	private void updateServiceView(Service service) {
		PointOfContact[] pointsOfContact = null;

		if (service != null && service.getPointOfContactCollection() != null) {
			pointsOfContact = service.getPointOfContactCollection().getPointOfContact();
		}

		List<PointOfContact> list = new ArrayList<PointOfContact>();
		if (pointsOfContact != null) {
			list = new ArrayList<PointOfContact>(pointsOfContact.length);
			Collections.addAll(list, pointsOfContact);
		}

		getServicePointsOfContactEditorPanel().setPointsOfContact(list);
	}


	private void updateCenterView(ResearchCenter hostingResearchCenter) {
		String displayName = null;
		String shortName = null;
		Address address = null;
		ResearchCenterDescription researchCenterDescription = null;
		PointOfContact[] pointsOfContact = null;

		if (hostingResearchCenter != null) {
			displayName = hostingResearchCenter.getDisplayName();
			shortName = hostingResearchCenter.getShortName();
			address = hostingResearchCenter.getAddress();
			researchCenterDescription = hostingResearchCenter.getResearchCenterDescription();
			if (hostingResearchCenter.getPointOfContactCollection() != null) {
				pointsOfContact = hostingResearchCenter.getPointOfContactCollection().getPointOfContact();
			}
		}

		List<PointOfContact> list = new ArrayList<PointOfContact>();
		if (pointsOfContact != null) {
			list = new ArrayList<PointOfContact>(pointsOfContact.length);
			Collections.addAll(list, pointsOfContact);
		}
		
		getCenterDisplayNameTextField().setText(displayName);
		getCenterShortNameTextField().setText(shortName);
		getCenterAddressEditorPanel().setAddress(address);
		updateCenterInfoView(researchCenterDescription);
		getCenterPointsOfContactEditorPanel().setPointsOfContact(list);
	}


	private void updateCenterInfoView(ResearchCenterDescription researchCenterDescription) {
		String description = null;
		String homepageURL = null;
		String imageURL = null;
		String rssNewsURL = null;

		if (researchCenterDescription != null) {
			description = researchCenterDescription.getDescription();
			homepageURL = researchCenterDescription.getHomepageURL();
			imageURL = researchCenterDescription.getImageURL();
			rssNewsURL = researchCenterDescription.getRssNewsURL();
		}

		getCenterDescTextArea().setText(description);
		getCenterHomepageTextField().setText(homepageURL);
		getCenterImageTextField().setText(imageURL);
		getCenterRSSTextField().setText(rssNewsURL);
	}


	@Override
	public boolean save() {
		return false;
	}


	@Override
	public String getResultRPString() {
		return getRPString();
	}
} // @jve:decl-index=0:visual-constraint="10,10"
