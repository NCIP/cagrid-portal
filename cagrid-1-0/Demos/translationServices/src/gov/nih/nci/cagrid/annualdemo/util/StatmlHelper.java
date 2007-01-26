/*
 * Created on Jan 25, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.io.IOException;

import edu.duke.cabig.javar.io.StatMLSerializationException;
import edu.duke.cabig.javar.io.StatMLSerializer;
import gridextensions.Array;
import gridextensions.Data;

public class StatmlHelper
{
	
	/**
	 * Creates a Data object.
	 * @param rowNames names of the rows.
	 * @param rowDescriptions descriptions of the rows.
	 * @param columnNames names of the columns.
	 * @param values values, not including the name and description columns
	 * @return Data object
	 * @throws StatMLSerializationException
	 * @throws IOException
	 */
	public static Data createStatmlData(String[] rowNames,
		String[] rowDescriptions, String[] columnNames, double[][] values)
		throws StatMLSerializationException, IOException
	{
		Data data = new Data();
		int numRows = rowNames.length;
		int numCols = columnNames.length;

		Array[] arrays = new Array[numCols + 2];
		StatMLSerializer ser = new StatMLSerializer();

		Array rowNamesArray = createStringArray(rowNames, ser);
		Array rowDescArray = createStringArray(rowDescriptions, ser);

		arrays[0] = rowNamesArray;
		arrays[1] = rowDescArray;

		for (int j = 0; j < numCols; j++) {
			Double[] doubleVals = new Double[numRows];
			for (int i = 0; i < numRows; i++) {

				doubleVals[i] = values[i][j];
			}
			Array array = createDoubleArray(columnNames[j], doubleVals, ser);
			arrays[j + 2] = array;
		}
		data.setArray(arrays);
		return data;
	}
	
	/**
	 * Creates a StatML Array object containing the values.
	 * 
	 * @param values
	 *            Values to serialize into the array.
	 * @param ser
	 *            Serializer.
	 * @return StatML Array object.
	 * @throws StatMLSerializationException
	 * @throws IOException
	 */
	public static Array createStringArray(String[] values, 
		StatMLSerializer ser) throws StatMLSerializationException, IOException {
		Array array = new Array();
		
		array.setDimensions(String.valueOf(values.length));
		array.setType("java.lang.String");
		String encoded = ser.serialize(null, 
			values);
		array.setBase64Value(encoded);
		
		return array;
	}
	
	/**
	 * Creates a StatML Array object containing the values.
	 * 
	 * @param name
	 *            Name of the column.
	 * @param values
	 *            Values for the column.
	 * @param ser
	 *            Statml Serializer
	 * @return Statml Array object.
	 * @throws StatMLSerializationException
	 * @throws IOException
	 */
	public static Array createDoubleArray(String name, 
		Double[] values, StatMLSerializer ser) throws 
		StatMLSerializationException, IOException {
		Array array = new Array();
		
		array.setDimensions(String.valueOf(values.length));
		array.setName(name);
		array.setType("java.lang.Double");
		String encoded = ser.serialize(null, 
			values);
		array.setBase64Value(encoded);
		
		return array;
	}

}
