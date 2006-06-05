package gov.nih.nci.cagrid.data.ui.model;

import gov.nih.nci.cagrid.graph.uml.Attribute;
import gov.nih.nci.cagrid.graph.uml.UMLDiagram;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

/** 
 *  DomainModelPanel
 *  Panel to display and manipulate a DomainModel graphicaly
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 5, 2006 
 * @version $Id$ 
 */
public class DomainModelPanel extends JPanel {

	private UMLDiagram diagram = null;
	
	public DomainModelPanel() {
		super();
		initialize();
	}
	
	
	private void initialize() {
        this.add(getDiagram(), null);
		
	}
	
	
	private UMLDiagram getDiagram() {
		if (diagram == null) {
			diagram = new UMLDiagram();
		}
		return diagram;
	}
	
	
	public void setDomainModel(DomainModel model) {
		// classes with attributes
		Map umlToGraphClasses = new HashMap();
		UMLClass[] classes = model.getExposedUMLClassCollection().getUMLClass();
		for (int i = 0; i < classes.length; i++) {
			gov.nih.nci.cagrid.graph.uml.UMLClass graphClass = convertUmlClass(classes[i]); 
			getDiagram().addClass(graphClass);
			umlToGraphClasses.put(classes[i], graphClass);
		}
		/*
		// associations
		UMLAssociation[] associations = model.getExposedUMLAssociationCollection().getUMLAssociation();
		for (int i = 0; i < associations.length; i++) {
			UMLAssociationEdge sourceEdge = associations[i].getTargetUMLAssociationEdge().getUMLAssociationEdge();
			UMLAssociationEdge targetEdge = associations[i].getSourceUMLAssociationEdge().getUMLAssociationEdge();
			gov.nih.nci.cagrid.graph.uml.UMLClass source = (gov.nih.nci.cagrid.graph.uml.UMLClass) 
				umlToGraphClasses.get(sourceEdge.getUmlClass());
			gov.nih.nci.cagrid.graph.uml.UMLClass target = (gov.nih.nci.cagrid.graph.uml.UMLClass)
				umlToGraphClasses.get(targetEdge.getUmlClass());
			getDiagram().addAssociation(source, target, sourceEdge.getRoleName(), targetEdge.getRoleName(),
				String.valueOf(sourceEdge.getMaxCardinality()), String.valueOf(targetEdge.getMaxCardinality()));
		}
		*/
	}
	
	
	private gov.nih.nci.cagrid.graph.uml.UMLClass convertUmlClass(UMLClass umlClass) {
		gov.nih.nci.cagrid.graph.uml.UMLClass graphClass = 
			new gov.nih.nci.cagrid.graph.uml.UMLClass(umlClass.getClassName());
		UMLAttribute[] attribs = umlClass.getUmlAttributeCollection().getUMLAttribute();
		for (int i = 0; i < attribs.length; i++) {
			Attribute graphAttrib = new Attribute(Attribute.PUBLIC, String.class.getName(), attribs[i].getName());
			graphClass.addAttribute(graphAttrib);
		}
		return graphClass;
	}
}
