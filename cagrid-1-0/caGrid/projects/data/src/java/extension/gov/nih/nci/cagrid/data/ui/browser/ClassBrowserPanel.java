package gov.nih.nci.cagrid.data.ui.browser;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.data.common.AxisJdomUtils;
import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.axis.message.MessageElement;
import org.jdom.Element;

/** 
 *  ClassBrowserPanel
 *  Panel to enable browsing for a class and building up a list of JARs to look in
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 11, 2006 
 * @version $Id$ 
 */
public class ClassBrowserPanel extends JPanel {
		
	private JList additionalJarsList = null;
	private JScrollPane additionalJarsScrollPane = null;
	private JButton addJarButton = null;
	private JButton removeJarsButton = null;
	private JPanel jarButtonsPanel = null;
	private JPanel jarsPanel = null;
	private JComboBox classSelectionComboBox = null;
	private JPanel classSelectionPanel = null;
	private JLabel classSelectionLabel = null;
	
	private ExtensionTypeExtensionData extensionData = null;
	
	private List classChangeListeners = null;
	private List additionalJarsListeners = null;
	
	public ClassBrowserPanel() {
		this(null);
	}
	
	
	public ClassBrowserPanel(ExtensionTypeExtensionData extensionData) {
		this.extensionData = extensionData;
		classChangeListeners = new LinkedList();
		additionalJarsListeners = new LinkedList();
		initialize();
	}
	
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints7.weightx = 1.0D;
        gridBagConstraints7.weighty = 1.0D;
        gridBagConstraints7.gridy = 1;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.weightx = 1.0D;
        gridBagConstraints6.gridy = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(304,229));
        this.add(getClassSelectionPanel(), gridBagConstraints6);
        this.add(getJarsPanel(), gridBagConstraints7);
	}


	public String getSelectedClassName() {
		return getClassSelectionComboBox().getSelectedItem().toString();
	}
	
	
	public void setSelectedClassName(String className) {
		getClassSelectionComboBox().setSelectedItem(className);
	}
	
	
	public String[] getAdditionalJars() {
		String[] jars = new String[getAdditionalJarsList().getModel().getSize()];
		for (int i = 0; i < getAdditionalJarsList().getModel().getSize(); i++) {
			jars[i] = (String) getAdditionalJarsList().getModel().getElementAt(i);
		}
		return jars;
	}


	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getAdditionalJarsList() {
		if (additionalJarsList == null) {
			additionalJarsList = new JList();
			// load any previous additional jars information
			if (extensionData != null) {
				MessageElement jarsElement = ExtensionTools.getExtensionDataElement(extensionData, DataServiceConstants.QUERY_PROCESSOR_ADDITIONAL_JARS_ELEMENT);
				if (jarsElement != null) {
					Element qpLibs = AxisJdomUtils.fromMessageElement(jarsElement);
					Vector jars = new Vector();
					Iterator jarElemIter = qpLibs.getChildren(DataServiceConstants.QUERY_PROCESSOR_JAR_ELEMENT, qpLibs.getNamespace()).iterator();
					while (jarElemIter.hasNext()) {
						String jarFilename = ((Element) jarElemIter.next()).getText();
						jars.add(jarFilename);
					}
					additionalJarsList.setListData(jars);
				}
			}
		}
		return additionalJarsList;
	}


	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getAdditionalJarsScrollPane() {
		if (additionalJarsScrollPane == null) {
			additionalJarsScrollPane = new JScrollPane();
			additionalJarsScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Additional Jars", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			additionalJarsScrollPane.setViewportView(getAdditionalJarsList());
		}
		return additionalJarsScrollPane;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddJarButton() {
		if (addJarButton == null) {
			addJarButton = new JButton();
			addJarButton.setText("Add Jar");
			addJarButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					browseForJar();
					populateClassDropdown();
					fireAdditionalJarsChanged();
				}
			});
		}
		return addJarButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveJarsButton() {
		if (removeJarsButton == null) {
			removeJarsButton = new JButton();
			removeJarsButton.setText("Remove Jars");
			removeJarsButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int[] selected = getAdditionalJarsList().getSelectedIndices();
					if (selected.length != 0) {
						String[] currentJars = getAdditionalJars();
						String[] storedJars = new String[currentJars.length - selected.length];
						int selectIndex = 0;
						int storeIndex = 0;
						for (int i = 0; i < currentJars.length; i++) {
							String jar = currentJars[i];
							if (i == selected[selectIndex]) {
								// skip the selected jar
								selectIndex++;
							} else {
								// add the jar to the new list
								storedJars[storeIndex] = jar;
								storeIndex++;
							}
						}
						getAdditionalJarsList().setListData(storedJars);
						populateClassDropdown();
						fireAdditionalJarsChanged();
					}
				}
			});
		}
		return removeJarsButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJarButtonsPanel() {
		if (jarButtonsPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			jarButtonsPanel = new JPanel();
			jarButtonsPanel.setLayout(new GridBagLayout());
			jarButtonsPanel.add(getAddJarButton(), gridBagConstraints);
			jarButtonsPanel.add(getRemoveJarsButton(), gridBagConstraints1);
		}
		return jarButtonsPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJarsPanel() {
		if (jarsPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0D;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridx = 0;
			jarsPanel = new JPanel();
			jarsPanel.setLayout(new GridBagLayout());
			jarsPanel.add(getAdditionalJarsScrollPane(), gridBagConstraints2);
			jarsPanel.add(getJarButtonsPanel(), gridBagConstraints3);
		}
		return jarsPanel;
	}


	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getClassSelectionComboBox() {
		if (classSelectionComboBox == null) {
			classSelectionComboBox = new JComboBox();
			classSelectionComboBox.setEditable(true);
			classSelectionComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					fireClassSelectionChanged();
				}
			});
			Component c = classSelectionComboBox.getEditor().getEditorComponent();
			((JTextField) c).getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					fireClassSelectionChanged();
				}
				
				
				public void removeUpdate(DocumentEvent e) {
					fireClassSelectionChanged();
				}
				
				
				public void changedUpdate(DocumentEvent e) {
					fireClassSelectionChanged();
				}
			});
		}
		return classSelectionComboBox;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getClassSelectionPanel() {
		if (classSelectionPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridy = 0;
			classSelectionPanel = new JPanel();
			classSelectionPanel.setLayout(new GridBagLayout());
			classSelectionPanel.add(getClassSelectionLabel(), gridBagConstraints4);
			classSelectionPanel.add(getClassSelectionComboBox(), gridBagConstraints5);
		}
		return classSelectionPanel;
	}
	
	
	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getClassSelectionLabel() {
		if (classSelectionLabel == null) {
			classSelectionLabel = new JLabel();
			classSelectionLabel.setText("Selected Class:");
		}
		return classSelectionLabel;
	}
	
	
	private void browseForJar() {
		String jarFile = null;
		try {
			jarFile = ResourceManager.promptFile(this, ".", new JarFileFilter());
			// only bother adding the jar file to the list if it's not in there yet
			boolean shouldAdd = true;
			String[] currentJars = getAdditionalJars();
			for (int i = 0; i < currentJars.length; i++) {
				if (jarFile.equals(currentJars[i])) {
					shouldAdd = false;
					break;
				}
			}
			if (shouldAdd) {
				String[] additionalJars = new String[currentJars.length + 1];
				System.arraycopy(currentJars, 0, additionalJars, 0, currentJars.length);
				additionalJars[additionalJars.length - 1] = jarFile;
				getAdditionalJarsList().setListData(additionalJars);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			PortalUtils.showErrorMessage("Error opening the jar " + jarFile, ex);
		}
	}
	
	
	private void populateClassDropdown() {
		SortedSet classNames = new TreeSet();
		String[] jars = getAdditionalJars();
		try {
			URL[] urls = new URL[jars.length];
			for (int i = 0; i < jars.length; i++) {
				urls[i] = (new File(jars[i])).toURL();
			}
			ClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader());
			Class queryProcessorClass = CQLQueryProcessor.class;
			for (int i = 0; i < jars.length; i++) {
				JarFile jarFile = new JarFile(jars[i]);
				Enumeration jarEntries = jarFile.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry entry = (JarEntry) jarEntries.nextElement();
					String name = entry.getName();
					if (name.endsWith(".class")) {
						name = name.replace('/', '.');
						name = name.substring(0, name.length() - 6);
						Class loadedClass = loader.loadClass(name);
						if (queryProcessorClass.isAssignableFrom(loadedClass)) {
							classNames.add(name);
						}
					}
				}
			}
			while (getClassSelectionComboBox().getItemCount() != 0) {
				getClassSelectionComboBox().removeItemAt(0);
			}
			Iterator nameIter = classNames.iterator();
			while (nameIter.hasNext()) {
				getClassSelectionComboBox().addItem(nameIter.next());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			PortalUtils.showErrorMessage("Error populating class names " + ex.getMessage(), ex);
		}
	}
	
	
	public void addClassSelectionListener(ClassSelectionListener listener) {
		this.classChangeListeners.add(listener);
	}
	
	
	public boolean removeClassSelectionListener(ClassSelectionListener listener) {
		return this.classChangeListeners.remove(listener);
	}
	
	
	public void addAdditionalJarsChangeListener(AdditionalJarsChangeListener listener) {
		this.additionalJarsListeners.add(listener);
	}
	
	
	public boolean removeAdditionalJarsChangeListener(AdditionalJarsChangeListener listener) {
		return this.additionalJarsListeners.remove(listener);
	}
	
	
	protected synchronized void fireClassSelectionChanged() {
		ClassSelectionEvent event = null;
		Iterator listenerIter = classChangeListeners.iterator();
		while (listenerIter.hasNext()) {
			if (event == null) {
				event = new ClassSelectionEvent(this);
			}
			((ClassSelectionListener) listenerIter.next()).classSelectionChanged(event);
		}
	}
	
	
	protected synchronized void fireAdditionalJarsChanged() {
		AdditionalJarsChangedEvent event = null;
		Iterator listenerIter = additionalJarsListeners.iterator();
		while (listenerIter.hasNext()) {
			if (event == null) {
				event = new AdditionalJarsChangedEvent(this);
			}
			((AdditionalJarsChangeListener) listenerIter.next()).additionalJarsChanged(event);
		}
	}
}
