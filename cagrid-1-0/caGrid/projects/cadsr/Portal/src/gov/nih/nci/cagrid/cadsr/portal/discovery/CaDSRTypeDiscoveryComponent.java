package gov.nih.nci.cagrid.cadsr.portal.discovery;

import gov.nih.nci.cadsr.domain.ValueDomain;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.cadsr.portal.PackageSelectedListener;
import gov.nih.nci.cagrid.cadsr.portal.ProjectSelectedListener;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.graph.uml.UMLClass;
import gov.nih.nci.cagrid.graph.uml.UMLDiagram;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.NamespaceTypeToolsComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * @author oster
 * 
 */

public class CaDSRTypeDiscoveryComponent extends NamespaceTypeToolsComponent
	implements
		PackageSelectedListener,
		ProjectSelectedListener {
	private CaDSRBrowserPanel caDSRPanel = null;
	private JPanel graphPanel = null;
	private UMLDiagram umlDiagram;


	/**
	 * 
	 * @param desc
	 */
	public CaDSRTypeDiscoveryComponent(DiscoveryExtensionDescriptionType desc) {
		super(desc);
		initialize();
		this.getCaDSRPanel().setDefaultCaDSRURL(getCaDSRURL());
		this.getCaDSRPanel().discoverFromCaDSR();
	}


	private String getCaDSRURL() {
		return ResourceManager.getServiceURLProperty(CaDSRDiscoveryConstants.CADSR_URL_PROPERTY);
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
		gridBagConstraints.weightx = 0.0D;
		gridBagConstraints.weighty = 0.0D;
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
			caDSRPanel.addProjectSelectionListener(this);
		}
		return caDSRPanel;
	}


	public void handleProjectSelection(Project project) {
		this.getCaDSRPanel().getCadsr().setText(getCaDSRURL());
	}


	public void handlePackageSelection(final UMLPackageMetadata pkg) {
		this.getCaDSRPanel().getCadsr().setText(getCaDSRURL());
		// update the graph for the given package
		Thread t = new Thread() {
			public void run() {
				try {
					final int progressEventID = getCaDSRPanel().getMultiEventProgressBar().startEvent(
						"Processing Package " + pkg.getName());
					CaDSRServiceI cadsrService = new CaDSRServiceClient(getCaDSRPanel().getCadsr().getText());

					getUMLDiagram().clear();
					UMLClassMetadata[] classes = cadsrService.findClassesInPackage(
						getCaDSRPanel().getSelectedProject(), pkg.getName());

					Map classMap = new HashMap();

					if (classes != null) {
						for (int i = 0; i < classes.length; i++) {
							UMLClassMetadata clazz = classes[i];
							UMLClass c = new UMLClass(clazz.getName());
							getCaDSRPanel().getMultiEventProgressBar().updateProgress(
								"Processing Class " + clazz.getName() + " ( " + i + " of " + classes.length + ")", 0,
								classes.length, i);

							UMLAttributeMetadata[] atts = cadsrService.findAttributesInClass(getCaDSRPanel()
								.getSelectedProject(), clazz);
							if (atts != null) {
								for (int j = 0; j < atts.length; j++) {
									UMLAttributeMetadata att = atts[j];
									ValueDomain domain = cadsrService.findValueDomainForAttribute(getCaDSRPanel()
										.getSelectedProject(), att);
									c.addAttribute(domain.getDatatypeName(), att.getName());

								}
							}
							classMap.put(clazz.getId(), c);
							getUMLDiagram().addClass(c);
						}
					}

					final int assocProgressEventID = getCaDSRPanel().getMultiEventProgressBar().startEvent(
						"Processing Associations...");
					UMLAssociation[] assocs = cadsrService.findAssociationsInPackage(getCaDSRPanel()
						.getSelectedProject(), pkg.getName());
					if (assocs != null) {
						for (int i = 0; i < assocs.length; i++) {
							UMLAssociation assoc = assocs[i];
							getCaDSRPanel().getMultiEventProgressBar().updateProgress(
								"Processing Association " + " ( " + i + " of " + assocs.length + ")", 0, assocs.length,
								i);
							UMLClassMetadata source = assoc.getSourceUMLClassMetadata().getUMLClassMetadata();
							UMLClassMetadata target = assoc.getTargetUMLClassMetadata().getUMLClassMetadata();

							UMLClass sourceGraph = (UMLClass) classMap.get(source.getId());
							UMLClass targetGraph = (UMLClass) classMap.get(target.getId());

							if (sourceGraph == null || targetGraph == null) {
								System.out
									.println("Skipping association, as both source and target are not in this package.");
								System.out.println("Source:" + source.getFullyQualifiedName());
								System.out.println("Target:" + target.getFullyQualifiedName());
							} else {
								getUMLDiagram().addAssociation(
									sourceGraph,
									targetGraph,
									assoc.getSourceRoleName(),
									assoc.getSourceMinCardinality()
										+ ".."
										+ (assoc.getSourceMaxCardinality() == -1 ? "*" : String.valueOf(assoc
											.getSourceMaxCardinality())),
									assoc.getTargetRoleName(),
									assoc.getTargetMinCardinality()
										+ ".."
										+ (assoc.getTargetMaxCardinality() == -1 ? "*" : String.valueOf(assoc
											.getTargetMaxCardinality())));
							}
						}
					}

					getCaDSRPanel().getMultiEventProgressBar().stopEvent(assocProgressEventID,
						"Done with Associations.");

					final int renderProgressEventID = getCaDSRPanel().getMultiEventProgressBar().startEvent(
						"Rendering...");
					getUMLDiagram().refresh();
					getCaDSRPanel().getMultiEventProgressBar().stopEvent(renderProgressEventID, "Done with Rendering.");
					getCaDSRPanel().getMultiEventProgressBar().stopEvent(progressEventID, "Done with Package.");

				} catch (RemoteException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(CaDSRTypeDiscoveryComponent.this,
						"Error communicating with caDSR; please check the caDSR URL!");
					getCaDSRPanel().getMultiEventProgressBar().stopAll(
						"Error communicating with caDSR; please check the caDSR URL!");
					getUMLDiagram().clear();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(CaDSRTypeDiscoveryComponent.this, "Error processing model!");
					getCaDSRPanel().getMultiEventProgressBar().stopAll("Error processing model!");
					getUMLDiagram().clear();
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
