package gov.nih.nci.cagrid.data.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/** 
 *  DataServiceTypesTableCellRenderer
 *  Renders cells for the data service types table
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 13, 2006 
 * @version $Id$ 
 */
public class DataServiceTypesTableCellRenderer extends DefaultTableCellRenderer {
	
	public DataServiceTypesTableCellRenderer() {
		super();
	}
	
	
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof JComponent) {
			return (JComponent) value;
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
