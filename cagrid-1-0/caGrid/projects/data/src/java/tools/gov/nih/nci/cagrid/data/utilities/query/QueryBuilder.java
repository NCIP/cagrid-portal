package gov.nih.nci.cagrid.data.utilities.query;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.client.DataServiceClient;
import gov.nih.nci.cagrid.data.cql.validation.CqlDomainValidator;
import gov.nih.nci.cagrid.data.cql.validation.CqlStructureValidator;
import gov.nih.nci.cagrid.data.cql.validation.DomainModelValidator;
import gov.nih.nci.cagrid.data.cql.validation.ObjectWalkingCQLValidator;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.AssociationTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.AttributeTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.GroupTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.IconTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.QueryTree;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.QueryTreeNode;
import gov.nih.nci.cagrid.data.utilities.query.cqltree.TargetTreeNode;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/** 
 *  QueryBuilder
 *  Graphical tool to build queries against a domain model
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 4, 2006 
 * @version $Id$ 
 */
public class QueryBuilder extends JFrame {
	
	public static final String LOGIC_PANEL = "logic";
	public static final String PREDICATES_PANEL = "predicates";
	
	private QueryTree queryTree = null;
	private JScrollPane queryTreeScrollPane = null;
	private JPanel contextButtonPanel = null;
	private JButton setTargetButton = null;
	private JButton addAssociationButton = null;
	private JButton addAttributeButton = null;
	private JButton addGroupButton = null;
	private JMenuBar mainMenuBar = null;
	private JMenu fileMenu = null;
	private JMenuItem newQueryMenuItem = null;
	private JMenuItem loadQueryMenuItem = null;
	private JMenuItem saveQueryMenuItem = null;
	private JMenuItem exitMenuItem = null;
	private JButton removeItemButton = null;
	private JMenu domainModelMenu = null;
	private JMenuItem loadModelMenuItem = null;
	private JMenuItem retrieveDomainModelMenuItem = null;
	private JMenuItem saveDomainModelMenuItem = null;
	private JButton changePredicateButton = null;
	private JButton changeLogicButton = null;
	private JButton changeValueButton = null;
	private JPanel queryPanel = null;
	private JSplitPane mainSplitPane = null;
	private TypeDisplayPanel typeDisplayPanel = null;
	
	private String lastDirectory = null;
	private transient CqlDomainValidator domainValidator = null;	
	private transient DomainModel domainModel = null;	
	
