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
	public static MicroarrayData extractArrayInfo(Data data)
		throws IOException, StatMLSerializationException 
	{
		Array[] arrays = data.getArray();
		String[] rowNames = null;
		String[] rowDescriptions = null;
		String[] colNames = null;
		Double[][] values = null;
		
		StatMLSerializer serializer = new StatMLSerializer();
		
		Array namesArray = arrays[0];
		Object[] rowNamesObj = serializer.deserialize(namesArray.getBase64Value());
		int numRows = rowNamesObj.length;
		int numCols = arrays.length - 2;
		
		rowNames = new String[numRows];
		System.arraycopy(rowNamesObj, 0, rowNames, 0, numRows);
		
		Array descriptionArray = arrays[1];
		Object[] rowDescriptionsObj = serializer.deserialize(descriptionArray.getBase64Value());
		rowDescriptions = new String[numRows];
		System.arraycopy(rowDescriptionsObj, 0, rowDescriptions, 0, numRows);
		
//		for (int i = 0; i < numRows; i++) {
//			System.out.println("i=[" + i + "] rowName=[" + rowNames[i] + 
//				"] rowDescription=[" + rowDescriptions[i] + "]");
//		}
		
		values = new Double[numRows][numCols];
		colNames = new String[numCols];
		
		for (int i = 2; i < arrays.length; i++) {
			Array array = arrays[i];
			int index = i - 2;
			colNames[index] = array.getName();
//			System.out.println("i=[" + i + "] index=[" + index + "]");
			Object[] objs = serializer.deserialize(array.getBase64Value());
			for (int row = 0; row < objs.length; row++) {
				Double value = (Double) objs[row];
//				System.out.println("row=[" + row + "] index=[" + index + "] value=[" +
//					value + "]");
				values[row][index] = (Double) objs[row];
			}
		}
		
		MicroarrayData microarray = new MicroarrayData();
		for (String rowName : rowNames) microarray.geneNames.add(rowName);
		for (String colName : colNames) microarray.arrayNames.add(colName);
		for (Double[] rowVals : values) {
			double[] drowVals = new double[rowVals.length];
			for (int i = 0; i < rowVals.length; i++) {
				drowVals[i] = rowVals[i].doubleValue();
			}  
			microarray.data.add(drowVals);
		}
		return microarray;
	}		 
	
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
