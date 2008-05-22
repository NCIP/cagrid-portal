package gov.nih.nci.cagrid.introduce.portal.common;

import org.cagrid.grape.ApplicationComponent;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.File;

public class LogViewer extends ApplicationComponent {

    private LogPanel logPanel = null;

    /**
     * This method initializes 
     * 
     */
    public LogViewer() {
    	super();
    	initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setContentPane(getLogPanel());
    		
    }

    /**
     * This method initializes logPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private LogPanel getLogPanel() {
        if (logPanel == null) {
            logPanel = new LogPanel("log" + File.separator + "introduce.log");
        }
        return logPanel;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
