package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.io.File;
import java.util.Properties;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import org.projectmobius.portal.PortalTable;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: MethodsTable.java,v 1.10 2006-02-28 16:42:43 oster Exp $
 */
public class MethodsTable extends PortalTable {
	public static final String OPERATION = "Operation";
	private MethodsType methodsType;


	public MethodsTable(MethodsType methodsType, File methodsDirectory, Properties serviceProperties) {
		super(createTableModel());
		this.methodsType = methodsType;
		this.setRowSelectionAllowed(true);
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return false;
	}


	private void initialize() {
		if (methodsType.getMethod() != null) {
			for (int i = 0; i < methodsType.getMethod().length; i++) {
				this.addRow(methodsType.getMethod(i));
			}
		}
	}


	public MethodType getSelectedMethodType() {
		int row = getSelectedRow();
		if ((row < 0) || (row >= getRowCount())) {
			return null;
		}

		return ((MethodTypeContainer) getValueAt(getSelectedRow(), 0)).getMethod();
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
			// assume its void to start with
			String output = "void";

			MethodTypeOutput outputType = this.method.getOutput();
			if (outputType != null) {
				// use classname if set, else use schema type
				if (outputType.getClassName() != null && !outputType.getClassName().trim().equals("")) {
					output = outputType.getClassName();
				} else if (outputType.getType() != null && !outputType.getType().trim().equals("")) {
					output = outputType.getType();
				}

				// add array notation if its an array
				if (outputType.getIsArray() != null && outputType.getIsArray().booleanValue()) {
					output += "[]";
				}
			}

			String input = "";
			MethodTypeInputs inputs = this.method.getInputs();
			if (inputs != null) {
				MethodTypeInputsInput[] inputarr = inputs.getInput();
				if (inputarr != null) {
					for (int i = 0; i < inputarr.length; i++) {
						MethodTypeInputsInput inputType = inputarr[i];
						// use classname if set, else use schema type
						if (inputType.getClassName() != null && !inputType.getClassName().trim().equals("")) {
							if (!input.equals("")) {
								input += ",";
							}
							input += inputType.getClassName();
						} else if (inputType.getType() != null && !inputType.getType().trim().equals("")) {
							if (!input.equals("")) {
								input += ",";
							}
							input += inputType.getType();
						} else {
							// why would this be the case?
							continue;
						}

						// add array notation if its an array
						if (inputType.getIsArray() != null && inputType.getIsArray().booleanValue()) {
							input += "[]";
						}

						input += " " + inputType.getName();
					}
				}
			}

			output += " " + method.getName() + "(" + input + ")";

			return output;
		}
	}

}