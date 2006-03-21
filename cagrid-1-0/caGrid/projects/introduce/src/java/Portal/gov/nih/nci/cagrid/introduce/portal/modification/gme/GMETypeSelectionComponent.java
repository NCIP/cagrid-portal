package gov.nih.nci.cagrid.introduce.portal.modification.gme;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;


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
public class GMETypeSelectionComponent extends JPanel {
	private JPanel mainPanel = null;

	private GMEConfigurationPanel gmePanel = null;

	JComponent me;


	public GMETypeSelectionComponent() {
		me = this;
		initialize();
		this.gmePanel.discoverFromGME();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getMainPanel(), gridBagConstraints);
	}


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.gridwidth = 1;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getGmePanel(), gridBagConstraints4);
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
			gmePanel = new GMEConfigurationPanel();
			gmePanel.setLayout(new GridBagLayout());
			gmePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "GME",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
		}
		return gmePanel;
	}


	public NamespaceType createNamespace() {
		NamespaceType input = new NamespaceType();

		// set the package name
		String packageName = CommonTools.getPackageName(gmePanel.currentNamespace);
		input.setPackageName(packageName);

		if (this.gmePanel.currentNamespace != null) {
			input.setNamespace(this.gmePanel.currentNamespace.getRaw());
		}

		if (this.gmePanel.currentNamespace != null) {
			ImportInfo ii = new ImportInfo(this.gmePanel.currentNamespace);
			input.setLocation("./" + ii.getFileName());
		}
		
		Document doc = null;
		try {
			doc = XMLUtilities.stringToDocument(gmePanel.currentNode.getSchemaContents());
		} catch (MobiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List elementTypes = doc.getRootElement().getChildren("element", doc.getRootElement().getNamespace());
		SchemaElementType[] schemaTypes = new SchemaElementType[elementTypes.size()];
		for (int i = 0; i < elementTypes.size(); i++) {
			Element element = (Element) elementTypes.get(i);
			SchemaElementType type = new SchemaElementType();
			type.setType(element.getAttributeValue("name"));
			schemaTypes[i] = type;
		}
		input.setSchemaElement(schemaTypes);

		return input;
	}
} // @jve:decl-index=0:visual-constraint="2,2"
