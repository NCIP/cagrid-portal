package gov.nih.nci.cagrid.gums.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.globus.gsi.GlobusCredential;
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class ProxyManager {

	private static ProxyManager instance;
	private Map proxies;
	
	private ProxyManager(){
		this.proxies = new HashMap();
	}
	
	public static ProxyManager getInstance(){
		if(instance == null){
			instance = new ProxyManager();
		}
		return instance;
	}
	
	public void addProxy(GlobusCredential cred){
		proxies.put(cred.getIdentity(),cred);
	}
	
	public List getProxies(){
		List l = new ArrayList();
		Iterator itr = this.proxies.values().iterator();
		while(itr.hasNext()){
			l.add(itr.next());
		}
		return l;
	}

}
