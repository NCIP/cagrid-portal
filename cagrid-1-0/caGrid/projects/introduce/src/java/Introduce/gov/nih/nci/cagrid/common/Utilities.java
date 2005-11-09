package gov.nih.nci.cagrid.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


/**
 * Place to put static common utility functions
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 13, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class Utilities {

	public static StringBuffer fileToStringBuffer(File file) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		try {
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
		} catch (Exception e) {
			throw new Exception("Error reading the buffer: " + e.getMessage());
		}

		return sb;
	}

}
