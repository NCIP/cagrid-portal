package gov.nih.nci.cagrid.gums.ifs.portal;





import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: IFSMenu.java,v 1.2 2005-12-03 06:12:19 langella Exp $
 */
public class IFSMenu extends GridPortalComponent {

	private javax.swing.JPanel jContentPane = null;
	
	private JPanel mainPanel = null;
	private JPanel menuPanel = null;
	private JRadioButton createProxy = null;
	private JLabel createProxyLabel = null;
	private ButtonGroup group;
	private JPanel buttonPanel = null;
	private JButton perform = null;
	private JButton close = null;

	public IFSMenu() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400,200);
		this.setContentPane(getJContentPane());
		this.setFrameIcon(IFSLookAndFeel.getIFSIcon());
		this.setTitle("Identity Federation Menu");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			group = new ButtonGroup();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0D;
			gridBagConstraints9.weighty = 1.0D;
			gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 1;
			gridBagConstraints15.insets = new java.awt.Insets(10,10,10,10);
			mainPanel.add(getMenuPanel(), gridBagConstraints9);
			mainPanel.add(getButtonPanel(), gridBagConstraints15);
		}
		return mainPanel;
	}

	private JPanel getMenuPanel() {
		if (menuPanel == null) {
			createProxyLabel = new JLabel();
			menuPanel = new JPanel();
			menuPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		
			createProxyLabel.setText("Create Proxy");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			menuPanel.add(getRegister(), gridBagConstraints1);
			menuPanel.add(createProxyLabel, gridBagConstraints2);
			menuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Identity Federation Options",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IFSLookAndFeel.getPanelLabelColor()));	
			
		}
		return menuPanel;
	}

	
	private JRadioButton getRegister() {
		if (createProxy == null) {
			createProxy = new JRadioButton();
			group.add(createProxy);
		}
		return createProxy;
	}

	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getPerform(), null);
			buttonPanel.add(getClose(), null);
		}
		return buttonPanel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getPerform() {
		if (perform == null) {
			perform = new JButton();
			perform.setText("Select");
			perform.setIcon(IFSLookAndFeel.getSelectIcon());
			perform.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					perform();
				}
			});
		}
		return perform;
	}
	
	
	private void perform(){
		if(createProxy.isSelected()){
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new CreateProxyComponent(),450,300);
		}
		
	}
	
	
	
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getClose() {
		if (close == null) {
			close = new JButton();
			close.setText("Close");
			close.setIcon(IFSLookAndFeel.getCloseIcon());
			close.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					dispose();
				}
			});
		}
		return close;
	}
     }
