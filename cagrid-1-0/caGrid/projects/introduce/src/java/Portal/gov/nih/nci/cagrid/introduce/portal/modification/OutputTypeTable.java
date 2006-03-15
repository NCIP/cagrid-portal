package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.modification.gme.GMEParameterConfigurationComponent;

import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: OutputTypeTable.java,v 1.17 2006-03-15 17:36:23 hastings Exp $
 */
public class OutputTypeTable extends PortalBaseTable {

	public static String PACKAGENAME = "Package Name";

	public static String CLASSNAME = "Classname";

	public static String ISARRAY = "Is Array";

	public static String NAMESPACE = "Namespace";

	public static String TYPE = "Type";

	public static String LOCATION = "Location";

	public static String DATA1 = "DATA1";

	private MethodType method;


	public OutputTypeTable(MethodType method) {
		super(createTableModel());
		this.method = method;
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return true;
		// if (column == 4) {
		// return true;
		// } else {
		// return false;
		// }
	}


	private void initialize() {
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		MethodTypeOutput output = method.getOutput();
		final Vector v = new Vector();
		v.add(output.getPackageName());
		v.add(output.getClassName());
		if (output.getIsArray() != null) {
			v.add(String.valueOf(output.getIsArray().booleanValue()));
		} else {
			v.add("");
		}
		v.add(output.getNamespace());
		v.add(output.getType());
		v.add(output.getLocation());
		v.add(v);
		((DefaultTableModel) this.getModel()).addRow(v);
	}

	
	public void modifyRow(int row, final MethodTypeOutput output) throws Exception {
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		Vector v = (Vector) getValueAt(row, 6);
		v.set(0, output.getPackageName());
		v.set(1, output.getClassName());
		if (output.getIsArray() != null) {
			v.set(2, (String.valueOf(output.getIsArray().booleanValue())));
		} else {
			v.set(2, "");
		}
		v.set(3, output.getNamespace());
		v.set(4, output.getType());
		v.set(5, output.getLocation());
		v.set(6, v);
	}

	public void modifySelectedRow(final MethodTypeOutput output) throws Exception {
		modifyRow(getSelectedRow(),output);
	}
	
	public MethodTypeOutput getRowData(int row) throws Exception{
		
		MethodTypeOutput output = new MethodTypeOutput();

		String packageName = ((String) getValueAt(row, 0));
		String className = ((String) getValueAt(row, 1));
		Boolean isArray = new Boolean(((String) getValueAt(row, 2)));
		String namespace = ((String) getValueAt(row, 3));
		String type = ((String) getValueAt(row, 4));
		String location = ((String) getValueAt(row, 5));

		if (packageName != null && !packageName.equals("")) {
			output.setPackageName(packageName);
		}
		if (className != null && !className.equals("")) {
			output.setClassName(className);
		}
		if (isArray != null) {
			output.setIsArray(isArray);
		}
		if (namespace != null && !namespace.equals("")) {
			output.setNamespace(namespace);
		}
		if (type != null && !type.equals("")) {
			output.setType(type);
		}
		if (location != null && !location.equals("")) {
			output.setLocation(location);
		}
		
		return output;
	}

	public void removeSelectedRow() throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		int oldSelectedRow = getSelectedRow();
		((DefaultTableModel) getModel()).removeRow(oldSelectedRow);
		if (oldSelectedRow == 0) {
			oldSelectedRow++;
		}
		if (getRowCount() > 0) {
			setRowSelectionInterval(oldSelectedRow - 1, oldSelectedRow - 1);
		}
	}


	public MethodTypeInputsInput getSelectedRowData() throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		Vector v = (Vector) getValueAt(getSelectedRow(), 1);
		return (MethodTypeInputsInput) v.get(6);
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(PACKAGENAME);
		model.addColumn(CLASSNAME);
		model.addColumn(ISARRAY);
		model.addColumn(NAMESPACE);
		model.addColumn(TYPE);
		model.addColumn(LOCATION);
		model.addColumn("DATA1");

		return model;
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}
}