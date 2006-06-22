package gov.nih.nci.cagrid.cadsr.portal;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class CaDSRBrowserPanel extends JPanel implements ProjectSelectedListener, PackageSelectedListener {

	private JPanel queryPanel = null;
	private JButton queryButton = null;
	protected File schemaDir;
	private JComboBox projectComboBox = null;
	private JComboBox packageComboBox = null;
	private JPanel projectsPanel = null;
	private JLabel projectLabel = null;
	private JTextField cadsr = null;
	private String default_cadsrURL = "http://localhost:8080/wsrf/services/cagrid/CaDSRService";
	private JLabel cadsrAddressLabel = null;
	private JLabel packageLabel = null;
	private JComboBox classComboBox = null;
	private JLabel classLabel = null;
	private boolean showQueryPanel = true;
	private boolean showClassSelection = true;

	private List packageSelectionListeners = null;
	private List projectSelectionListeners = null;
	private List classSelectionListeners = null;


	public CaDSRBrowserPanel() {
		this(true, true);
	}


	public CaDSRBrowserPanel(boolean showQueryPanel, boolean showClassSelection) {
		super();
		this.showQueryPanel = showQueryPanel;
		this.showClassSelection = showClassSelection;

		packageSelectionListeners = new ArrayList();
		projectSelectionListeners = new ArrayList();
		classSelectionListeners = new ArrayList();

		this.addProjectSelectionListener(this);
		this.addPackageSelectionListener(this);
		initialize();
	}


	public boolean addPackageSelectionListener(PackageSelectedListener listener) {
		return packageSelectionListeners.add(listener);
	}


	public boolean addProjectSelectionListener(ProjectSelectedListener listener) {
		return projectSelectionListeners.add(listener);
	}


	public boolean addClassSelectionListener(ClassSelectedListener listener) {
		return classSelectionListeners.add(listener);
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		setLayout(new GridBagLayout());
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		if (isShowQueryPanel()) {
			add(getQueryPanel(), gridBagConstraints1);
		}
		add(getProjectsPanel(), gridBagConstraints);
	}


	public void setDefaultCaDSRURL(String url) {
		this.default_cadsrURL = url;
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	protected JPanel getQueryPanel() {
		if (queryPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.weightx = 1.0;
			cadsrAddressLabel = new JLabel();
			cadsrAddressLabel.setText("caDSR");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridx = 0;
			queryPanel = new JPanel();
			queryPanel.setLayout(new GridBagLayout());
			queryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Discover Data Types",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			queryPanel.add(getQueryButton(), gridBagConstraints4);
			queryPanel.add(cadsrAddressLabel, gridBagConstraints6);
			queryPanel.add(getCadsr(), gridBagConstraints5);
		}
		return queryPanel;
	}


	public void discoverFromCaDSR() {
		final CaDSRServiceI cadsrService = new CaDSRServiceClient(getCadsr().getText());
		getProjectComboBox().removeAllItems();
		makeCombosEnabled(false);
		Thread t = new Thread() {
			public void run() {
				try {
					Project[] projects = cadsrService.findAllProjects();
					if (projects != null) {
						for (int i = 0; i < projects.length; i++) {
							getProjectComboBox().addItem(new ProjectDisplay(projects[i]));
						}
					}
					makeCombosEnabled(true);
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(CaDSRBrowserPanel.this,
						"Error communicating with caDSR; please check the caDSR URL!");
				}
			}
		};
		t.start();
	}


	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	protected JButton getQueryButton() {
		if (queryButton == null) {
			queryButton = new JButton("Refresh from caDSR Service");
			queryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					discoverFromCaDSR();
				}
			});
		}
		return queryButton;
	}


	public Project getSelectedProject() {
		ProjectDisplay p = (ProjectDisplay) projectComboBox.getSelectedItem();
		if (p != null) {
			return p.getProject();
		}
		return null;
	}


	public UMLPackageMetadata getSelectedPackage() {
		PackageDisplay p = (PackageDisplay) getPackageComboBox().getSelectedItem();
		if (p != null) {
			return p.getPackage();
		}
		return null;
	}


	public UMLClassMetadata getSelectedClass() {
		ClassDisplay c = (ClassDisplay) getClassComboBox().getSelectedItem();
		if (c != null) {
			return c.getClazz();
		}
		return null;
	}


	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	protected JComboBox getProjectComboBox() {
		if (projectComboBox == null) {
			projectComboBox = new JComboBox();
			projectComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED && projectComboBox.getSelectedItem() != null) {
						Project project = getSelectedProject();
						for (int i = 0; i < projectSelectionListeners.size(); i++) {
							ProjectSelectedListener listener = (ProjectSelectedListener) projectSelectionListeners
								.get(i);
							listener.handleProjectSelection(project);
						}
					}
				}
			});
		}
		return projectComboBox;
	}


	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	protected JComboBox getPackageComboBox() {
		if (packageComboBox == null) {
			packageComboBox = new JComboBox();
			packageComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED && getPackageComboBox().getSelectedItem() != null) {
						UMLPackageMetadata pkg = getSelectedPackage();
						for (int i = 0; i < packageSelectionListeners.size(); i++) {
							PackageSelectedListener listener = (PackageSelectedListener) packageSelectionListeners
								.get(i);
							listener.handlePackageSelection(pkg);
						}
					}
				}

			});
		}
		return packageComboBox;
	}


	/**
	 * This method initializes projectsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getProjectsPanel() {
		if (projectsPanel == null) {
			packageLabel = new JLabel();
			packageLabel.setText("Package:");

			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 2;
			classLabel = new JLabel();
			classLabel.setText("Class:");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 1.0;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridx = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridx = 0;
			projectLabel = new JLabel();
			projectLabel.setText("Project:");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridx = 1;
			projectsPanel = new JPanel();
			projectsPanel.setLayout(new GridBagLayout());
			if (isShowQueryPanel()) {
				projectsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Data Type",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			}
			projectsPanel.add(getProjectComboBox(), gridBagConstraints7);
			projectsPanel.add(projectLabel, gridBagConstraints9);
			projectsPanel.add(getPackageComboBox(), gridBagConstraints8);
			projectsPanel.add(packageLabel, gridBagConstraints10);
			if (isShowClassSelection()) {
				projectsPanel.add(getClassComboBox(), gridBagConstraints2);
				projectsPanel.add(classLabel, gridBagConstraints3);
			}
		}
		return projectsPanel;
	}


	/**
	 * This method initializes cadsr
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getCadsr() {
		if (cadsr == null) {
			cadsr = new JTextField();
			cadsr.setText(this.default_cadsrURL);
		}
		return cadsr;
	}


	/**
	 * This method initializes classComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getClassComboBox() {
		if (classComboBox == null) {
			classComboBox = new JComboBox();
			classComboBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED && getClassComboBox().getSelectedItem() != null) {
						UMLClassMetadata clazz = getSelectedClass();
						for (int i = 0; i < classSelectionListeners.size(); i++) {
							ClassSelectedListener listener = (ClassSelectedListener) classSelectionListeners.get(i);
							listener.handleClassSelection(clazz);
						}
					}
				}
			});
		}
		return classComboBox;
	}


	public boolean isShowQueryPanel() {
		return showQueryPanel;
	}


	public boolean isShowClassSelection() {
		return showClassSelection;
	}


	protected class ProjectDisplay {
		private Project project;


		public ProjectDisplay(Project project) {
			setProject(project);
		}


		public Project getProject() {
			return project;
		}


		public void setProject(Project project) {
			this.project = project;
		}


		public String toString() {
			return project.getShortName() + " (version: " + project.getVersion() + ")";
		}

	}


	protected class PackageDisplay {
		private UMLPackageMetadata pack;


		public PackageDisplay(UMLPackageMetadata pack) {
			setPackage(pack);
		}


		public UMLPackageMetadata getPackage() {
			return pack;
		}


		public void setPackage(UMLPackageMetadata pack) {
			this.pack = pack;
		}


		public String toString() {
			return pack.getName();
		}

	}


	protected class ClassDisplay {
		private UMLClassMetadata clazz;


		public ClassDisplay(UMLClassMetadata clazz) {
			setClazz(clazz);
		}


		public UMLClassMetadata getClazz() {
			return clazz;
		}


		public void setClazz(UMLClassMetadata clazz) {
			this.clazz = clazz;
		}


		public String toString() {
			return clazz.getName();
		}

	}


	public static void main(String[] args) {
		CaDSRBrowserPanel panel = new CaDSRBrowserPanel();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}


	public void handleProjectSelection(final Project project) {
		// System.out.println("Handle Project:" + project.getShortName());
		getPackageComboBox().removeAllItems();
		makeCombosEnabled(false);
		Thread t = new Thread() {
			public void run() {
				try {
					CaDSRServiceI cadsrService = new CaDSRServiceClient(getCadsr().getText());
					UMLPackageMetadata[] metadatas = cadsrService.findPackagesInProject(project);
					if (metadatas != null) {
						for (int i = 0; i < metadatas.length; i++) {
							getPackageComboBox().addItem(new PackageDisplay(metadatas[i]));
						}
					}
					makeCombosEnabled(true);
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(CaDSRBrowserPanel.this,
						"Error communicating with caDSR; please check the caDSR URL!");
				}
			}
		};
		t.start();
	}


	private synchronized void makeCombosEnabled(boolean enabled) {
		getProjectComboBox().setEnabled(enabled);
		getPackageComboBox().setEnabled(enabled);
		getClassComboBox().setEnabled(enabled);
	}


	public void handlePackageSelection(final UMLPackageMetadata pkg) {
		// System.out.println("Handle package:" + pkg.getName());
		if (isShowClassSelection()) {
			getClassComboBox().removeAllItems();
			makeCombosEnabled(false);
			Thread t = new Thread() {
				public void run() {
					try {
						CaDSRServiceI cadsrService = new CaDSRServiceClient(getCadsr().getText());
						UMLClassMetadata[] metadatas = cadsrService.findClassesInPackage(
							((ProjectDisplay) getProjectComboBox().getSelectedItem()).getProject(), pkg.getName());
						if (metadatas != null) {
							for (int i = 0; i < metadatas.length; i++) {
								getClassComboBox().addItem(new ClassDisplay(metadatas[i]));
							}
						}
						makeCombosEnabled(true);
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(CaDSRBrowserPanel.this,
							"Error communicating with caDSR; please check the caDSR URL!");
					}
				}
			};
			t.start();
		}

	}

} // @jve:decl-index=0:visual-constraint="10,10"
