package gov.nih.nci.cagrid.syncgts.core;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.syncgts.bean.DateFilter;
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

	private final static QName reportQN = new QName(SyncGTSDefault.SYNC_GTS_NAMESPACE, "SyncReport");


	public File addReport(SyncReport report) throws Exception {
		File r = getFile(report.getTimestamp());
		Utils.serializeDocument(r.getAbsolutePath(), report, reportQN);
		return r;
	}


	public SyncReport getReport(String fileName) throws Exception {
		return (SyncReport) Utils.deserializeDocument(fileName, SyncReport.class);
	}


	public SyncReport[] search(DateFilter start, DateFilter end) throws Exception {
		File startDir = getDirectory(start);
		if((startDir.exists())&&(startDir.isDirectory())){
		
		}
		return null;
	}


	private File getFile(String timestamp) {
		File dir = getDirectory(timestamp);
		return new File(dir.getAbsolutePath() + File.separator + timestamp + ".xml");
	}


	private File getHistoryDirectory() {
		File dir = new File(SyncGTSDefault.getSyncGTSUserDir() + File.separator + "history");
		dir.mkdirs();
		return dir;
	}


	private File getDirectory(DateFilter f) {
		File histDir = getHistoryDirectory();
		File dir = new File(histDir.getAbsolutePath() + File.separator + f.getYear() + File.separator + f.getMonth()
			+ File.separator + f.getDay());
		return dir;
	}


	private File getDirectory(String timestamp) {
		String year = timestamp.substring(0, 4);
		String month = timestamp.substring(4, 6);
		String days = timestamp.substring(6, 8);
		File histDir = getHistoryDirectory();
		File dir = new File(histDir.getAbsolutePath() + File.separator + year + File.separator + month + File.separator
			+ days);
		dir.mkdirs();
		return dir;
	}

}
