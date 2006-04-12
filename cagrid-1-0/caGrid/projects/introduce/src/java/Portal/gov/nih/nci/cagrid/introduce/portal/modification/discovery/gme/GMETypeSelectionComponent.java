package gov.nih.nci.cagrid.introduce.portal.modification.discovery.gme;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.PortalResourceManager;


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
public class GMETypeSelectionComponent extends NamespaceTypeDiscoveryComponent  {
	public static String GME_URL = "GME_URL";
	public static String TYPE = "GME";
	
	private GMEConfigurationPanel gmePanel = null;
	
	public GMETypeSelectionComponent(DiscoveryExtensionDescriptionType descriptor) {
		super(descriptor);
		initialize();
		this.getGmePanel().discoverFromGME();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.insets = new java.awt.Insets(0,0,0,0);
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints4.gridwidth = 1;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.weighty = 1.0D;
		gridBagConstraints4.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getGmePanel(), gridBagConstraints4);
	}


	/**
	 * This method initializes gmePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private GMEConfigurationPanel getGmePanel() {
		if (gmePanel == null) {
			gmePanel = new GMEConfigurationPanel(ExtensionTools.getProperty(getDescriptor().getProperties(),GMETypeSelectionComponent.GME_URL));
		}
		return gmePanel;
	}


	public NamespaceType createNamespaceType(File schemaDestinationDir) {
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
		
		cacheSchema(schemaDestinationDir,
			input.getNamespace());

		return input;
	}
	

	private void cacheSchema(File dir, String namespace) {
		if (namespace.equals(IntroduceConstants.W3CNAMESPACE)) {
			// this is "natively supported" so we don't need to cache it
			return;
		}
		IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager.getInstance().getResource(
			IntroducePortalConf.RESOURCE);
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		try {
			XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance().getGridService(ExtensionTools.getProperty(getDescriptor().getProperties(),GMETypeSelectionComponent.GME_URL));
			handle.cacheSchema(new Namespace(namespace), dir);
		} catch (MobiusException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(GMETypeSelectionComponent.this,
				"Error retrieving schemas from GME");
		}

	}
}
