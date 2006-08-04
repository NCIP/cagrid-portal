package gov.nih.nci.cagrid.gridca.portal;

import gov.nih.nci.cagrid.common.security.ProxyUtil;

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

	private static final String NO_PROXY = "None";

	public ProxyComboBox() {
		this(false);
	}

	public ProxyComboBox(boolean none) {
		List creds = ProxyManager.getInstance().getProxies();
		if (none) {
			addItem(new ProxyCaddy(NO_PROXY, null));
		}
		addItem(new ProxyCaddy(DEFAULT_PROXY, null));
		for (int i = 0; i < creds.size(); i++) {
			addItem(new ProxyCaddy((GlobusCredential) creds.get(i)));
		}
	}

	public ProxyComboBox(GlobusCredential cred) {
		this(false);
		this.setSelectedItem(new ProxyCaddy(cred));
	}

	public ProxyComboBox(GlobusCredential cred, boolean none) {
		this(none);
		this.setSelectedItem(new ProxyCaddy(cred));
	}

	public ProxyCaddy getSelectedProxyCaddy() {
		ProxyCaddy caddy = ((ProxyCaddy) this.getSelectedItem());
		return caddy;
	}

	public GlobusCredential getSelectedProxy() throws Exception {
		ProxyCaddy caddy = ((ProxyCaddy) this.getSelectedItem());
		if (caddy.getIdentity().equals(DEFAULT_PROXY)) {
			try {
				caddy.setProxy(ProxyUtil.getDefaultProxy());
			} catch (Exception e) {
				throw new Exception("No default proxy found!!!");
			}
			if (caddy.getProxy().getTimeLeft() == 0) {
				throw new Exception("The default proxy has expired!!!");
			}
		}
		return caddy.getProxy();
	}

}
