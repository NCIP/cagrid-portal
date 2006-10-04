package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.introduce.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class GridGrouperURLManager {
	public final static String GRID_GROUPER_URL_PROPERTY = "Grid Grouper URLs";
	public final static String GRID_GROUPER_LOAD_PROPERTY = "Grid Grouper Load On Startup";
	public final static String DEFAULT_GRID_GROUPER_URL = "https://cagrid02.bmi.ohio-state.edu:8443/wsrf/services/cagrid/GridGrouper";
	public final static String DEFAULT_LOAD_ON_STARTUP = "true";

	public static boolean getLoadOnStartup() {
		boolean loadOnStartup = false;
		try {
			String load = ResourceManager.getStateProperty(GRID_GROUPER_LOAD_PROPERTY);
			if (load == null) {
				ResourceManager.setStateProperty(GRID_GROUPER_LOAD_PROPERTY, DEFAULT_LOAD_ON_STARTUP);
				load = ResourceManager.getStateProperty(GRID_GROUPER_LOAD_PROPERTY);
			}
			if (load.equalsIgnoreCase("true")) {
				loadOnStartup = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadOnStartup;
	}


	public static List getGridGroupers() {
		ArrayList groupers = new ArrayList();
		try {
			String urls = ResourceManager.getServiceURLProperty(GRID_GROUPER_URL_PROPERTY);
			if (urls == null) {
				ResourceManager.setServiceURLProperty(GRID_GROUPER_URL_PROPERTY, DEFAULT_GRID_GROUPER_URL);
				urls = ResourceManager.getServiceURLProperty(GRID_GROUPER_URL_PROPERTY);
			}
			StringTokenizer st = new StringTokenizer(urls, ";");
			while (st.hasMoreElements()) {
				groupers.add(st.nextToken());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupers;
	}
}
