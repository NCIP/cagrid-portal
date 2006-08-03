package gov.nih.nci.cagrid.cadsr.service;

import gov.nih.nci.cadsr.umlproject.domain.Project;

import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * @author osterl
 */
public class ProjectCache {
	private static final String PROJECT_CACHE = "projectCache";
	private CacheManager manager;


	public ProjectCache() throws CacheException {
		URL url = getClass().getResource("project.ehcache.xml");
		manager = CacheManager.create(url);
		manager.addCache(PROJECT_CACHE);
	}


	public void cacheProject(Project p) {
		String key = "NAME:" + p.getShortName() + "VERSION:" + p.getVersion();
		getCache().put(new Element(key, p));
	}


	public Project getProject(String projectShortName, String projectVersion) {
		String key = "NAME:" + projectShortName + "VERSION:" + projectVersion;
		try {
			Element element = getCache().get(key);
			return (element != null ? (Project) element.getValue() : null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private Cache getCache() {
		return this.manager.getCache(PROJECT_CACHE);
	}

}
