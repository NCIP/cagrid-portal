package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

/** 
 *  AbstractWizardPanel
 *  Base class for panels to be used by a service creation wizard
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 25, 2006 
 * @version $Id$ 
 */
public abstract class AbstractWizardPanel extends JPanel {
	private static Map bitBucket;

	private ServiceExtensionDescriptionType extDescription;
	private ServiceInformation serviceInfo;
	private List listeners;
	
	public AbstractWizardPanel(ServiceExtensionDescriptionType extensionDescription, ServiceInformation info) {
		this.extDescription = extensionDescription;
		this.serviceInfo = info;
		this.listeners = new LinkedList();
	}
	
	
	protected ServiceExtensionDescriptionType getExtensionDescription() {
		return extDescription;
	}
	
	
	protected ServiceInformation getServiceInformation() {
		return serviceInfo;
	}
	
	
	protected ExtensionTypeExtensionData getExtensionData() {
		return ExtensionTools.getExtensionData(extDescription, serviceInfo);
	}
	
	
	/**
	 * Gets a Map into which data can be stored for communicating with other panels
	 * in a sequence in a wizard.  This bit bucket is not persistant, so only use it for
	 * data which doesn't really belong in the service information or extension description.
	 * @return
	 */
	protected Map getBitBucket() {
		if (bitBucket == null) {
			bitBucket = new HashMap();
		}
		return bitBucket;
	}
	
	
	/**
	 * Enable or disable the wizard button which allows moving 
	 * to the next panel in the sequence.  When the panel is shown,
	 * the previous and next buttons will be enabled / disabled
	 * according to the panel's order in the panel sequence.  If the
	 * buttons state needs to be changed, do so in the update() method.
	 * @param enable
	 */
	protected void setNextEnabled(boolean enable) {
		Iterator i = listeners.iterator();
		while (i.hasNext()) {
			((ButtonEnableListener) i.next()).setNextEnabled(enable);
		}
	}
	
	
	/**
	 * Enable or disable the wizard button which allows moving
	 * to the previous panel in the sequence.  When the panel is shown,
	 * the previous and next buttons will be enabled / disabled
	 * according to the panel's order in the panel sequence.  If the
	 * buttons state needs to be changed, do so in the update() method.
	 * @param enable
	 */
	protected void setPrevEnabled(boolean enable) {
		Iterator i = listeners.iterator();
		while (i.hasNext()) {
			((ButtonEnableListener) i.next()).setPrevEnabled(enable);
		}
	}
	
	
	void addButtonEnableListener(ButtonEnableListener l) {
		listeners.add(l);
	}
	
	
	/**
	 * Called when the panel is shown in the wizard.  Use this call to perform any updates
	 * to the GUI needed by data changed in the extension data / bit bucket
	 */
	public abstract void update();
	
	
	/**
	 * Gets the descriptive title for this panel.
	 * @return
	 */
	public abstract String getPanelTitle();
	
	
	/**
	 * Gets the short name for this panel.  This name will be shown in the next / back buttons
	 * of the wizard
	 * @return
	 */
	public abstract String getPanelShortName();
}
