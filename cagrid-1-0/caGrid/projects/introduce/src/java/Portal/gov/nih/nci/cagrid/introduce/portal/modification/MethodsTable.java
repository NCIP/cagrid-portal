package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsTypeMethod;

import java.io.File;
import java.util.Properties;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.projectmobius.portal.PortalTable;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: MethodsTable.java,v 1.7 2005-12-16 14:00:22 hastings Exp $
 */
public class MethodsTable extends PortalTable {

	public static String NAME = "Operation Name";

	public static String DATA = "Data";

	private MethodsType methodsType;

	private File methodsDirectory;

	private Properties serviceProperties;

	private MethodsTable me;

	public MethodsTable(MethodsType methodsType, File methodsDirectory,
			Properties serviceProperties) {
		super(createTableModel());
		this.methodsType = methodsType;
		this.setRowSelectionAllowed(true);
		this.methodsDirectory = methodsDirectory;
		this.serviceProperties = serviceProperties;
		me = this;
		initialize();
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	private void initialize() {
		this.getColumn(DATA).setMaxWidth(0);
		this.getColumn(DATA).setMinWidth(0);
		this.getColumn(DATA).setPreferredWidth(0);

		if (methodsType.getMethod() != null) {
			for (int i = 0; i < methodsType.getMethod().length; i++) {
				this.addRow(methodsType.getMethod(i));
			}
		}
	}

	public void addRow(final MethodsTypeMethod method) {
		final Vector v = new Vector();
		v.add(method.getName());
		v.add(method);

		((DefaultTableModel) this.getModel()).addRow(v);
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this
				.getModel().getRowCount() - 1);

	}

	public void changeMethodName(int row, String newName) {
		setValueAt(newName, row, 0);
	}

	public String getMethodName(int row) {
		return (String) getValueAt(row, 0);
	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(NAME);
		model.addColumn(DATA);
		return model;
	}
}