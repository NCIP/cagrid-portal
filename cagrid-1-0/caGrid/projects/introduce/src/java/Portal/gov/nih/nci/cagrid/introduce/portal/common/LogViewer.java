package gov.nih.nci.cagrid.introduce.portal.common;

import org.cagrid.grape.ApplicationComponent;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class LogViewer extends ApplicationComponent {

    private LogPanel logPanel = null;

    /**
     * This method initializes 
     * 
     */
    public LogViewer() {
    	super();
    	initialize();
    	this.addInternalFrameListener(new InternalFrameListener() {
        
            public void internalFrameOpened(InternalFrameEvent e) {
                // TODO Auto-generated method stub
        
            }
        
        
            public void internalFrameIconified(InternalFrameEvent e) {
                // TODO Auto-generated method stub
        
            }
        
        
            public void internalFrameDeiconified(InternalFrameEvent e) {
                // TODO Auto-generated method stub
        
            }
        
        
            public void internalFrameDeactivated(InternalFrameEvent e) {
                // TODO Auto-generated method stub
        
            }
        
        
            public void internalFrameClosing(InternalFrameEvent e) {
                logPanel.cancel();
            }
        
        
            public void internalFrameClosed(InternalFrameEvent e) {
                // TODO Auto-generated method stub
        
            }
        
        
            public void internalFrameActivated(InternalFrameEvent e) {
                // TODO Auto-generated method stub
        
            }
        });
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
