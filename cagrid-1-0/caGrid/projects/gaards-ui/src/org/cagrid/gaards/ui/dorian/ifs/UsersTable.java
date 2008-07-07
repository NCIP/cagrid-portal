package org.cagrid.gaards.ui.dorian.ifs;

import gov.nih.nci.cagrid.common.Runner;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.cagrid.gaards.dorian.client.GridAdministrationClient;
import org.cagrid.gaards.dorian.federation.IFSUser;
import org.cagrid.gaards.dorian.federation.TrustedIdP;
import org.cagrid.gaards.ui.dorian.SessionPanel;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.table.GrapeBaseTable;
import org.cagrid.grape.utils.ErrorDialog;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: UsersTable.java,v 1.4 2008-07-07 18:47:48 langella Exp $
 */
public class UsersTable extends GrapeBaseTable {
	public final static String USER = "user";

	public final static String IDP = "IdP Id";

	public final static String UID = "User Id";

	public final static String GRID_IDENTITY = "Grid Identity";

	public final static String EMAIL = "Email";

	public final static String FIRST_NAME = "First Name";

	public final static String LAST_NAME = "Last Name";

	SessionPanel session;

	public UsersTable(SessionPanel session) {
		super(createTableModel());
		this.session = session;
		TableColumn c = this.getColumn(USER);
		c.setMaxWidth(0);
		c.setMinWidth(0);
		c.setPreferredWidth(0);
		c.setResizable(false);

		c = this.getColumn(IDP);
		c.setMaxWidth(35);
		c.setMinWidth(35);
		c.setPreferredWidth(0);

		c = this.getColumn(GRID_IDENTITY);
		c.setMinWidth(350);
		c.setPreferredWidth(0);

		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(USER);
		model.addColumn(IDP);
		model.addColumn(UID);
		model.addColumn(GRID_IDENTITY);
		model.addColumn(FIRST_NAME);
		model.addColumn(LAST_NAME);
		model.addColumn(EMAIL);
		return model;

	}

	public void addUser(final IFSUser u) {
		Vector v = new Vector();
		v.add(u);
		v.add(String.valueOf(u.getIdPId()));
		v.add(String.valueOf(u.getUID()));
		v.add(u.getGridId());
		v.add(u.getFirstName());
		v.add(u.getLastName());
		v.add(u.getEmail());
		addRow(v);
	}

	public synchronized IFSUser getSelectedUser() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (IFSUser) getValueAt(row, 0);
		} else {
			throw new Exception("Please select a user!!!");
		}
	}

	public synchronized void removeSelectedUser() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			removeRow(row);
		} else {
			throw new Exception("Please select a user!!!");
		}
	}

	public void doubleClick() {
		Runner runner = new Runner() {
			public void execute() {
				try {
					IFSUser user = getSelectedUser();
					GridAdministrationClient client = session.getAdminClient();
					List<TrustedIdP> idps = client.getTrustedIdPs();
					TrustedIdP tidp = null;
					for (int i = 0; i < idps.size(); i++) {
						if (idps.get(i).getId() == user.getIdPId()) {
							tidp = idps.get(i);
							break;
						}
					}
					GridApplication.getContext().addApplicationComponent(
							new UserWindow(session.getServiceURI(), session
									.getCredential(), user, tidp),800,650);
				} catch (Exception e) {
					ErrorDialog.showError(e);
				}
			}
		};
		try {
			GridApplication.getContext().executeInBackground(runner);
		} catch (Exception t) {
			t.getMessage();
		}

	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}