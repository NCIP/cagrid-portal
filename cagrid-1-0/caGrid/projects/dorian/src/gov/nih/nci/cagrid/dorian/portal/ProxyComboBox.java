package gov.nih.nci.cagrid.gums.portal;

import java.util.List;

import javax.swing.JComboBox;

import org.globus.gsi.GlobusCredential;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class ProxyComboBox extends JComboBox {
	
	private static final String DEFAULT_PROXY = "Globus Default Proxy";

	public ProxyComboBox() {
		List creds = ProxyManager.getInstance().getProxies();
		addItem(new ProxyCaddy(DEFAULT_PROXY, null));
		for (int i = 0; i < creds.size(); i++) {
			addItem(new ProxyCaddy((GlobusCredential) creds.get(i)));
		}
	}
	
	public GlobusCredential getSelectedProxy(){
		return ((ProxyCaddy)this.getSelectedItem()).getProxy();
	}

}
