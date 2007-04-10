package gov.nih.nci.cagrid.data.ui.browser;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.extension.AdditionalLibraries;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.ResourceManager;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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


/**
 * ClassBrowserPanel 
 * Panel to enable browsing for a class and building up a list
 * of JARs to look in
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
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
	
	private transient List classSelectionListeners = null;
	private transient List additionalJarsListeners = null;
	
	private transient ExtensionTypeExtensionData extensionData = null;
	private transient ServiceInformation serviceInfo = null;

	public ClassBrowserPanel(ExtensionTypeExtensionData extensionData, ServiceInformation serviceInfo) {
		this.extensionData = extensionData;
		this.serviceInfo = serviceInfo;
		classSelectionListeners = new LinkedList();
		additionalJarsListeners = new LinkedList();
		populateFields();
		initialize();
	}
	
	
	private void populateFields() {
		// get the additional jars
		AdditionalLibraries additionalLibs = null;
		try {
			additionalLibs = ExtensionDataUtils.getExtensionData(extensionData).getAdditionalLibraries();
		} catch (Exception ex) {
			ErrorDialog.showErrorDialog("Error loading list of additional jars", ex);
		}
		if (additionalLibs != null && additionalLibs.getJarName() != null) {
			addJars(additionalLibs.getJarName());
		}
		// populate available classes from the jars
		populateClassDropdown();
		// set the selected query processor class
		if (CommonTools.servicePropertyExists(
			serviceInfo.getServiceDescriptor(), DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY)) {
			try {
				String qpClassname = CommonTools.getServicePropertyValue(
					serviceInfo.getServiceDescriptor(), DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY);
				getClassSelectionComboBox().setSelectedItem(qpClassname);
			} catch (Exception ex) {
				ex.printStackTrace();
				ErrorDialog.showErrorDialog("Error getting query processor class name from properties", ex);
			}
		}
	}
	
	
	private void addJars(String[] jarFiles) {
		// only bother adding the jar file to the list if it's not in there yet
		for (int i = 0; i < jarFiles.length; i++) {
			String jarFile = jarFiles[i];
			String shortJarName = (new File(jarFile)).getName();
			boolean shouldAdd = true;
			String[] currentJars = getAdditionalJars();
			for (int j = 0; j < currentJars.length; j++) {
				if (shortJarName.equals(currentJars[j])) {
					shouldAdd = false;
					break;
				}
			}
			if (shouldAdd) {
				// copy the jar to the service's lib directory
				copyJarToService(jarFile, shortJarName);
				// add the jar to the extension data's list of additional jars
				// add the new jar name to the jars list
				String[] additionalJars = new String[currentJars.length + 1];
				System.arraycopy(currentJars, 0, additionalJars, 0, currentJars.length);
				additionalJars[additionalJars.length - 1] = shortJarName;
				getAdditionalJarsList().setListData(additionalJars);
			}
		}
	}


	/**
	 * This method initializes this
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
		this.add(getClassSelectionPanel(), gridBagConstraints6);
		this.add(getJarsPanel(), gridBagConstraints7);
	}


	public String getSelectedClassName() {
		Object selected = getClassSelectionComboBox().getSelectedItem();
		if (selected != null && selected.toString().length() != 0) {
			return selected.toString();
		}
		return null;
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
				AdditionalLibraries additionalLibs = null;
				try {
					additionalLibs = ExtensionDataUtils.getExtensionData(extensionData).getAdditionalLibraries();
				} catch (Exception ex) {
					ErrorDialog.showErrorDialog("Error loading list of additional jars", ex);
				}
				if (additionalLibs != null && additionalLibs.getJarName() != null) {
					additionalJarsList.setListData(additionalLibs.getJarName());
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
					browseForJars();
					fireAdditionalJarsChanged();
					populateClassDropdown();
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
					// identify selected / kept jars
					Set selected = new HashSet();
					Collections.addAll(selected, getAdditionalJarsList().getSelectedValues());
					Vector keptJars = new Vector();
					for (int i = 0; i < getAdditionalJarsList().getModel().getSize(); i++) {
						String jarName = (String) getAdditionalJarsList().getModel().getElementAt(i);
						if (!selected.contains(jarName)) {
							keptJars.add(jarName);
						}
					}
					// change the list contents
					getAdditionalJarsList().setListData(keptJars);
					// delete the selected jars
					Iterator deleteJarsIter = selected.iterator();
					while (deleteJarsIter.hasNext()) {
						deleteAdditionalJar((String) deleteJarsIter.next());
					}
					
					// update the class selection dropdown
					populateClassDropdown();
					
					// notify listeners
					fireAdditionalJarsChanged();
				}
			});
		}
		return removeJarsButton;
	}


	private void deleteAdditionalJar(String shortJarName) {
		String libDir = serviceInfo.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
		File jarFile = new File(libDir + File.separator + shortJarName);
		jarFile.delete();
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
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
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
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
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
			// add listener for manual changes to the class selection
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
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
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


	private void browseForJars() {
		String[] jarFiles = null;
		try {
			jarFiles = ResourceManager.promptMultiFiles(this, null, new FileFilters.JarFileFilter());
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error selecting files: " + ex.getMessage(), ex);
		}
		if (jarFiles != null) {
			addJars(jarFiles);
		}
	}
	

	private synchronized void copyJarToService(final String jarFile, final String shortJarName) {
		String libDir = serviceInfo.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
		try {
			File inJarFile = new File(jarFile);
			File outJarFile = new File(libDir + File.separator + shortJarName);
			Utils.copyFile(inJarFile, outJarFile);
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error copying the jar " + jarFile, ex);
		}
	}


	private void populateClassDropdown() {
		String libDir = serviceInfo.getIntroduceServiceProperties().getProperty(
			IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR) + File.separator + "lib";
		SortedSet classNames = new TreeSet();
		String[] jars = getAdditionalJars();
		try {
			URL[] urls = new URL[jars.length];
			for (int i = 0; i < jars.length; i++) {
				urls[i] = (new File(libDir + File.separator + jars[i])).toURL();
			}
			ClassLoader loader = new URLClassLoader(urls, getClass().getClassLoader());
			Class queryProcessorClass = CQLQueryProcessor.class;
			for (int i = 0; i < jars.length; i++) {
				JarFile jarFile = new JarFile(libDir + File.separator + jars[i]);
				Enumeration jarEntries = jarFile.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry entry = (JarEntry) jarEntries.nextElement();
					String name = entry.getName();
					if (name.endsWith(".class")) {
						name = name.replace('/', '.');
						name = name.substring(0, name.length() - 6);
						Class loadedClass = null;
						try {
							loadedClass = loader.loadClass(name);
						} catch (Throwable e) {
							// theres a lot of these...
							// System.err.println("Error loading class (" + name
							// + "):" + e.getMessage());
						}
						if (loadedClass != null && queryProcessorClass.isAssignableFrom(loadedClass)) {
							classNames.add(name);
						}
					}
				}
				jarFile.close();
			}
			loader = null;
			// remove all the classes currently in the drop down
			while (getClassSelectionComboBox().getItemCount() != 0) {
				getClassSelectionComboBox().removeItemAt(0);
			}
			// ensure the query processor stub is available
			String qpStubName = ExtensionDataUtils.getQueryProcessorStubClassName(serviceInfo);
			if (!classNames.contains(qpStubName)) {
				classNames.add(qpStubName);
			}
			// populate the drop down
			Iterator nameIter = classNames.iterator();
			while (nameIter.hasNext()) {
				getClassSelectionComboBox().addItem(nameIter.next());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.showErrorDialog("Error populating class names: " + ex.getMessage(), ex);
		}
	}


	public void addClassSelectionListener(ClassSelectionListener listener) {
		this.classSelectionListeners.add(listener);
	}
	
	
	public ClassSelectionListener[] getClassSelectionListeners() {
		ClassSelectionListener[] listeners = 
			new ClassSelectionListener[this.classSelectionListeners.size()];
		this.classSelectionListeners.toArray(listeners);
		return listeners;
	}


	public boolean removeClassSelectionListener(ClassSelectionListener listener) {
		return this.classSelectionListeners.remove(listener);
	}


	public void addAdditionalJarsChangeListener(AdditionalJarsChangeListener listener) {
		this.additionalJarsListeners.add(listener);
	}
	
	
	public AdditionalJarsChangeListener[] getAdditionalJarsChangeListeners() {
		AdditionalJarsChangeListener[] listeners = 
			new AdditionalJarsChangeListener[this.additionalJarsListeners.size()];
		this.additionalJarsListeners.toArray(listeners);
		return listeners;
	}


	public boolean removeAdditionalJarsChangeListener(AdditionalJarsChangeListener listener) {
		return this.additionalJarsListeners.remove(listener);
	}


	protected synchronized void fireClassSelectionChanged() {
		ClassSelectionEvent event = null;
		Iterator listenerIter = classSelectionListeners.iterator();
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
