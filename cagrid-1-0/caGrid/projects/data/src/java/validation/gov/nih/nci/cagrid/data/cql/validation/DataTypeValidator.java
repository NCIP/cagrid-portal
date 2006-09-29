package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cagrid.data.MalformedQueryException;

import java.text.DateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


/**
 * ValueDomainValidator Validates a value against its ValueDomain
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jul 31, 2006
 * @version $Id$
 */
public class DataTypeValidator {
	private static Logger LOG = Logger.getLogger(DataTypeValidator.class);


	public static void validate(String value, String dataType) throws MalformedQueryException {
		if (dataType.equals(String.class.getName())) {
			// this is fairly common, so returning immediatly is a slight
			// performance boost
			return;
		} else if (dataType.equals(Long.class.getName())) {
			validateLong(value);
		} else if (dataType.equals(Integer.class.getName())) {
			validateInteger(value);
		} else if (dataType.equals(Date.class.getName())) {
			validateDate(value);
		} else if (dataType.equals(Boolean.class.getName())) {
			validateBoolean(value);
		} else if (dataType.equals(Character.class.getName()) || dataType.equals("CHARACTER")) {
			validateCharacter(value);
		} else {
			LOG.warn("Data type " + dataType + " not recognized; Validated only as a String");
		}
	}


	private static void validateInteger(String value) throws MalformedQueryException {
		// parse the integer
		int intValue;
		try {
			intValue = Integer.parseInt(value);
		} catch (Exception ex) {
			throw new MalformedQueryException("Value " + value + " does not parse as an Integer");
		}
	}


	private static void validateLong(String value) throws MalformedQueryException {
		// parse the long
		long longValue;
		try {
			longValue = Long.parseLong(value);
		} catch (Exception ex) {
			throw new MalformedQueryException("Value " + value + " does not parse as a Long");
		}
	}


	private static void validateDate(String value) throws MalformedQueryException {
		try {
			DateFormat.getInstance().parse(value);
		} catch (Exception ex) {
			throw new MalformedQueryException("Value " + value + " does not parse as a Date");
		}
	}


	private static void validateBoolean(String value) throws MalformedQueryException {
		try {
			Boolean.valueOf(value);
		} catch (Exception ex) {
			throw new MalformedQueryException("Value " + value + " does not parse as a Boolean");
		}
	}


	private static void validateCharacter(String value) throws MalformedQueryException {
		if (value.length() > 1) {
			throw new MalformedQueryException("Value " + value + " is not a single Character or empty");
		}
	}
}
