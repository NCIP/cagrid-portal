package gov.nih.nci.cagrid.introduce.portal.discoverytools.gme;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.cagrid.grape.utils.CompositeErrorDialog;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.protocol.gme.SchemaNode;


public class GMESchemaLocatorPanel extends JPanel {
    
    private static final Logger logger = Logger.getLogger(GMESchemaLocatorPanel.class);

	public static String GME_URL = "Global Model Exchange URL";
	public static String TYPE = "gme_discovery";

	private JPanel mainPanel = null;
	private JButton queryButton = null;
	protected File schemaDir;
	private JComboBox namespaceComboBox = null;
	private JComboBox schemaComboBox = null;
	private JPanel schemaPanel = null;
	private JLabel namespaceLabel = null;
	private JLabel nameLabel = null;
    
	public SchemaNode currentNode = null;
    private Object modificationMutex = null;
	private boolean showGMESelection;


	/**
	 * This method initializes
	 */
	public GMESchemaLocatorPanel(boolean showGMESelection) {
		super();
		this.showGMESelection = showGMESelection;
        modificationMutex = new Object();
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0,0,0,0);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getSchemaPanel(), gridBagConstraints);
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			if (this.showGMESelection) {
			}
		}
		return mainPanel;
	}


	public void discoverFromGME() {
        synchronized (modificationMutex) {
            try {
                GridServiceResolver.getInstance().setDefaultFactory(
                    new GlobusGMEXMLDataModelServiceFactory());
                XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
                    .getGridService(ResourceManager.getServiceURLProperty(GME_URL));
                List domains = handle.getNamespaceDomainList();

                Collections.sort(domains, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        String s1 = o1.toString();
                        String s2 = o2.toString();
                        return s1.toLowerCase().compareTo(s2.toLowerCase());
                    }
                });
                
                makeCombosEnabled(false);

                getNamespaceComboBox().removeAllItems();
                for (int i = 0; i < domains.size(); i++) {
                    getNamespaceComboBox().addItem(domains.get(i));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                CompositeErrorDialog.showErrorDialog("Error contacting the GME", ex.getMessage(), ex);
            }
            makeCombosEnabled(true);
        };
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getQueryButton() {
		if (queryButton == null) {
			queryButton = new JButton("Refresh");
			queryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					discoverFromGME();
				}
			});
		}
		return queryButton;
	}


	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getNamespaceComboBox() {
	    if (namespaceComboBox == null) {
	        namespaceComboBox = new JComboBox();
	        namespaceComboBox.addItemListener(new java.awt.event.ItemListener() {
	            public void itemStateChanged(java.awt.event.ItemEvent e) {
	                if (e.getStateChange() == ItemEvent.SELECTED) {
	                    if ((String) namespaceComboBox.getSelectedItem() != null
	                        && ((String) namespaceComboBox.getSelectedItem()).length() > 0) {
	                        Comparator namespaceSorter = new Comparator() {
	                            public int compare(Object o1, Object o2) {
	                                String ns1 = ((Namespace) o1).getNamespace();
	                                String ns2 = ((Namespace) o2).getNamespace();
	                                return ns1.toLowerCase().compareTo(ns2.toLowerCase());
	                            }
	                        };
	                        synchronized (modificationMutex) {
	                            try {
	                                GridServiceResolver.getInstance().setDefaultFactory(
	                                    new GlobusGMEXMLDataModelServiceFactory());
	                                XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
	                                    .getInstance().getGridService(
                                            ResourceManager.getServiceURLProperty(GME_URL));
	                                List namespaces = handle.getSchemaListForNamespaceDomain(
	                                    (String) namespaceComboBox.getSelectedItem());
	                                Collections.sort(namespaces, namespaceSorter);
	                                makeCombosEnabled(false);
	                                getSchemaComboBox().removeAllItems();
	                                for (int i = 0; i < namespaces.size(); i++) {
	                                    getSchemaComboBox().addItem(
	                                        new SchemaWrapper((Namespace) namespaces.get(i)));
	                                }
	                            } catch (Exception ex) {
	                                ex.printStackTrace();
	                                CompositeErrorDialog.showErrorDialog("Error contacting GME", ex.getMessage(), ex);
	                            }
	                            makeCombosEnabled(true);
	                        }
	                    }
	                }
	            }
	        });
	    }
	    return namespaceComboBox;
	}


	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getSchemaComboBox() {
		if (schemaComboBox == null) {
			schemaComboBox = new JComboBox();
			schemaComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
                        synchronized (modificationMutex) {
                            GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
                            
                            try {
                                XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
                                    .getGridService(
                                        gov.nih.nci.cagrid.introduce.common.ResourceManager.getServiceURLProperty(GME_URL));
                                if (schemaComboBox.getSelectedItem() != null) {
                                    Namespace namespace = ((SchemaWrapper) schemaComboBox.getSelectedItem()).getNamespace();
                                    currentNode = handle.getSchema(namespace, false);
                                }
                            } catch (MobiusException ex) {
                                ex.printStackTrace();
                                CompositeErrorDialog.showErrorDialog("Error contacting GME", ex.getMessage(), ex);
                            }
                        }
					}
				}
			});
		}
		return schemaComboBox;
	}


	/**
	 * This method initializes schemaPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getSchemaPanel() {
		if (schemaPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.CENTER;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			nameLabel = new JLabel();
			nameLabel.setText("Name");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.gridx = 0;
			namespaceLabel = new JLabel();
			namespaceLabel.setText("Namespace");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridx = 1;
			schemaPanel = new JPanel();
			schemaPanel.setLayout(new GridBagLayout());
			schemaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Schema",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			schemaPanel.add(getNamespaceComboBox(), gridBagConstraints7);
			schemaPanel.add(namespaceLabel, gridBagConstraints9);
			schemaPanel.add(getSchemaComboBox(), gridBagConstraints8);
			schemaPanel.add(nameLabel, gridBagConstraints10);
			schemaPanel.add(getQueryButton(), gridBagConstraints2);
		}
		return schemaPanel;
	}


	private synchronized void makeCombosEnabled(boolean enabled) {
		getNamespaceComboBox().setEnabled(enabled);
		getSchemaComboBox().setEnabled(enabled);
	}


	public static void main(String[] args) {
		final GMESchemaLocatorPanel panel = new GMESchemaLocatorPanel(true);
		JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JButton getNamespaceButton = new JButton("get namespace");
        getNamespaceButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
                Namespace ns = panel.getSelectedSchemaNamespace();
                System.out.println(ns.getNamespace());
           }
        });
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(getNamespaceButton, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}


	public Namespace getSelectedSchemaNamespace() {
        synchronized (modificationMutex) {
            SchemaWrapper schema = (SchemaWrapper) getSchemaComboBox().getSelectedItem();
            if (schema != null) {
                return schema.getNamespace();
            }
            return null;   
        }
	}

    
    class SchemaWrapper {
        Namespace ns;


        public Namespace getNamespace() {
            return ns;
        }


        public SchemaWrapper(Namespace ns) {
            this.ns = ns;
        }


        public String toString() {
            return ns.getName();
        }
    }
}
