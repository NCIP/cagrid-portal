package gov.nih.nci.cagrid.syncgts.core;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.syncgts.bean.SyncReport;

import java.io.File;

import javax.xml.namespace.QName;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class HistoryManager {

	private final static QName reportQN = new QName("http://cagrid.nci.nih.gov/8/SyncGTS", "SyncReport");


	public File addReport(SyncReport report) throws Exception {
		File r = getFile(report.getTimestamp());
		Utils.serializeDocument(r.getAbsolutePath(), report, reportQN);
		return r;
	}


	private File getFile(String timestamp) {
		File dir = getDirectory(timestamp);
		return new File(dir.getAbsolutePath() + File.separator + timestamp + ".xml");
	}


	private File getDirectory(String timestamp) {
		String year = timestamp.substring(0, 4);
		String month = timestamp.substring(4, 6);
		String days = timestamp.substring(6, 8);
		File dir = new File(Utils.getCaGridUserHome() + File.separator + "syncgts-history" + File.separator + year
			+ File.separator + month + File.separator + days);
		dir.mkdirs();
		return dir;
	}

}
