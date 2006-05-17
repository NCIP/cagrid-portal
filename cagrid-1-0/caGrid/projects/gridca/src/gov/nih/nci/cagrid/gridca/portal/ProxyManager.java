package gov.nih.nci.cagrid.gridca.portal;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.security.ProxyUtil;

import java.io.File;
import java.io.FileFilter;
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

	private File proxyDir;

	private long lastId = 0;

	private Map proxiesToFile;


	private ProxyManager() {
		this.proxies = new HashMap();
		this.proxiesToFile = new HashMap();
		String dir = Utils.getCaGridUserHome() + File.separator + File.separator + "proxy";
		proxyDir = new File(dir);
		proxyDir.mkdirs();
		FileFilter ff = new ProxyFilter();
		File list[] = proxyDir.listFiles(ff);
		for (int i = 0; i < list.length; i++) {
			try {
				long fileId = getFileId(list[i]);
				if (fileId > lastId) {
					lastId = fileId;
				}

				GlobusCredential cred = ProxyUtil.loadProxy(list[i].getAbsolutePath());
				if (cred.getTimeLeft() == 0) {
					list[i].delete();
				} else {
					proxies.put(cred.getIdentity(), cred);
					proxiesToFile.put(cred.getIdentity(), list[i]);
				}

			} catch (Exception e) {
				list[i].delete();
				e.printStackTrace();
			}
		}
	}


	private long getFileId(File f) throws Exception {
		String name = f.getName();
		int index = name.indexOf(".proxy");
		String sid = name.substring(0, index);
		return Long.valueOf(sid).longValue();
	}


	public class ProxyFilter implements FileFilter {

		public boolean accept(File pathname) {
			String name = pathname.getName();
			if (name.endsWith(".proxy")) {
				int index = name.indexOf(".proxy");
				String sid = name.substring(0, index);
				try {
					Long.valueOf(sid).longValue();
					return true;
				} catch (Exception e) {
					return false;
				}
			}
			return false;
		}

	}


	public static ProxyManager getInstance() {
		if (instance == null) {
			instance = new ProxyManager();
		}
		return instance;
	}


	public synchronized void addProxy(GlobusCredential cred) throws Exception {
		proxies.put(cred.getIdentity(), cred);
		lastId = lastId + 1;
		File f = new File(proxyDir.getAbsolutePath() + File.separator + lastId + ".proxy");
		ProxyUtil.saveProxy(cred, f.getAbsolutePath());
		proxiesToFile.put(cred.getIdentity(), f);
	}


	public synchronized void deleteProxy(GlobusCredential cred) {
		proxies.remove(cred.getIdentity());
		File f = (File) proxiesToFile.get(cred.getIdentity());
		f.delete();
		proxiesToFile.remove(cred.getIdentity());
	}


	public synchronized List getProxies() {
		List l = new ArrayList();
		Iterator itr = this.proxies.values().iterator();
		while (itr.hasNext()) {
			GlobusCredential cred = (GlobusCredential) itr.next();
			if (cred.getTimeLeft() == 0) {
				deleteProxy(cred);
			} else {
				l.add(cred);
			}
		}
		return l;
	}

}