	public QueryBuilder() {
		super();
		lastDirectory = "c:/caGrid/cagrid-1-0/caGrid/projects/data";
		setTitle("CQL Query Builder");
		domainValidator = new DomainModelValidator();
		initialize();
	}
	
	
	private void initialize() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				handleExit();
			}
		});
        this.setSize(new java.awt.Dimension(834,546));
        this.setContentPane(getMainSplitPane());
        this.setJMenuBar(getMainMenuBar());
        this.pack();
        this.setVisible(true);
	}
	
	
	private QueryTree getQueryTree() {
		if (queryTree == null) {
			queryTree = new QueryTree();
			queryTree.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					if (e.isAddedPath()) {
						// selection made
						TreePath selectionPath = e.getPath();
						if (selectionPath.getLastPathComponent() instanceof IconTreeNode) {
							IconTreeNode node = (IconTreeNode) selectionPath.getLastPathComponent();
							// set up editor based on type of node
							if (node instanceof QueryTreeNode) {
								// can only set the target at this point
								enableQueryBuildingButtons(new JButton[] {getSetTargetButton()});
							} else if (node instanceof TargetTreeNode ||
								node instanceof AssociationTreeNode) {
								// find the query object
								Object queryObject = null;
								if (node instanceof TargetTreeNode) {
									queryObject = ((TargetTreeNode) node).getTarget();
								} else {
									queryObject = ((AssociationTreeNode) node).getAssociation();
								}
								
								BaseType type = new BaseType(queryObject.getName());
								System.out.println("Changing type diaplay to: " + type.getTypeName());
								// change the selection of type
								getTypeDisplayPanel().setSelectedType(type);
								
								// count children of the target node
								if (node.getChildCount() == 0) {
									enableQueryBuildingButtons(new JButton[] {
										getAddAssociationButton(), getAddAttributeButton(), getAddGroupButton(),
										getRemoveItemButton()
									});
								} else {
									// node already has children, turn off the add buttons
									enableQueryBuildingButtons(new JButton[] {getRemoveItemButton()});
								}
							} else if (node instanceof AttributeTreeNode) {
								enableQueryBuildingButtons(new JButton[] {
									getChangePredicateButton(), getChangeValueButton(), getRemoveItemButton()
								});
							} else if (node instanceof GroupTreeNode) {
								enableQueryBuildingButtons(new JButton[] {
									getAddAssociationButton(), getAddAttributeButton(), getAddGroupButton(),
									getRemoveItemButton(), getChangeLogicButton()
								});
							} else {
								throw new IllegalArgumentException("What the heck is " + node.getClass().getName() + " doing in the tree??");
							}
						}						
					} else {
						enableQueryBuildingButtons(new JButton[] {});
					}
				}
			});
		}
		return queryTree;
	}
	
	
	private JScrollPane getQueryTreeScrollPane() {
		if (queryTreeScrollPane == null) {
			queryTreeScrollPane = new JScrollPane();
			queryTreeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			queryTreeScrollPane.setBorder(BorderFactory.createTitledBorder(
				null, "Query", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			queryTreeScrollPane.setViewportView(getQueryTree());
		}
		return queryTreeScrollPane;
	}
	

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContextButtonPanel() {
		if (contextButtonPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);
			gridLayout.setVgap(2);
			gridLayout.setColumns(4);
			gridLayout.setHgap(2);
			contextButtonPanel = new JPanel();
			contextButtonPanel.setLayout(gridLayout);
			contextButtonPanel.add(getSetTargetButton());
			contextButtonPanel.add(getAddAssociationButton());
			contextButtonPanel.add(getAddAttributeButton());
			contextButtonPanel.add(getAddGroupButton());
			contextButtonPanel.add(getChangeValueButton());
			contextButtonPanel.add(getChangePredicateButton());
			contextButtonPanel.add(getChangeLogicButton());
			contextButtonPanel.add(getRemoveItemButton());
			// disable all buttons until a domain model has been loaded...
			enableQueryBuildingButtons(new JButton[] {});
		}
		return contextButtonPanel;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSetTargetButton() {
		if (setTargetButton == null) {
			setTargetButton = new JButton();
			setTargetButton.setText("Set Target");
			setTargetButton.setName("setTargetButton");
			setTargetButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// see if there is an existing target node
					QueryTreeNode queryNode = getQueryTree().getQueryTreeNode();
					if (queryNode.getQuery().getTarget() != null) {
						String[] message = {
							"The query already has a target.  Setting a new one",
							"will remove all information from the query."
						};
						int choice = JOptionPane.showConfirmDialog(
							QueryBuilder.this, message, "Confirm", JOptionPane.YES_NO_OPTION);
						if (choice != JOptionPane.YES_OPTION) {
							return;
						}
					}
					// get the selected target out of the types panel
					BaseType selectedType = getTypeDisplayPanel().getSelectedType();
					if (selectedType != null) {
						Object targetObject = new Object();
						targetObject.setName(selectedType.getTypeName());
						queryNode.getQuery().setTarget(targetObject);
						queryNode.rebuild();
						getQueryTree().refreshTree();
					} else {
						JOptionPane.showMessageDialog(QueryBuilder.this, "Please select a type to target");
					}
				}
			});
		}
		return setTargetButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddAssociationButton() {
		if (addAssociationButton == null) {
			addAssociationButton = new JButton();
			addAssociationButton.setText("Add Association");
			addAssociationButton.setName("addAssociationButton");
			addAssociationButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// verify the base type selected matches the one in the query tree
					IconTreeNode node = (IconTreeNode) getQueryTree().getSelectionPath().getLastPathComponent();
					Object parentQueryObject = getParentQueryObject(node);
					BaseType selectedType = getTypeDisplayPanel().getSelectedType();
					if (selectedType == null || !selectedType.getTypeName().equals(parentQueryObject.getName())) {
						JOptionPane.showMessageDialog(
							QueryBuilder.this, "Please select associations from the type " + parentQueryObject.getName());
						return;
					}
					
					// see what association is selected
					AssociatedType assocType = getTypeDisplayPanel().getSelectedAssociation();
					if (assocType != null) {
						Association association = new Association();
						association.setRoleName(assocType.getRoleName());
						association.setName(assocType.getTypeName());
						if (node instanceof AssociationTreeNode || node instanceof TargetTreeNode) {
							parentQueryObject.setAssociation(association);
						} else { // group
							Group group = ((GroupTreeNode) node).getGroup();
							Association[] currentAssociations = group.getAssociation();
							if (currentAssociations != null) {
								group.setAssociation((Association[]) Utils.appendToArray(currentAssociations, association));
							} else {
								group.setAssociation(new Association[] {association});
							}
						}
						node.rebuild();
						getQueryTree().refreshTree();
					} else {
						JOptionPane.showMessageDialog(QueryBuilder.this, "Please select an association");
					}
				}
			});
		}
		return addAssociationButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddAttributeButton() {
		if (addAttributeButton == null) {
			addAttributeButton = new JButton();
			addAttributeButton.setText("Add Attribute");
			addAttributeButton.setName("addAttributeButton");
			addAttributeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// verify the base type selected matches the one in the query tree
					IconTreeNode node = (IconTreeNode) getQueryTree().getSelectionPath().getLastPathComponent();
					Object parentQueryObject = getParentQueryObject(node);
					BaseType selectedType = getTypeDisplayPanel().getSelectedType();
					if (selectedType == null || !selectedType.getTypeName().equals(parentQueryObject.getName())) {
						JOptionPane.showMessageDialog(
							QueryBuilder.this, "Please select attributes from the type " + parentQueryObject.getName());
						return;
					}
					
					// see if an attribute is selected
					AttributeType attribType = getTypeDisplayPanel().getSelectedAttribute();
					if (attribType != null) {
						Attribute attrib = AttributeModifyDialog.getAttribute(QueryBuilder.this, attribType);
						if (attrib != null) {
							// get the selected target / assocition / group node
							if (node instanceof TargetTreeNode || node instanceof AssociationTreeNode) {
								parentQueryObject.setAttribute(attrib);
							} else if (node instanceof GroupTreeNode) {
								Group group = ((GroupTreeNode) node).getGroup();
								if (group.getAttribute() != null) {
									Attribute[] addedAttribs = (Attribute[]) Utils.appendToArray(group.getAttribute(), attrib);
									group.setAttribute(addedAttribs);
								} else {
									group.setAttribute(new Attribute[] {attrib});
								}
							}
							// rebuild the tree for the new node
							node.rebuild();
							getQueryTree().refreshTree();
						}
					} else {
						JOptionPane.showMessageDialog(QueryBuilder.this, "Please select an attribute first!");
					}
				}
			});
		}
		return addAttributeButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddGroupButton() {
		if (addGroupButton == null) {
			addGroupButton = new JButton();
			addGroupButton.setText("Add Group");
			addGroupButton.setName("addGroupButton");
			addGroupButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// present user with a choice of logic for the group
					LogicalOperator operator = (LogicalOperator) JOptionPane.showInputDialog(
						QueryBuilder.this, "Select a logical operator", "Group Logic", JOptionPane.QUESTION_MESSAGE, null, 
						new java.lang.Object[] {LogicalOperator.AND, LogicalOperator.OR}, LogicalOperator.AND);
					if (operator != null) {
						Group group = new Group();
						group.setLogicRelation(operator);
						// add the group to the selected node
						IconTreeNode node = (IconTreeNode) getQueryTree().getSelectionPath().getLastPathComponent();
						if (node instanceof TargetTreeNode) {
							Object target = ((TargetTreeNode) node).getTarget();
							target.setGroup(group);
						} else if (node instanceof AssociationTreeNode) {
							Association assoc = ((AssociationTreeNode) node).getAssociation();
							assoc.setGroup(group);
						} else if (node instanceof GroupTreeNode) {
							Group g = ((GroupTreeNode) node).getGroup();
							if (g.getGroup() != null) {
								Group[] allGroups = (Group[]) Utils.appendToArray(g.getGroup(), group);
								g.setGroup(allGroups);
							} else {
								g.setGroup(new Group[] {group});
							}
						}
						node.rebuild();
						getQueryTree().refreshTree();
					}
				}
			});
		}
		return addGroupButton;
	}


	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMainMenuBar() {
		if (mainMenuBar == null) {
			mainMenuBar = new JMenuBar();
			mainMenuBar.add(getFileMenu());
			mainMenuBar.add(getDomainModelMenu());
		}
		return mainMenuBar;
	}


	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getNewQueryMenuItem());
			fileMenu.add(getLoadQueryMenuItem());
			fileMenu.add(getSaveQueryMenuItem());
			fileMenu.addSeparator();
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}
	
	
	private JMenuItem getNewQueryMenuItem() {
		if (newQueryMenuItem == null) {
			newQueryMenuItem = new JMenuItem();
			newQueryMenuItem.setText("New Query");
			newQueryMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newQuery();
				}
			});
		}
		return newQueryMenuItem;
	}


	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getLoadQueryMenuItem() {
		if (loadQueryMenuItem == null) {
			loadQueryMenuItem = new JMenuItem();
			loadQueryMenuItem.setText("Load Query");
			loadQueryMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					loadQuery();
				}
			});
		}
		return loadQueryMenuItem;
	}


	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getSaveQueryMenuItem() {
		if (saveQueryMenuItem == null) {
			saveQueryMenuItem = new JMenuItem();
			saveQueryMenuItem.setText("Save Query");
			saveQueryMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					saveQuery();
				}
			});
		}
		return saveQueryMenuItem;
	}


	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					handleExit();
				}
			});
		}
		return exitMenuItem;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveItemButton() {
		if (removeItemButton == null) {
			removeItemButton = new JButton();
			removeItemButton.setText("Remove Query Item");
			removeItemButton.setName("removeItemButton");
			removeItemButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// get the selected node (which will be removed) and its parent
					IconTreeNode selected = (IconTreeNode) getQueryTree().getSelectionPath().getLastPathComponent();
					IconTreeNode parent = (IconTreeNode) selected.getParent();
					if (parent instanceof QueryTreeNode) {
						if (selected instanceof TargetTreeNode) {
							((QueryTreeNode) parent).getQuery().setTarget(null);
						} else {
							// TODO: hande query modifier removal
						}
					} else if (parent instanceof TargetTreeNode) {
						Object target = ((TargetTreeNode) parent).getTarget();
						if (selected instanceof AttributeTreeNode) {
							target.setAttribute(null);
						} else if (selected instanceof AssociationTreeNode) {
							target.setAssociation(null);
						} else { // group
							target.setGroup(null);
						}
					} else if (parent instanceof AssociationTreeNode) {
						Association assoc = ((AssociationTreeNode) parent).getAssociation();
						if (selected instanceof AttributeTreeNode) {
							assoc.setAttribute(null);
						} else if (selected instanceof AssociationTreeNode) {
							assoc.setAssociation(null);
						} else { // group
							assoc.setGroup(null);
						}
					} else if (parent instanceof GroupTreeNode) {
						Group group = ((GroupTreeNode) parent).getGroup();
						if (selected instanceof AttributeTreeNode) {
							Attribute attrib = ((AttributeTreeNode) selected).getAttribute();
							Attribute[] cleaned = (Attribute[]) Utils.removeFromArray(group.getAttribute(), attrib);
							group.setAttribute(cleaned);
						} else if (selected instanceof AssociationTreeNode) {
							Association assoc = ((AssociationTreeNode) selected).getAssociation();
							Association[] cleaned = (Association[]) Utils.removeFromArray(group.getAssociation(), assoc);
							group.setAssociation(cleaned);
						} else { // group
							Group g = ((GroupTreeNode) selected).getGroup();
							Group[] cleaned = (Group[]) Utils.removeFromArray(g.getGroup(), g);
							group.setGroup(cleaned);
						}
					}
					parent.rebuild();
					getQueryTree().refreshTree();
				}
			});
		}
		return removeItemButton;
	}


	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getDomainModelMenu() {
		if (domainModelMenu == null) {
			domainModelMenu = new JMenu();
			domainModelMenu.setText("Domain Model");
			domainModelMenu.add(getLoadModelMenuItem());
			domainModelMenu.add(getRetrieveDomainModelMenuItem());
			domainModelMenu.addSeparator();
			domainModelMenu.add(getSaveDomainModelMenuItem());
		}
		return domainModelMenu;
	}


	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getLoadModelMenuItem() {
		if (loadModelMenuItem == null) {
			loadModelMenuItem = new JMenuItem();
			loadModelMenuItem.setText("Load From File");
			loadModelMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					loadDomainModel();
				}
			});
		}
		return loadModelMenuItem;
	}


	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getRetrieveDomainModelMenuItem() {
		if (retrieveDomainModelMenuItem == null) {
			retrieveDomainModelMenuItem = new JMenuItem();
			retrieveDomainModelMenuItem.setText("From Data Service");
			retrieveDomainModelMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getDomainModelFromService();
				}
			});
		}
		return retrieveDomainModelMenuItem;
	}


	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getSaveDomainModelMenuItem() {
		if (saveDomainModelMenuItem == null) {
			saveDomainModelMenuItem = new JMenuItem();
			saveDomainModelMenuItem.setText("Save To Disk");
			saveDomainModelMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					saveDomainModel();
				}
			});
		}
		return saveDomainModelMenuItem;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getChangePredicateButton() {
		if (changePredicateButton == null) {
			changePredicateButton = new JButton();
			changePredicateButton.setText("Change Predicate");
			changePredicateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// walk the static predicate fields
					Field[] fields = Predicate.class.getFields();
					List predicates = new ArrayList();
					for (int i = 0; i < fields.length; i++) {
						int mods = fields[i].getModifiers();
						if (Modifier.isStatic(mods) && Modifier.isPublic(mods)
							&& fields[i].getType().equals(Predicate.class)) {
							try {
								Predicate p = (Predicate) fields[i].get(null);
								predicates.add(p);
							} catch (IllegalAccessException ex) {
								ex.printStackTrace();
							}
						}
					}
					// sort the predicates by value
					Collections.sort(predicates, new Comparator() {
						public int compare(java.lang.Object o1, java.lang.Object o2) {
							return o1.toString().compareTo(o2.toString());
						}
					});
					Predicate[] predArray = new Predicate[predicates.size()];
					predicates.toArray(predArray);
					// get the selected node
					AttributeTreeNode node = (AttributeTreeNode) getQueryTree().getSelectionPath().getLastPathComponent();
					Predicate choice = (Predicate) JOptionPane.showInputDialog(QueryBuilder.this, 
						"Select a predicate", "Prediacate", JOptionPane.QUESTION_MESSAGE, 
						null, predArray, node.getAttribute().getPredicate());
					if (choice != null) {
						node.getAttribute().setPredicate(choice);
						node.rebuild();
						getQueryTree().refreshTree();
					}
				}
			});
		}
		return changePredicateButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getChangeLogicButton() {
		if (changeLogicButton == null) {
			changeLogicButton = new JButton();
			changeLogicButton.setText("Change Logic");
			changeLogicButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// get selected group to see current logic
					GroupTreeNode node = (GroupTreeNode) getQueryTree().getSelectionPath().getLastPathComponent();
					LogicalOperator[] ops = new LogicalOperator[] {LogicalOperator.OR, LogicalOperator.AND};
					LogicalOperator choice = (LogicalOperator) JOptionPane.showInputDialog(QueryBuilder.this,
						"Choose Logical Operator", "Logic", JOptionPane.QUESTION_MESSAGE, 
						null, ops, node.getGroup().getLogicRelation());
					if (choice != null) {
						node.getGroup().setLogicRelation(choice);
						node.rebuild();
						getQueryTree().refreshTree();
					}
				}
			});
		}
		return changeLogicButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getChangeValueButton() {
		if (changeValueButton == null) {
			changeValueButton = new JButton();
			changeValueButton.setText("Change Value");
			changeValueButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// get the current node
					AttributeTreeNode node = (AttributeTreeNode) getQueryTree().getSelectionPath().getLastPathComponent();
					String choice = JOptionPane.showInputDialog(
						QueryBuilder.this, "Enter new value", node.getAttribute().getValue());
					if (choice != null) {
						node.getAttribute().setValue(choice);
						node.rebuild();
						getQueryTree().refreshTree();
					}
				}
			});
		}
		return changeValueButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getQueryPanel() {
		if (queryPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridx = 0;
			queryPanel = new JPanel();
			queryPanel.setLayout(new GridBagLayout());
			queryPanel.add(getQueryTreeScrollPane(), gridBagConstraints);
			queryPanel.add(getContextButtonPanel(), gridBagConstraints2);
		}
		return queryPanel;
	}
	
	
	private TypeDisplayPanel getTypeDisplayPanel() {
		if (typeDisplayPanel == null) {
			typeDisplayPanel = new TypeDisplayPanel();
		}
		return typeDisplayPanel;
	}


	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane();
			mainSplitPane.setOneTouchExpandable(true);
			mainSplitPane.setLeftComponent(getQueryPanel());
			mainSplitPane.setRightComponent(getTypeDisplayPanel());
		}
		return mainSplitPane;
	}
	
	
	private void enableQueryBuildingButtons(JButton[] buttons) {
		Set enabledButtons = new HashSet();
		Collections.addAll(enabledButtons, buttons);
		for (int i = 0; i < getContextButtonPanel().getComponentCount(); i++) {
			JButton button = (JButton) getContextButtonPanel().getComponent(i);
			button.setEnabled(enabledButtons.contains(button));
		}
	}
	
	
	private void handleExit() {
		int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm", 
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			dispose();
			System.exit(0);
		}
	}
	
	
	private void newQuery() {
		// see if there's already a query in the works
		QueryTreeNode node = getQueryTree().getQueryTreeNode();
		if (node != null) {
			int choice = JOptionPane.showConfirmDialog(this, "Clear current query?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (choice != JOptionPane.YES_OPTION) {
				return;
			}
		}
		// ok, clear 'er out
		getQueryTree().setQuery(new CQLQuery());
	}
	
	
	private void loadQuery() {
		if (domainModel != null) {
			JFileChooser chooser = new JFileChooser(lastDirectory);
			chooser.setFileFilter(FileFilters.XML_FILTER);
			int choice = chooser.showOpenDialog(this);
			if (choice == JFileChooser.APPROVE_OPTION) {
				File cqlFile = chooser.getSelectedFile();
				lastDirectory = cqlFile.getAbsolutePath();
				
				CQLQuery query = null;
				try {
					query = (CQLQuery) Utils.deserializeDocument(cqlFile.getAbsolutePath(), CQLQuery.class);
				} catch (Exception ex) {
					String[] error = {
						"Error loading CQL Query:",
						ex.getMessage()
					};
					JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					// validate the query syntax
					CqlStructureValidator structureValidator = new ObjectWalkingCQLValidator();
					structureValidator.validateCqlStructure(query);
				} catch (MalformedQueryException ex) {
					String[] error = {
						"The specified query is not structuraly valid CQL:",
						ex.getMessage()
					};
					JOptionPane.showMessageDialog(this, error, "CQL Structure Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// validate domain model
				try {
					domainValidator.validateDomainModel(query, domainModel);
				} catch (MalformedQueryException ex) {
					String[] error = {
						"Error validating query against currently loaded domain model:",
						ex.getMessage()
					};
					JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				// set the query into the query tree
				getQueryTree().setQuery(query);
				getQueryTree().refreshTree();
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please load a domain model first", "No model loaded", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	private void saveQuery() {
		JFileChooser chooser = new JFileChooser(lastDirectory);
		chooser.setFileFilter(FileFilters.XML_FILTER);
		int choice = chooser.showSaveDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File cqlFile = chooser.getSelectedFile();
			lastDirectory = cqlFile.getAbsolutePath();
			// get the CQL query from the tree
			QueryTreeNode queryNode = getQueryTree().getQueryTreeNode();
			CQLQuery query = queryNode.getQuery();
			try {
				FileWriter writer = new FileWriter(cqlFile);
				Utils.serializeObject(query, DataServiceConstants.CQL_QUERY_QNAME, writer);
				writer.close();
			} catch (Exception ex) {
				String[] error = {
					"Error saving the CQL Query to disk:",
					ex.getMessage()
				};
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, error, "Error Saving CQL", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	private void loadDomainModel() {
		JFileChooser chooser = new JFileChooser(lastDirectory);
		chooser.setFileFilter(FileFilters.XML_FILTER);
		int choice = chooser.showOpenDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File dmFile = chooser.getSelectedFile();
			lastDirectory = dmFile.getAbsolutePath();
			DomainModel tempModel = null;
			try {
				FileReader fileReader = new FileReader(dmFile);
				tempModel = MetadataUtils.deserializeDomainModel(fileReader);
			} catch (Exception ex) {
				String[] error = {
					"Error loading the domain model:",
					ex.getMessage()
				};
				JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}			
			attemptInstallDomainModel(tempModel);
		}
	}
	
	
	private void getDomainModelFromService() {
		String url = JOptionPane.showInputDialog(this, "Enter Service URL");
		if (url != null) {
			// contact the data service for the domain model
			DomainModel tempModel = null;
			try {
				DataServiceClient client = new DataServiceClient(url);
				tempModel = MetadataUtils.getDomainModel(client.getEndpointReference());
			} catch (Exception ex) {
				String[] error = {
					"Error retrieving domain model from service:",
					ex.getMessage()
				};
				JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			attemptInstallDomainModel(tempModel);
		}
	}
	
	
	private void attemptInstallDomainModel(DomainModel tempModel) {
		// we can now add the query node to the query tree
		// see if there is currently a query in the query tree
		QueryTreeNode node = getQueryTree().getQueryTreeNode();
		if (node != null) {
			// validate the existing query against the new dommain model
			CQLQuery query = node.getQuery();
			try {
				domainValidator.validateDomainModel(query, tempModel);
			} catch (MalformedQueryException ex) {
				String[] error = {
					"The current query is not compatible with the selected domain model:",
					ex.getMessage(), "\n",
					"Please select another, or save this query and start a new one."
				};
				JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		// domain model checked out, add it as the currently loaded one
		domainModel = tempModel;
		JOptionPane.showMessageDialog(this, "Domain Model loaded");
		getTypeDisplayPanel().setTypeTraverser(new DomainModelTypeTraverser(domainModel));
		getQueryTree().setQuery(new CQLQuery());
	}
	
	
	private void saveDomainModel() {
		if (domainModel != null) {
			JFileChooser chooser = new JFileChooser(lastDirectory);
			chooser.setFileFilter(FileFilters.XML_FILTER);
			int choice = chooser.showSaveDialog(this);
			if (choice == JFileChooser.APPROVE_OPTION) {
				File outFile = chooser.getSelectedFile();
				lastDirectory = outFile.getAbsolutePath();
				try {
					FileWriter writer = new FileWriter(outFile);
					MetadataUtils.serializeDomainModel(domainModel, writer);
					writer.flush();
					writer.close();
				} catch (Exception ex) {
					String[] error = {
						"Error saving domain model to disk:",
						ex.getMessage()
					};
					JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please load a domain model first.", "No domain model", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	private Object getParentQueryObject(IconTreeNode node) {
		Object parentQueryObject = null;
		if (node instanceof AssociationTreeNode) {
			parentQueryObject = ((AssociationTreeNode) node).getAssociation();
		} else if (node instanceof TargetTreeNode) {
			parentQueryObject = ((TargetTreeNode) node).getTarget();
		} else if (node instanceof GroupTreeNode) {
			IconTreeNode testNode = node;
			while (!(testNode instanceof AssociationTreeNode)
				&& !(testNode instanceof TargetTreeNode)) {
				testNode = (IconTreeNode) testNode.getParent();
			}
			if (testNode instanceof AssociationTreeNode) {
				parentQueryObject = ((AssociationTreeNode) testNode).getAssociation();
			} else {
				parentQueryObject = ((TargetTreeNode) testNode).getTarget();
			}
		}
		return parentQueryObject;
	}


	public static void main(String[] args) {
		JFrame builder = new QueryBuilder();
		builder.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
}
