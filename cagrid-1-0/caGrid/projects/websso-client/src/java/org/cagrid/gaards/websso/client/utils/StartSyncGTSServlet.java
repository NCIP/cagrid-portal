package org.cagrid.gaards.websso.client.utils;

import java.io.File;
import java.net.URI;
import java.net.URL;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class StartSyncGTSServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		try
		{
			URL url = Thread.currentThread().getContextClassLoader().getResource("sync-description.xml");		
            String pathToSyncDescription = url.getPath();
            URI uri = new URI(url.toString());
            pathToSyncDescription = new File(uri).getAbsolutePath();
    		System.out.println("PATH >>>>>>>>>>>>>>>" + pathToSyncDescription);
            SyncDescription description = (SyncDescription)Utils.deserializeDocument(pathToSyncDescription, SyncDescription.class);
    		System.out.println("Description >>>>>>>>>>>>>>>" );
    		SyncGTS.getInstance().syncAndResyncInBackground(description, false);
    		System.out.println("Start SyncGTS >>>>>>>>>>>>>>>" );    		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new ServletException ("Unable to Start Sync GTS Service.");
		}
		super.init(config);
	}
}
