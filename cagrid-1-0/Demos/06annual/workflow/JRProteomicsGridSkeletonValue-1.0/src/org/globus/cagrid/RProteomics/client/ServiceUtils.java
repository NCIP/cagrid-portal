/*
 * Created on Mar 10, 2006
 */
package org.globus.cagrid.RProteomics.client;

import edu.duke.cabig.rproteomics.domain.serviceinterface.*;
import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileFilter;

public class ServiceUtils
{
	public static ScanFeaturesType[] readScanFeaturesFromDir(File dir) 
		throws Exception
	{
		  File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		  });
		  ScanFeaturesType[] scans = new ScanFeaturesType[files.length];
		  for (int i = 0; i < files.length; i++) {
			  scans[i] = readScanFeaturesFromFile(files[i]);
		  }
		  return scans;
	}
	
	public static ScanFeaturesType readScanFeaturesFromFile(File file)
		throws Exception
	{
		return (ScanFeaturesType) Utils.deserializeDocument(file.toString(), ScanFeaturesType.class);
	}
}
