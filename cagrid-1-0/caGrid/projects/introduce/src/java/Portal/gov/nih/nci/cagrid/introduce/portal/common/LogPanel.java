package gov.nih.nci.cagrid.introduce.portal.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class LogPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(LogPanel.class); // @jve:decl-index=0:

    private JTextArea logTextArea = null;

    private String fileName = null;

    private JScrollPane textScrollPane = null;


    /**
     * This method initializes
     * 
     */
    public LogPanel(String fileName) {
        super();
        this.fileName = fileName;
        initialize();
    }


    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        this.setLayout(new GridBagLayout());
        this.add(getTextScrollPane(), gridBagConstraints);

    }


    /**
     * This method initializes logTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextArea getLogTextArea() {
        if (logTextArea == null) {
            logTextArea = new JTextArea();
            logTextArea.setEditable(false);
            logTextArea.setFont(new Font("Lucida Console", Font.PLAIN, 10));
            logTextArea.setLineWrap(true);
            //logTextArea.setFont(logTextArea.getFont().deriveFont((float) 10));
            Thread th = new Thread(new Runnable() {

                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        BufferedReader in = new BufferedReader(new FileReader(fileName));
                        boolean execute = true;
                        String line;
                        while (execute) {
                            line = in.readLine();
                            final String finalLine = line;
                            if (line != null) {
                               
                                        int oldLength = logTextArea.getText().length();
                                        logTextArea.insert(finalLine + "\n", oldLength + 1);
                                        logTextArea.setCaretPosition(oldLength + 1);
                                
                                 
                                
                            } else {
                                try {
                                    Thread.sleep(500);
                                } catch (Throwable t) {
                                }
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            th.start();

        }
        return logTextArea;
    }


    /**
     * This method initializes textScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getTextScrollPane() {
        if (textScrollPane == null) {
            textScrollPane = new JScrollPane();
            textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            textScrollPane.setViewportView(getLogTextArea());
        }
        return textScrollPane;
    }

}
