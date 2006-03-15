/*
 * Created on Feb 24, 2006
 */
package org.globus.cagrid.RProteomics.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axis.encoding.Base64;

import edu.duke.cabig.rproteomics.domain.serviceinterface.*;


public class AnnualMeetingWorkflowDemo
{
	public static String getDuration(Date start, Date end)
	{
		return String.valueOf(Math.round(
			((double) (end.getTime() - start.getTime())) / 1000.0
		));
	}
	
	public static void main(String[] args)
		throws Exception
	{
		// some parameters
		String analysisEndpoint = "http://ccis1716.duhs.duke.edu/wsrf/services/cagrid/RProteomics";
		//String analysisEndpoint = "http://localhost:8080/wsrf/services/cagrid/RProteomics";
		File imageFile = new File("plot.jpg");
		
		// some helper objects
		Date start = null;
		Date end = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
		
		// construct client
		RProteomicsClient client = new RProteomicsClient(analysisEndpoint);
		
		// preprocessing: read data from file
		ScanFeaturesType[] startScans = ServiceUtils.readScanFeaturesFromDir(new File("data"));
		
		// step 3: interpolate
		start = new Date();
		System.out.println("Running general_interpolate at " + dateFormat.format(start));
		ScanFeaturesType[] scans = client.general_interpolateByValue(startScans);
		end = new Date();
		System.out.println("  completed at " + dateFormat.format(end) + " in " + getDuration(start, end) + " second(s)");
		
//		// step 4: determine background
//		start = new Date();
//		System.out.println("Running removeBackground_runningQuantile at " + dateFormat.format(start));
//		ScanFeaturesType[] bgScans = client.removeBackground_runningQuantileByValue(
//			scans, new WindowType(1023), new PercentileType(75)
//		);
//		end = new Date();
//		System.out.println("  completed at " + dateFormat.format(end) + " in " + getDuration(start, end) + " second(s)");
//		
//		// step 5: subtract background
//		start = new Date();
//		System.out.println("Running removeBackground_minus at " + dateFormat.format(start));
//		scans = client.removeBackground_minusByValue(scans, bgScans);
//		end = new Date();
//		System.out.println("  completed at " + dateFormat.format(end) + " in " + getDuration(start, end) + " second(s)");
//
//		// step 6: denoise
//		start = new Date();
//		System.out.println("Running denoise_waveletUDWTW at " + dateFormat.format(start));
//		ThresholdType threshold = new ThresholdType();
//		threshold.setMultiplier(new Double(1));
//		scans = client.denoise_waveletUDWTWByValue(scans, new WindowType(1024), threshold);
//		end = new Date();
//		System.out.println("  completed at " + dateFormat.format(end) + " in " + getDuration(start, end) + " second(s)");
//		
//		// step 7: align
//		start = new Date();
//		System.out.println("Running align_alignx at " + dateFormat.format(start));
//		scans = client.align_alignxByValue(scans);
//		end = new Date();
//		System.out.println("  completed at " + dateFormat.format(end) + " in " + getDuration(start, end) + " second(s)");
//		
//		// step 8: normalize
//		start = new Date();
//		System.out.println("Running normalize_quantile at " + dateFormat.format(start));
//		scans = client.normalize_quantileByValue(scans, new QuantileType(1));
//		end = new Date();
//		System.out.println("  completed at " + dateFormat.format(end) + " in " + getDuration(start, end) + " second(s)");
		
		// step 9: plot data
		start = new Date();
		System.out.println("Running plot_2DStacked at " + dateFormat.format(start));
		//JpegImageType image = client.plot_2DStackedByValue(startScans, scans);
		JpegImageType image = client.plot_2DByValue(scans);
		end = new Date();
		System.out.println("  completed at " + dateFormat.format(end) + " in " + getDuration(start, end) + " second(s)");
				
		// post processing: write image to file
		byte[] imageBytes = Base64.decode(image.getData());
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(imageFile));
		os.write(imageBytes);
		os.flush();
		os.close();		
	}
}
