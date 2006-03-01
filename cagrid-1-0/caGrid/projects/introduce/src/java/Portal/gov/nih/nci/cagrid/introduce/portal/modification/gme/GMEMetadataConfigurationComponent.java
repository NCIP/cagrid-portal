package gov.nih.nci.cagrid.introduce.portal.modification.gme;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.modification.gme.GMEConfigurationPanel.SchemaWrapper;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;


/**
 * GMEMetadataConfigurationComponent
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jul 7, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class GMEMetadataConfigurationComponent extends GridPortalComponent {
	private JPanel customPanel = null;
	protected Vector typeInfo;
	private JPanel mainPanel = null;
	private File schemaDir = null;
	private GMEConfigurationPanel gmePanel = null;
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


	public GMEMetadataConfigurationComponent(Vector typeInfo, File schemaDir) {
		this.typeInfo = typeInfo;
		this.schemaDir = schemaDir;
		me = this;
		initialize();
		this.gmePanel.discoverFromGME();

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
			mainPanel.add(getGmePanel(), gridBagConstraints4);
			mainPanel.add(getCustomPanel(), gridBagConstraints5);
			mainPanel.add(getButtonPanel(), gridBagConstraints6);
		}
		return mainPanel;
	}


	/**
	 * This method initializes gmePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private GMEConfigurationPanel getGmePanel() {
		if (gmePanel == null) {
			gmePanel = new GMEConfigurationPanel(GMEConfigurationPanel.ELEMENT_ONLY);
			gmePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "GME",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			gmePanel.getTypesComboBox().addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					qnameDomain.setText(((SchemaWrapper) gmePanel.getSchemaComboBox().getSelectedItem()).getNamespace()
						.getDomain());
					qnameName.setText((String) gmePanel.getTypesComboBox().getSelectedItem());

				}
			});
		}
		return gmePanel;
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
					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
					try {
						IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance()
							.getResource(IntroducePortalConf.RESOURCE);
						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(conf.getGME());
						if (gmePanel.currentNamespace != null) {
							handle.cacheSchema(gmePanel.currentNamespace, schemaDir);
						}
					} catch (MobiusException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(me,
							"Please check the GME URL and make sure that you have the appropriate credentials!");
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

		// set the package name
		String packageName = CommonTools.getPackageName(gmePanel.currentNamespace);

		typeInfo.set(index++, packageName);

		// skip classname
		index++;
		if (this.gmePanel.currentNamespace != null) {
			typeInfo.set(index++, this.gmePanel.currentNamespace.getRaw());
		} else {
			typeInfo.set(index++, null);
		}
		typeInfo.set(index++, this.gmePanel.currentType);
		if (this.gmePanel.currentNamespace != null) {
			ImportInfo ii = new ImportInfo(this.gmePanel.currentNamespace);
			typeInfo.set(index++, "./" + ii.getFileName());
		} else {
			typeInfo.set(index++, null);
		}
		typeInfo.set(index++, String.valueOf(isPopulateFromFile.isSelected()));
		typeInfo.set(index++, String.valueOf(isRegister.isSelected()));
		typeInfo.set(index++, getQnameDomain().getText());
		typeInfo.set(index++, getQnameName().getText());

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
