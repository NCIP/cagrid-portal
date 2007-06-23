/**
 * 
 */
package gov.nih.nci.cagrid.workflow.client;

import gov.nih.nci.cagrid.workflow.stubs.types.WSDLReferences;

import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import java.awt.Insets;
import javax.swing.JButton;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.grape.utils.ErrorDialog;

import java.awt.ComponentOrientation;

/**
 * @author madduri
 *
 */
public class PartnerLinkFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel partnerLinkTypeLabel = null;

	private JComboBox jComboBox = null;

	private JLabel jLabel = null;

	private JTextField serviceEndpointTF = null;

	private JLabel serviceNa = null;

	private JTextField wsdlLocationTF = null;

	private JLabel jLabel1 = null;

	private JTextField namespaceTF = null;

	private JButton jButton = null;

	private WSDLReferences wsdlReference = null;  //  @jve:decl-index=0:
	/**
	 * @throws HeadlessException
	 */
	public PartnerLinkFrame() throws HeadlessException {
		// TODO Auto-generated constructor stub
		super();
		initialize();
	}

	/**
	 * @param gc
	 */
	public PartnerLinkFrame(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public PartnerLinkFrame(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * @param title
	 * @param gc
	 */
	public PartnerLinkFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setMaximumRowCount(2);
			jComboBox.addItem(new String("Static"));
			jComboBox.addItem(new String("Dynamic"));
			jComboBox.setName("jComboBox");
			jComboBox.setPreferredSize(new Dimension(100, 25));
		}
		return jComboBox;
	}

	/**
	 * This method initializes serviceEndpointTF	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getServiceEndpointTF() {
		if (serviceEndpointTF == null) {
			serviceEndpointTF = new JTextField();
			serviceEndpointTF.setPreferredSize(new Dimension(200, 25));
		}
		return serviceEndpointTF;
	}

	/**
	 * This method initializes wsdlLocationTF	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getWsdlLocationTF() {
		if (wsdlLocationTF == null) {
			wsdlLocationTF = new JTextField();
			wsdlLocationTF.setPreferredSize(new Dimension(200, 25));
		}
		return wsdlLocationTF;
	}

	/**
	 * This method initializes namespaceTF	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNamespaceTF() {
		if (namespaceTF == null) {
			namespaceTF = new JTextField();
			namespaceTF.setPreferredSize(new Dimension(200, 25));
		}
		return namespaceTF;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setPreferredSize(new Dimension(100, 25));
			jButton.setText("Add");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					
					try {
						wsdlReference.setPartnerLinkType((String)getJComboBox().getSelectedItem());
						wsdlReference.setServiceUrl(new URI(getServiceEndpointTF().getText()));
						wsdlReference.setWsdlNamespace(new URI(getNamespaceTF().getText()));
						wsdlReference.setWsdlLocation(getWsdlLocationTF().getText());
					} catch (Exception e1) {
						ErrorDialog.showError("Invalid Endpoint", e1);
					}
					
					
					dispose();
				}
			});
		}
		return jButton;
	}

	public WSDLReferences getWSDLReference() {
		if (this.wsdlReference != null) {
			return this.wsdlReference;
		} else {
			ErrorDialog.showError("Invalid PartnerLink");
			dispose();
			return null;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PartnerLinkFrame thisClass = new PartnerLinkFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 196);
		this.setContentPane(getJContentPane());
		this.setTitle("PartnerLinkFrame");
		this.wsdlReference = new WSDLReferences();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 1;
			gridBagConstraints41.gridy = 4;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints31.gridy = 3;
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.weightx = 1.0;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = -1;
			gridBagConstraints21.gridy = 3;
			jLabel1 = new JLabel();
			jLabel1.setText("Namespace:");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridy = 2;
			serviceNa = new JLabel();
			serviceNa.setText("WSDL Location:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(9, 3, 7, 25);
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridheight = 1;
			gridBagConstraints1.weighty = 0.0D;
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.insets = new Insets(5, 3, 2, 2);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(9, 24, 7, 2);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			jLabel = new JLabel();
			jLabel.setText("Service Endpoint:");
			partnerLinkTypeLabel = new JLabel();
			partnerLinkTypeLabel.setText("Select Type:");
			partnerLinkTypeLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			partnerLinkTypeLabel.setName("partnerLinkTypeLabel");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(partnerLinkTypeLabel, gridBagConstraints);
			jContentPane.add(getJComboBox(), gridBagConstraints1);
			jContentPane.add(jLabel, gridBagConstraints2);
			jContentPane.add(getServiceEndpointTF(), gridBagConstraints3);
			jContentPane.add(serviceNa, gridBagConstraints4);
			jContentPane.add(getWsdlLocationTF(), gridBagConstraints11);
			jContentPane.add(jLabel1, gridBagConstraints21);
			jContentPane.add(getNamespaceTF(), gridBagConstraints31);
			jContentPane.add(getJButton(), gridBagConstraints41);
		}
		return jContentPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
