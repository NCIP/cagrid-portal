package gov.nih.nci.cagrid.data.ui.cacore.appservice;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/** 
 *  BaseAppserviceConfigPanel
 *  Base panel for configuration of an application service instance
 *  used by the caCORE SDK query processors
 * 
 * @author David Ervin
 * 
 * @created Mar 23, 2007 1:46:03 PM
 * @version $Id: BaseAppserviceConfigPanel.java,v 1.2 2007-04-10 14:30:30 hastings Exp $ 
 */
public abstract class BaseAppserviceConfigPanel extends JPanel {
    private ExtensionTypeExtensionData extensionData;
    private ServiceInformation serviceInfo;
    private List<AppserviceConfigCompletionListener> completionListeners;

    public BaseAppserviceConfigPanel(
        ExtensionTypeExtensionData extensionData, ServiceInformation serviceInfo) {
        this.extensionData = extensionData;
        this.serviceInfo = serviceInfo;
        this.completionListeners = new ArrayList<AppserviceConfigCompletionListener>();
    }
    
    
    public void addCompletionListener(AppserviceConfigCompletionListener listener) {
        completionListeners.add(listener);
    }
    
    
    public boolean removeCompletionListener(AppserviceConfigCompletionListener listener) {
        return completionListeners.remove(listener);
    }
    
    
    protected ExtensionTypeExtensionData getExtensionData() {
        return extensionData;
    }
    
    
    protected ServiceInformation getServiceInfo() {
        return serviceInfo;
    }
    
    
    protected void setConfigurationComplete(boolean complete) {
        for (AppserviceConfigCompletionListener listener : completionListeners) {
            listener.completionStatusChanged(complete);
        }
    }
}
