package gov.nih.nci.cagrid.syncgts.core;

import gov.nih.nci.cagrid.common.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import junit.framework.TestCase;

public class TestHistoryManager extends TestCase {

	private static final String zipDir = "test" + File.separator + "resources"
			+ File.separator + "history.zip";

	private static final String baseDir = "test" + File.separator + "resources";

	public void testSearch() {
		try {
			/*
			//test multiple months
			HistoryManager hm = new HistoryManager();
			hm.maxSyncReports = 150;
			DateFilter start = new DateFilter();
			start.setYear(2004);
			start.setMonth(1);
			start.setDay(1);
			DateFilter end = new DateFilter();
			end.setYear(2005);
			end.setMonth(12);
			end.setDay(31);
			SyncReport[] reports = hm.search(start, end, new File(baseDir + File.separator + "history"));
			assertEquals(reports.length, 116);
			
			//test multiple years
			end.setYear(2006);
			end.setMonth(4);
			end.setDay(1);
			reports = hm.search(start, end, new File(baseDir + File.separator + "history"));
			assertEquals(reports.length, 117);
			
			//test same date
			start.setYear(2006);
			start.setMonth(5);
			start.setDay(9);
			end.setYear(2006);
			end.setMonth(5);
			end.setDay(9);
			reports = hm.search(start, end, new File(baseDir + File.separator + "history"));
			assertEquals(reports.length, 53);
			
			//test reports.length == 0
			start.setYear(1990);
			start.setMonth(1);
			start.setDay(1);
			end.setYear(1992);
			end.setMonth(12);
			end.setDay(31);
			reports = hm.search(start, end, new File(baseDir + File.separator + "history"));
			assertEquals(reports.length, 0);
			
			
			//max should be reached with these dates
			start.setYear(2004);
			start.setMonth(1);
			start.setDay(1);
			end.setYear(2006);
			end.setMonth(12);
			end.setDay(31);
			try{
				reports = hm.search(start, end, new File(baseDir + File.separator + "history"));
				fail("Max SyncReports should have been reached");
			}catch (Exception e){
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
			fail("An error has occurred in testSearch");
		}
	}

	private static void unzip(String baseDir, ZipInputStream zin, String s)
			throws Exception {
		File file = new File(new File(baseDir).getAbsolutePath()
				+ File.separator + s);
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		byte[] b = new byte[512];
		int len = 0;
		while ((len = zin.read(b)) != -1) {
			out.write(b, 0, len);
		}
		out.close();
	}

	protected void setUp() throws Exception {
		super.setUp();
		InputStream in = new BufferedInputStream(new FileInputStream(zipDir));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry e;

		while ((e = zin.getNextEntry()) != null) {
			if (e.isDirectory()) {
				new File(new File(baseDir).getAbsolutePath() + File.separator
						+ e.getName()).mkdirs();
			} else {
				unzip(baseDir, zin, e.getName());
			}
		}
		zin.close();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if(!(Utils.deleteDir(new File(baseDir + File.separator + "history"))))
			fail("Unable to delete /test/resources/history");
	}

}
