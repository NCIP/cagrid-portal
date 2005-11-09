package gov.nih.nci.cagrid.introduce.portal.modification;

import java.util.List;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.jdom.Element;
import org.projectmobius.portal.PortalTable;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: InputParametersTable.java,v 1.1 2005/06/29 19:31:01 hastings
 *          Exp $
 */
public class ExceptionsTable extends PortalTable {

	public static String NAME = "Name";
	public static String DATA1 = "DATA1";
	public static String DATA2 = "DATA2";
	private Element method;

	public ExceptionsTable(Element method) {
		super(createTableModel());
		this.method = method;
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		initialize();
	}

	public boolean isCellEditable(int row, int column) {
		return true;
	}

	public void addRow(final Element exception) {
		final Vector v = new Vector();
		v.add(exception.getAttributeValue("name"));
		v.add(exception);
		v.add(v);

		((DefaultTableModel) this.getModel()).addRow(v);
	}

	private void initialize() {
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		this.getColumn(DATA2).setMaxWidth(0);
		this.getColumn(DATA2).setMinWidth(0);
		this.getColumn(DATA2).setPreferredWidth(0);

		List exceptions = method.getChild("exceptions", method.getNamespace())
				.getChildren();
		for (int i = 0; i < exceptions.size(); i++) {
			final Element exception = (Element) exceptions.get(i);
			addRow(exception);
		}
	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(NAME);
		model.addColumn(DATA1);
		model.addColumn(DATA2);

		return model;
	}
}