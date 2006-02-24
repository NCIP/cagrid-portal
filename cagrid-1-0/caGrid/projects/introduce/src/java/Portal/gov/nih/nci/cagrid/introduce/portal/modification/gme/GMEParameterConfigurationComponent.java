package gov.nih.nci.cagrid.introduce.portal.modification.gme;

import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

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
public class GMEParameterConfigurationComponent extends JPanel {
	JPanel customPanel = null;

	private JPanel mainPanel = null;

	private GMEConfigurationPanel gmePanel = null;

	JComponent me;


	public GMEParameterConfigurationComponent() {
		me = this;
		initialize();
		this.gmePanel.discoverFromGME();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.add(getMainPanel());
	}


	public JPanel getCustomPanel() {
		if (customPanel == null) {
			customPanel = new JPanel();
			customPanel.setLayout(new GridBagLayout());
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


	public void populateRow(Vector typeInfo, boolean handleParameterName) {
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

		typeInfo.set(index++, "false");
		if (handleParameterName) {
			typeInfo.set(index++, this.gmePanel.currentType);
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
