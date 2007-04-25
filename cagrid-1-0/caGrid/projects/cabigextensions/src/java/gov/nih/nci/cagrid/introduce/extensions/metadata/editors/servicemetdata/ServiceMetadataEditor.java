/**
 * 
 */
package gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata;

import gov.nih.nci.cagrid.introduce.portal.extension.ResourcePropertyEditorPanel;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


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
        this.setSize(new java.awt.Dimension(487,281));
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
			metadataTabbedPane.addTab("Hosting Center", null, getCenterPanel(), "Information on the Hosting Research Center");
			metadataTabbedPane.addTab("Service Information", null, getServicePanel(), "Information about the service itself");
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
			gridBagConstraints11.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints2.gridy = 1;
			centerShortNameLabel = new JLabel();
			centerShortNameLabel.setText("Short Name");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints1.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints.insets = new java.awt.Insets(5,5,5,5);
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
			centerAdditionalInfoPanel = new JPanel();
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
			servicePOCPanel = new JPanel();
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
    }


    @Override
    public boolean save() {
        return false;
    }


    @Override
    public String getResultRPString() {
        return getRPString();
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
