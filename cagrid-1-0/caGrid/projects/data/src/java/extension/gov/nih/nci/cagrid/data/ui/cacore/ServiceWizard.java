package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/** 
 *  ServiceWizard
 *  A wizard to simplify creation of a grid service model, which
 *  will later be processed by the Introduce toolkit
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 25, 2006 
 * @version $Id$ 
 */
public class ServiceWizard extends JDialog {

	private LinkedList panelSequence;
	private String baseTitle;
	private ListIterator panelSequenceIter;
	
	private Font stepFont = null;
	private JLabel stepLabel = null;
	private JLabel stepCurrentLabel = null;
	private JLabel stepOfLabel = null;
	private JLabel stepTotalLabel = null;
	private JPanel stepPanel = null;
	private JButton previousPanelButton = null;
	private JButton nextPanelButton = null;
	private JPanel buttonPanel = null;
	private JPanel controlPanel = null;
	private JPanel mainPanel = null;
	private JPanel wizardPanel = null;
	
	public ServiceWizard(String baseTitle) {
		super();
		this.panelSequence = new LinkedList();
		this.panelSequenceIter = null;
		this.stepFont = new Font("Dialog", java.awt.Font.ITALIC, 10);
		this.baseTitle = baseTitle;
		initialize();
	}
	
	
	public void addWizardPanel(AbstractWizardPanel panel) {
		if (isVisible()) {
			throw new IllegalStateException("Cannot add panels while showing wizard!");
		}
		panelSequence.add(panel);
	}
	
	
	public void insertWizardPanel(AbstractWizardPanel panel, int index) {
		if (isVisible()) {
			throw new IllegalStateException("Cannot add panels while showing wizard!");
		}
		panelSequence.add(index, panel);
	}
	
	
	public boolean removePanel(AbstractWizardPanel panel) {
		if (isVisible()) {
			throw new IllegalStateException("Cannot remove panels while showing wizard!");
		}
		return panelSequence.remove(panel);
	}
	
	
	public AbstractWizardPanel getPanelAt(int index) {
		return (AbstractWizardPanel) panelSequence.get(index);
	}
	
	
	public AbstractWizardPanel[] getWizardPanels() {
		AbstractWizardPanel[] arr = new AbstractWizardPanel[panelSequence.size()];
		panelSequence.toArray(arr);
		return arr;
	}
	
	
	public void showAt(int x, int y) {
		setLocation(x, y);
		// get labels and buttons set up
		panelSequenceIter = panelSequence.listIterator();
		loadWizardPanel((AbstractWizardPanel) panelSequence.getFirst());
		setVisible(true);
	}
	
	
	private void initialize() {
        this.setSize(new java.awt.Dimension(640, 325));
        this.setContentPane(getMainPanel());
        this.setTitle(baseTitle);
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getStepLabel() {
		if (stepLabel == null) {
			stepLabel = new JLabel();
			stepLabel.setText("Step:");
			stepLabel.setFont(stepFont);
		}
		return stepLabel;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getStepCurrentLabel() {
		if (stepCurrentLabel == null) {
			stepCurrentLabel = new JLabel();
			stepCurrentLabel.setText("x");
			stepCurrentLabel.setFont(stepFont);
		}
		return stepCurrentLabel;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getStepOfLabel() {
		if (stepOfLabel == null) {
			stepOfLabel = new JLabel();
			stepOfLabel.setText("of");
			stepOfLabel.setFont(stepFont);
		}
		return stepOfLabel;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getStepTotalLabel() {
		if (stepTotalLabel == null) {
			stepTotalLabel = new JLabel();
			stepTotalLabel.setText("y");
			stepTotalLabel.setFont(stepFont);
		}
		return stepTotalLabel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStepPanel() {
		if (stepPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.insets = new java.awt.Insets(2,0,2,2);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.insets = new java.awt.Insets(2,0,2,2);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new java.awt.Insets(2,0,2,2);
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			stepPanel = new JPanel();
			stepPanel.setLayout(new GridBagLayout());
			stepPanel.add(getStepLabel(), gridBagConstraints);
			stepPanel.add(getStepCurrentLabel(), gridBagConstraints1);
			stepPanel.add(getStepOfLabel(), gridBagConstraints2);
			stepPanel.add(getStepTotalLabel(), gridBagConstraints3);
		}
		return stepPanel;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPreviousPanelButton() {
		if (previousPanelButton == null) {
			previousPanelButton = new JButton();
			previousPanelButton.setText("Prev: ");
			previousPanelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (panelSequenceIter.hasPrevious()) {
						AbstractWizardPanel prevPanel = (AbstractWizardPanel) panelSequenceIter.previous();
						loadWizardPanel(prevPanel);
					}
				}
			});
		}
		return previousPanelButton;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNextPanelButton() {
		if (nextPanelButton == null) {
			nextPanelButton = new JButton();
			nextPanelButton.setText("Next: ");
			nextPanelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (panelSequenceIter.hasNext()) {
						AbstractWizardPanel nextPanel = (AbstractWizardPanel) panelSequenceIter.next();
						loadWizardPanel(nextPanel);
					}
				}
			});
		}
		return nextPanelButton;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			gridLayout.setHgap(4);
			gridLayout.setColumns(2);
			buttonPanel = new JPanel();
			buttonPanel.setLayout(gridLayout);
			buttonPanel.add(getPreviousPanelButton(), null);
			buttonPanel.add(getNextPanelButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getControlPanel() {
		if (controlPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints5.ipadx = 160;
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridy = 0;
			controlPanel = new JPanel();
			controlPanel.setLayout(new GridBagLayout());
			controlPanel.add(getStepPanel(), gridBagConstraints4);
			controlPanel.add(getButtonPanel(), gridBagConstraints5);
		}
		return controlPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.weighty = 1.0D;
			gridBagConstraints6.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getWizardPanel(), gridBagConstraints6);
			mainPanel.add(getControlPanel(), gridBagConstraints7);
		}
		return mainPanel;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWizardPanel() {
		if (wizardPanel == null) {
			wizardPanel = new JPanel(new GridBagLayout());
			wizardPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Panel Title", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		}
		return wizardPanel;
	}
	
	
	private void loadWizardPanel(AbstractWizardPanel panel) {
		// have the panel refresh itself
		panel.update();
		
		int currentStep = panelSequence.indexOf(panel);
		// set the current step
		getStepCurrentLabel().setText(String.valueOf(currentStep + 1));
		getStepTotalLabel().setText(String.valueOf(panelSequence.size()));
		
		// enable / disable previous and next buttons as needed
		getPreviousPanelButton().setEnabled(currentStep != 0);
		getNextPanelButton().setEnabled(currentStep != panelSequence.size() - 1);
		
		// set the text of the prev / next buttons
		if (currentStep < panelSequence.size() - 1) {
			AbstractWizardPanel nextPanel = (AbstractWizardPanel) panelSequence.get(currentStep + 1);
			getNextPanelButton().setText("Next: " + nextPanel.getPanelShortName());
		} else {
			// TODO: change this to "Done" && handle a clean exit of the wizard
			getNextPanelButton().setText("Next: ");
		}
		if (currentStep != 0) {
			AbstractWizardPanel prevPanel = (AbstractWizardPanel) panelSequence.get(currentStep - 1);
			getPreviousPanelButton().setText("Prev: " + prevPanel.getPanelShortName());
		} else {
			getPreviousPanelButton().setText("Prev: ");
		}
		
		// set the border text for the wizard component
		TitledBorder border = (TitledBorder) getWizardPanel().getBorder();
		border.setTitle(panel.getPanelShortName());
		
		// set the title of the dialog
		setTitle(baseTitle + ": " + panel.getPanelTitle());
		
		// load the panel into the wizard panel
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weightx = 1.0d;
		cons.weighty = 1.0d;
		cons.fill = GridBagConstraints.BOTH;
		while (getWizardPanel().getComponentCount() != 0) {
			getWizardPanel().remove(0);
		}
		getWizardPanel().add(panel, cons);
	}
}
