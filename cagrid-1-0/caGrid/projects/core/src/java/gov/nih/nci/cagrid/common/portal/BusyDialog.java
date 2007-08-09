package gov.nih.nci.cagrid.common.portal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class BusyDialog extends JDialog {

    private JPanel mainPanel = null;
    private JProgressBar progress = null;


    /**
     * This method initializes
     */
    public BusyDialog(JFrame owner, String title) {
        super(owner, title, true);
        initialize();
    }


    /**
     * This method initializes
     */
    public BusyDialog(JFrame owner, String title, JProgressBar progressBar) {
        super(owner, title, true);
        this.progress = progressBar;
        initialize();
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        this.setModal(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setContentPane(getMainPanel());
        this.setSize(new java.awt.Dimension(400, 100));
        // this.pack();
        PortalUtils.centerComponent(this);
    }


    /**
     * This method initializes mainPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (this.mainPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.weightx = 1.0D;
            this.mainPanel = new JPanel();
            this.mainPanel.setLayout(new GridBagLayout());
            this.mainPanel.add(getProgress(), gridBagConstraints4);
        }
        return this.mainPanel;
    }


    /**
     * This method initializes progress
     * 
     * @return javax.swing.JProgressBar
     */
    public JProgressBar getProgress() {
        if (this.progress == null) {
            this.progress = new JProgressBar();
            this.progress.setStringPainted(true);
            this.progress.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
            this.progress.setForeground(new java.awt.Color(153, 153, 255));
            this.progress.setString("");
        }
        return this.progress;
    }


    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(1600, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        BusyDialog d = new BusyDialog(frame, "test");

        d.setVisible(true);
        Thread.sleep(5000);
        d.getProgress().setString("foo");

    }
}
