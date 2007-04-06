package gov.nih.nci.cagrid.data.utilities.vizquery;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.data.utilities.dmviz.DomainModelVisualizationPanel;
import gov.nih.nci.cagrid.data.utilities.dmviz.ModelSelectionListener;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.AssociationTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.AttributeTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.GroupTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.IconTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.QueryTree;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.QueryTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.RebuildableTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.TargetTreeNode;
import gov.nih.nci.cagrid.data.utilities.vizquery.attributes.SetAttributePredicateDialog;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReference;

/** 
 *  VisualQueryBuilder
 *  CQL Query builder driven from graphical presentation of a Domain Model
 * 
 * @author David Ervin
 * 
 * @created Mar 30, 2007 3:46:34 PM
 * @version $Id: VisualQueryBuilder.java,v 1.2 2007-04-06 14:50:14 dervin Exp $ 
 */
public class VisualQueryBuilder extends JFrame {
    
    private DomainModelVisualizationPanel domainModelPanel = null;
    private QueryTree cqlQueryTree = null;
    private AssociationInformationPanel associationInfoPanel = null;
    private JMenuBar mainMenuBar = null;
    private JMenu modelMenu = null;
    private JMenu queryMenu = null;
    private JMenuItem loadModelFromFileMenuItem = null;
    private JMenuItem loadModelFromServiceMenuItem = null;
    private JMenuItem loadQueryMenuItem = null;
    private JMenuItem saveQueryMenuItem = null;
    private JPanel modelPanel = null;
    private JList attributesList = null;
    private JScrollPane attributesScrollPane = null;
    private JScrollPane cqlQueryTreeScrollPane = null;
    private JSplitPane mainSplitPane = null;
    private JPanel queryBuildingPanel = null;
    private JMenuBar queryBuildingMenuBar = null;
    private JMenu queryAddMenu = null;
    private JMenu querySetMenu = null;
    private JMenu queryRemoveMenu = null;
    private JMenuItem addGroupMenuItem = null;
    private JMenuItem addAssociationMenuItem = null;
    private JMenuItem addAttributeMenuItem = null;
    private JMenuItem setTargetMenuItem = null;
    private JMenuItem setGroupLogicMenuItem = null;
    private JMenuItem setAttributeValueMenuItem = null;
    private JMenuItem setAttributePredicateMenuItem = null;
    private JMenuItem removeCurrentItemMenuItem = null;
    private JMenuItem clearQueryMenuItem = null;
    
    private DomainModel currentModel = null;
    
    public VisualQueryBuilder() {
        super("Visual CQL Query Builder");
        ErrorDialog.setOwnerFrame(this);
        initialize();
    }


