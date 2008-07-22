package org.cagrid.gaards.websso.utils;

import java.io.InputStream;
import java.io.InputStreamReader;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.cagrid.gaards.websso.beans.WebSSOServerInformation;


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
			WebSSOProperties webSSOProperties = (WebSSOProperties)ObjectFactory.getObject(WebSSOConstants.WEBSSO_PROPERTIES);
			WebSSOServerInformation webSSOServerInformation = webSSOProperties.getWebSSOServerInformation();

			if ("yes".equalsIgnoreCase(webSSOServerInformation.getStartAutoSyncGTS()))
			{
				FileHelper fileHelper = (FileHelper)ObjectFactory.getObject(WebSSOConstants.FILE_HELPER);
			
				//String pathToSyncDescription = fileHelper.getFileAsURL("sync-description.xml").getPath();
				
				InputStream fileInputStream = fileHelper.getFileAsStream("sync-description.xml");
				//Load Sync Description
				SyncDescription description = (SyncDescription) Utils.deserializeObject(new InputStreamReader(fileInputStream),SyncDescription.class);

				// Sync with the Trust Fabric Once
				SyncGTS.getInstance().syncAndResyncInBackground(description, false);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new ServletException ("Unable to Start Sync GTS Service.");
		}
		super.init(config);
	}
	
}
