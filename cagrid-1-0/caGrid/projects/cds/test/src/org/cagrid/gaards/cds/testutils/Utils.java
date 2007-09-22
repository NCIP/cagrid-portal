package org.cagrid.gaards.cds.testutils;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.cagrid.gaards.cds.conf.CDSConfiguration;
import org.cagrid.tools.database.Database;
import org.cagrid.tools.database.DatabaseConfiguration;

public class Utils {

	private static final String DB = "test_cds";

	private static Database db = null;

	public static CDSConfiguration getConfiguration() throws Exception {
		InputStream resource = TestCase.class
				.getResourceAsStream(Constants.CDS_CONF);
		CDSConfiguration conf = (CDSConfiguration) gov.nih.nci.cagrid.common.Utils
				.deserializeObject(new InputStreamReader(resource),
						CDSConfiguration.class);
		return conf;
	}

	public static Database getDB() throws Exception {
		if (db == null) {
			DatabaseConfiguration conf = getConfiguration()
					.getDatabaseConfiguration();
			db = new Database(conf, DB);
			db.createDatabaseIfNeeded();
		}
		return db;
	}
}
