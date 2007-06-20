package gov.nih.nci.cagrid.common.portal;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public abstract class BusyDialogRunnable implements Runnable {

    final private BusyDialog dialog;

    private String errorMessage = "";

    private boolean valid = true;

    private JFrame owner;


    public BusyDialogRunnable(JFrame owner, String title) {
        this.owner = owner;
        this.dialog = new BusyDialog(owner, "Progress (" + title + ")");
        this.dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }


    public void setProgressText(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BusyDialogRunnable.this.dialog.getProgress().setString(text);
            }
        });
    }


    public void run() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                BusyDialogRunnable.this.dialog.setVisible(true);
            }
        });
        thread.start();
        try {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    BusyDialogRunnable.this.dialog.getProgress().setIndeterminate(true);
                }
            });
            process();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    BusyDialogRunnable.this.dialog.getProgress().setIndeterminate(false);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
            this.valid = false;
            this.errorMessage = e.getMessage();
        }

        if (!this.valid) {
            JOptionPane.showMessageDialog(this.owner, this.errorMessage);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BusyDialogRunnable.this.dialog.dispose();
            }
        });

    }


    public abstract void process();


    public String getErrorMessage() {
        return this.errorMessage;
    }


    public void setErrorMessage(String message) {
        this.valid = false;
        this.errorMessage = message;
    }

}
