package gov.nih.nci.cagrid.gts.test;

import gov.nih.nci.cagrid.gts.common.Database;

import java.io.InputStream;

import junit.framework.TestCase;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.db.ConnectionManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: Utils.java,v 1.3 2006-03-08 20:56:46 langella Exp $
 */
public class Utils {
	
	private static final String DB = "TEST_GTS";
	
	public static Database getDB() throws Exception{
		InputStream resource = TestCase.class.getResourceAsStream(GTSConstants.DB_CONFIG);
		Document doc = XMLUtilities.streamToDocument(resource);
		ConnectionManager cm = new ConnectionManager(doc.getRootElement());
		Database db = new Database(cm, DB);
		db.destroyDatabase();
		db.createDatabaseIfNeeded();	
		return db;
	}

}
