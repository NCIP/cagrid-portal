package gov.nih.nci.cagrid.gums.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: IOUtils.java,v 1.1 2005-09-27 18:31:18 langella Exp $
 */
public class IOUtils {
	public static int readInteger(String prompt) {
		String s = readLine(prompt);
		try {
			return Integer.valueOf(s).intValue();
		} catch (Exception e) {
			System.err
					.println("Please try again, this time enter an Integer!!!");
			return readInteger(prompt);
		}

	}
	
	public static String readLineUntilEntered(String prompt) {
		// prompt the user to enter their name
		

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String s = null;

		// read the username from the command-line; need to use try/catch with
		// the
		// readLine() method
		try {
			while ((s == null) || (s.trim().length() == 0)) {
				System.out.print(prompt + ":");
				s = br.readLine();
			}
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}

		return s;
	}

	public static String readLine(String prompt) {
		// prompt the user to enter their name
		System.out.print(prompt + ":");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String s = null;

		// read the username from the command-line; need to use try/catch with
		// the
		// readLine() method
		try {
			s = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}

		return s;
	}

}
