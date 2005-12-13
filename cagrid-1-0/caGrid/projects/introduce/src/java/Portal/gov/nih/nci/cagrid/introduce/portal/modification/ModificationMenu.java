package gov.nih.nci.cagrid.introduce.portal.modification;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ModificationMenu extends JMenuBar {

	private JMenu fileMenu = null;
	private JMenuItem load = null;
	/**
	 * This method initializes 
	 * 
	 */
	public ModificationMenu() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.add(getFileMenu());
			
	}

	/**
	 * This method initializes fileMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getLoad());
		}
		return fileMenu;
	}

	/**
	 * This method initializes load	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getLoad() {
		if (load == null) {
			load = new JMenuItem();
			load.setText("Load From Cache");
		}
		return load;
	}

}
