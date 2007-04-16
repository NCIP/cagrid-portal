package gov.nih.nci.cagrid.introduce.portal.modification.upgrade;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.upgrade.common.UpgradeStatus;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class UpgradeStatusView extends JDialog {

    public static final int PROCEED = 1;
    public static final int ROLL_BACK = 2;
    public static final int CANCEL = 3;

    private UpgradeStatus status = null;
    private JPanel mainPanel = null;
    private JPanel buttonPanel = null;
    private JPanel statusPanel = null;
    private JTextArea statusTextArea = null;
    private JButton proceedButton = null;
    private JButton rollBackButton = null;
    private JButton editButton = null;
    private JScrollPane statusScrollPane = null;

    private int result = -1;


    /**
     * This method initializes
     */
    public UpgradeStatusView(UpgradeStatus status) {
        super();
        this.status = status;
        this.setModal(true);
        initialize();
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(new Dimension(500, 600));
        this.setTitle("Upgrade Status Report");
        this.setContentPane(getMainPanel());

    }


    /**
     * This method initializes mainPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.weighty = 1.0D;
            gridBagConstraints1.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.weighty = 0.0D;
            gridBagConstraints.gridx = 0;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getButtonPanel(), gridBagConstraints);
            mainPanel.add(getStatusPanel(), gridBagConstraints1);
        }
        return mainPanel;
    }


    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.gridy = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 2;
            gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints4.gridy = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints3.gridy = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getProceedButton(), gridBagConstraints3);
            buttonPanel.add(getRollBackButton(), gridBagConstraints4);
            buttonPanel.add(getEditButton(), gridBagConstraints5);
        }
        return buttonPanel;
    }


    /**
     * This method initializes statusPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getStatusPanel() {
        if (statusPanel == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            gridBagConstraints6.weighty = 1.0;
            gridBagConstraints6.weightx = 1.0;
            statusPanel = new JPanel();
            statusPanel.setLayout(new GridBagLayout());
            statusPanel.add(getStatusScrollPane(), gridBagConstraints6);
        }
        return statusPanel;
    }


    /**
     * This method initializes statusTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getStatusTextArea() {
        if (statusTextArea == null) {
            statusTextArea = new JTextArea();
            statusTextArea.setEditable(false);
            statusTextArea.setText(status.toString());
        }
        return statusTextArea;
    }


    /**
     * This method initializes proceedButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getProceedButton() {
        if (proceedButton == null) {
            proceedButton = new JButton();
            proceedButton.setText("Proceed");
            proceedButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    result = UpgradeStatusView.PROCEED;
                    dispose();

                }

            });
        }
        return proceedButton;
    }


    /**
     * This method initializes rollBackButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getRollBackButton() {
        if (rollBackButton == null) {
            rollBackButton = new JButton();
            rollBackButton.setText("Roll Back");
            rollBackButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    result = UpgradeStatusView.ROLL_BACK;
                    dispose();

                }

            });
        }
        return rollBackButton;
    }


    /**
     * This method initializes editButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getEditButton() {
        if (editButton == null) {
            editButton = new JButton();
            editButton.setText("Edit");
            editButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    result = UpgradeStatusView.CANCEL;
                    dispose();

                }

            });
        }
        return editButton;
    }


    /**
     * This method initializes statusScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getStatusScrollPane() {
        if (statusScrollPane == null) {
            statusScrollPane = new JScrollPane();
            statusScrollPane.setViewportView(getStatusTextArea());
        }
        return statusScrollPane;
    }


    public int getResult() {
        return this.result;
    }


    public static int showUpgradeStatusView(UpgradeStatus status) {
        UpgradeStatusView view = new UpgradeStatusView( status);
        PortalUtils.centerWindow(view);
        view.setVisible(true);
        return view.getResult();
    }


    public static void main(String[] args) {
        int result = UpgradeStatusView.showUpgradeStatusView(new UpgradeStatus());
        System.out.println("result: " + result);
    }

} // @jve:decl-index=0:visual-constraint="10,10"
