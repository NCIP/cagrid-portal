package gov.nih.nci.cagrid.data.cql.validation;

import java.text.DateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import gov.nih.nci.cadsr.domain.ValueDomain;
import gov.nih.nci.cagrid.data.MalformedQueryException;

/** 
 *  ValueDomainValidator
 *  Validates a value against its ValueDomain
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jul 31, 2006 
 * @version $Id$ 
 */
public class ValueDomainValidator {
	private static Logger LOG = Logger.getLogger(ValueDomainValidator.class);

	public static void validate(String value, ValueDomain domain) throws MalformedQueryException {
		// decide which validator function to apply
		String dataType = domain.getDatatypeName();
		// everything can be validated as a string
		validateString(value, domain);
		if (dataType.equals(String.class.getName())) {
			// this is fairly common, so returning immediatly is a slight performance boost
			return;
		} else if (dataType.equals(Long.class.getName())) {
			validateLong(value, domain);
		} else if (dataType.equals(Integer.class.getName())) {
			validateInteger(value, domain);
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
	
	
	private static void validateString(String value, ValueDomain domain) throws MalformedQueryException {
		int length = value.length();
		if (domain.getMinimumLengthNumber() != null) {
			int minLength = domain.getMinimumLengthNumber().intValue();
			if (length < minLength) {
				throw new MalformedQueryException("Value " + value + " must be at least of length " + minLength);
			}
		}
		if (domain.getMaximumLengthNumber() != null) {
			int maxLength = domain.getMaximumLengthNumber().intValue();
			if (length > maxLength) {
				throw new MalformedQueryException("Value " + value + " must be at most of length " + maxLength);
			}
		}
	}
	
	
	private static void validateInteger(String value, ValueDomain domain) throws MalformedQueryException {
		// parse the integer
		int intValue;
		try {
			intValue = Integer.parseInt(value);
		} catch (Exception ex) {
			throw new MalformedQueryException("Value " + value + " does not parse as an Integer");
		}
		// validate the range of the integer
		if (domain.getLowValueNumber() != null) {
			int lowValue;
			try {
				lowValue = Integer.parseInt(domain.getLowValueNumber());
			} catch (Exception ex) {
				throw new MalformedQueryException("Low value number " 
					+ domain.getLowValueNumber() + " does not parse as an Integer");
			}
			if (intValue < lowValue) {
				throw new MalformedQueryException("Value " + value + " is less than min allowed value " + lowValue);
			}
		}
		if (domain.getHighValueNumber() != null) {
			int highValue;
			try {
				highValue = Integer.parseInt(domain.getHighValueNumber());
			} catch (Exception ex) {
				throw new MalformedQueryException("High value number "
					+ domain.getHighValueNumber() + " does not parse as an Integer");
			}
			if (intValue > highValue) {
				throw new MalformedQueryException("Value " + value + " is greater than max allowed value " + highValue);
			}
		}
	}
	
	
	private static void validateLong(String value, ValueDomain domain) throws MalformedQueryException {
		// parse the long
		long longValue;
		try {
			longValue = Long.parseLong(value);
		} catch (Exception ex) {
			throw new MalformedQueryException("Value " + value + " does not parse as a Long");
		}
		// validate the range of the long
		if (domain.getLowValueNumber() != null) {
			long lowValue;
			try {
				lowValue = Long.parseLong(domain.getLowValueNumber());
			} catch (Exception ex) {
				throw new MalformedQueryException("Low value number " 
					+ domain.getLowValueNumber() + " does not parse as a Long");
			}
			if (longValue < lowValue) {
				throw new MalformedQueryException("Value " + value + " is less than min allowed value " + lowValue);
			}
		}
		if (domain.getHighValueNumber() != null) {
			long highValue;
			try {
				highValue = Long.parseLong(domain.getHighValueNumber());
			} catch (Exception ex) {
				throw new MalformedQueryException("High value number "
					+ domain.getHighValueNumber() + " does not parse as a Long");
			}
			if (longValue > highValue) {
				throw new MalformedQueryException("Value " + value + " is greater than max allowed value " + highValue);
			}
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
