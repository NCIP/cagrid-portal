package gov.nih.nci.cagrid.dorian.common;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class LoggingObject {
	private static final Level DEBUG_LEVEL = Level.FINE;
	private static final Level INFO_LEVEL = Level.INFO;
	private static final Level WARN_LEVEL = Level.WARNING;
	private static final Level ERROR_LEVEL = Level.SEVERE;
	private static final Level FATAL_LEVEL = Level.SEVERE;

	protected Logger logger;
	protected boolean loggerLookup = false;


	private void initLogger() {
		if (!loggerLookup) {
			logger = Logger.getLogger(this.getClass().getName());
			loggerLookup = true;
		}
	}


	public boolean isDebugEnabled() {
		initLogger();
		if (logger != null) {
			return logger.isLoggable(DEBUG_LEVEL);
		}
		return false;
	}


	public void debug(String s) {
		initLogger();
		if (logger != null) {
			logger.log(DEBUG_LEVEL, s);
		}
	}


	public boolean isInfoEnabled() {
		initLogger();
		if (logger != null) {
			return logger.isLoggable(INFO_LEVEL);
		}
		return false;
	}


	public void info(String s) {
		initLogger();
		if (logger != null) {
			logger.log(INFO_LEVEL, s);
		}
	}


	public boolean isWarningEnabled() {
		initLogger();
		if (logger != null) {
			return logger.isLoggable(WARN_LEVEL);
		}
		return false;
	}


	public void logWarning(String s) {
		initLogger();
		if (logger != null) {
			logger.log(WARN_LEVEL, s);
		}
	}


	public void logWarning(String s, Throwable thrown) {
		initLogger();
		if (logger != null) {
			logger.log(WARN_LEVEL, s, thrown);
		}
	}


	public boolean isErrorEnabled() {
		initLogger();
		if (logger != null) {
			return logger.isLoggable(ERROR_LEVEL);
		}
		return false;
	}


	public void logError(String s) {
		initLogger();
		if (logger != null) {
			logger.log(ERROR_LEVEL, s);
		}
	}


	public void logError(String s, Throwable thrown) {
		initLogger();
		if (logger != null) {
			logger.log(ERROR_LEVEL, s, thrown);
		}
	}


	public boolean isFatalErrorEnabled() {
		initLogger();
		if (logger != null) {
			return logger.isLoggable(FATAL_LEVEL);
		}
		return false;
	}


	public void logFatalError(String s) {
		initLogger();
		if (logger != null) {
			logger.log(FATAL_LEVEL, s);
		}
	}


	public void logFatalError(String s, Throwable thrown) {
		initLogger();
		if (logger != null) {
			logger.log(FATAL_LEVEL, s, thrown);
		}
	}

}