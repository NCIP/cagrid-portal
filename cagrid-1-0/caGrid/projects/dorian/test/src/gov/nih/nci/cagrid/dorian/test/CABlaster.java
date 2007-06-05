package gov.nih.nci.cagrid.dorian.test;

import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CABlaster {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			String userPrefix = "user";
			String file = "count.txt";
			CertificateAuthority ca = Utils.getCA();
			Calendar c = new GregorianCalendar();
			Date start = c.getTime();
			c.add(Calendar.YEAR, 1);
			Date expiration = c.getTime();
			int count = 1;
			while (true) {
				try {
					String user = userPrefix + count;
					ca.createCredentials(user, user, null, start, expiration);
					System.out.println("Created credentials for the user " + user);
					PrintWriter out = new PrintWriter(new FileOutputStream(new File(file), false));
					out.println(user);
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