    private void initialize() {
        this.setSize(new Dimension(571, 307));
        this.setContentPane(getMainSplitPane());
        this.setSize(new Dimension(421, 251));
        this.setJMenuBar(getMainMenuBar());
    }
    
    
    private void setCurrentModel(DomainModel model) {
        // TODO: clear query tree, validation, etc. BEFORE setting the model
        getDomainModelPanel().setDomainModel(currentModel);
        currentModel = model;
    }
    
    
    private DomainModelVisualizationPanel getDomainModelPanel() {
        if (domainModelPanel == null) {
            domainModelPanel = new DomainModelVisualizationPanel();
            domainModelPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Domain Model", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, null));
            domainModelPanel.addModelSelectionListener(new ModelSelectionListener() {
                public void classSelected(UMLClass selection) {
                    UMLAttribute[] attribs = selection.getUmlAttributeCollection().getUMLAttribute();
                    String[] values = new String[attribs.length];
                    for (int i = 0; i < attribs.length; i++) {
                        values[i] = attribs[i].getName() + " : " + attribs[i].getDataTypeName();
                    }
                    getAttributesList().setListData(values);
                }
                
                
                public void associationSelected(UMLAssociation selection) {
                    getAssociationInfoPanel().setAssociation(selection, currentModel);
                }
            });
        }
        return domainModelPanel;
    }
    
    
    private QueryTree getCqlQueryTree() {
        if (cqlQueryTree == null) {
            cqlQueryTree = new QueryTree();
            // give the tree an empty query to start with
            cqlQueryTree.setQuery(new CQLQuery());
            cqlQueryTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    if (e.isAddedPath()) {
                        // selection made
                        TreePath selectionPath = e.getPath();
                        if (selectionPath.getLastPathComponent() instanceof IconTreeNode) {
                            IconTreeNode node = (IconTreeNode) selectionPath.getLastPathComponent();
                            if (node instanceof QueryTreeNode) {
                                // root of the query
                                PortalUtils.setContainerEnabled(getQueryBuildingMenuBar(), false);
                                if (((QueryTreeNode) node).getQuery().getTarget() == null) {
                                    getQuerySetMenu().setEnabled(true);
                                    getSetTargetMenuItem().setEnabled(true);   
                                }
                            } else if (node instanceof TargetTreeNode) {
                                // query target selected
                                // disable most of the query building toolbar
                                PortalUtils.setContainerEnabled(getQueryBuildingMenuBar(), false);
                                // can always remove stuff
                                PortalUtils.setContainerEnabled(getQueryRemoveMenu(), true);
                                // if target has no children, it can be added to
                                Object target = ((TargetTreeNode) node).getTarget();
                                if (target.getAssociation() == null
                                    && target.getGroup() == null
                                    && target.getAttribute() == null) {
                                    PortalUtils.setContainerEnabled(getQueryAddMenu(), true);
                                }
                            } else if (node instanceof GroupTreeNode) {
                                // group selected
                                // disable most of the query building toolbar
                                PortalUtils.setContainerEnabled(getQueryBuildingMenuBar(), false);
                                // can always remove stuff
                                PortalUtils.setContainerEnabled(getQueryRemoveMenu(), true);
                                // groups can always be added to
                                PortalUtils.setContainerEnabled(getQueryAddMenu(), true);
                                // enable choice items
                                getQuerySetMenu().setEnabled(true);
                                getSetGroupLogicMenuItem().setEnabled(true);
                            } else if (node instanceof AssociationTreeNode) {
                                // association selected
                                // disable most of the query building toolbar
                                PortalUtils.setContainerEnabled(getQueryBuildingMenuBar(), false);
                                // can always remove stuff
                                PortalUtils.setContainerEnabled(getQueryRemoveMenu(), true);
                                // see what (if any) further restrictions exist on the association
                                Association association = ((AssociationTreeNode) node).getAssociation();
                                if (association.getAssociation() == null 
                                    && association.getGroup() == null
                                    && association.getAttribute() == null) {
                                    // can add to the association
                                    PortalUtils.setContainerEnabled(getQueryAddMenu(), true);
                                }
                            } else if (node instanceof AttributeTreeNode) {
                                // attribute selected
                                // disable everything in the query builing toolbar
                                PortalUtils.setContainerEnabled(getQueryBuildingMenuBar(), false);
                                // can always remove stuff
                                PortalUtils.setContainerEnabled(getQueryRemoveMenu(), true);
                                // enable a few choice items
                                getQuerySetMenu().setEnabled(true);
                                getSetAttributeValueMenuItem().setEnabled(true);
                                getSetAttributeValueMenuItem().setEnabled(true);
                            }
                        }
                    }
                }
            });

        }
        return cqlQueryTree;
    }
    
    
    private AssociationInformationPanel getAssociationInfoPanel() {
        if (associationInfoPanel == null) {
            associationInfoPanel = new AssociationInformationPanel();
            associationInfoPanel.setBorder(BorderFactory.createTitledBorder(
                null, "Association Information", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, null));
        }
        return associationInfoPanel;
    }
    

    /**
     * This method initializes mainMenuBar	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getMainMenuBar() {
        if (mainMenuBar == null) {
            mainMenuBar = new JMenuBar();
            mainMenuBar.add(getModelMenu());
            mainMenuBar.add(getQueryMenu());
        }
        return mainMenuBar;
    }


    /**
     * This method initializes modelMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getModelMenu() {
        if (modelMenu == null) {
            modelMenu = new JMenu();
            modelMenu.setText("Model");
            modelMenu.add(getLoadModelFromFileMenuItem());
            modelMenu.add(getLoadModelFromServiceMenuItem());
        }
        return modelMenu;
    }


    /**
     * This method initializes queryMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getQueryMenu() {
        if (queryMenu == null) {
            queryMenu = new JMenu();
            queryMenu.setText("Query");
            queryMenu.add(getLoadQueryMenuItem());
            queryMenu.add(getSaveQueryMenuItem());
        }
        return queryMenu;
    }


    /**
     * This method initializes loadModelFromFileMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getLoadModelFromFileMenuItem() {
        if (loadModelFromFileMenuItem == null) {
            loadModelFromFileMenuItem = new JMenuItem();
            loadModelFromFileMenuItem.setText("Load From File");
            loadModelFromFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(FileFilters.XML_FILTER);
                    int choice = chooser.showOpenDialog(VisualQueryBuilder.this);
                    if (choice == JFileChooser.APPROVE_OPTION) {
                        File dmFile = chooser.getSelectedFile();
                        try {
                            DomainModel model = MetadataUtils.deserializeDomainModel(
                                new FileReader(dmFile));
                            setCurrentModel(model);
                        } catch (Exception ex) {
                            ErrorDialog.showErrorDialog(
                                "Error loading domain model: " + ex.getMessage(), ex);
                        }
                    }
                }
            });
        }
        return loadModelFromFileMenuItem;
    }


    /**
     * This method initializes loadModelFromServiceMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getLoadModelFromServiceMenuItem() {
        if (loadModelFromServiceMenuItem == null) {
            loadModelFromServiceMenuItem = new JMenuItem();
            loadModelFromServiceMenuItem.setText("Load From Service");
            loadModelFromServiceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String url = JOptionPane.showInputDialog(VisualQueryBuilder.this, 
                        "Enter the data service URL", "Enter URL", JOptionPane.INFORMATION_MESSAGE);
                    if (url != null) { // null URL means canceled
                        try {
                            Address addy = new Address(url);
                            EndpointReference epr = new EndpointReference(addy);
                            DomainModel model = MetadataUtils.getDomainModel(epr);
                            setCurrentModel(model);
                        } catch (Exception ex) {
                            ErrorDialog.showErrorDialog(
                                "Error retrieving domain model from service: " + ex.getMessage(), ex);
                        }                 
                    }
                }
            });
        }
        return loadModelFromServiceMenuItem;
    }


    /**
     * This method initializes loadQueryMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getLoadQueryMenuItem() {
        if (loadQueryMenuItem == null) {
            loadQueryMenuItem = new JMenuItem();
            loadQueryMenuItem.setText("Load");
        }
        return loadQueryMenuItem;
    }


    /**
     * This method initializes saveQueryMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getSaveQueryMenuItem() {
        if (saveQueryMenuItem == null) {
            saveQueryMenuItem = new JMenuItem();
            saveQueryMenuItem.setText("Save");
        }
        return saveQueryMenuItem;
    }


    /**
     * This method initializes modelPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getModelPanel() {
        if (modelPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 2;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.ipady = 50;
            gridBagConstraints1.gridx = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.weighty = 1.0D;
            gridBagConstraints.gridy = 0;
            modelPanel = new JPanel();
            modelPanel.setLayout(new GridBagLayout());
            modelPanel.add(getDomainModelPanel(), gridBagConstraints);
            modelPanel.add(getAttributesScrollPane(), gridBagConstraints1);
            modelPanel.add(getAssociationInfoPanel(), gridBagConstraints11);
        }
        return modelPanel;
    }


    /**
     * This method initializes attributesList	
     * 	
     * @return javax.swing.JList	
     */
    private JList getAttributesList() {
        if (attributesList == null) {
            attributesList = new JList();
        }
        return attributesList;
    }


    /**
     * This method initializes attributesScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getAttributesScrollPane() {
        if (attributesScrollPane == null) {
            attributesScrollPane = new JScrollPane();
            attributesScrollPane.setBorder(BorderFactory.createTitledBorder(
                null, "Attributes", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, null));
            attributesScrollPane.setViewportView(getAttributesList());
        }
        return attributesScrollPane;
    }


    /**
     * This method initializes cqlQueryTreeScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getCqlQueryTreeScrollPane() {
        if (cqlQueryTreeScrollPane == null) {
            cqlQueryTreeScrollPane = new JScrollPane();
            cqlQueryTreeScrollPane.setBorder(BorderFactory.createTitledBorder(
                null, "CQL Query", TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, null, null));
            cqlQueryTreeScrollPane.setViewportView(getCqlQueryTree());
        }
        return cqlQueryTreeScrollPane;
    }


    /**
     * This method initializes mainSplitPane	
     * 	
     * @return javax.swing.JSplitPane	
     */
    private JSplitPane getMainSplitPane() {
        if (mainSplitPane == null) {
            mainSplitPane = new JSplitPane();
            mainSplitPane.setResizeWeight(0.5D);
            mainSplitPane.setLeftComponent(getModelPanel());
            mainSplitPane.setRightComponent(getQueryBuildingPanel());
            mainSplitPane.setOneTouchExpandable(true);
        }
        return mainSplitPane;
    }


    /**
     * This method initializes queryBuildingPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getQueryBuildingPanel() {
        if (queryBuildingPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.BOTH;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.ipady = 30;
            gridBagConstraints3.gridx = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.BOTH;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.weighty = 1.0D;
            gridBagConstraints2.gridx = 0;
            queryBuildingPanel = new JPanel();
            queryBuildingPanel.setLayout(new GridBagLayout());
            queryBuildingPanel.add(getCqlQueryTreeScrollPane(), gridBagConstraints2);
            queryBuildingPanel.add(getQueryBuildingMenuBar(), gridBagConstraints3);
        }
        return queryBuildingPanel;
    }


    /**
     * This method initializes queryBuildingToolBar	
     * 	
     * @return javax.swing.JToolBar	
     */
    private JMenuBar getQueryBuildingMenuBar() {
        if (queryBuildingMenuBar == null) {
            queryBuildingMenuBar = new JMenuBar();
            queryBuildingMenuBar.add(getQueryAddMenu());
            queryBuildingMenuBar.add(getQuerySetMenu());
            queryBuildingMenuBar.add(getQueryRemoveMenu());
        }
        return queryBuildingMenuBar;
    }


    /**
     * This method initializes queryAddMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getQueryAddMenu() {
        if (queryAddMenu == null) {
            queryAddMenu = new JMenu();
            queryAddMenu.setText("Add");
            queryAddMenu.add(getAddGroupMenuItem());
            queryAddMenu.add(getAddAssociationMenuItem());
            queryAddMenu.add(getAddAttributeMenuItem());
        }
        return queryAddMenu;
    }


    /**
     * This method initializes querySetMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getQuerySetMenu() {
        if (querySetMenu == null) {
            querySetMenu = new JMenu();
            querySetMenu.setText("Set");
            querySetMenu.add(getSetTargetMenuItem());
            querySetMenu.add(getSetGroupLogicMenuItem());
            querySetMenu.add(getSetAttributeValueMenuItem());
            querySetMenu.add(getSetAttributePredicateMenuItem());
        }
        return querySetMenu;
    }


    /**
     * This method initializes queryRemoveMenu	
     * 	
     * @return javax.swing.JMenu	
     */
    private JMenu getQueryRemoveMenu() {
        if (queryRemoveMenu == null) {
            queryRemoveMenu = new JMenu();
            queryRemoveMenu.setText("Remove");
            queryRemoveMenu.add(getRemoveCurrentItemMenuItem());
            queryRemoveMenu.add(getClearQueryMenuItem());
        }
        return queryRemoveMenu;
    }


    /**
     * This method initializes addGroupMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getAddGroupMenuItem() {
        if (addGroupMenuItem == null) {
            addGroupMenuItem = new JMenuItem();
            addGroupMenuItem.setText("Group");
            addGroupMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // create the new group
                    Group group = new Group();
                    SetGroupLogicDialog.setLogic(VisualQueryBuilder.this, group);
                    // attach the group to the selected node
                    RebuildableTreeNode selectedNode = (RebuildableTreeNode) getCqlQueryTree()
                        .getSelectionPath().getLastPathComponent();
                    if (selectedNode instanceof TargetTreeNode) {
                        ((TargetTreeNode) selectedNode).getTarget().setGroup(group);
                    } else if (selectedNode instanceof AssociationTreeNode) {
                        ((AssociationTreeNode) selectedNode).getAssociation().setGroup(group);
                    } else if (selectedNode instanceof GroupTreeNode) {
                        Group selectedGroup = ((GroupTreeNode) selectedNode).getGroup();
                        Group[] childGroups = selectedGroup.getGroup();
                        if (childGroups == null) {
                            childGroups = new Group[] {group};
                        } else {
                            childGroups = (Group[]) Utils.appendToArray(childGroups, group);
                        }
                        selectedGroup.setGroup(childGroups);
                    }
                    // rebuild the diplay
                    selectedNode.rebuild();
                }
            });
        }
        return addGroupMenuItem;
    }


    /**
     * This method initializes addAssociationMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getAddAssociationMenuItem() {
        if (addAssociationMenuItem == null) {
            addAssociationMenuItem = new JMenuItem();
            addAssociationMenuItem.setText("Association");
            addAssociationMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // create the association
                    Association association = new Association();
                    association.setName(getAssociationInfoPanel().getTargetClassName());
                    association.setRoleName(getAssociationInfoPanel().getSourceRoleName());
                    // get the selected query point
                    RebuildableTreeNode selectedNode = (RebuildableTreeNode) getCqlQueryTree().getSelectionPath().getLastPathComponent();
                    if (selectedNode instanceof TargetTreeNode) {
                        ((TargetTreeNode) selectedNode).getTarget().setAssociation(association);
                    } else if (selectedNode instanceof AssociationTreeNode) {
                        ((AssociationTreeNode) selectedNode).getAssociation().setAssociation(association);
                    } else if (selectedNode instanceof GroupTreeNode) {
                        Group currentGroup = ((GroupTreeNode) selectedNode).getGroup();
                        Association[] groupAssociations = currentGroup.getAssociation();
                        if (groupAssociations == null) {
                            groupAssociations = new Association[] {association};
                        } else {
                            groupAssociations = (Association[]) Utils.appendToArray(
                                groupAssociations, association);
                        }
                        currentGroup.setAssociation(groupAssociations);
                    }
                    selectedNode.rebuild();
                }
            });
        }
        return addAssociationMenuItem;
    }


    /**
     * This method initializes addAttributeMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getAddAttributeMenuItem() {
        if (addAttributeMenuItem == null) {
            addAttributeMenuItem = new JMenuItem();
            addAttributeMenuItem.setText("Attribute");
            addAttributeMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return addAttributeMenuItem;
    }


    /**
     * This method initializes setTargetMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getSetTargetMenuItem() {
        if (setTargetMenuItem == null) {
            setTargetMenuItem = new JMenuItem();
            setTargetMenuItem.setText("Target");
            setTargetMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return setTargetMenuItem;
    }


    /**
     * This method initializes setGroupLogicMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getSetGroupLogicMenuItem() {
        if (setGroupLogicMenuItem == null) {
            setGroupLogicMenuItem = new JMenuItem();
            setGroupLogicMenuItem.setText("Group Logic");
            setGroupLogicMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // get the selected group node
                    GroupTreeNode groupNode = (GroupTreeNode) getCqlQueryTree()
                        .getSelectionPath().getLastPathComponent();
                    Group group = groupNode.getGroup();
                    SetGroupLogicDialog.setLogic(VisualQueryBuilder.this, group);
                    // re-render the group node
                    groupNode.rebuild();
                }
            });
        }
        return setGroupLogicMenuItem;
    }


    /**
     * This method initializes setAttributeValueMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getSetAttributeValueMenuItem() {
        if (setAttributeValueMenuItem == null) {
            setAttributeValueMenuItem = new JMenuItem();
            setAttributeValueMenuItem.setText("Attribute Value");
            setAttributeValueMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return setAttributeValueMenuItem;
    }


    /**
     * This method initializes setAttributePredicateMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getSetAttributePredicateMenuItem() {
        if (setAttributePredicateMenuItem == null) {
            setAttributePredicateMenuItem = new JMenuItem();
            setAttributePredicateMenuItem.setText("Attribute Predicate");
            setAttributePredicateMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // get the attribute
                    AttributeTreeNode selectedNode = (AttributeTreeNode) 
                        getCqlQueryTree().getSelectionPath().getLastPathComponent();
                    Attribute attrib = selectedNode.getAttribute();
                    // set the new value
                    SetAttributePredicateDialog.setAttributePredicate(
                        VisualQueryBuilder.this, attrib);
                    // rebuild the GUI
                    selectedNode.rebuild();
                }
            });
        }
        return setAttributePredicateMenuItem;
    }


    /**
     * This method initializes removeCurrentItemMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getRemoveCurrentItemMenuItem() {
        if (removeCurrentItemMenuItem == null) {
            removeCurrentItemMenuItem = new JMenuItem();
            removeCurrentItemMenuItem.setText("Current Item");
            removeCurrentItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return removeCurrentItemMenuItem;
    }


    /**
     * This method initializes clearQueryMenuItem	
     * 	
     * @return javax.swing.JMenuItem	
     */
    private JMenuItem getClearQueryMenuItem() {
        if (clearQueryMenuItem == null) {
            clearQueryMenuItem = new JMenuItem();
            clearQueryMenuItem.setText("Clear Query");
            clearQueryMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int choice = JOptionPane.showConfirmDialog(VisualQueryBuilder.this, 
                        "Clear the entire CQL query", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        getCqlQueryTree().setQuery(new CQLQuery());
                    }
                }
            });
        }
        return clearQueryMenuItem;
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Error setting system look and feel");
        }
        JFrame app = new VisualQueryBuilder();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }
}
