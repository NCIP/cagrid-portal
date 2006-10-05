package gov.nih.nci.cagrid.syncgts.core;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.syncgts.bean.DateFilter;
import gov.nih.nci.cagrid.syncgts.bean.SyncReport;

import java.io.File;
import java.io.FileReader;

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
	public static int maxSyncReports = 150;


	public File addReport(SyncReport report) throws Exception {
		File r = getFile(report.getTimestamp());
		Utils.serializeDocument(r.getAbsolutePath(), report, reportQN);
		return r;
	}


	public SyncReport getLastReport() throws Exception{
		File dir = getLastestDayDir();
		if (dir != null) {
			File latest = null;
			int num = -1;
			File files[] = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					
						int index = files[i].getName().indexOf(".xml");
						if (index != -1) {
							try {
							int temp = Integer.valueOf(files[i].getName().substring(0,index)).intValue();
							if (temp > num) {
								num = temp;
								latest = files[i];
							}
							} catch (NumberFormatException e) {

							}
						}
					
				}
			}
			if(latest != null){
				return getReport(latest.getAbsolutePath());
			}

		}
		return null;
	}


	private File getLastestDayDir() {
		return getLatestDir(getLatestMonthDir());
	}


	private File getLatestMonthDir() {
		return getLatestDir(getLatestYearDir());
	}


	private File getLatestYearDir() {
		return getLatestDir(getHistoryDirectory());
	}


	private File getLatestDir(File dir) {
		File latest = null;
		int num = -1;
		if (dir != null) {
			if (dir.exists() && dir.isDirectory()) {
				File[] dirs = dir.listFiles();
				for (int i = 0; i < dirs.length; i++) {
					if (dirs[i].isDirectory()) {
						try {
							int temp = Integer.valueOf(dirs[i].getName()).intValue();
							if (temp > num) {
								num = temp;
								latest = dirs[i];
							}
						} catch (NumberFormatException e) {

						}
					}
				}

			}
		}
		return latest;
	}


	public SyncReport getReport(String fileName) throws Exception {
		return (SyncReport) Utils.deserializeDocument(fileName, SyncReport.class);
	}


	public SyncReport[] search(DateFilter startDate, DateFilter end) throws Exception {
		DateFilter start = new DateFilter();
		start.setDay(startDate.getDay());
		start.setMonth(startDate.getMonth());
		start.setYear(startDate.getYear());
		SyncReport[] reports = new SyncReport[maxSyncReports];
		int checkMax = 0;
		int iterator = 0;
		this.incrementDate(end);
		while (!start.equals(end)) {
			File startDir = getDirectory(start);
			if ((startDir.exists()) && (startDir.isDirectory())) {
				String[] fileList = startDir.list();
				checkMax = checkMax + fileList.length;
				if (checkMax > maxSyncReports)
					throw new Exception();
				else {
					for (int i = 0; i < fileList.length; i++) {
						File inputFile = new File(startDir.getAbsolutePath() + File.separator + fileList[i]);
						FileReader in = new FileReader(inputFile);
						if (in.read() != -1) {
							reports[iterator] = this.getReport(startDir.getAbsolutePath() + File.separator
								+ fileList[i]);
							iterator++;
						} else
							throw new Exception();
						in.close();
					}
				}
			}
			this.incrementDate(start);
		}
		SyncReport[] returnReports = new SyncReport[checkMax];
		System.arraycopy(reports, 0, returnReports, 0, returnReports.length);

		return returnReports;
	}


	public SyncReport[] search(DateFilter startDate, DateFilter end, File histDir) throws Exception {
		DateFilter start = new DateFilter();
		start.setDay(startDate.getDay());
		start.setMonth(startDate.getMonth());
		start.setYear(startDate.getYear());
		SyncReport[] reports = new SyncReport[maxSyncReports];
		int checkMax = 0;
		int iterator = 0;
		this.incrementDate(end);
		while (!start.equals(end)) {
			File startDir = getDirectory(start, histDir);
			if ((startDir.exists()) && (startDir.isDirectory())) {
				String[] fileList = startDir.list();
				checkMax = checkMax + fileList.length;
				if (checkMax > maxSyncReports)
					throw new Exception();
				else {
					for (int i = 0; i < fileList.length; i++) {
						File inputFile = new File(startDir.getAbsolutePath() + File.separator + fileList[i]);
						FileReader in = new FileReader(inputFile);
						if (in.read() != -1) {
							reports[iterator] = this.getReport(startDir.getAbsolutePath() + File.separator
								+ fileList[i]);
							iterator++;
						} else
							throw new Exception();
						in.close();
					}
				}
			}
			this.incrementDate(start);
		}
		SyncReport[] returnReports = new SyncReport[checkMax];
		System.arraycopy(reports, 0, returnReports, 0, returnReports.length);

		return returnReports;
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
		String month;
		String day;
		if (f.getMonth() < 10) {
			month = "0" + f.getMonth();
		} else
			month = "" + f.getMonth();

		if (f.getDay() < 10) {
			day = "0" + f.getDay();
		} else
			day = "" + f.getDay();

		File dir = new File(histDir.getAbsolutePath() + File.separator + f.getYear() + File.separator + month
			+ File.separator + day);
		return dir;
	}


	private File getDirectory(DateFilter f, File histDir) {
		String month;
		String day;
		if (f.getMonth() < 10) {
			month = "0" + f.getMonth();
		} else
			month = "" + f.getMonth();

		if (f.getDay() < 10) {
			day = "0" + f.getDay();
		} else
			day = "" + f.getDay();

		File dir = new File(histDir.getAbsolutePath() + File.separator + f.getYear() + File.separator + month
			+ File.separator + day);
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


	private void incrementDate(DateFilter f) {
		if (f.getDay() >= 31) {
			f.setDay(1);
			if (f.getMonth() >= 12) {
				f.setMonth(1);
				f.setYear(f.getYear() + 1);
			} else {
				f.setMonth(f.getMonth() + 1);
			}
		} else
			f.setDay(f.getDay() + 1);

	}

}
