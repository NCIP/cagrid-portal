package gov.nih.nci.cagrid.introduce.portal.modification.services.methods;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class MethodsTable extends PortalBaseTable {
	class MethodTypeContainer implements Comparable {
		private MethodType method;


		public MethodType getMethod() {
			return method;
		}


		public void setMethod(MethodType method) {
			this.method = method;
		}


		public MethodTypeContainer(MethodType method) {
			this.method = method;
		}


		public String toString() {
			return CommonTools.methodTypeToString(this.method);
		}


		public int compareTo(Object arg0) {
			MethodTypeContainer mtc = (MethodTypeContainer) (arg0);
			if (getMethod().isIsImported() && mtc.getMethod().isIsImported()) {
				return 100 + getMethod().getName().compareTo(mtc.getMethod().getName());
			} else if (!getMethod().isIsImported() && !mtc.getMethod().isIsImported()) {
				return getMethod().getName().compareTo(mtc.getMethod().getName());
			} else if (this.getMethod().isIsImported()) {
				return 100;
			} else if (!this.getMethod().isIsImported()) {
				return 0;
			}
			return 0;
		}
	}

	public static final String OPERATION = "Operation";
	private ServiceType serviceType;


	public MethodsTable(ServiceType service, File methodsDirectory, Properties serviceProperties) {
		super(createTableModel());
		this.serviceType = service;
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return false;
	}


	private void initialize() {
		this.setAutoCreateColumnsFromModel(false);
		this.setRowSelectionAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		
		for (int i = this.getRowCount() - 1; i == 0; i--) {
			this.removeRow(i);
		}

		if (serviceType != null && serviceType.getMethods() != null && serviceType.getMethods().getMethod() != null) {
			for (int i = 0; i < serviceType.getMethods().getMethod().length; i++) {
				if (!serviceType.getMethods().getMethod(i).getName().equals(
					IntroduceConstants.SERVICE_SECURITY_METADATA_METHOD)) {
					final Vector v = new Vector();
					v.add(new MethodTypeContainer(serviceType.getMethods().getMethod(i)));

					((DefaultTableModel) this.getModel()).addRow(v);
					this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
				}
			}
		}
	}


	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		MethodType method = getMethodType(rowIndex);
		c.setForeground(Color.BLACK);
		if (method.isIsImported()) {
			c.setBackground(new Color(235, 235, 235));
		} else {
			c.setBackground(getBackground());
		}
		if (isCellSelected(rowIndex, vColIndex)) {
			c.setBackground(getSelectionBackground());
			c.setForeground(Color.WHITE);
		}
		return c;
	}


	public void setMethods(ServiceType service) {
		this.serviceType = service;
		initialize();
	}


	public void removeSelectedRow() throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		MethodType method = getMethodType(getSelectedRow());
		CommonTools.removeMethod(serviceType.getMethods(), method);
		int oldSelectedRow = getSelectedRow();
		((DefaultTableModel) getModel()).removeRow(oldSelectedRow);
		if (oldSelectedRow == 0) {
			oldSelectedRow++;
		}
		if (getRowCount() > 0) {
			setRowSelectionInterval(oldSelectedRow - 1, oldSelectedRow - 1);
		}
	}


	public MethodType getMethodType(int row) {
		if ((row < 0) || (row >= getRowCount())) {
			return null;
		}

		return ((MethodTypeContainer) getValueAt(row, 0)).getMethod();
	}


	public MethodType getSelectedMethodType() {
		return getMethodType(getSelectedRow());
	}


	public void refreshRowFromMethodType(int rowIndex) {
		TableModelEvent event = new TableModelEvent(getModel(), rowIndex, rowIndex);
		tableChanged(event);
	}


	public void addRow(final MethodType method) {
		CommonTools.addMethod(serviceType, method);

		final Vector v = new Vector();
		MethodTypeContainer mtc = new MethodTypeContainer(method);
		v.add(mtc);

		((DefaultTableModel) this.getModel()).addRow(v);

		sort();
		for (int i = 0; i < getRowCount(); i++) {
			if (getMethodType(i).equals(mtc.getMethod())) {
				setRowSelectionInterval(i, i);
				return;
			}
		}
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(OPERATION);
		return model;
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void sort() {
		DefaultTableModel model = (DefaultTableModel) getModel();
		Vector data = model.getDataVector();
		Collections.sort(data, new ColumnSorter(0, true));
		model.fireTableStructureChanged();
	}


	public class ColumnSorter implements Comparator {
		int colIndex;
		boolean ascending;


		ColumnSorter(int colIndex, boolean ascending) {
			this.colIndex = colIndex;
			this.ascending = ascending;
		}


		public int compare(Object a, Object b) {
			Vector v1 = (Vector) a;
			Vector v2 = (Vector) b;
			MethodTypeContainer o1 = (MethodTypeContainer) v1.get(colIndex);
			MethodTypeContainer o2 = (MethodTypeContainer) v2.get(colIndex);

			if (o1 == null && o2 == null) {
				return 0;
			} else if (o1 == null) {
				return 1;
			} else if (o2 == null) {
				return -1;
			} else if (o1 instanceof Comparable) {
				if (ascending) {
					return ((Comparable) o1).compareTo(o2);
				} else {
					return ((Comparable) o2).compareTo(o1);
				}
			} else {
				if (ascending) {
					return o1.toString().compareTo(o2.toString());
				} else {
					return o2.toString().compareTo(o1.toString());
				}
			}
		}
	}

}