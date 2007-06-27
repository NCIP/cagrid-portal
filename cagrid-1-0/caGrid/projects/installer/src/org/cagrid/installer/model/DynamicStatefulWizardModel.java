/**
 * 
 */
package org.cagrid.installer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;
import org.pietschy.wizard.models.DynamicModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DynamicStatefulWizardModel extends DynamicModel implements

		CaGridInstallerModel {
	
	private static final Log logger = LogFactory.getLog(DynamicStatefulWizardModel.class);

	private PropertyChangeEventProviderMap state;

	private ResourceBundle messages;

	/**
	 * 
	 */
	public DynamicStatefulWizardModel() {
		this(null, null);
	}

	public DynamicStatefulWizardModel(Map state) {
		this(state, null);
	}

	public DynamicStatefulWizardModel(Map state, ResourceBundle messages) {
		
		if (state == null) {
			this.state = new PropertyChangeEventProviderMap(new HashMap());
		}else{
			this.state = new PropertyChangeEventProviderMap(state);
		}
		this.messages = messages;
		if (this.messages == null) {
			// Load messages
			try {
				this.messages = ResourceBundle.getBundle(Constants.MESSAGES,
						Locale.US);
			} catch (Exception ex) {
				throw new RuntimeException("Error loading messages: "
						+ ex.getMessage());
			}
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l){
		super.addPropertyChangeListener(l);
		this.state.addPropertyChangeListener(l);
	}

	public Map<String, String> getState() {
		return state;
	}

	public String getMessage(String key) {
		String message = null;
		if (this.messages != null) {
			message = this.messages.getString(key);
		}
		return message;
	}

	private class PropertyChangeEventProviderMap extends HashMap {
		private PropertyChangeSupport pcs = new PropertyChangeSupport(
				DynamicStatefulWizardModel.this);

		PropertyChangeEventProviderMap(Map map) {
			super(map);
		}

		void addPropertyChangeListener(PropertyChangeListener l) {
			this.pcs.addPropertyChangeListener(l);
		}

		public Object put(Object key, Object newValue) {
			Object oldValue = get(key);
			if (oldValue != null) {
				this.pcs.firePropertyChange((String) oldValue, oldValue,
						newValue);
			}
			logger.info("Setting " + key + " = " + newValue);
			super.put(key, newValue);
			return oldValue;
		}
		public void putAll(Map m){
			for(Iterator i = m.entrySet().iterator(); i.hasNext();){
				Map.Entry entry = (Map.Entry)i.next();
				put(entry.getKey(), entry.getValue());
			}
		}
	}

}
