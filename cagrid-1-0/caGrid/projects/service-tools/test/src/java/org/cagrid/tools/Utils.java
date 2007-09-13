package org.cagrid.tools;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.cagrid.tools.database.Database;
import org.cagrid.tools.database.DatabaseConfiguration;
import org.cagrid.tools.events.EventHandlerConfiguration;


public class Utils {

	private static final String DB = "test_tools";

	private static Database db = null;


	public static Database getDB() throws Exception {
		if (db == null) {
			InputStream resource = TestCase.class.getResourceAsStream(Constants.DB_CONFIG);
			DatabaseConfiguration conf = (DatabaseConfiguration) gov.nih.nci.cagrid.common.Utils.deserializeObject(
				new InputStreamReader(resource), DatabaseConfiguration.class);
			db = new Database(conf, DB);
			db.createDatabaseIfNeeded();
		}
		return db;
	}


	public static EventHandlerConfiguration getEventAuditorConfiguration() throws Exception {
		InputStream resource = TestCase.class.getResourceAsStream(Constants.EVENT_AUDITOR_CONFIG);
		EventHandlerConfiguration conf = (EventHandlerConfiguration) gov.nih.nci.cagrid.common.Utils.deserializeObject(
			new InputStreamReader(resource), EventHandlerConfiguration.class);
		return conf;
	}
}
