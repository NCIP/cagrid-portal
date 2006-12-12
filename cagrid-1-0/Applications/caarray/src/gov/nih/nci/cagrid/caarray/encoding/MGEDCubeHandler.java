package gov.nih.nci.cagrid.caarray.encoding;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import gov.nih.nci.mageom.domain.BioAssayData.impl.BioDataCubeImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

public class MGEDCubeHandler implements FieldHandler {
	
	private static Log LOG = LogFactory.getLog(MGEDCubeHandler.class);
	
	public static final String LINE_DELIMITER = "\\";
	public static final String LINE_DELIMITER_PATTERN = "\\\\";
	public static final String VALUE_DELIMITER = "|";
	public static final String VALUE_DELIMITER_PATTERN = "\\|";

	public void checkValidity(Object arg0) throws ValidityException,
			IllegalStateException {
		// TODO Auto-generated method stub

	}

	public Object getValue(Object obj) throws IllegalStateException {
		Object value = null;
		if (obj != null) {
			if (obj instanceof BioDataCubeImpl) {

				BioDataCubeImpl bdc = (BioDataCubeImpl) obj;
				Object[][][] cube = bdc.getCube();
				if (cube != null) {

					try {

						LOG.debug("Beginning encoding...");
						value = getCubeAsString(cube, LINE_DELIMITER, VALUE_DELIMITER);
						LOG.debug("...done encoding.");

					} catch (Exception ex) {
						LOG.error("Error enconding cube: " + ex.getMessage(), ex); 
						throw new RuntimeException("Error encoding cube: "
								+ ex.getMessage(), ex);
					}
				}

			} else {
				throw new IllegalStateException("Object not instanceof: "
						+ BioDataCubeImpl.class.getName()
						+ obj.getClass().getName());
			}
		}

		return value;
	}

	public Object newInstance(Object arg0) throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetValue(Object arg0) throws IllegalStateException,
			IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	public void setValue(Object obj, Object value)
			throws IllegalStateException, IllegalArgumentException {


		if (obj != null) {

			BioDataCubeImpl bdc = null;
			if (!(obj instanceof BioDataCubeImpl)) {
				throw new IllegalArgumentException("Object not instance of "
						+ BioDataCubeImpl.class.getName() + ": "
						+ obj.getClass().getName());
			}
			bdc = (BioDataCubeImpl) obj;

			if (value == null) {
				bdc.setCube(null);
			} else {

				if (!(value instanceof String)) {
					throw new IllegalArgumentException(
							"Value not instanceof String: "
									+ value.getClass().getName());
				}
				Object[][][] cube = null;
				try {
					
					cube = getCubeFromString((String)value, LINE_DELIMITER_PATTERN, VALUE_DELIMITER_PATTERN);
					bdc.setCube(cube);
					
				} catch (Exception ex) {
					throw new RuntimeException("Error decoding cube: "
							+ ex.getMessage(), ex);
				}

			}

		}
	}
	
	public static String getCubeAsString(Object[][][] cube, String lineDelim, String valueDelim) {
		StringBuffer sb = new StringBuffer();
		Object value = null;
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[i].length; j++) {
				for (int k = 0; k < cube[i][j].length; k++) {
					value = cube[i][j][k];
					if (value == null
							|| (value instanceof Double && ((Double) value)
									.isInfinite())) {
						sb.append("NaN");
					} else {
						sb.append(value);
					}
					sb.append(valueDelim);
				}
				sb.setLength(sb.length() - 1); // Remove the last tab
				sb.append(lineDelim);
			}
			sb.append(lineDelim);
		}
		return sb.toString();
	}
	
	/**
	 * Ripped from MAGEstk
	 * 
	 * @param dim1
	 * @param dim2
	 * @param dim3
	 * @param str
	 * @return
	 */
	public static Object[][][] getCubeFromString(int dim1, int dim2, int dim3,
			String str, String lineDelimPatt, String valueDelimPatt) {
		Object[][][] cube = new Object[dim1][dim2][dim3];

		List lines = Arrays.asList(str.split(lineDelimPatt));
		Iterator linesIt = lines.iterator();

		for (int i = 0; i < dim1; i++) {
			for (int j = 0; j < dim2; j++) {
				String line = (String) linesIt.next();

				// To make up for the added empty line between the first
				// dimension in the write method.
				if (line.length() < 1) {
					line = (String) linesIt.next();
				}

				// Assume tab is the delimiter.
				String[] tmp = line.split(valueDelimPatt);
				Double[] dtmp = new Double[tmp.length];

				for (int k = 0; k < tmp.length; k++) {
					if (tmp[k].trim().equalsIgnoreCase("NaN")) {
						dtmp[k] = null;
					} else {
						dtmp[k] = new Double(tmp[k]);
					}
				}

				// Check loaded dimension and trow exception if not the
				// same as the input parameter.
				if (dtmp.length == dim3) {
					cube[i][j] = dtmp;
				} else if (dtmp.length == dim2 * dim3) {
					for (int k = 0; k < dim2; k++)
						for (int l = 0; l < dim3; l++) {
							cube[i][k][l] = dtmp[k * dim3 + l];
						}

					// force return of j-loop, both dim 2 and 3 was on the same
					// line
					j = dim2;
				} else {
					throw new ArrayIndexOutOfBoundsException(dtmp.length);
				}

			}
		}

		return cube;
	}

	public static Object[][][] getCubeFromString(String str, String lineDelimPatt, String valueDelimPatt) {
		Object[][][] cube = null;

		int dim1 = 1;
		int dim2 = 0;
		int dim3 = 0;
		int tempDim2 = 0;
		int tempDim3 = 0;
		List lines = Arrays.asList(str.split(lineDelimPatt));
		for (Iterator linesIt = lines.iterator(); linesIt.hasNext();) {
			String line = (String) linesIt.next();
			if (line.length() == 0) {
				dim1++;
				if (tempDim2 > dim2) {
					dim2 = tempDim2;
				}
				tempDim2 = 0;
			} else {
				tempDim2++;
				tempDim3 = line.split(valueDelimPatt).length;
				if (tempDim3 > dim3) {
					dim3 = tempDim3;
				}
			}
		}

		cube = getCubeFromString(dim1, dim2, dim3, str, lineDelimPatt, valueDelimPatt);

		return cube;
	}

}
