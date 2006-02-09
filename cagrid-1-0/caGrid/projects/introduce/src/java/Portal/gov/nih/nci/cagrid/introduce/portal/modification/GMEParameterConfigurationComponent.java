package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

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


/**
 * GMETypeExtractionPanel TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jul 7, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class GMEParameterConfigurationComponent extends GridPortalComponent {
	boolean handleParameterName = true;

	JLabel paramNameLabel = null;

	JTextField paramName = null;

	JCheckBox isArrayCheckBox = null;

	JPanel customPanel = null;

	protected Vector typeInfo;

	private JLabel arrayLabel = null;

	private JPanel mainPanel = null;

	private File schemaDir = null;

	private GMEConfigurationPanel gmePanel = null;

	private JPanel buttonPanel = null;

	private JButton doneButton = null;

	JComponent me;


	public GMEParameterConfigurationComponent(Vector typeInfo, File schemaDir, boolean handleParameterName) {
		this.typeInfo = typeInfo;
		this.schemaDir = schemaDir;
		this.handleParameterName = handleParameterName;
		me = this;
		initialize();
		this.gmePanel.discoverFromGME();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(372, 404));
		this.setTitle("Parameter Configuration");
		this.setContentPane(getMainPanel());
	}


	/**
	 * This method initializes paramName
	 * 
	 * @return javax.swing.JTextField
	 */
	JTextField getParamName() {
		if (paramName == null) {
			paramName = new JTextField();
		}
		return paramName;
	}


	/**
	 * This method initializes isArrayCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	JCheckBox getIsArrayCheckBox() {
		if (isArrayCheckBox == null) {
			isArrayCheckBox = new JCheckBox();
			isArrayCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return isArrayCheckBox;
	}


	public JPanel getCustomPanel() {
		if (customPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			customPanel = new JPanel();
			customPanel.setLayout(new GridBagLayout());
			paramNameLabel = new JLabel();
			paramNameLabel.setText("Parameter Name");
			arrayLabel = new JLabel();
			arrayLabel.setText("Is Array");
			if (this.handleParameterName) {
				customPanel.add(paramNameLabel, gridBagConstraints);
				customPanel.add(getParamName(), gridBagConstraints1);
			}
			customPanel.add(arrayLabel, gridBagConstraints2);
			customPanel.add(getIsArrayCheckBox(), gridBagConstraints3);
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
			gmePanel = new GMEConfigurationPanel(GMEConfigurationPanel.TYPES_ONLY);
			gmePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "GME",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
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
						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(gmePanel.getGme().getText());
						if (gmePanel.currentNamespace != null) {
							handle.cacheSchema(gmePanel.currentNamespace, schemaDir);
						}
					} catch (MobiusException e1) {
						// TODO Auto-generated catch block
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
		
		//set the package name
		String domain = this.gmePanel.currentNamespace.getDomain();
		String csi = this.gmePanel.currentNamespace.getName();
		int csiDomainIndex = csi.lastIndexOf(".");
		if(csiDomainIndex>=0){
			domain = csi.substring(0,csiDomainIndex);
		}
		typeInfo.set(index++, domain);
		
		// set classname
		typeInfo.set(index++, "");

		typeInfo.set(index++, String.valueOf(isArrayCheckBox.isSelected()));
		if (handleParameterName) {
			typeInfo.set(index++, paramName.getText());
		}
		if (this.gmePanel.currentNamespace != null) {
			typeInfo.set(index++, "gme://" + this.gmePanel.currentNamespace.getRaw());
		} else {
			typeInfo.set(index++, null);
		}
		typeInfo.set(index++, this.gmePanel.currentType);
		if (this.gmePanel.currentNamespace != null) {
			typeInfo.set(index++, "./" + this.gmePanel.currentNamespace.getName() + ".xsd");
		} else {
			typeInfo.set(index++, null);
		}

	}

} // @jve:decl-index=0:visual-constraint="2,2"
