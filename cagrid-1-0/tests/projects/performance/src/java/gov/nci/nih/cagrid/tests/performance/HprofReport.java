/*
 * Created on May 23, 2006
 */
package gov.nci.nih.cagrid.tests.performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Patrick McConnell
 */
public class HprofReport
{
	private Hashtable memTable;
	private int minTrace = Integer.MAX_VALUE;
	private int maxTrace = Integer.MIN_VALUE;
	
	public HprofReport()
	{
		super();
	}
	
	public void parse(File file)
		throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(file));	
		
		Pattern p = Pattern.compile("\\w+ \\d+ \\(sz=(\\d+), trace=(\\d+), class=.+\\)");
		
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("TRACE ")) {
				int trace = 0;
			}
			if (line.startsWith("OBJ ") || line.startsWith("ARR ")) {
				Matcher m = p.matcher(line);
				if (! m.matches()) continue;
				
			}
		}
		
		br.close();
	}
}
