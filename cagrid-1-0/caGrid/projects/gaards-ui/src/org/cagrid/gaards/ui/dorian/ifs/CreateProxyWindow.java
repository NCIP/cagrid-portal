package org.cagrid.gaards.ui.dorian.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.xml.namespace.QName;

import org.cagrid.gaards.authentication.client.AuthenticationClient;
import org.cagrid.gaards.authentication.common.AuthenticationProfile;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.ProxyLifetime;
import org.cagrid.gaards.ui.common.CredentialManager;
import org.cagrid.gaards.ui.common.CredentialManagerComponent;
import org.cagrid.gaards.ui.common.GAARDSLookAndFeel;
import org.cagrid.gaards.ui.dorian.AuthenticationServiceHandle;
import org.cagrid.gaards.ui.dorian.DorianHandle;
import org.cagrid.gaards.ui.dorian.DorianLookAndFeel;
import org.cagrid.gaards.ui.dorian.DorianServiceListComboBox;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;
import org.globus.gsi.GlobusCredential;


public class CreateProxyWindow extends ApplicationComponent {

    private JPanel jContentPane = null;

    private JPanel mainPanel = null;

    private JPanel idpPanel = null;

    private JLabel idpLabel = null;

    private JComboBox identityProvider = null;

    private JPanel buttonPanel = null;

    private JPanel loginPanel = null;

    private JLabel ifsLabel = null;

    private DorianServiceListComboBox dorianService = null;

    private JButton authenticateButton = null;

    private JButton close = null;

    private JLabel lifetimeLabel = null;

    private JPanel lifetimePanel = null;

    private JComboBox hours = null;

    private JLabel hourLabel = null;

    private JComboBox minutes = null;

    private JLabel minutesLabel = null;

    private JComboBox seconds = null;

    private JLabel secondsLabel = null;

    private JPanel progressPanel = null;

    private JProgressBar progress = null;

    private boolean isCreating = false;

    private Object mutex = new Object();

    private CardLayout credentialLayout = null;
    
    private CredentialPanel currentCredentialPanel = null;
    
    private Map<QName, CredentialPanel> credentialPanels;

	private JPanel titlePanel = null;

	private JLabel icon = null;

	private JLabel jLabel = null;

    /**
     * This is the default constructor
     */
    public CreateProxyWindow() {
        super();
        this.credentialPanels = new Hashtable<QName, CredentialPanel>();
        initialize();
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        this.setContentPane(getJContentPane());
        this.setFrameIcon(DorianLookAndFeel.getCertificateIcon());
        this.setTitle("Login");
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
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.gridx = 0;
            gridBagConstraints17.gridy = 0;
            gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints17.weightx = 1.0D;
            gridBagConstraints17.anchor = GridBagConstraints.WEST;
            gridBagConstraints17.insets = new Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.weightx = 1.0D;
            gridBagConstraints3.gridy = 2;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.weighty = 1.0D;
            gridBagConstraints.gridy = 1;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getIdpPanel(), gridBagConstraints);
            mainPanel.add(getButtonPanel(), gridBagConstraints3);
            mainPanel.add(getTitlePanel(), gridBagConstraints17);
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
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.insets = new java.awt.Insets(3, 20, 3, 20);
            gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.gridwidth = 2;
            gridBagConstraints15.weightx = 1.0D;
            gridBagConstraints15.gridy = 4;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints8.weighty = 0.0D;
            gridBagConstraints8.weightx = 1.0D;
            gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints8.gridy = 2;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints7.gridy = 2;
            lifetimeLabel = new JLabel();
            lifetimeLabel.setText("Lifetime");
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints6.gridx = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints5.gridx = 0;
            ifsLabel = new JLabel();
            ifsLabel.setText("Authority");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridwidth = 2;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.weighty = 1.0D;
            gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints4.gridy = 3;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints2.weightx = 1.0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints1.gridx = 0;
            idpLabel = new JLabel();
            idpLabel.setText("Organization");
            idpPanel = new JPanel();
            idpPanel.setLayout(new GridBagLayout());
        
