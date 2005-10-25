package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.gums.portal.GUMSServiceListComboBox;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.projectmobius.portal.GridPortalComponent;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class FindUsersWindow extends GridPortalComponent {

	private JPanel jContentPane = null;
	private JPanel mainPanel = null;
	private JPanel infoPanel = null;
	private JLabel firstNameLabel = null;
	private JTextField firstName = null;
	private JLabel lastNameLabel = null;
	private JTextField lastName = null;
	private JLabel organizationLabel = null;
	private JTextField organization = null;
	private JLabel addressLabel = null;
	private JTextField address = null;
	private JLabel address2Label = null;
	private JTextField address2 = null;
	private JLabel cityLabel = null;
	private JTextField city = null;
	private JLabel stateLabel = null;
	private JComboBox state = null;
	private JLabel zipcodeLabel = null;
	private JTextField zipcode = null;
	private JLabel phoneNumberLabel = null;
	private JTextField phoneNumber = null;
	private JLabel emailLabel = null;
	private JTextField email = null;
	private JLabel countryLabel = null;
	private JComboBox country = null;
	private JLabel useridLabel = null;
	private JTextField username = null;
	private JPanel criteriaPanel = null;
	private JLabel Role = null;
	private JTabbedPane criteriaSelection = null;
	private JPanel loginPanel = null;
	private JLabel usernameLabel = null;
	private JTextField userId = null;
	private JLabel passwordLabel = null;
	private JLabel serviceLabel = null;
	private JPasswordField password = null;
	private JComboBox service = null;
	private JPanel queryButtonPanel = null;
	private JButton query = null;
	private JPanel jPanel = null;
	private JScrollPane jScrollPane = null;
	private UsersTable usersTable = null;
	/**
	 * This is the default constructor
	 */
	public FindUsersWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		//this.setSize(600, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("IdP User Application");
		this.setFrameIcon(IdPLookAndFeel.getApplicationIcon());
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
			jContentPane.add(getMainPanel(), java.awt.BorderLayout.NORTH);
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
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.gridx = 0;
			gridBagConstraints37.gridy = 2;
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.gridx = 0;
			gridBagConstraints36.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getCriteriaPanel(), gridBagConstraints1);
			mainPanel.add(getQueryButtonPanel(), gridBagConstraints36);
			mainPanel.add(getJPanel(), gridBagConstraints37);
			
		}
		return mainPanel;
	}

	/**
	 * This method initializes infoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.gridwidth = 1;
			gridBagConstraints3.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.weightx = 1.0D;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints.gridy = 0;
			useridLabel = new JLabel();
			useridLabel.setText("Username");
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints32.gridy = 5;
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints32.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints32.weighty = 0.0D;
			gridBagConstraints32.gridx = 3;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 2;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints31.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints31.gridy = 5;
			countryLabel = new JLabel();
			countryLabel.setText("Country");
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.gridy = 5;
			gridBagConstraints30.weightx = 1.0D;
			gridBagConstraints30.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints30.weighty = 0.0D;
			gridBagConstraints30.gridx = 1;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints29.gridy = 5;
			gridBagConstraints29.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints29.gridx = 0;
			emailLabel = new JLabel();
			emailLabel.setText("Email");
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints28.gridy = 4;
			gridBagConstraints28.weightx = 1.0D;
			gridBagConstraints28.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints28.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints28.weighty = 0.0D;
			gridBagConstraints28.gridx = 1;
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints27.gridy = 4;
			gridBagConstraints27.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints27.gridx = 0;
			phoneNumberLabel = new JLabel();
			phoneNumberLabel.setText("Phone Number");
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints26.gridy = 4;
			gridBagConstraints26.weightx = 1.0D;
			gridBagConstraints26.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints26.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints26.weighty = 0.0D;
			gridBagConstraints26.gridx = 3;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 2;
			gridBagConstraints25.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints25.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints25.gridy = 4;
			zipcodeLabel = new JLabel();
			zipcodeLabel.setText("Zipcode");
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints24.gridy = 3;
			gridBagConstraints24.weightx = 1.0D;
			gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints24.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints24.weighty = 0.0D;
			gridBagConstraints24.gridx = 3;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints23.gridy = 3;
			gridBagConstraints23.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints23.gridx = 2;
			stateLabel = new JLabel();
			stateLabel.setText("State");
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.gridy = 2;
			gridBagConstraints22.weightx = 1.0D;
			gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints22.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints22.weighty = 0.0D;
			gridBagConstraints22.gridx = 3;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 2;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints21.gridy = 2;
			cityLabel = new JLabel();
			cityLabel.setText("City");
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.gridy = 1;
			gridBagConstraints20.weightx = 1.0D;
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints20.weighty = 0.0D;
			gridBagConstraints20.gridx = 3;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints19.gridx = 2;
			gridBagConstraints19.gridy = 1;
			gridBagConstraints19.anchor = java.awt.GridBagConstraints.WEST;
			address2Label = new JLabel();
			address2Label.setText("Address2");
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.weightx = 1.0D;
			gridBagConstraints18.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints18.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints18.weighty = 0.0D;
			gridBagConstraints18.gridx = 3;
			organizationLabel = new JLabel();
			organizationLabel.setText("Organization");
	
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints17.gridx = 2;
			addressLabel = new JLabel();
			addressLabel.setText("Address");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 3;
			gridBagConstraints16.weightx = 1.0D;
			gridBagConstraints16.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.gridwidth = 1;
			gridBagConstraints16.weighty = 0.0D;
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints15.gridwidth = 1;
			gridBagConstraints15.gridy = 3;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.gridy = 2;
			gridBagConstraints14.weightx = 1.0D;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints14.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints14.weighty = 0.0D;
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 2;
			gridBagConstraints13.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints13.gridx = 0;
			lastNameLabel = new JLabel();
			lastNameLabel.setText("Last Name");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints12.weighty = 0.0D;
			gridBagConstraints12.weightx = 1.0D;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.insets = new java.awt.Insets(1,1,1,1);
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.gridx = 0;
			firstNameLabel = new JLabel();
			firstNameLabel.setText("First Name");
			infoPanel = new JPanel();
			infoPanel.setLayout(new GridBagLayout());
			
			
			infoPanel.setName("infoPanel");
			infoPanel.add(firstNameLabel, gridBagConstraints11);
			infoPanel.add(getFirstName(), gridBagConstraints12);
			infoPanel.add(lastNameLabel, gridBagConstraints13);
			infoPanel.add(getLastName(), gridBagConstraints14);
			infoPanel.add(organizationLabel, gridBagConstraints15);
			infoPanel.add(getOrganization(), gridBagConstraints16);
			infoPanel.add(addressLabel, gridBagConstraints17);
			infoPanel.add(getAddress(), gridBagConstraints18);
			infoPanel.add(address2Label, gridBagConstraints19);
			infoPanel.add(getAddress2(), gridBagConstraints20);
			infoPanel.add(cityLabel, gridBagConstraints21);
			infoPanel.add(getCity(), gridBagConstraints22);
			infoPanel.add(stateLabel, gridBagConstraints23);
			infoPanel.add(getState(), gridBagConstraints24);
			infoPanel.add(zipcodeLabel, gridBagConstraints25);
			infoPanel.add(getZipcode(), gridBagConstraints26);
			infoPanel.add(phoneNumberLabel, gridBagConstraints27);
			infoPanel.add(getPhoneNumber(), gridBagConstraints28);
			infoPanel.add(emailLabel, gridBagConstraints29);
			infoPanel.add(getEmail(), gridBagConstraints30);
			infoPanel.add(countryLabel, gridBagConstraints31);
			infoPanel.add(getCountry(), gridBagConstraints32);
			infoPanel.add(useridLabel, gridBagConstraints);
			infoPanel.add(getUsername(), gridBagConstraints3);
		}
		return infoPanel;
	}

	/**
	 * This method initializes firstName	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getFirstName() {
		if (firstName == null) {
			firstName = new JTextField();
		}
		return firstName;
	}

	/**
	 * This method initializes lastName	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getLastName() {
		if (lastName == null) {
			lastName = new JTextField();
		}
		return lastName;
	}

	/**
	 * This method initializes organization	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getOrganization() {
		if (organization == null) {
			organization = new JTextField();
		}
		return organization;
	}

	/**
	 * This method initializes address	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getAddress() {
		if (address == null) {
			address = new JTextField();
		}
		return address;
	}

	/**
	 * This method initializes address2	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getAddress2() {
		if (address2 == null) {
			address2 = new JTextField();
		}
		return address2;
	}

	/**
	 * This method initializes city	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getCity() {
		if (city == null) {
			city = new JTextField();
		}
		return city;
	}

	/**
	 * This method initializes state	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getState() {
		if (state == null) {
			state = new StateListComboBox();
		}
		return state;
	}

	/**
	 * This method initializes zipcode	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getZipcode() {
		if (zipcode == null) {
			zipcode = new JTextField();
		}
		return zipcode;
	}

	/**
	 * This method initializes phoneNumber	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getPhoneNumber() {
		if (phoneNumber == null) {
			phoneNumber = new JTextField();
		}
		return phoneNumber;
	}

	/**
	 * This method initializes email	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getEmail() {
		if (email == null) {
			email = new JTextField();
		}
		return email;
	}

	/**
	 * This method initializes country	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCountry() {
		if (country == null) {
			country = new CountryListComboBox();
		}
		return country;
	}

	/**
	 * This method initializes username	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getUsername() {
		if (username == null) {
			username = new JTextField();
		}
		return username;
	}

	/**
	 * This method initializes criteriaPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getCriteriaPanel() {
		if (criteriaPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.weighty = 0.0D;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			criteriaPanel = new JPanel();
			criteriaPanel.setLayout(new GridBagLayout());
			criteriaPanel.add(getCriteriaSelection(), gridBagConstraints4);
			criteriaPanel.add(getLoginPanel(), gridBagConstraints7);
		}
		return criteriaPanel;
	}

	/**
	 * This method initializes criteriaSelection	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */    
	private JTabbedPane getCriteriaSelection() {
		if (criteriaSelection == null) {
			criteriaSelection = new JTabbedPane();
			criteriaSelection.addTab("Personal Information", null, getInfoPanel(), null);
			criteriaSelection.setBorder(javax.swing.BorderFactory
					.createTitledBorder(
							null,
							"Search Criteria",
							javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
							javax.swing.border.TitledBorder.DEFAULT_POSITION,
							null, IdPLookAndFeel.getPanelLabelColor()));
		}
		return criteriaSelection;
	}

	/**
	 * This method initializes loginPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getLoginPanel() {
		if (loginPanel == null) {
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints35.gridy = 0;
			gridBagConstraints35.weightx = 1.0;
			gridBagConstraints35.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints35.gridwidth = 3;
			gridBagConstraints35.gridx = 1;
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints34.gridx = 3;
			gridBagConstraints34.gridy = 1;
			gridBagConstraints34.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints34.weightx = 1.0;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 2;
			gridBagConstraints33.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints33.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints33.gridy = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridwidth = 1;
			gridBagConstraints10.gridy = 0;
			serviceLabel = new JLabel();
			serviceLabel.setText("Service");
			passwordLabel = new JLabel();
			passwordLabel.setText("Password");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(2,2,2,2);
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.gridx = 0;
			usernameLabel = new JLabel();
			usernameLabel.setText("Username");
			loginPanel = new JPanel();
			loginPanel.setLayout(new GridBagLayout());
			loginPanel.add(usernameLabel, gridBagConstraints8);
			loginPanel.add(getUserId(), gridBagConstraints9);
			loginPanel.add(passwordLabel, gridBagConstraints33);
			loginPanel.add(serviceLabel, gridBagConstraints10);
			loginPanel.add(getPassword(), gridBagConstraints34);
			loginPanel.add(getService(), gridBagConstraints35);
			loginPanel.setBorder(javax.swing.BorderFactory
					.createTitledBorder(
							null,
							"Login Information",
							javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
							javax.swing.border.TitledBorder.DEFAULT_POSITION,
							null, IdPLookAndFeel.getPanelLabelColor()));
		}
		return loginPanel;
	}

	/**
	 * This method initializes userId	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getUserId() {
		if (userId == null) {
			userId = new JTextField();
		}
		return userId;
	}

	/**
	 * This method initializes password	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */    
	private JPasswordField getPassword() {
		if (password == null) {
			password = new JPasswordField();
		}
		return password;
	}

	/**
	 * This method initializes service	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getService() {
		if (service == null) {
			service = new GUMSServiceListComboBox();
		}
		return service;
	}

	/**
	 * This method initializes queryButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getQueryButtonPanel() {
		if (queryButtonPanel == null) {
			queryButtonPanel = new JPanel();
			queryButtonPanel.add(getQuery(), null);
		}
		return queryButtonPanel;
	}

	/**
	 * This method initializes query	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getQuery() {
		if (query == null) {
			query = new JButton();
			query.setText("Query");
		}
		return query;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.weightx = 1.0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setBorder(BorderFactory.createTitledBorder(null, "Users", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, GumsLookAndFeel.getPanelLabelColor()));
			jPanel.add(getJScrollPane(), gridBagConstraints2);
		}
		return jPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getUsersTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes usersTable	
	 * 	
	 * @return gov.nih.nci.cagrid.gums.idp.portal.UsersTable	
	 */    
	private UsersTable getUsersTable() {
		if (usersTable == null) {
			usersTable = new UsersTable();
		}
		return usersTable;
	}
}
