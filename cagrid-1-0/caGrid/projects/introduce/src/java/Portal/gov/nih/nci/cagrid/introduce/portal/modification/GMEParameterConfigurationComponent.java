package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.introduce.portal.AnalyticalLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.protocol.gme.SchemaNode;


/**
 * GMETypeExtractionPanel TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jul 7, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class GMEParameterConfigurationComponent extends GridPortalComponent {
	
	private static String ARRAY_YES = "Yes";
	private static String ARRAY_NO = "No";

	private JPanel mainPanel = null;
	private JPanel queryPanel = null;
	private JPanel typesPanel = null;
	private JPanel buttonPanel = null;

	private JButton queryButton = null;
	private JButton doneButton = null;
	private JComboBox typesComboBox = null;
	Namespace currentNamespace = null;
	String currentType = null;

	Vector typeInfo;
	File schemaDir;

	private JComboBox namespaceComboBox = null;
	private JComboBox schemaComboBox = null;
	private JComponent me;

	private boolean handleParameterName = false;
	private JPanel schemaPanel = null;
	private JLabel namespaceLabel = null;
	private JLabel nameLabel = null;
	private JLabel jLabel = null;
	private JTextField gme = null;
	private JLabel typeLabel = null;
	private JLabel classLabel = null;
	private JTextField className = null;
	private JLabel arrayLabel = null;
	private JComboBox arrayCombo = null;
	private JLabel paramNameLabel = null;
	private JTextField paramName = null;
	public GMEParameterConfigurationComponent(Vector typeInfo, File schemaDir, boolean handleParameterName) {
		this.typeInfo = typeInfo;
		this.schemaDir = schemaDir;
		this.handleParameterName = handleParameterName;
		this.me = this;
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getMainPanel());
		this.setTitle("Configure Parameter with GME");
		this.setFrameIcon(AnalyticalLookAndFeel.getMobiusIcon());
		this.setSize(409, 400);

	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.weighty = 0.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 3;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 0.0D;
			gridBagConstraints2.weighty = 0.0D;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.weightx = 4.0D;
			gridBagConstraints3.weighty = 4.0D;
			mainPanel.add(getQueryPanel(), gridBagConstraints1);
			mainPanel.add(getTypesPanel(), gridBagConstraints3);
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getSchemaPanel(), gridBagConstraints);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getQueryPanel() {
		if (queryPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.weightx = 1.0;
			jLabel = new JLabel();
			jLabel.setText("GME");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridx = 0;
			queryPanel = new JPanel();
			queryPanel.setLayout(new GridBagLayout());
			queryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Discover Schemas",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, AnalyticalLookAndFeel.getPanelLabelColor()));
			queryPanel.add(getQueryButton(), gridBagConstraints4);
			queryPanel.add(jLabel, gridBagConstraints6);
			queryPanel.add(getGme(), gridBagConstraints5);
		}
		return queryPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTypesPanel() {
		if (typesPanel == null) {
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints19.gridy = 3;
			gridBagConstraints19.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints19.gridx = 0;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.gridy = 3;
			gridBagConstraints18.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints18.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints18.weighty = 1.0D;
			gridBagConstraints18.weightx = 1.0;
			paramNameLabel = new JLabel();
			paramNameLabel.setText("Parameter Name");
			arrayLabel = new JLabel();
			arrayLabel.setText("Is Array");
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 2;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints17.weighty = 1.0D;
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints16.gridy = 2;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.gridy = 1;
			gridBagConstraints15.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints15.gridx = 0;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.gridy = 1;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints14.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints14.weighty = 1.0D;
			gridBagConstraints14.weightx = 1.0;
			classLabel = new JLabel();
			classLabel.setText("Class Name");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints13.gridy = 0;
			typeLabel = new JLabel();
			typeLabel.setText("Element Type");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.weighty = 1.0D;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			typesPanel = new JPanel();
			typesPanel.setLayout(new GridBagLayout());
			typesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configure Parameter",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, AnalyticalLookAndFeel.getPanelLabelColor()));
			typesPanel.add(getTypesComboBox(), gridBagConstraints12);
			typesPanel.add(typeLabel, gridBagConstraints13);
			typesPanel.add(classLabel, gridBagConstraints15);
			typesPanel.add(getClassName(), gridBagConstraints14);
			typesPanel.add(arrayLabel, gridBagConstraints16);
			typesPanel.add(getArrayCombo(), gridBagConstraints17);
			if (this.handleParameterName) {
				typesPanel.add(paramNameLabel, gridBagConstraints19);
				typesPanel.add(getParamName(), gridBagConstraints18);
			}
		}
		return typesPanel;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getDoneButton(), null);
		}
		return buttonPanel;
	}


	


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getQueryButton() {
		if (queryButton == null) {
			queryButton = new JButton("Discover Schemas", AnalyticalLookAndFeel.getMobiusIcon());
			queryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
					List namespaces = null;
					try {
						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(gme.getText());
						namespaces = handle.getNamespacesList();

						getNamespaceComboBox().removeAllItems();
						for (int i = 0; i < namespaces.size(); i++) {
							getNamespaceComboBox().addItem(namespaces.get(i));
						}

					} catch (MobiusException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(me,
							"Please check the GME URL and make sure that you have the appropriate credentials!");
					}
				}
			});
		}
		return queryButton;
	}


	private void initializeTypes(SchemaNode node) {
		try {
			Document doc = XMLUtilities.stringToDocument(node.getSchemaContents());
			List complexTypes = doc.getRootElement().getChildren("complexType", doc.getRootElement().getNamespace());
			List simpleTypes = doc.getRootElement().getChildren("simpleType", doc.getRootElement().getNamespace());
			JComboBox typesBox = this.getTypesComboBox();
			typesBox.removeAllItems();
			for (int i = 0; i < complexTypes.size(); i++) {
				Element element = (Element) complexTypes.get(i);
				String name = element.getAttributeValue("name");
				typesBox.addItem(name);
			}
			for (int i = 0; i < simpleTypes.size(); i++) {
				Element element = (Element) simpleTypes.get(i);
				String name = element.getAttributeValue("name");
				typesBox.addItem(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDoneButton() {
		if (doneButton == null) {
			doneButton = new JButton(AnalyticalLookAndFeel.getSelectIcon());
			doneButton.setText("Done");
			doneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
					List writtenNamespaces = null;
					try {
						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(gme.getText());
						if (currentNamespace != null) {
							writtenNamespaces = handle.cacheSchema(currentNamespace.getRaw(), schemaDir);
						}
					} catch (MobiusException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(me,
							"Please check the GME URL and make sure that you have the appropriate credentials!");
					}

					File namespace2Mappings = new File(schemaDir.getAbsolutePath() + File.separator + ".."
						+ File.separator + ".." + File.separator + ".." + File.separator + "namespace2package.mappings");

					List mappings = new ArrayList();
					if (namespace2Mappings.exists()) {
						try {
							BufferedReader reader = new BufferedReader(new FileReader(namespace2Mappings));
							String line;
							try {
								line = reader.readLine();
								while (line != null) {
									mappings.add(line);
									line = reader.readLine();
								}
							} catch (IOException e3) {
								// TODO Auto-generated catch block
								e3.printStackTrace();
							}

						} catch (FileNotFoundException e2) {
							e2.printStackTrace();
						}
					}
					if (writtenNamespaces != null) {
						for (int i = 0; i < writtenNamespaces.size(); i++) {
							Namespace namespace;
							try {
								namespace = new Namespace((String) writtenNamespaces.get(i));
								StringTokenizer tokenizer = new StringTokenizer(namespace.getDomain(), ".", true);
								StringBuffer packageName = new StringBuffer();
								while (tokenizer.hasMoreElements()) {
									packageName.insert(0, tokenizer.nextToken());
								}
								String newLine = namespace.getRaw() + "=" + packageName.toString() + ".bean";
								//turns http: into http\: 
								String newnewLine = newLine.replaceFirst(":","\\\\:");
								newnewLine = newnewLine.replaceFirst("\\Q\\\\\\\\:\\E","\\\\:");
								if (!mappings.contains(newnewLine)) {
									mappings.add(newnewLine);
								}
							} catch (MalformedNamespaceException e1) {
								e1.printStackTrace();
							}
						}
					}

					try {
						FileWriter fw = new FileWriter(namespace2Mappings);
						Iterator it = mappings.iterator();
						while (it.hasNext()) {
							String next = (String) it.next();
							fw.write(next + "\n");
						}
						fw.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}

					//populate the data vector from the prior screen now......
					int index = 0;
					typeInfo.set(index++, className.getText());
					if (handleParameterName) {
						typeInfo.set(index++, paramName.getText());
					}
					if (currentNamespace != null) {
						typeInfo.set(index++, "gme://" + currentNamespace.getRaw());
					} else {
						typeInfo.set(index++, null);
					}
					typeInfo.set(index++, currentType);
					if (currentNamespace != null) {
						typeInfo.set(index++, "./" + currentNamespace.getName() + ".xsd");
					} else {
						typeInfo.set(index++, null);
					}

					dispose();
				}
			});
		}
		return doneButton;
	}


	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getTypesComboBox() {
		if (typesComboBox == null) {
			typesComboBox = new JComboBox();
			typesComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					currentType = (String) typesComboBox.getSelectedItem();
					if (currentType != null) {

						currentNamespace = null;
						try {
							currentNamespace = new Namespace((String) getNamespaceComboBox().getSelectedItem()
								+ (String) getSchemaComboBox().getSelectedItem());
						} catch (MalformedNamespaceException e1) {
							e1.printStackTrace();
						}
						StringTokenizer tokenizer = new StringTokenizer(currentNamespace.getDomain(), ".", true);
						StringBuffer packageName = new StringBuffer();
						while (tokenizer.hasMoreElements()) {
							packageName.insert(0, tokenizer.nextToken());
						}

						char[] typeChars = currentType.toCharArray();
						typeChars[0] = currentType.toUpperCase().charAt(0);
						String type = new String(typeChars);

						className.setText(packageName + ".bean." + type);
						toggleArray();
					}

				}
			});
		}
		return typesComboBox;
	}


	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getNamespaceComboBox() {
		if (namespaceComboBox == null) {
			namespaceComboBox = new JComboBox();
			namespaceComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
					try {

						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(gme.getText());
						List schemas = handle.getSchemaListForNamespace((String) namespaceComboBox.getSelectedItem());

						getSchemaComboBox().removeAllItems();
						for (int i = 0; i < schemas.size(); i++) {
							getSchemaComboBox().addItem(schemas.get(i));
						}
					} catch (MobiusException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(me,
							"Please check the GME URL and make sure that you have the appropriate credentials!");
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
	private JComboBox getSchemaComboBox() {
		if (schemaComboBox == null) {
			schemaComboBox = new JComboBox();
			schemaComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());
					try {
						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(gme.getText());
						if (schemaComboBox.getSelectedItem() != null) {
							SchemaNode node = handle.getSchema((String) namespaceComboBox.getSelectedItem()
								+ (String) schemaComboBox.getSelectedItem(), false);
							initializeTypes(node);
						}
					} catch (MobiusException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(me,
							"Please check the GME URL and make sure that you have the appropriate credentials!");
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
	private JPanel getSchemaPanel() {
		if (schemaPanel == null) {
			nameLabel = new JLabel();
			nameLabel.setText("Name");
			
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints10.gridx = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints9.gridx = 0;
			namespaceLabel = new JLabel();
			namespaceLabel.setText("Namespace");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridx = 1;
			schemaPanel = new JPanel();
			schemaPanel.setLayout(new GridBagLayout());
			schemaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Schema",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, AnalyticalLookAndFeel.getPanelLabelColor()));
			schemaPanel.add(getNamespaceComboBox(), gridBagConstraints7);
			schemaPanel.add(namespaceLabel, gridBagConstraints9);
			schemaPanel.add(getSchemaComboBox(), gridBagConstraints8);
			schemaPanel.add(nameLabel, gridBagConstraints10);
		}
		return schemaPanel;
	}


	/**
	 * This method initializes gme	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getGme() {
		if (gme == null) {
			gme = new JTextField();
			gme.setText("http://localhost:8080/wsrf/services/GlobalModelExchange");
		}
		return gme;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getClassName() {
		if (className == null) {
			className = new JTextField();
			className.setEditable(false);
		}
		return className;
	}


	/**
	 * This method initializes arrayCombo	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getArrayCombo() {
		if (arrayCombo == null) {
			arrayCombo = new JComboBox();
			arrayCombo.addItemListener(new java.awt.event.ItemListener() { 
				public void itemStateChanged(java.awt.event.ItemEvent e) {    
					toggleArray();
				}
			});
			arrayCombo.addItem(ARRAY_NO);
			arrayCombo.addItem(ARRAY_YES);
			
		}
		return arrayCombo;
	}
	
	private void toggleArray(){
		if (((String)arrayCombo.getSelectedItem()).equals(ARRAY_YES)) {
			if(!className.getText().endsWith("[]")){
				String newClassName = className.getText() + "[]";
			   className.setText(newClassName);
			}
		} else {
		   if(className.getText().endsWith("[]")){
				className.setText(className.getText().substring(0,
						className.getText().length() - 2));
		   }
		}
		
	}


	/**
	 * This method initializes paramName	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getParamName() {
		if (paramName == null) {
			paramName = new JTextField();
		}
		return paramName;
	}
} //  @jve:decl-index=0:visual-constraint="10,10"
