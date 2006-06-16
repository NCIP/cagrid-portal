package gov.nih.nci.cagrid.cadsr.portal.discovery;

import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.cadsr.portal.PackageSelectedListener;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.graph.uml.UMLClass;
import gov.nih.nci.cagrid.graph.uml.UMLDiagram;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.NamespaceTypeToolsComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * @author oster
 * 
 */

public class CaDSRTypeDiscoveryComponent extends NamespaceTypeToolsComponent implements PackageSelectedListener {

	private String cadsrURL = null;
	private CaDSRBrowserPanel caDSRPanel = null;
	private JPanel graphPanel = null;
	private UMLDiagram umlDiagram;


	/**
	 * 
	 * @param desc
	 */
	public CaDSRTypeDiscoveryComponent(DiscoveryExtensionDescriptionType desc) {
		super(desc);
		this.cadsrURL = ExtensionTools.getProperty(desc.getProperties(), "CADSR_URL");
		initialize();
		this.getCaDSRPanel().setDefaultCaDSRURL(this.cadsrURL);
		this.getCaDSRPanel().discoverFromCaDSR();
	}


	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1D;
		gridBagConstraints.weighty = 1D;
		this.setLayout(new GridBagLayout());
		this.add(getCaDSRPanel(), gridBagConstraints);
		this.add(getGraphPanel(), gridBagConstraints1);
	}


	/**
	 * This method initializes cadsrPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CaDSRBrowserPanel getCaDSRPanel() {
		if (caDSRPanel == null) {
			caDSRPanel = new CaDSRBrowserPanel(false, false);
			caDSRPanel.addPackageSelectionListener(this);
		}
		return caDSRPanel;
	}


	public void handlePackageSelection(final UMLPackageMetadata pkg) {
		// update the graph for the given package
		Thread t = new Thread() {
			public void run() {
				CaDSRServiceI cadsrService = new CaDSRServiceClient(getCaDSRPanel().getCadsr().getText());
				try {
			
					getUMLDiagram().clear(); 
					UMLClassMetadata[] classes = cadsrService.findClassesInPackage(
						getCaDSRPanel().getSelectedProject(), pkg.getName());

					if (classes != null) {
						for (int i = 0; i < classes.length; i++) {
							UMLClassMetadata clazz = classes[i];
							UMLClass c = new UMLClass(clazz.getName());

							UMLAttributeMetadata[] atts = cadsrService.findAttributesInClass(getCaDSRPanel()
								.getSelectedProject(), clazz);
							if (atts != null) {
								for (int j = 0; j < atts.length; j++) {
									UMLAttributeMetadata att = atts[j];
									// Attribute a = new
									// Attribute(Attribute.PUBLIC, "",
									// att.getName());
									c.addAttribute( /* type */"", att.getName());

								}
							}

							getUMLDiagram().addClass(c);


						}
					}

					UMLAssociation[] assocs = cadsrService.findAssociationsInPackage(getCaDSRPanel()
						.getSelectedProject(), pkg.getName());
					if (assocs != null) {
						// for (int i = 0; i < assocs.length; i++) {
						// UMLAssociation assoc = assocs[i];
						// TODO: create and add Assocation to the graph
						// need to handle class already being in the graph,
						// and not being in the graph
						// also need to handle the association linking to
						// classes in external packages
						// }
					}

					
					getUMLDiagram().refresh();

				} catch (RemoteException e) {
					JOptionPane.showMessageDialog(CaDSRTypeDiscoveryComponent.this,
						"Error communicating with caDSR; please check the caDSR URL!");
				}
			}
		};
		t.start();
	}


	/**
	 * This method initializes graphPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGraphPanel() {
		if (graphPanel == null) {
			graphPanel = new JPanel();
			graphPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints1.gridy = 1;
			graphPanel.add(getUMLDiagram(), gridBagConstraints1);

		}
		return graphPanel;
	}


	private UMLDiagram getUMLDiagram() {
		if (umlDiagram == null) {
			umlDiagram = new UMLDiagram();

		}
		return umlDiagram;
	}


	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ExtensionDescription ext = (ExtensionDescription) Utils.deserializeDocument("extension.xml",
				ExtensionDescription.class);
			final CaDSRTypeDiscoveryComponent panel = new CaDSRTypeDiscoveryComponent(ext
				.getDiscoveryExtensionDescription());
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
