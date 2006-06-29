package gov.nih.nci.cagrid.installer.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logutil {

	private static Logger logger;

	public static Logger getLogger() {

		logger = Logger.getLogger("gov.nih.nci.cagrid.installer");
		FileHandler fh = null;
		try {
			fh = new FileHandler("install.log");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.addHandler(fh);
		logger.setLevel(Level.ALL);

		return logger;
	}

}
