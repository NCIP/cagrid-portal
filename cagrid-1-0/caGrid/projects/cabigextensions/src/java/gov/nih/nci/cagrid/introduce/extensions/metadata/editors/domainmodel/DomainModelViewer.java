/**
 * 
 */
package gov.nih.nci.cagrid.introduce.extensions.metadata.editors.domainmodel;

import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.graph.uml.UMLDiagram;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * @author oster
 */
public class DomainModelViewer extends JPanel {
	private DomainModel domainModel = null;
	private UMLDiagram umlDiagram = null;
	private JPanel graphPanel = null;


	public DomainModelViewer() {
		super();
		initialize();
	}


	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.gridy = 1;

		this.setLayout(new GridBagLayout());
		this.add(getGraphPanel(), gridBagConstraints1);
	}


	public static void main(String[] args) {
		JFrame f = new JFrame();
		DomainModelViewer viewer = new DomainModelViewer();

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
			// class ID->UMLClass
			Map classMap = new HashMap();
			if (this.domainModel.getExposedUMLClassCollection() != null
				&& this.domainModel.getExposedUMLClassCollection().getUMLClass() != null) {
				UMLClass[] classArr = this.domainModel.getExposedUMLClassCollection().getUMLClass();
				for (int k = 0; k < classArr.length; k++) {
					gov.nih.nci.cagrid.metadata.common.UMLClass c = classArr[k];

					gov.nih.nci.cagrid.graph.uml.UMLClass C = new gov.nih.nci.cagrid.graph.uml.UMLClass(trimClassName(c
						.getClassName()));
					if (c.getUmlAttributeCollection() != null) {
						if (c.getUmlAttributeCollection().getUMLAttribute() != null) {
							for (int j = 0; j < c.getUmlAttributeCollection().getUMLAttribute().length; j++) {
								C.addAttribute(c.getUmlAttributeCollection().getUMLAttribute()[j].getName(), " ");
							}
						}
					}
					classMap.put(c.getId(), C);
					getUMLDiagram().addClass(C);
				}

				// TODO: add assocs
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
							System.err.println("processing");
							getUMLDiagram().addAssociation(source, target, sourceEdge.getRoleName(),
								sourceEdge.getMinCardinality() + ".." + sourceEdge.getMaxCardinality(),
								targetEdge.getRoleName(),
								targetEdge.getMinCardinality() + ".." + targetEdge.getMaxCardinality());
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
}
