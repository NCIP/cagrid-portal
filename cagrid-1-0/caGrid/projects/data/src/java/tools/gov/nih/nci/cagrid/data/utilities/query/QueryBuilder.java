package gov.nih.nci.cagrid.data.utilities.query;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
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

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
	
	private PredicatesPanel predicatesPanel = null;
	private LogicalOperatorPanel logicPanel = null;
	private JPanel restrictionTypePanel = null;
	private QueryTree queryTree = null;
	private JScrollPane queryTreeScrollPane = null;
	private JPanel contextButtonPanel = null;
	private JButton setTargetButton = null;
	private JButton addAssociationButton = null;  //  @jve:decl-index=0:visual-constraint="112,161"
	private JButton addAttributeButton = null;  //  @jve:decl-index=0:visual-constraint="148,184"
	private JButton addGroupButton = null;  //  @jve:decl-index=0:visual-constraint="169,209"
	private JMenuBar mainMenuBar = null;  //  @jve:decl-index=0:visual-constraint="143,10"
	private JMenu fileMenu = null;
	private JMenuItem loadQueryMenuItem = null;
	private JMenuItem saveQueryMenuItem = null;
	private JMenuItem exitMenuItem = null;  //  @jve:decl-index=0:visual-constraint="156,166"
	private JButton removeItemButton = null;
	private JMenu domainModelMenu = null;
	private JMenuItem loadModelMenuItem = null;
	private JMenuItem retrieveDomainModelMenuItem = null;
	private JMenuItem saveDomainModelMenuItem = null;  //  @jve:decl-index=0:visual-constraint="180,187"
	private JButton changePredicateButton = null;
	private JButton changeLogicButton = null;
	private JButton changeValueButton = null;
	private JPanel mainPanel = null;
	
	private String lastDirectory = null;
	private CqlDomainValidator domainValidator = null;
	
	private transient DomainModel domainModel = null;
	
	public QueryBuilder() {
		super();
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
        this.setSize(new java.awt.Dimension(696,546));
        this.setContentPane(getMainPanel());
        this.setJMenuBar(getMainMenuBar());
	}
	
	
	private JPanel getRestrictionTypePanel() {
		if (restrictionTypePanel == null) {
			restrictionTypePanel = new JPanel();
			restrictionTypePanel.setLayout(new CardLayout());
			restrictionTypePanel.add(getPredicatesPanel(), PREDICATES_PANEL);
			restrictionTypePanel.add(getLogicPanel(), LOGIC_PANEL);
		}
		return restrictionTypePanel;
	}
	
	
	private void showPredicatesPanel() {
		((CardLayout) getRestrictionTypePanel().getLayout()).show(getRestrictionTypePanel(), PREDICATES_PANEL);
	}
	
	
	private void showLogicPanel() {
		((CardLayout) getRestrictionTypePanel().getLayout()).show(getRestrictionTypePanel(), LOGIC_PANEL);
	}
	
	
	private PredicatesPanel getPredicatesPanel() {
		if (predicatesPanel == null) {
			predicatesPanel = new PredicatesPanel();
		}
		return predicatesPanel;
	}
	
	
	private LogicalOperatorPanel getLogicPanel() {
		if (logicPanel == null) {
			logicPanel = new LogicalOperatorPanel();
		}
		return logicPanel;
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
								
							} else if (node instanceof TargetTreeNode) {
								
							} else if (node instanceof AssociationTreeNode) {
								
							} else if (node instanceof AttributeTreeNode) {
								
							} else if (node instanceof GroupTreeNode) {
								
							} else {
								throw new IllegalArgumentException("What the heck is " + node.getClass().getName() + " doing in the tree??");
							}
						}						
					} else {
						// deselection
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
			// disable all buttons for now...
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
			mainMenuBar.setSize(new java.awt.Dimension(139,58));
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
			fileMenu.add(getLoadQueryMenuItem());
			fileMenu.add(getSaveQueryMenuItem());
			fileMenu.addSeparator();
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
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
			exitMenuItem.setSize(new java.awt.Dimension(106,46));
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
			saveDomainModelMenuItem.setSize(new java.awt.Dimension(116,44));
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
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
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getQueryTreeScrollPane(), gridBagConstraints);
			mainPanel.add(getRestrictionTypePanel(), gridBagConstraints1);
			mainPanel.add(getContextButtonPanel(), gridBagConstraints2);
		}
		return mainPanel;
	}
	
	
	private void handleExit() {
		int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm", 
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			dispose();
			System.exit(0);
		}
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
			
			try {
				FileReader fileReader = new FileReader(dmFile);
				domainModel = MetadataUtils.deserializeDomainModel(fileReader);
			} catch (Exception ex) {
				String[] error = {
					"Error loading the domain model:",
					ex.getMessage()
				};
				JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	private void getDomainModelFromService() {
		String url = JOptionPane.showInputDialog(this, "Enter Service URL");
		if (url != null) {
			// contact the data service for the domain model
			try {
				DataServiceClient client = new DataServiceClient(url);
				domainModel = MetadataUtils.getDomainModel(client.getEndpointReference());
			} catch (Exception ex) {
				String[] error = {
					"Error retrieving domain model from service:",
					ex.getMessage()
				};
				JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
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


	public static void main(String[] args) {
		JFrame builder = new QueryBuilder();
		builder.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
}
