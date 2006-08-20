/**
 * 
 */
package gov.nih.nci.cagrid.introduce.extensions.metadata.editors.domainmodel;

import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.graph.uml.UMLDiagram;
import gov.nih.nci.cagrid.introduce.portal.extension.ResourcePropertyEditorPanel;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author oster
 */
public class DomainModelViewer extends ResourcePropertyEditorPanel {
	private DomainModel domainModel = null;
	private UMLDiagram umlDiagram = null;
	private JPanel graphPanel = null;
	private JPanel infoPanel = null;
	private JLabel domainLabel = null;
	private JLabel projectDescLabel = null;


	public DomainModelViewer(InputStream doc, File schemaFile, File schemaDir) {
		super(doc, schemaFile, schemaDir);
		if (doc != null) {
			try {
				setDomainModel(MetadataUtils.deserializeDomainModel(new InputStreamReader(doc)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		initialize();
	}


	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.gridy = 1;

		this.setLayout(new GridBagLayout());
		this.add(getGraphPanel(), gridBagConstraints1);
		this.add(getInfoPanel(), gridBagConstraints);
	}


	/**
	 * This method initializes infoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1;
			gridBagConstraints3.weighty = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1;
			gridBagConstraints4.weighty = 1;
			infoPanel = new JPanel();
			infoPanel.setLayout(new GridBagLayout());
			infoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Service Domain Model",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			infoPanel.add(getDomainLabel(), gridBagConstraints3);
			infoPanel.add(getDomainDescLabel(), gridBagConstraints4);
		}
		return infoPanel;
	}


	private JLabel getDomainLabel() {
		if (this.domainLabel == null) {
			domainLabel = new JLabel();
			domainLabel.setText("Project Name Version: ");
			domainLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
		}

		return domainLabel;
	}


	private JLabel getDomainDescLabel() {
		if (this.projectDescLabel == null) {
			projectDescLabel = new JLabel();
			projectDescLabel.setText("Project Description");
			projectDescLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
		}

		return this.projectDescLabel;
	}


	public static void main(String[] args) {
		JFrame f = new JFrame();
		DomainModelViewer viewer = new DomainModelViewer(null, null, null);

		try {
			DomainModel model = MetadataUtils.deserializeDomainModel(new FileReader("domainModel.xml"));
			viewer.setDomainModel(model);

			f.getContentPane().add(viewer);
			f.pack();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the domainModel
	 */
	public DomainModel getDomainModel() {
		return this.domainModel;
	}


	/**
	 * @param domainModel
	 *            the domainModel to set
	 */
	public void setDomainModel(DomainModel domainModel) {
		this.domainModel = domainModel;
		initializeUMLDiagram();
	}


	private void initializeUMLDiagram() {
		getUMLDiagram().clear();
		// add classes
		if (this.domainModel != null) {

			getDomainLabel()
				.setText(domainModel.getProjectLongName() + "  version: " + domainModel.getProjectVersion());
			getDomainDescLabel().setText(domainModel.getProjectDescription());

			// class ID->UMLClass
			Map classMap = new HashMap();
			if (this.domainModel.getExposedUMLClassCollection() != null
				&& this.domainModel.getExposedUMLClassCollection().getUMLClass() != null) {
				UMLClass[] classArr = this.domainModel.getExposedUMLClassCollection().getUMLClass();
				for (int k = 0; k < classArr.length; k++) {
					gov.nih.nci.cagrid.metadata.common.UMLClass c = classArr[k];

					gov.nih.nci.cagrid.graph.uml.UMLClass diagramClass = new gov.nih.nci.cagrid.graph.uml.UMLClass(
						trimClassName(c.getClassName()));
					if (c.getUmlAttributeCollection() != null) {
						if (c.getUmlAttributeCollection().getUMLAttribute() != null) {
							for (int j = 0; j < c.getUmlAttributeCollection().getUMLAttribute().length; j++) {
								UMLAttribute attribute = c.getUmlAttributeCollection().getUMLAttribute()[j];
								diagramClass.addAttribute(attribute.getValueDomain().getDatatypeName(), attribute
									.getName());
							}
						}
					}
					diagramClass.setToolTip(c.getDescription());
					classMap.put(c.getId(), diagramClass);
					getUMLDiagram().addClass(diagramClass);
				}

				if (this.domainModel.getExposedUMLAssociationCollection() != null
					&& this.domainModel.getExposedUMLAssociationCollection().getUMLAssociation() != null) {

					UMLAssociation[] assocArr = this.domainModel.getExposedUMLAssociationCollection()
						.getUMLAssociation();
					for (int k = 0; k < assocArr.length; k++) {
						UMLAssociation assoc = assocArr[k];

						UMLAssociationEdge sourceEdge = assoc.getSourceUMLAssociationEdge().getUMLAssociationEdge();
						UMLAssociationEdge targetEdge = assoc.getTargetUMLAssociationEdge().getUMLAssociationEdge();

						gov.nih.nci.cagrid.graph.uml.UMLClass source = (gov.nih.nci.cagrid.graph.uml.UMLClass) classMap
							.get(sourceEdge.getUMLClassReference().getRefid());
						gov.nih.nci.cagrid.graph.uml.UMLClass target = (gov.nih.nci.cagrid.graph.uml.UMLClass) classMap
							.get(targetEdge.getUMLClassReference().getRefid());

						if (source == null || target == null) {
							System.err
								.println("ERROR: can't process the association, as it references an unexposed class... ignoring!");
							System.err.println("Source ID:" + sourceEdge.getUMLClassReference().getRefid());
							System.err.println("Target ID:" + targetEdge.getUMLClassReference().getRefid());
						} else {
							getUMLDiagram().addAssociation(
								source,
								target,
								sourceEdge.getRoleName(),
								sourceEdge.getMinCardinality()
									+ ".."
									+ (sourceEdge.getMaxCardinality() == -1 ? "*" : String.valueOf(sourceEdge
										.getMaxCardinality())),
								targetEdge.getRoleName(),
								targetEdge.getMinCardinality()
									+ ".."
									+ (targetEdge.getMaxCardinality() == -1 ? "*" : String.valueOf(targetEdge
										.getMaxCardinality())));
						}

					}
				}
			}

		}
		getUMLDiagram().refresh();
	}


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


	public static String trimClassName(String name) {
		if (name == null) {
			return null;
		}

		int ind = name.lastIndexOf(".");
		if (ind >= 0 && ind < name.length() - 1) {
			return name.substring(ind + 1);
		} else {
			return name.trim();
		}

	}


	public boolean save() {
		return false;
	}


	public InputStream getResultRPInputStream() {
		return null;
	}
}
