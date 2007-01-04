package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CadsrInformation;
import gov.nih.nci.cagrid.data.extension.CadsrPackage;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.projectmobius.client.gme.ImportInfo;
import org.projectmobius.common.GridServiceFactory;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;

/** 
 *  SchemaTypesPanel
 *  Panel to select schemas for the types in the domain model
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 26, 2006 
 * @version $Id$ 
 */
public class SchemaTypesPanel extends AbstractWizardPanel {

	private JLabel gmeUrlLabel = null;
	private JTextField gmeUrlTextField = null;
	private JButton gmeSchemasButton = null;
	private JPanel gmePanel = null;
	private JScrollPane packageNamespaceScrollPane = null;
	private PackageSchemasTable packageNamespaceTable = null;

	public SchemaTypesPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		super(extensionDescription, info);
		initialize();
	}


	public void update() {
		try {
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CadsrInformation info = data.getCadsrInformation();
			Set currentPackageNames = new HashSet();
			for (int i = 0; i < getPackageNamespaceTable().getRowCount(); i++) {
				currentPackageNames.add(getPackageNamespaceTable().getValueAt(i, 0));
			}
			if (info != null && info.getPackages() != null) {
				CadsrPackage[] packs = info.getPackages();
				if (packs != null && packs.length != 0) {
					// add any new packages to the table
					for (int i = 0; i < packs.length; i++) {
						if (!getPackageNamespaceTable().isPackageInTable(packs[i])) {
							getPackageNamespaceTable().addNewCadsrPackage(getServiceInformation(), packs[i]);
						}
						currentPackageNames.remove(packs[i].getName());
					}
				}
			}
			Iterator invalidPackageNameIter = currentPackageNames.iterator();
			while (invalidPackageNameIter.hasNext()) {
				String invalidName = (String) invalidPackageNameIter.next();
				getPackageNamespaceTable().removeCadsrPackage(invalidName);
			}
			setWizardComplete(allSchemasResolved());
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error populating the packages table", ex);
		}		
	}


	public String getPanelTitle() {
		return "Schema Type Selection";
	}


	public String getPanelShortName() {
		return "Schemas";
	}
	
	
	private void initialize() {
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.weighty = 1.0;
        gridBagConstraints4.gridx = 0;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(505,209));
        this.add(getGmePanel(), gridBagConstraints3);
        this.add(getPackageNamespaceScrollPane(), gridBagConstraints4);		
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getGmeUrlLabel() {
		if (gmeUrlLabel == null) {
			gmeUrlLabel = new JLabel();
			gmeUrlLabel.setText("GME URL:");
		}
		return gmeUrlLabel;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getGmeUrlTextField() {
		if (gmeUrlTextField == null) {
			gmeUrlTextField = new JTextField();
			String url = ResourceManager.getServiceURLProperty(DataServiceConstants.GME_SERVICE_URL);
			gmeUrlTextField.setText(url);
		}
		return gmeUrlTextField;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getGmeSchemasButton() {
		if (gmeSchemasButton == null) {
			gmeSchemasButton = new JButton();
			gmeSchemasButton.setText("Find Schemas Using GME");
			gmeSchemasButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					findSchemas();
				}
			});
		}
		return gmeSchemasButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getGmePanel() {
		if (gmePanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			gmePanel = new JPanel();
			gmePanel.setLayout(new GridBagLayout());
			gmePanel.add(getGmeUrlLabel(), gridBagConstraints);
			gmePanel.add(getGmeUrlTextField(), gridBagConstraints1);
			gmePanel.add(getGmeSchemasButton(), gridBagConstraints2);
		}
		return gmePanel;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getPackageNamespaceScrollPane() {
		if (packageNamespaceScrollPane == null) {
			packageNamespaceScrollPane = new JScrollPane();
			packageNamespaceScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			packageNamespaceScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Schema Mappings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			packageNamespaceScrollPane.setViewportView(getPackageNamespaceTable());
		}
		return packageNamespaceScrollPane;
	}


	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private PackageSchemasTable getPackageNamespaceTable() {
		if (packageNamespaceTable == null) {
			packageNamespaceTable = new PackageSchemasTable();
			packageNamespaceTable.getModel().addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					if (e.getType() == TableModelEvent.UPDATE) {
						setWizardComplete(allSchemasResolved());
						try {
							storePackageMappings();
						} catch (Exception ex) {
							ex.printStackTrace();
							ErrorDialog.showErrorDialog("Error storing namespace mappings", ex);
						}
					}
				}
			});
		}
		return packageNamespaceTable;
	}
	
	
	private void findSchemas() {
		// get the GME handle
		try {
			XMLDataModelService gmeHandle = getGmeHandle();
			// get the domains administered by the GME
			List domains = gmeHandle.getNamespaceDomainList();
			// get the selected packages
			Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
			CadsrInformation info = data.getCadsrInformation();
			if (info != null && info.getPackages() != null) {
				CadsrPackage[] packs = info.getPackages();
				for (int i = 0; i < packs.length; i++) {
					Namespace ns = new Namespace(packs[i].getMappedNamespace());
					// see if the GME has the domain in question
					if (domains.contains(ns.getDomain())) {
						// domain found, get the namespaces within it
						List namespaces = gmeHandle.getSchemaListForNamespaceDomain(ns.getDomain());
						if (namespaces.contains(ns)) {
							// found the namespace as well
							// download the schema locally
							pullSchemas(ns, gmeHandle);
							// change the package namespace table to reflect the found schema
							int row = 0;
							while (!getPackageNamespaceTable().getValueAt(row, 0).equals(packs[i].getName())) {
								row++;
							}
							getPackageNamespaceTable().setValueAt(PackageSchemasTable.STATUS_SCHEMA_FOUND, row, 2);
						}
						break;
					}
				}
			} else {
				JOptionPane.showMessageDialog(SchemaTypesPanel.this, "No packages to find schemas for");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error retrieving schemas from the GME", ex);
		}
	}
	
	
	private XMLDataModelService getGmeHandle() throws MobiusException {
		XMLDataModelService service = null;
		GridServiceFactory oldFactory = GridServiceResolver.getInstance().getDefaultFactory();
		GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
		try {
			service = (XMLDataModelService) GridServiceResolver.getInstance()
				.getGridService(getGmeUrlTextField().getText());
		} catch (MobiusException ex) {
			throw ex;
		} finally {
			GridServiceResolver.getInstance().setDefaultFactory(oldFactory);
		}
		return service;
	}
	
	
	private void pullSchemas(Namespace ns, XMLDataModelService gme) throws MobiusException {
		// get the service's schema dir
		File schemaDir = new File(CacoreWizardUtils.getServiceBaseDir(getServiceInformation()) + File.separator + "schema" 
			+ File.separator + getServiceInformation().getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
		// have the GME cache the schema and its imports locally
		List cachedNamespaces = gme.cacheSchema(ns, schemaDir);
		// create namespace types and add them to the service
		Iterator nsIter = cachedNamespaces.iterator();
		while (nsIter.hasNext()) {
			Namespace storedNs = (Namespace) nsIter.next();
			ImportInfo storedSchemaInfo = new ImportInfo(storedNs);
			File location = new File(schemaDir.getAbsolutePath() + File.separator + storedSchemaInfo.getFileName());
			NamespaceType nsType = CommonTools.createNamespaceType(location.getAbsolutePath());
			// make the location relative to the schema directory
			nsType.setLocation("./" + location.getName());
			// set the package name
			String packName = CommonTools.getPackageName(storedNs);
			nsType.setPackageName(packName);
			// fix the serialization / deserialization on the namespace types
			for (int i = 0; nsType.getSchemaElement() != null && i < nsType.getSchemaElement().length; i++) {
				SchemaElementType type = nsType.getSchemaElement(i);
				type.setSerializer(DataServiceConstants.SDK_SERIALIZER);
				type.setDeserializer(DataServiceConstants.SDK_DESERIALIZER);
				type.setClassName(type.getType());
			}
			CommonTools.addNamespace(getServiceInformation().getServiceDescriptor(), nsType);
			// add the namespace to the introduce namespace excludes list so
			// that beans will not be built for these data types
			String excludes = getServiceInformation().getIntroduceServiceProperties()
				.getProperty(IntroduceConstants.INTRODUCE_NS_EXCLUDES);
			excludes += " -x " + nsType.getNamespace();
			getServiceInformation().getIntroduceServiceProperties().setProperty(
				IntroduceConstants.INTRODUCE_NS_EXCLUDES, excludes);
		}
	}
	
	
	private boolean allSchemasResolved() {
		for (int i = 0; i < getPackageNamespaceTable().getRowCount(); i++) {
			String status = (String) getPackageNamespaceTable().getValueAt(i, 2);
			if (!status.equals(PackageSchemasTable.STATUS_SCHEMA_FOUND)) {
				return false;
			}
		}
		return true;
	}
	
	
	private void storePackageMappings() throws Exception {
		Data data = ExtensionDataUtils.getExtensionData(getExtensionData());
		CadsrInformation info = data.getCadsrInformation();
		if (info == null) {
			info = new CadsrInformation();
			info.setNoDomainModel(true);
			data.setCadsrInformation(info);
		}
		for (int i = 0; info.getPackages() != null && i < info.getPackages().length; i++) {
			CadsrPackage currentPackage = info.getPackages(i);
			// find the package's row in the table
			for (int row = 0; row < getPackageNamespaceTable().getRowCount(); row++) {
				if (currentPackage.getName().equals(getPackageNamespaceTable().getValueAt(row, 0))) {
					// set the mapped namespace
					currentPackage.setMappedNamespace((String) getPackageNamespaceTable().getValueAt(row, 1));
					break;
				}
			}
		}
		ExtensionDataUtils.storeExtensionData(getExtensionData(), data);
	}
}
