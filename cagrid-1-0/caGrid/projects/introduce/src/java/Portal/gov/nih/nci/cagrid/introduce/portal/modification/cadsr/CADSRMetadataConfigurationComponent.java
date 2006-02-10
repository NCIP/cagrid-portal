package gov.nih.nci.cagrid.introduce.portal.modification.cadsr;

import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;


/**
 * CADSRMetadataConfigurationComponent
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jul 7, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class CADSRMetadataConfigurationComponent extends GridPortalComponent {
	private JPanel customPanel = null;
	protected Vector typeInfo;
	private JPanel mainPanel = null;
	private File schemaDir = null;
	private CADSRConfigurationPanel cadsrPanel = null;
	private JPanel buttonPanel = null;
	private JButton doneButton = null;
	private JComponent me;
	private JLabel isRegisterLabel = null;
	private JLabel isPopulateFromFileLabel = null;
	private JLabel qnameLabel = null;
	private JCheckBox isPopulateFromFile = null;
	private JCheckBox isRegister = null;
	private JTextField qnameDomain = null;
	private JTextField qnameName = null;
	private JLabel qnameDomainLabel = null;
	private JLabel qnameNameLabel = null;


	public CADSRMetadataConfigurationComponent(Vector typeInfo, File schemaDir) {
		this.typeInfo = typeInfo;
		this.schemaDir = schemaDir;
		me = this;
		initialize();
		this.cadsrPanel.discoverFromCADSR();

	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(372, 404));
		this.setTitle("Metadata Configuration");
		this.setContentPane(getMainPanel());
	}


	public JPanel getCustomPanel() {
		if (customPanel == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints31.gridy = 3;
			qnameNameLabel = new JLabel();
			qnameNameLabel.setText("name");
			qnameNameLabel.setEnabled(false);
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints21.gridy = 2;
			qnameDomainLabel = new JLabel();
			qnameDomainLabel.setText("domain");
			qnameDomainLabel.setEnabled(false);
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridx = 2;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.gridx = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 2;
			qnameLabel = new JLabel();
			qnameLabel.setText("Qname:");
			qnameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.gridy = 0;
			isPopulateFromFileLabel = new JLabel();
			isPopulateFromFileLabel.setText("Poplate From File");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 1;
			isRegisterLabel = new JLabel();
			isRegisterLabel.setText("Register");
			customPanel = new JPanel();
			customPanel.setLayout(new GridBagLayout());
			customPanel.add(isRegisterLabel, gridBagConstraints);
			customPanel.add(isPopulateFromFileLabel, gridBagConstraints1);
			customPanel.add(qnameLabel, gridBagConstraints2);
			customPanel.add(getIsPopulateFromFile(), gridBagConstraints3);
			customPanel.add(getIsRegister(), gridBagConstraints7);
			customPanel.add(getQnameDomain(), gridBagConstraints8);
			customPanel.add(getQnameName(), gridBagConstraints11);
			customPanel.add(qnameDomainLabel, gridBagConstraints21);
			customPanel.add(qnameNameLabel, gridBagConstraints31);
		}
		return customPanel;
	}


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints5.weightx = 0.0D;
			gridBagConstraints5.weighty = 1.0D;
			gridBagConstraints5.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.gridwidth = 1;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.weighty = 0.0D;
			gridBagConstraints4.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getCADSRPanel(), gridBagConstraints4);
			mainPanel.add(getCustomPanel(), gridBagConstraints5);
			mainPanel.add(getButtonPanel(), gridBagConstraints6);
		}
		return mainPanel;
	}


	/**
	 * This method initializes cadsrPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CADSRConfigurationPanel getCADSRPanel() {
		if (cadsrPanel == null) {
			cadsrPanel = new CADSRConfigurationPanel(CADSRConfigurationPanel.ELEMENT_ONLY);
			cadsrPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CADSR",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
		}
		return cadsrPanel;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getDoneButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes doneButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDoneButton() {
		if (doneButton == null) {
			doneButton = new JButton(IntroduceLookAndFeel.getSelectIcon());
			doneButton.setText("Done");
			doneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager
					.getInstance().getResource(IntroducePortalConf.RESOURCE);
					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
					try {
						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(conf.getGME());
						if (cadsrPanel.currentNamespace != null) {
							handle.cacheSchema(cadsrPanel.currentNamespace, schemaDir);
						}
					} catch (MobiusException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(me,
							"Please check the CADSR URL and make sure that you have the appropriate credentials!");
					}
					performDone();

					dispose();
				}
			});
		}
		return doneButton;
	}


	public void performDone() {
		// populate the data vector from the prior screen now......
		int index = 0;
		
		//set the package name
		String domain = this.cadsrPanel.currentNamespace.getDomain();
		String csi = this.cadsrPanel.currentNamespace.getName();
		int csiDomainIndex = csi.lastIndexOf(".");
		if(csiDomainIndex>=0){
			domain = csi.substring(0,csiDomainIndex);
		}
		
		typeInfo.set(index++, domain);
		
		//skip classname
		index++;
		if (this.cadsrPanel.currentNamespace != null) {
			typeInfo.set(index++, "cadsr://" + this.cadsrPanel.currentNamespace.getRaw());
		} else {
			typeInfo.set(index++, null);
		}
		typeInfo.set(index++, this.cadsrPanel.currentType);
		if (this.cadsrPanel.currentNamespace != null) {
			typeInfo.set(index++, "./" + this.cadsrPanel.currentNamespace.getName() + ".xsd");
		} else {
			typeInfo.set(index++, null);
		}
		typeInfo.set(index++, String.valueOf(isPopulateFromFile.isSelected()));
		typeInfo.set(index++, String.valueOf(isRegister.isSelected()));
		typeInfo.set(index++, getQnameDomain().getText() + ":" + getQnameName().getText());

	}


	/**
	 * This method initializes isPopulateFromFile
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsPopulateFromFile() {
		if (isPopulateFromFile == null) {
			isPopulateFromFile = new JCheckBox();
			isPopulateFromFile.setSelected(true);
		}
		return isPopulateFromFile;
	}


	/**
	 * This method initializes isRegister
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsRegister() {
		if (isRegister == null) {
			isRegister = new JCheckBox();
			isRegister.setSelected(true);
		}
		return isRegister;
	}


	/**
	 * This method initializes qname
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getQnameDomain() {
		if (qnameDomain == null) {
			qnameDomain = new JTextField();
		}
		return qnameDomain;
	}


	/**
	 * This method initializes qnameName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getQnameName() {
		if (qnameName == null) {
			qnameName = new JTextField();
		}
		return qnameName;
	}

} // @jve:decl-index=0:visual-constraint="2,2"
