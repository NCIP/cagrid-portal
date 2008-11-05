package gov.nih.nci.cagrid.introduce.portal.common;

import gov.nih.nci.cagrid.common.XMLUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
    
    private Thread th = null;
    
    private boolean cancel = false;


    /**
     * This method initializes
     */
    public LogPanel(String fileName) {
        super();
        this.fileName = fileName;
        initialize();
    }


    /**
     * This method initializes this
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
    
    public void cancel(){
        this.cancel = true;
    }


    /**
     * This method initializes logTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private synchronized JTextArea getLogTextArea() {
        if (logTextArea == null) {
            logTextArea = new JTextArea();
            logTextArea.setEditable(false);
            logTextArea.setFont(new Font("Lucida Console", Font.PLAIN, 10));
            // logTextArea.setLineWrap(true);
            try {
                final String contents = XMLUtilities.streamToString(new FileInputStream(new File(fileName)));
                logTextArea.insert(contents,0);
                logTextArea.setCaretPosition(contents.length());
                
                Runnable reader = new Runnable() {

                    public void run() {
                        try {
                            BufferedReader in = new BufferedReader(new FileReader(fileName));
                            in.skip(contents.length());
                            boolean execute = true;
                            String line;
                            while (execute) {
                                line = in.readLine();
                                if (line != null) {

                                    final String finalLine = line;
                                    final int oldLength = logTextArea.getText().length();

                                    logTextArea.insert(finalLine + "\n", oldLength + 1);
                                    logTextArea.setCaretPosition(oldLength + 1);

                                } else {
                                    try {
                                        Thread.sleep(500);
                                    } catch (Throwable t) {
                                        t.printStackTrace();
                                    }
                                }
                                
                                if(cancel){
                                    return;
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                };
                th = new Thread(reader);
                th.start();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            

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
