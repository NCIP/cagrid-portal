package org.cagrid.gaards.ui.dorian.federation;

import java.security.cert.X509Certificate;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.cagrid.gaards.dorian.federation.UserCertificateRecord;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.ui.dorian.DorianSession;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.table.GrapeBaseTable;
import org.cagrid.grape.utils.ErrorDialog;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: UserCertificatesTable.java,v 1.2 2008-09-29 02:36:19 langella Exp $
 */
public class UserCertificatesTable extends GrapeBaseTable {
	public final static String USER_CERTIFICATE = "user";

	public final static String SERIAL_NUMBER = "Serial Number";

	public final static String STATUS = "Status";

	public final static String NOT_BEFORE = "Not Before";

	public final static String NOT_AFTER = "Not After";

	DorianSession session;

	public UserCertificatesTable(DorianSession session) {
		super(createTableModel());
		this.session = session;
		TableColumn c = this.getColumn(USER_CERTIFICATE);
		c.setMaxWidth(0);
		c.setMinWidth(0);
		c.setPreferredWidth(0);
		c.setResizable(false);
		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(USER_CERTIFICATE);
		model.addColumn(SERIAL_NUMBER);
		model.addColumn(STATUS);
		model.addColumn(NOT_BEFORE);
		model.addColumn(NOT_AFTER);
		return model;

	}

	public void addUserCertificate(final UserCertificateRecord r)
			throws Exception {
		X509Certificate cert = CertUtil.loadCertificate(r.getCertificate()
				.getCertificateAsString());
		Vector v = new Vector();
		v.add(r);
		v.add(String.valueOf(r.getSerialNumber()));
		v.add(r.getStatus().getValue());
		v.add(cert.getNotBefore());
		v.add(cert.getNotAfter());
		addRow(v);
	}

	public synchronized UserCertificateRecord getSelectedCertificate()
			throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (UserCertificateRecord) getValueAt(row, 0);
		} else {
			throw new Exception("Please select a user!!!");
		}
	}

	public synchronized void removeSelectedUser() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			removeRow(row);
		} else {
			throw new Exception("Please select a certificate!!!");
		}
	}

	public void doubleClick() {
		try {
			GridApplication.getContext().addApplicationComponent(
					new UserCertificateWindow(this.session,getSelectedCertificate()), 600,
					450);
		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}