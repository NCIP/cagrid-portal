package gov.nih.nci.cagrid.gts.test;

import gov.nih.nci.cagrid.common.SimpleResourceManager;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.common.MySQLDatabase;
import gov.nih.nci.cagrid.gts.service.GTSConfiguration;

import java.io.InputStream;

import junit.framework.TestCase;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.db.ConnectionManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: Utils.java,v 1.5 2006-05-05 15:24:51 langella Exp $
 */
public class Utils {

	private static final String DB = "TEST_GTS";


	public static Database getDB() throws Exception {
		InputStream resource = TestCase.class.getResourceAsStream(GTSConstants.DB_CONFIG);
		Document doc = XMLUtilities.streamToDocument(resource);
		ConnectionManager cm = new ConnectionManager(doc.getRootElement());
		Database db = new MySQLDatabase(cm, DB);
		db.destroyDatabase();
		db.createDatabase();
		return db;
	}


	public static GTSConfiguration getGTSConfiguration() throws Exception {
		InputStream in = TestCase.class.getResourceAsStream(GTSConstants.GTS_CONFIG);
		SimpleResourceManager srm = new SimpleResourceManager(in);
		return (GTSConfiguration) srm.getResource(GTSConfiguration.RESOURCE);
	}

}