            //idpPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            /*
            idpPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, LookAndFeel.getPanelLabelColor()));
            */
            idpPanel.add(getProgressPanel(), gridBagConstraints15);
            idpPanel.add(idpLabel, gridBagConstraints1);
            idpPanel.add(getIdentityProvider(), gridBagConstraints2);
            idpPanel.add(getLoginPanel(), gridBagConstraints4);
            idpPanel.add(ifsLabel, gridBagConstraints5);
            idpPanel.add(getDorianService(), gridBagConstraints6);
            idpPanel.add(lifetimeLabel, gridBagConstraints7);
            idpPanel.add(getLifetimePanel(), gridBagConstraints8);
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
            identityProvider.addActionListener(new java.awt.event.ActionListener() {
            	public void actionPerformed(java.awt.event.ActionEvent e) {
            		showLoginInformation();
            	}
            });
        }
        return identityProvider;
    }
    
    private void showLoginInformation(){
    	AuthenticationServiceHandle handle = (AuthenticationServiceHandle)getIdentityProvider().getSelectedItem();
    	if(handle!=null){
    	Set<QName> profiles = handle.getAuthenticationProfiles();
    	if((profiles == null) || (profiles.size()<=0)){
    		this.credentialLayout.show(getLoginPanel(), profileToString(AuthenticationProfile.BASIC_AUTHENTICATION));
    		this.currentCredentialPanel = this.credentialPanels.get(AuthenticationProfile.BASIC_AUTHENTICATION);
    	}else{
    		QName profile = profiles.iterator().next();
    		this.credentialLayout.show(getLoginPanel(), profileToString(profile));
    		this.currentCredentialPanel = this.credentialPanels.get(profile);
    	}
    	}
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
     * This method initializes loginPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getLoginPanel() {
        if (loginPanel == null) {
            loginPanel = new JPanel();
            credentialLayout = new CardLayout();
            loginPanel.setLayout(credentialLayout);
            loginPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login Information",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, LookAndFeel.getPanelLabelColor()));
           
            CredentialPanel panel1 = new BasicAuthenticationPanel();
            CredentialPanel panel2 = new OneTimePasswordAuthenticationPanel();
            this.credentialPanels.put(AuthenticationProfile.BASIC_AUTHENTICATION, panel1);
            this.credentialPanels.put(AuthenticationProfile.ONE_TIME_PASSWORD, panel2);
         
            loginPanel.add(panel1,profileToString(AuthenticationProfile.BASIC_AUTHENTICATION));
            loginPanel.add(panel2, profileToString(AuthenticationProfile.ONE_TIME_PASSWORD));
        }
        return loginPanel;
    }


    /**
     * This method initializes dorianService
     * 
     * @return javax.swing.JComboBox
     */
    private DorianServiceListComboBox getDorianService() {
        if (dorianService == null) {
            dorianService = new DorianServiceListComboBox();
            dorianService.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    populateAuthenticationServices();
                }
            });
            populateAuthenticationServices();
        }
        return dorianService;
    }


    private void populateAuthenticationServices() {
        try {
            this.getIdentityProvider().removeAllItems();
            DorianHandle handle = getDorianService().getSelectedService();
            if (handle != null) {
                List<AuthenticationServiceHandle> providers = handle.getAuthenticationServices();
                if (providers != null) {
                    for (int i = 0; i < providers.size(); i++) {
                        this.getIdentityProvider().addItem(providers.get(i));
                    }
                }
            }
        } catch (Exception e) {
            FaultUtil.printFault(e);
        }
    }


    /**
     * This method initializes authenticateButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAuthenticateButton() {
        if (authenticateButton == null) {
            authenticateButton = new JButton();
            authenticateButton.setText("Login");
            authenticateButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Runner runner = new Runner() {
                        public void execute() {
                            authenticate();
                        }
                    };
                    try {
                        GridApplication.getContext().executeInBackground(runner);
                    } catch (Exception t) {
                        t.getMessage();
                    }
                }
            });
            //authenticateButton.setIcon(DorianLookAndFeel.getAuthenticateIcon());
        }
        return authenticateButton;
    }


    private void authenticate() {
        synchronized (mutex) {
            if (isCreating) {
                ErrorDialog.showError("Already trying to create a proxy, please wait!!!");
                return;
            } else {
                isCreating = true;
            }
        }
        // prevent clicking this button while working
        getAuthenticateButton().setEnabled(false);

        DorianHandle dorian = ((DorianServiceListComboBox) this.getDorianService()).getSelectedService();
        AuthenticationServiceHandle as = ((AuthenticationServiceHandle) getIdentityProvider().getSelectedItem());

        this.updateProgress(true, "Authenticating with IdP...");

        try {
            AuthenticationClient client = as.getAuthenticationClient();
            SAMLAssertion saml = client.authenticate(this.currentCredentialPanel.getCredential());
            this.updateProgress(true, "Creating Proxy...");
            GridUserClient c2 = dorian.getUserClient();
            ProxyLifetime lifetime = new ProxyLifetime();
            lifetime.setHours(Integer.valueOf((String) getHours().getSelectedItem()).intValue());
            lifetime.setMinutes(Integer.valueOf((String) getMinutes().getSelectedItem()).intValue());
            lifetime.setSeconds(Integer.valueOf((String) getSeconds().getSelectedItem()).intValue());
          
            GlobusCredential cred = c2.createProxy(saml, lifetime, 0);
            this.updateProgress(false, "Proxy Created!!!");
            CredentialManager.getInstance().addCredential(cred);
            GridApplication.getContext().addApplicationComponent(new CredentialManagerComponent(cred), 800, 500);

            // enable the authenticate button
            getAuthenticateButton().setEnabled(true);

            dispose();
        } catch (Throwable e) {
            this.updateProgress(false, "Error");
            ErrorDialog.showError(e);
            getAuthenticateButton().setEnabled(true);
        }

        isCreating = false;
    }


    private JButton getClose() {
        if (close == null) {
            close = new JButton();
            close.setText("Cancel");
            //close.setIcon(LookAndFeel.getCloseIcon());
            close.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return close;
    }


    /**
     * This method initializes lifetimePanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getLifetimePanel() {
        if (lifetimePanel == null) {
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 5;
            gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints14.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints14.gridy = 0;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 3;
            gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints13.gridy = 0;
            secondsLabel = new JLabel();
            secondsLabel.setText("sec");
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridx = 4;
            gridBagConstraints12.gridy = 0;
            gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints12.weightx = 1.0;
            minutesLabel = new JLabel();
            minutesLabel.setText("min");
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints11.gridx = 2;
            gridBagConstraints11.gridy = 0;
            gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints11.weightx = 1.0;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints10.gridy = 0;
            hourLabel = new JLabel();
            hourLabel.setText("hrs");
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints9.weightx = 1.0;
            lifetimePanel = new JPanel();
            lifetimePanel.setLayout(new GridBagLayout());
            lifetimePanel.add(getHours(), gridBagConstraints9);
            lifetimePanel.add(hourLabel, gridBagConstraints10);
            lifetimePanel.add(getMinutes(), gridBagConstraints11);
            lifetimePanel.add(minutesLabel, gridBagConstraints13);
            lifetimePanel.add(getSeconds(), gridBagConstraints12);
            lifetimePanel.add(secondsLabel, gridBagConstraints14);
        }
        return lifetimePanel;
    }


    /**
     * This method initializes hours
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getHours() {
        if (hours == null) {
            hours = new JComboBox();
            for (int i = 0; i < 23; i++) {
                hours.addItem(String.valueOf(i));
            }
            hours.setSelectedItem("12");
        }
        return hours;
    }


    /**
     * This method initializes minutes
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getMinutes() {
        if (minutes == null) {
            minutes = new JComboBox();
            for (int i = 0; i < 59; i++) {
                minutes.addItem(String.valueOf(i));
            }
        }
        return minutes;
    }


    /**
     * This method initializes seconds
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getSeconds() {
        if (seconds == null) {
            seconds = new JComboBox();
            for (int i = 0; i < 59; i++) {
                seconds.addItem(String.valueOf(i));
            }
        }
        return seconds;
    }


    /**
     * This method initializes progressPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getProgressPanel() {
        if (progressPanel == null) {
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints16.gridy = 0;
            gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints16.weightx = 1.0D;
            gridBagConstraints16.gridx = 0;
            progressPanel = new JPanel();
            progressPanel.setLayout(new GridBagLayout());
            progressPanel.add(getProgress(), gridBagConstraints16);
        }
        return progressPanel;
    }


    /**
     * This method initializes progress
     * 
     * @return javax.swing.JProgressBar
     */
    private JProgressBar getProgress() {
        if (progress == null) {
            progress = new JProgressBar();
            progress.setForeground(LookAndFeel.getPanelLabelColor());
            progress.setString("");
            progress.setStringPainted(true);
        }
        return progress;
    }


    public void updateProgress(final boolean working, final String s) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getProgress().setString(s);
                getProgress().setIndeterminate(working);
            }
        });

    }


    private String profileToString(QName profile){
    	return profile.getNamespaceURI()+":"+profile.getLocalPart();
    }


	/**
	 * This method initializes titlePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTitlePanel() {
		if (titlePanel == null) {
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.gridx = 1;
			gridBagConstraints19.gridy = 0;
			gridBagConstraints19.weightx = 1.0D;
			gridBagConstraints19.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints18.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Login");
			jLabel.setFont(new Font("Dialog", Font.BOLD, 14));
			icon = new JLabel(LookAndFeel.getLogoNoText22x22());
			titlePanel = new JPanel();
			titlePanel.setLayout(new GridBagLayout());
			titlePanel.add(icon, gridBagConstraints18);
			titlePanel.add(jLabel, gridBagConstraints19);
		}
		return titlePanel;
	}

}
