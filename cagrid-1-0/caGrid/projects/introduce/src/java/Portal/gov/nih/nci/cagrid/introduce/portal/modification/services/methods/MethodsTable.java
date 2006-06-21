package gov.nih.nci.cagrid.introduce.portal.modification.services.methods;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.util.Properties;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class MethodsTable extends PortalBaseTable {
	public static final String OPERATION = "Operation";
	private ServiceType serviceType;


	public MethodsTable(ServiceType service, File methodsDirectory, Properties serviceProperties) {
		super(createTableModel());
		this.serviceType = service;
		this.setRowSelectionAllowed(true);
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return false;
	}


	private void initialize() {
		for (int i = this.getRowCount() - 1; i == 0; i--) {
			this.removeRow(i);
		}

		if (serviceType!=null && serviceType.getMethods() != null && serviceType.getMethods().getMethod() != null) {
			for (int i = 0; i < serviceType.getMethods().getMethod().length; i++) {
				final Vector v = new Vector();
				v.add(new MethodTypeContainer(serviceType.getMethods().getMethod(i)));

				((DefaultTableModel) this.getModel()).addRow(v);
				this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
			}
		}
	}
	
	public void setMethods(ServiceType service){
		this.serviceType = service;
		initialize();
	}


	public void removeSelectedRow() throws Exception {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			throw new Exception("invalid row");
		}
		MethodType method = getMethodType(getSelectedRow());
		CommonTools.removeMethod(serviceType.getMethods(),method);
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
		final Vector v = new Vector();
		v.add(new MethodTypeContainer(method));

		((DefaultTableModel) this.getModel()).addRow(v);
		this.setRowSelectionInterval(this.getModel().getRowCount() - 1, this.getModel().getRowCount() - 1);
		CommonTools.addMethod(serviceType,method);
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(OPERATION);
		return model;
	}


	class MethodTypeContainer {
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
	}


	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}


	public void doubleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}