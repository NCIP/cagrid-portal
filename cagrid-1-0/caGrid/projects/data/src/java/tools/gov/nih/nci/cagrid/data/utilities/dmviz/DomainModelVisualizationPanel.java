package gov.nih.nci.cagrid.data.utilities.dmviz;

import gov.nih.nci.cagrid.graph.uml.UMLClassAssociation;
import gov.nih.nci.cagrid.graph.uml.UMLDiagram;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.tigris.gef.event.GraphSelectionEvent;
import org.tigris.gef.event.GraphSelectionListener;

/** 
 *  DomainModelVisualizationPanel
 *  Visualizes a domain model
 * 
 * @author David Ervin
 * 
 * @created Mar 30, 2007 10:23:05 AM
 * @version $Id: DomainModelVisualizationPanel.java,v 1.1 2007-03-30 20:40:42 dervin Exp $ 
 */
public class DomainModelVisualizationPanel extends JPanel {

    private List<ModelSelectionListener> modelListeners;
    private Map<gov.nih.nci.cagrid.graph.uml.UMLClass, UMLClass> graphClassToUML;
    
    private UMLDiagram umlDiagram = null;
    
    public DomainModelVisualizationPanel() {
        modelListeners = new LinkedList<ModelSelectionListener>();
        graphClassToUML = new HashMap<gov.nih.nci.cagrid.graph.uml.UMLClass, UMLClass>();
        setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0D;
        cons.weighty = 1.0D;
        add(getUmlDiagram(), cons);
    }
    
    
    public void addModelSelectionListener(ModelSelectionListener listener) {
        modelListeners.add(listener);
    }
    
    
    public boolean removeModelSelectionListener(ModelSelectionListener listener) {
        return modelListeners.remove(listener);
    }
    
    
    protected void fireClassSelected(UMLClass selected) {
        for (ModelSelectionListener listener : modelListeners) {
            listener.classSelected(selected);
        }
    }
    
    
    protected void fireAssociationSelected(UMLAssociation selected) {
        for (ModelSelectionListener listener : modelListeners) {
            listener.associationSelected(selected);
        }
    }
    
    
    public void setDomainModel(DomainModel model) {
        // this keeps UML class ids mapped to the classes in the graph 
        Map<String, gov.nih.nci.cagrid.graph.uml.UMLClass> idsToGraphClasses = 
            new HashMap<String, gov.nih.nci.cagrid.graph.uml.UMLClass>();
        
        graphClassToUML.clear();
        getUmlDiagram().clear();
        
        DomainModelExposedUMLClassCollection exposedClassCollection = 
            model.getExposedUMLClassCollection();
        if (exposedClassCollection == null || exposedClassCollection.getUMLClass() == null) {
            return;
        }
        
        // put the classes into the graph
        UMLClass[] modelClasses = exposedClassCollection.getUMLClass();
        for (UMLClass currentModelClass : modelClasses) {
            gov.nih.nci.cagrid.graph.uml.UMLClass graphClass = 
                new gov.nih.nci.cagrid.graph.uml.UMLClass(
                    currentModelClass.getPackageName() + 
                    "." + currentModelClass.getClassName());
            
            // add attributes to the graph class
            UMLAttribute[] attribs = currentModelClass.getUmlAttributeCollection().getUMLAttribute();
            for (UMLAttribute currentAttrib : attribs) {
                graphClass.addAttribute(currentAttrib.getDataTypeName(), currentAttrib.getName());
            }
            getUmlDiagram().addClass(graphClass);
            
            idsToGraphClasses.put(currentModelClass.getId(), graphClass);
            graphClassToUML.put(graphClass, currentModelClass);
        }
        
        // associations
        DomainModelExposedUMLAssociationCollection exposedAssociationCollection = 
            model.getExposedUMLAssociationCollection();
        if (exposedAssociationCollection != null 
            && exposedAssociationCollection.getUMLAssociation() != null) {
           UMLAssociation[] associations = exposedAssociationCollection.getUMLAssociation();
           for (UMLAssociation currentAssociation : associations) {
               String sourceId = null;
               String sourceRoleName = null;
               int sourceMinCardinality = -1;
               int sourceMaxCardinality = -1;
               if (currentAssociation.getSourceUMLAssociationEdge() != null 
                   && currentAssociation.getSourceUMLAssociationEdge().getUMLAssociationEdge() != null) {
                   UMLAssociationEdge sourceEdge = currentAssociation.getSourceUMLAssociationEdge().getUMLAssociationEdge();
                   sourceId = sourceEdge.getUMLClassReference().getRefid();
                   sourceRoleName = sourceEdge.getRoleName();
                   sourceMinCardinality = sourceEdge.getMinCardinality();
                   sourceMaxCardinality = sourceEdge.getMaxCardinality();
               }
               String targetId = null;
               String targetRoleName = null;
               int targetMinCardinality = -1;
               int targetMaxCardinality = -1;
               if (currentAssociation.getTargetUMLAssociationEdge() != null
                   && currentAssociation.getTargetUMLAssociationEdge().getUMLAssociationEdge() != null) {
                   UMLAssociationEdge targetEdge = currentAssociation.getTargetUMLAssociationEdge().getUMLAssociationEdge();
                   targetId = targetEdge.getUMLClassReference().getRefid();
                   targetRoleName = targetEdge.getRoleName();
                   targetMinCardinality = targetEdge.getMinCardinality();
                   targetMaxCardinality = targetEdge.getMaxCardinality();
               }
               gov.nih.nci.cagrid.graph.uml.UMLClass sourceClass 
                   = idsToGraphClasses.get(sourceId);
               gov.nih.nci.cagrid.graph.uml.UMLClass targetClass 
                   = idsToGraphClasses.get(targetId);
               String sourceCardinality = sourceMinCardinality + ".." + 
                   (sourceMaxCardinality == -1 ? "*" : String.valueOf(sourceMaxCardinality));
               String targetCardinality = targetMinCardinality + ".." +
                   (targetMaxCardinality == -1 ? "*" : String.valueOf(targetMaxCardinality));
               
               getUmlDiagram().addAssociation(sourceClass, targetClass,
                   sourceRoleName, sourceCardinality, targetRoleName, targetCardinality);
           }
        }
        getUmlDiagram().refresh();
    }
    
    
    private UMLDiagram getUmlDiagram() {
        if (umlDiagram == null) {
            umlDiagram = new UMLDiagram();
            umlDiagram.getViewer().addGraphSelectionListener(new GraphSelectionListener() {
                public void selectionChanged(GraphSelectionEvent e) {
                    if (e.getSelections() != null && e.getSelections().size() != 0) {
                        Object selection = e.getSelections().get(0);
                        if (selection instanceof gov.nih.nci.cagrid.graph.uml.UMLClass) {
                            UMLClass selectedUmlClass = graphClassToUML.get(selection);
                            fireClassSelected(selectedUmlClass);
                        } else if (selection instanceof UMLClassAssociation) {
                            // get the endpoints of the graphical association
                            UMLClassAssociation graphAssociation = (UMLClassAssociation) selection;
                            gov.nih.nci.cagrid.graph.uml.UMLClass sourceGraphClass = 
                                (gov.nih.nci.cagrid.graph.uml.UMLClass) graphAssociation.getSourcePortFig();
                            gov.nih.nci.cagrid.graph.uml.UMLClass targetGraphClass = 
                                (gov.nih.nci.cagrid.graph.uml.UMLClass) graphAssociation.getDestPortFig();
                            
                            // get the UML classes
                            UMLClass sourceClass = graphClassToUML.get(sourceGraphClass);
                            String sourceRoleName = graphAssociation.sourceLabel.getText();
                            int[] sourceCardinality = getCardinalities(graphAssociation.sourceMultiplicity.getText());
                            
                            UMLClass targetClass = graphClassToUML.get(targetGraphClass);
                            String targetRoleName = graphAssociation.destinationLabel.getText();
                            int[] targetCardinality = getCardinalities(graphAssociation.destinationMultiplicity.getText());
                            
                            // create the source and target edges
                            UMLAssociationEdge sourceEdge = new UMLAssociationEdge();
                            sourceEdge.setUMLClassReference(new UMLClassReference(sourceClass.getId()));
                            sourceEdge.setRoleName(sourceRoleName);
                            sourceEdge.setMinCardinality(sourceCardinality[0]);
                            sourceEdge.setMaxCardinality(sourceCardinality[1]);
                            
                            UMLAssociationEdge targetEdge = new UMLAssociationEdge();
                            targetEdge.setUMLClassReference(new UMLClassReference(targetClass.getId()));
                            targetEdge.setRoleName(targetRoleName);
                            targetEdge.setMinCardinality(targetCardinality[0]);
                            targetEdge.setMaxCardinality(targetCardinality[1]);
                            
                            // create the association instance
                            UMLAssociation association = new UMLAssociation();
                            // TODO: determine bidirectionality of the association
                            association.setSourceUMLAssociationEdge(new UMLAssociationSourceUMLAssociationEdge(sourceEdge));
                            association.setTargetUMLAssociationEdge(new UMLAssociationTargetUMLAssociationEdge(targetEdge));
                            
                            // fire the event
                            fireAssociationSelected(association);
                        }
                    }
                }
            });
        }
        return umlDiagram;
    }
    
    
    private int[] getCardinalities(String cards) {
        int dotIndex = cards.indexOf("..");
        int min = Integer.valueOf(cards.substring(0, dotIndex)).intValue();
        String maxVal = cards.substring(dotIndex + "..".length());
        if (maxVal.equals("*")) {
            return new int[] {min, -1};
        }
        return new int[] {min, Integer.valueOf(maxVal).intValue()};
    }
    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DomainModelVisualizationPanel panel = new DomainModelVisualizationPanel();
        frame.setContentPane(panel);
        // JFileChooser chooser = new JFileChooser();
        // chooser.showOpenDialog(frame);
        // File choice = chooser.getSelectedFile();
        File choice = new File("../data/caBIO-domainModel.xml");
        try {
            DomainModel model = MetadataUtils.deserializeDomainModel(new FileReader(choice));
            panel.setDomainModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame.setVisible(true);
    }
}
