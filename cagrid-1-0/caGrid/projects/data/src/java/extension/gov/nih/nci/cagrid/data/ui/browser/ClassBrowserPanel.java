package gov.nih.nci.cagrid.data.ui.browser;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.PortalUtils;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

	private static File lastDir = null;
		
	private JList additionalJarsList = null;
	private JScrollPane additionalJarsScrollPane = null;
	private JButton addJarButton = null;
	private JButton removeJarsButton = null;
	private JPanel jarButtonsPanel = null;
	private JPanel jarsPanel = null;
	private JComboBox classSelectionComboBox = null;
	private JPanel classSelectionPanel = null;
	private JLabel classSelectionLabel = null;
	
	private List classChangeListeners = null;
	private List additionalJarsListeners = null;
	
	public ClassBrowserPanel() {
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
	
	
	public JarFile[] getAdditionalJars() {
		JarFile[] jars = new JarFile[getAdditionalJarsList().getModel().getSize()];
		for (int i = 0; i < getAdditionalJarsList().getModel().getSize(); i++) {
			jars[i] = (JarFile) getAdditionalJarsList().getModel().getElementAt(i);
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
			additionalJarsList.setCellRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof JarFile) {
						setText(((JarFile) value).getName());
					}
					return this;
				}
			});
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
						JarFile[] currentJars = getAdditionalJars();
						JarFile[] storedJars = new JarFile[currentJars.length - selected.length];
						int selectIndex = 0;
						int storeIndex = 0;
						for (int i = 0; i < currentJars.length; i++) {
							JarFile jar = currentJars[i];
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
			if (c instanceof JTextField) {
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
		JFileChooser chooser = new JFileChooser(lastDir);
		chooser.setFileFilter(new JarFileFilter());
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		int choice = chooser.showDialog(this, "Select");
		if (choice == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();
			lastDir = selected;
			try {
				JarFile jarFile = new JarFile(selected);
				// only bother adding the jar file to the list if it's not in there yet
				boolean shouldAdd = true;
				JarFile[] currentJars = getAdditionalJars();
				for (int i = 0; i < currentJars.length; i++) {
					if (jarFile.getName().equals(currentJars[i])) {
						shouldAdd = false;
						break;
					}
				}
				if (shouldAdd) {
					JarFile[] additionalJars = new JarFile[currentJars.length + 1];
					System.arraycopy(currentJars, 0, additionalJars, 0, currentJars.length);
					additionalJars[additionalJars.length - 1] = jarFile;
					getAdditionalJarsList().setListData(additionalJars);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				PortalUtils.showErrorMessage("Error opening the jar " + selected.getAbsolutePath(), ex);
			}
		}
	}
	
	
	private void populateClassDropdown() {
		SortedSet classNames = new TreeSet();
		JarFile[] jars = getAdditionalJars();
		for (int i = 0; i < jars.length; i++) {
			Enumeration jarEntries = jars[i].entries();
			while (jarEntries.hasMoreElements()) {
				JarEntry entry = (JarEntry) jarEntries.nextElement();
				if (entry.getName().endsWith(".class")) {
					String name = entry.getName();
					classNames.add(name.replace('/', '.'));
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
