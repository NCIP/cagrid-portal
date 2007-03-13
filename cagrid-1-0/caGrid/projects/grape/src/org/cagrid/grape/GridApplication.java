package org.cagrid.grape;

import gov.nih.nci.cagrid.common.Utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.cagrid.grape.filters.XMLFileFilter;
import org.cagrid.grape.model.Application;
import org.cagrid.grape.model.Component;
import org.cagrid.grape.model.Components;
import org.cagrid.grape.model.Configuration;
import org.cagrid.grape.model.ConfigurationDescriptor;
import org.cagrid.grape.model.ConfigurationDescriptors;
import org.cagrid.grape.model.ConfigurationGroup;
import org.cagrid.grape.model.ConfigurationGroups;
import org.cagrid.grape.model.Menu;
import org.cagrid.grape.model.Menus;
import org.cagrid.grape.model.Submenus;
import org.cagrid.grape.utils.IconUtils;
import org.projectmobius.common.MobiusPoolManager;
import org.projectmobius.common.MobiusRunnable;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @created Oct 14, 2004
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class GridApplication extends JFrame {

	private javax.swing.JMenuBar jJMenuBar = null;

	private javax.swing.JMenu fileMenu = null;

	private javax.swing.JMenuItem exitMenuItem = null;

	private javax.swing.JMenu windowsMenu = null;

	private javax.swing.JMenu helpMenu = null;

	private javax.swing.JMenuItem jMenuItem = null;

	private javax.swing.JToolBar toolBar = null;

	private JScrollPane scrollPane = null;

	private MDIDesktopPane desktop = null;

	private javax.swing.JMenuItem closeMenuItem = null;

	private javax.swing.JMenuItem closeAllMenuItem = null;

	private ApplicationComponent lastComp = null;

	private Application app;

	private MobiusPoolManager threadManager;

	private ConfigurationManager configurationManager;

	public GridApplication(Application app) throws Exception {
		super();
		this.app = app;
		this.threadManager = new MobiusPoolManager();
		configurationManager = new ConfigurationManager(new ApplicationContext(
				this), app.getConfiguration());
		initialize();
	}

	public MobiusPoolManager getThreadManager() {
		return threadManager;
	}

	public static void main(String[] args) {
		File file = null;
		boolean promptForConfigFile = false;

		if (args.length == 0) {
			promptForConfigFile = true;
		} else {
			file = new File(args[0]);
			if (!file.exists()) {
				System.out
						.println("Invalid configuration file specified, prompting for one...");
				promptForConfigFile = true;
			}
		}
		if (promptForConfigFile) {
			// No config passed, try to prompt for one
			JFrame tempFrame = new JFrame(); // temp frame to open file
			// chooser from
			System.out
					.println("No configuration file passed in, prompting for one...");
			JFileChooser chooser = new JFileChooser(System
					.getProperty("user.dir"));
			chooser
					.setDialogTitle("Select an application configuration file to use.");
			chooser.setDialogType(JFileChooser.OPEN_DIALOG);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(false);
			// TODO: FIX THIS
			chooser.setFileFilter(new XMLFileFilter());
			int choice = chooser.showOpenDialog(tempFrame);
			if (choice == JFileChooser.APPROVE_OPTION) {
				try {
					file = new File(chooser.getSelectedFile().getAbsolutePath());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(),
							"Error loading file", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				System.err
						.println("No configuration file passed in or selected... exiting.");
				System.exit(1);
			}
			// destroy the temp frame
			tempFrame.dispose();
		} else if (args.length > 1) {
			// invalid usage
			System.out.println("USAGE:\n");
			System.out.println("	java " + GridApplication.class.getName()
					+ " [application-config-file]");
			System.exit(1);
		}

		try {

			Application app = (Application) Utils.deserializeDocument(file
					.getAbsolutePath(), Application.class);

			// launch the portal with the passed config
			GridApplication application = new GridApplication(app);
			Dimension d = new Dimension(app.getDimensions().getWidth(), app
					.getDimensions().getHeight());

			try {
				application.pack();
			} catch (Exception e) {
				application.setIconImage(null);
				application.pack();
			}
			application.setSize(d);
			application.setVisible(true);
			application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initialize() throws Exception {
		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Failed to setting system look and feel.");
		}
		List toolbarComponents = new ArrayList();
		this.setJMenuBar(getJJMenuBar(toolbarComponents));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(getJScrollPane(), BorderLayout.CENTER);
		this.getContentPane().add(getToolBar(toolbarComponents),
				BorderLayout.NORTH);

		this.setTitle(app.getName());

		if (app.getIcon() != null) {
			ImageIcon icon = IconUtils.loadIcon(app.getIcon());
			if (icon != null) {
				this.setIconImage(icon.getImage());
			}
		}

	}

	private void executeComponent(Component component) {
		try {
			ExecuteComponent comp = new ExecuteComponent(this, component);
			getThreadManager().executeInBackground(comp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private javax.swing.JMenuItem getComponentItem(final Component comp,
			boolean showIcon) {
		JMenuItem item = new javax.swing.JMenuItem();
		item.setText(comp.getTitle());
		if (showIcon) {
			item.setIcon(IconUtils.loadIcon(comp.getIcon()));
		}
		item.setMnemonic(java.awt.event.KeyEvent.VK_Q);
		item.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				executeComponent(comp);
			}
		});
		return item;
	}

	private javax.swing.JButton getComponentButton(final Component comp) {
		JButton button = new JButton();
		button = new javax.swing.JButton();
		button.setText(comp.getTitle());
		button.setToolTipText(comp.getDescription());
		button.setIcon(IconUtils.loadIcon(comp.getIcon()));
		button.setMnemonic(java.awt.event.KeyEvent.VK_Q);
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				executeComponent(comp);
			}
		});
		return button;
	}

	/**
	 * @return
	 */
	private JScrollPane getJScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.getViewport().add(this.getMDIDesktopPane());
		}

		return scrollPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private javax.swing.JMenuBar getJJMenuBar(List toolbarComponents)
			throws Exception {
		if (jJMenuBar == null) {
			jJMenuBar = new javax.swing.JMenuBar();
			jJMenuBar.add(getFileMenu());

			Menus menus = app.getMenus();
			if (menus != null) {
				Menu[] menu = menus.getMenu();
				if (menu != null) {
					for (int i = 0; i < menu.length; i++) {
						jJMenuBar.add(getMenu(toolbarComponents, menu[i]));
					}
				}
			}
			jJMenuBar.add(getWindowsMenu());
			javax.swing.JMenu conf = new javax.swing.JMenu();
			conf.setText("Configuration");
			conf.setMnemonic(java.awt.event.KeyEvent.VK_F);

			if (processConfiguration(app.getConfiguration(), conf)) {
				jJMenuBar.add(conf);
			}
			jJMenuBar.add(getHelpMenu());

		}
		return jJMenuBar;
	}

	private boolean processConfiguration(Configuration c, JMenu m)
			throws Exception {
		boolean show = false;
		if (this.processConfigurationGroups(c.getConfigurationGroups(), m)) {
			show = true;
		}

		if (this.processConfigurationDescriptors(c
				.getConfigurationDescriptors(), m)) {
			show = true;
		}

		return show;

	}

	private boolean processConfigurationGroups(ConfigurationGroups list, JMenu m)
			throws Exception {
		boolean show = false;
		if (list != null) {
			ConfigurationGroup[] group = list.getConfigurationGroup();
			if (group != null) {
				for (int i = 0; i < group.length; i++) {
					if (this.processConfigurationGroup(group[i], m)) {
						show = true;
					}
				}
			}
		}
		if (show) {

		}
		return show;
	}

	private boolean processConfigurationDescriptors(
			ConfigurationDescriptors list, JMenu m) throws Exception {
		boolean show = false;
		if (list != null) {
			ConfigurationDescriptor[] des = list.getConfigurationDescriptor();
			if (des != null) {
				for (int i = 0; i < des.length; i++) {
					if (this.processConfigurationDescriptor(des[i], m)) {
						show = true;
					}
				}

			}
		}
		return show;
	}

	private boolean processConfigurationGroup(ConfigurationGroup des, JMenu m)
			throws Exception {
		boolean show = false;
		if (des != null) {
			javax.swing.JMenu jmenu = new javax.swing.JMenu();
			jmenu.setText(des.getName());
			jmenu.setMnemonic(java.awt.event.KeyEvent.VK_F);
			show = processConfigurationDescriptors(des
					.getConfigurationDescriptors(), jmenu);
			if (show) {
				m.add(jmenu);
			}
		}

		return show;

	}

	private boolean processConfigurationDescriptor(
			final ConfigurationDescriptor des, JMenu m) throws Exception {
		if (des.getUIClassname() != null) {
			Class[] types = new Class[3];
			types[0] = ApplicationContext.class;
			types[1] = String.class;
			types[2] = Class.forName(des.getModelClassname());

			Constructor c = Class.forName(des.getUIClassname()).getConstructor(
					types);
			Object[] args = new Object[3];
			args[0] = new ApplicationContext(this);
			args[1] = des.getSystemName();
			args[2] = this.configurationManager.getConfigurationObject(des
					.getSystemName());
			final ConfigurationComponent config = (ConfigurationComponent) c
					.newInstance(args);
			JMenuItem item = new javax.swing.JMenuItem();
			item.setText(des.getDisplayName());
			item.setMnemonic(java.awt.event.KeyEvent.VK_Q);
			item.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addApplicationComponent(config);
				}
			});
			m.add(item);
			return true;
		} else {
			return false;
		}
	}

	private javax.swing.JMenu getMenu(List toolbarComponents, Menu menu) {
		javax.swing.JMenu jmenu = new javax.swing.JMenu();
		jmenu.setText(menu.getTitle());
		if (menu.getShowIcons()) {
			jmenu.setIcon(IconUtils.loadIcon(menu.getIcon()));
		}
		jmenu.setMnemonic(java.awt.event.KeyEvent.VK_F);

		Submenus submenus = menu.getSubmenus();
		if (submenus != null) {
			Menu[] submenu = submenus.getMenu();
			if (submenu != null) {
				for (int i = 0; i < submenu.length; i++) {
					jmenu.add(getMenu(toolbarComponents, submenu[i]));
				}
			}
		}

		Components comps = menu.getComponents();
		if (comps != null) {
			Component[] comp = comps.getComponent();
			if (comp != null) {
				for (int i = 0; i < comp.length; i++) {
					jmenu.add(getComponentItem(comp[i], menu.getShowIcons()));
					if (comp[i].isShowOnToolBar()) {
						toolbarComponents.add(comp[i]);
					}
				}
			}
		}

		return jmenu;
	}

	/**
	 * This method initializes windowsMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getWindowsMenu() {
		if (windowsMenu == null) {
			windowsMenu = new WindowMenu(getMDIDesktopPane(),this);
		}
		return windowsMenu;
	}

	/**
	 * This method initializes fileMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new javax.swing.JMenu();
			fileMenu.add(getCloseMenuItem());
			fileMenu.add(getCloseAllMenuItem());
			fileMenu.addSeparator();
			fileMenu.setText("File");
			fileMenu.setMnemonic(java.awt.event.KeyEvent.VK_F);
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes exitMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new javax.swing.JMenuItem();
			exitMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_X);
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					exit();
				}
			});
			exitMenuItem.setToolTipText("Closes the application.");
			exitMenuItem.setIcon(IconUtils.loadIcon("/Exit.gif"));
		}
		return exitMenuItem;
	}

	protected void exit() {
		System.out.println("Exiting...");
		System.exit(0);

	}

	private javax.swing.JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new javax.swing.JMenu();
			helpMenu.add(getJMenuItem());
			helpMenu.setText("Help");
			helpMenu.setMnemonic(java.awt.event.KeyEvent.VK_H);
		}
		return helpMenu;
	}

	private javax.swing.JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new javax.swing.JMenuItem();
			jMenuItem.setText("About");
			jMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_A);
			jMenuItem.setIcon(IconUtils.loadIcon("/Inform.gif"));
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// showAboutDialog();
				}
			});
		}
		return jMenuItem;
	}

	/*
	 * private void showAboutDialog() { JOptionPane.showMessageDialog(this,
	 * this.conf.getAboutHTML(), "About: " + this.conf.getApplicationName(),
	 * JOptionPane.INFORMATION_MESSAGE); }
	 */
	private javax.swing.JToolBar getToolBar(List comps) {
		if (toolBar == null) {
			toolBar = new javax.swing.JToolBar("tools",
					SwingConstants.HORIZONTAL);
			for (int i = 0; i < comps.size(); i++) {
				Component comp = (Component) comps.get(i);
				toolBar.add(this.getComponentButton(comp));
			}
			toolBar.setName("Tools");
			toolBar.setRollover(true);
		}
		return toolBar;
	}

	public void addApplicationComponent(ApplicationComponent frame) {
		this.lastComp = frame;
		getMDIDesktopPane().add(frame);
	}

	public void addApplicationComponent(ApplicationComponent frame, int width,
			int height) {
		this.lastComp = frame;
		getMDIDesktopPane().add(frame, width, height);
	}

	public ApplicationComponent getLastComponent() {
		return this.lastComp;
	}

	public MDIDesktopPane getMDIDesktopPane() {
		if (desktop == null) {
			desktop = new MDIDesktopPane();
		}
		return desktop;
	}

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	protected void closeAllFrames() {
		JInternalFrame[] frameList = getMDIDesktopPane().getAllFrames();
		for (int i = 0; i < frameList.length; i++) {
			try {
				frameList[i].setClosed(true);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private javax.swing.JMenuItem getCloseMenuItem() {
		if (closeMenuItem == null) {
			closeMenuItem = new javax.swing.JMenuItem();
			closeMenuItem.setText("Close");
			closeMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_C);
			closeMenuItem.setIcon(IconUtils.loadIcon("/Minus.gif"));
			closeMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							closeFrame();
						}
					});
		}
		return closeMenuItem;
	}

	/**
	 * 
	 */
	protected void closeFrame() {
		JInternalFrame jif = getMDIDesktopPane().getSelectedFrame();
		if (jif != null) {
			try {
				jif.setClosed(true);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

	}

	/**
	 * This method initializes closeAllMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getCloseAllMenuItem() {
		if (closeAllMenuItem == null) {
			closeAllMenuItem = new javax.swing.JMenuItem();
			closeAllMenuItem.setText("Close All");
			closeAllMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_A);
			closeAllMenuItem.setIcon(IconUtils.loadIcon("/MultiMinus.gif"));
			closeAllMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							closeAllFrames();
						}
					});
		}
		return closeAllMenuItem;
	}

	static public class ExecuteComponent extends MobiusRunnable {
		private Component component;

		private GridApplication app;

		public ExecuteComponent(GridApplication app, Component comp) {
			this.component = comp;
			this.app = app;

		}

		public void execute() {
			try {
				ApplicationContext context = new ApplicationContext(app,
						component);
				Class[] inputTypes = new Class[1];
				inputTypes[0] = ApplicationContext.class;
				Constructor constructor = Class.forName(
						component.getClassname()).getConstructor(inputTypes);
				Object[] inputs = new Object[1];
				inputs[0] = context;
				ApplicationComponent comp = (ApplicationComponent) constructor
						.newInstance(inputs);
				if (component.getDimensions() != null) {
					app.addApplicationComponent(comp, component.getDimensions()
							.getWidth(), component.getDimensions().getHeight());
				} else {
					app.addApplicationComponent(comp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}