package gov.nih.nci.cagrid.gums.ifs.portal;


import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gums.client.IFSUserClient;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gums.portal.GUMSServiceListComboBox;
import gov.nih.nci.cagrid.gums.portal.GumsPortalConf;
import gov.nih.nci.cagrid.gums.portal.IdPConf;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.globus.gsi.GlobusCredential;
import org.globus.util.ConfigUtil;
import org.opensaml.SAMLAssertion;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

public class CreateProxyComponent extends GridPortalComponent {

	private JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel idpPanel = null;
	private JLabel idpLabel = null;
	private JComboBox identityProvider = null;
	private JPanel buttonPanel = null;
	private JPanel cardPanel = null;
	private JLabel ifsLabel = null;
	private JComboBox ifs = null;
	private JButton authenticateButton = null;
	private JButton close = null;
	private IdPAuthenticationPanel selected;
	private Map authPanels;

	/**
	 * This is the default constructor
	 */
	public CreateProxyComponent() {
		super();
		initialize();
		GumsPortalConf conf = (GumsPortalConf) PortalResourceManager
		.getInstance().getResource(GumsPortalConf.RESOURCE);
		authPanels = new HashMap();
		List idps = conf.getIdPs();
		for(int i=0; i<idps.size(); i++){
			try{
			IdPConf idp = (IdPConf)idps.get(i);
			Class c = Class.forName(idp.getAuthenticationPanelClass());
			Class[] params = new Class[1];
			params[0] = IdPConf.class;
			Constructor cons = c.getConstructor(params);
			Object[] args = new Object[1];
			args[0] = idp;
			IdPAuthenticationPanel panel = (IdPAuthenticationPanel)cons.newInstance(args);
			this.getCardPanel().add(panel,idp.getName());
			this.getIdentityProvider().addItem(idp);
			this.authPanels.put(idp.getName(),panel);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setFrameIcon(IFSLookAndFeel.getProxyIcon());
		this.setTitle("Create Proxy");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes mainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints3.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(5,5,5,5);
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getIdpPanel(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints3);
		}
		return mainPanel;
	}

	/**
	 * This method initializes idpPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getIdpPanel() {
		if (idpPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridx = 0;
			ifsLabel = new JLabel();
			ifsLabel.setText("Identity Federation Service");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.weightx = 1.0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 0;
			idpLabel = new JLabel();
			idpLabel.setText("Identity Provider");
			idpPanel = new JPanel();
			idpPanel.setLayout(new GridBagLayout());
			idpPanel.add(idpLabel, gridBagConstraints1);
			idpPanel.add(getIdentityProvider(), gridBagConstraints2);
			idpPanel.add(getCardPanel(), gridBagConstraints4);
			idpPanel.add(ifsLabel, gridBagConstraints5);
			idpPanel.add(getIfs(), gridBagConstraints6);
		}
		return idpPanel;
	}

	/**
	 * This method initializes identityProvider	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getIdentityProvider() {
		if (identityProvider == null) {
			identityProvider = new JComboBox();
			identityProvider.addItemListener(new java.awt.event.ItemListener() { 
				public void itemStateChanged(java.awt.event.ItemEvent e) {    
					 CardLayout cl = (CardLayout)(getCardPanel().getLayout());
					 if(getIdentityProvider().getSelectedItem() instanceof IdPConf){
					 IdPConf idp = (IdPConf)getIdentityProvider().getSelectedItem();
					 cl.show(getCardPanel(), idp.getName());
					
					 }
				}
			});
		}
		return identityProvider;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getAuthenticateButton(), null);
			buttonPanel.add(getClose(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes cardPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getCardPanel() {
		if (cardPanel == null) {
			cardPanel = new JPanel();
			cardPanel.setLayout(new CardLayout());
			cardPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "IdP Authentication Information",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IFSLookAndFeel.getPanelLabelColor()));	
		}
		return cardPanel;
	}

	/**
	 * This method initializes ifs	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getIfs() {
		if (ifs == null) {
			ifs = new GUMSServiceListComboBox();
		}
		return ifs;
	}

	/**
	 * This method initializes authenticateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getAuthenticateButton() {
		if (authenticateButton == null) {
			authenticateButton = new JButton();
			authenticateButton.setText("Authenticate");
			authenticateButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					authenticate();
				}
			});
			authenticateButton.setIcon(IFSLookAndFeel.getAuthenticateIcon());
		}
		return authenticateButton;
	}
	
	private void authenticate(){
		String ifsService =((GUMSServiceListComboBox)this.getIfs()).getSelectedService();
		String idpService = ((IdPConf)getIdentityProvider().getSelectedItem()).getName();
	    IdPAuthenticationPanel panel = (IdPAuthenticationPanel)authPanels.get(idpService);
	   
	    try{
	    	SAMLAssertion saml = panel.authenticate();
	    	IFSUserClient c2 = new IFSUserClient(ifsService);
			ProxyLifetime lifetime = new ProxyLifetime();
			lifetime.setSeconds(500);
			GlobusCredential cred = c2.createProxy(saml,lifetime);
			FileOutputStream fos = new FileOutputStream(ConfigUtil
					.discoverProxyLocation());
			cred.save(fos);
			fos.close();
	    }catch(Exception e){
	    	e.printStackTrace();
	    	PortalUtils.showErrorMessage("Error Creating Proxy", e);
	    }
	}

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
