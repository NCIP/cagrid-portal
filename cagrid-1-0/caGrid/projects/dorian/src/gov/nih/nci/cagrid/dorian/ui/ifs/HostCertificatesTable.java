package gov.nih.nci.cagrid.dorian.ui.ifs;

import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.cagrid.grape.table.GrapeBaseTable;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: HostCertificatesTable.java,v 1.1 2007-06-06 19:27:54 langella Exp $
 */
public class HostCertificatesTable extends GrapeBaseTable {
	public final static String HOST_CERTIFICATE_RECORD = "record";

	public final static String ID = "Id";

	public final static String HOST = "Host";

	public final static String STATUS = "Status";

	public HostCertificatesTable() {
		super(createTableModel());
		TableColumn c = this.getColumn(HOST_CERTIFICATE_RECORD);
		c.setMaxWidth(0);
		c.setMinWidth(0);
		c.setPreferredWidth(0);
		c.setResizable(false);

		c = this.getColumn(ID);
		c.setMaxWidth(75);
		c.setMinWidth(75);

		c = this.getColumn(STATUS);
		c.setMaxWidth(50);
		c.setMinWidth(50);

		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(HOST_CERTIFICATE_RECORD);
		model.addColumn(ID);
		model.addColumn(HOST);
		model.addColumn(STATUS);
		return model;

	}

	public void addHostCertificate(final HostCertificateRecord record) {
		Vector v = new Vector();
		v.add(record);
		v.add(new Integer(record.getId()));
		v.add(record.getHost());
		v.add(record.getStatus().getValue());
		addRow(v);
	}

	public synchronized HostCertificateRecord getSelectedHostCertificate()
			throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (HostCertificateRecord) getValueAt(row, 0);
		} else {
			throw new Exception("Please select a host certificate!!!");
		}
	}

	public synchronized void removeSelectedTrustedIdP() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			removeRow(row);
		} else {
			throw new Exception("Please select a host certificate!!!");
		}
	}

	public void doubleClick() throws Exception {

	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}