package gov.nih.nci.cagrid.data.ui.wizard;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OneTimeInfoDialog extends JDialog {
    
    private String[] message = null;
    private Font messageFont = null;
    private JPanel messagePanel = null;
    private JPanel buttonPanel = null;
    private JCheckBox neverShowAgainCheckBox = null;
    private JButton okButton = null;

    private OneTimeInfoDialog(JFrame parent, String[] message) {
        super(parent);
        setTitle("Information");
        setModal(true);
        setAlwaysOnTop(true);
        setResizable(false);
        this.message = message;
        this.messageFont = new Font("Dialog", java.awt.Font.PLAIN, 12);
        initialize();
    }
    
    
    private void initialize() {
        // create a panel to hold the everything
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints messageCons = new GridBagConstraints();
        messageCons.gridx = 0;
        messageCons.gridy = 0;
        messageCons.fill = GridBagConstraints.BOTH;
        messageCons.weightx = 1.0D;
        messageCons.weighty = 1.0D;
        contentPanel.add(getMessagePanel(), messageCons);
        GridBagConstraints buttonCons = new GridBagConstraints();
        buttonCons.gridx = 0;
        buttonCons.gridy = 1;
        buttonCons.fill = GridBagConstraints.HORIZONTAL;
        buttonCons.weightx = 1.0D;
        contentPanel.add(getButtonPanel(), buttonCons);
        
        setContentPane(contentPanel);
        
        pack();
    }
    
    
    private JPanel getMessagePanel() {
        if (messagePanel == null) {
            messagePanel = new JPanel();
            // as many rows as it takes
            messagePanel.setLayout(new GridLayout(message.length, 1));
            for (String line : message) {
                JLabel label = new JLabel();
                label.setFont(messageFont);
                label.setText(line);
                messagePanel.add(label);
            }
        }
        return messagePanel;
    }
    
    
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            GridBagConstraints checkCons = new GridBagConstraints();
            checkCons.gridx = 0;
            checkCons.gridy = 0;
            checkCons.insets = new Insets(2,2,2,2);
            checkCons.anchor = GridBagConstraints.WEST;
            checkCons.fill = GridBagConstraints.HORIZONTAL;
            checkCons.weightx = 1.0D;
            buttonPanel.add(getNeverShowAgainCheckBox(), checkCons);
            GridBagConstraints buttonCons = new GridBagConstraints();
            buttonCons.gridx = 1;
            buttonCons.gridy = 0;
            buttonCons.insets = new Insets(2,2,2,2);
            buttonPanel.add(getOkButton(), buttonCons);
        }
        return buttonPanel;
    }
    
    
    private JCheckBox getNeverShowAgainCheckBox() {
        if (neverShowAgainCheckBox == null) {
            neverShowAgainCheckBox = new JCheckBox();
            neverShowAgainCheckBox.setText("Never show this message again");
        }
        return neverShowAgainCheckBox;
    }
    
    
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("OK");
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }
        return okButton;
    }
    
    
    public static boolean showDialog(JFrame parent, String[] message) {
        OneTimeInfoDialog dialog = new OneTimeInfoDialog(parent, message);
        if (parent != null) {
            dialog.setLocationRelativeTo(parent);
        }
        dialog.setVisible(true);
        boolean neverAgain = dialog.getNeverShowAgainCheckBox().isSelected();
        return neverAgain;
    }
}
