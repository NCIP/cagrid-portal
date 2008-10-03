package org.cagrid.gaards.ui.dorian.idp;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.cagrid.gaards.dorian.idp.LocalUser;
import org.cagrid.grape.table.GrapeBaseTable;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: UsersTable.java,v 1.3 2008-10-03 20:53:41 langella Exp $
 */
public class UsersTable extends GrapeBaseTable {
	public final static String USER = "user";

	public final static String USERNAME = "Username";

	public final static String FIRST_NAME = "First Name";

	public final static String LAST_NAME = "Last Name";

	public final static String ORGANIZATION = "Organization";

	public final static String EMAIL = "Email";

	public final static String STATUS = "Status";

	public final static String ROLE = "Role";
	
	UserManagerWindow window;

	public UsersTable(UserManagerWindow window) {
		super(createTableModel());
		this.window = window;
		TableColumn c = this.getColumn(USER);
		c.setMaxWidth(0);
		c.setMinWidth(0);
		c.setPreferredWidth(0);
		c.setResizable(false);
		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(USER);
		model.addColumn(USERNAME);
		model.addColumn(FIRST_NAME);
		model.addColumn(LAST_NAME);
		model.addColumn(ORGANIZATION);
		model.addColumn(EMAIL);
		model.addColumn(STATUS);
		model.addColumn(ROLE);
		return model;

	}

	public void addUser(final LocalUser u) {
		Vector v = new Vector();
		v.add(u);
		v.add(u.getUserId());
		v.add(u.getFirstName());
		v.add(u.getLastName());
		v.add(u.getOrganization());
		v.add(u.getEmail());
		v.add(u.getStatus().getValue());
		v.add(u.getRole().getValue());
		addRow(v);
	}

	public synchronized LocalUser getSelectedUser() {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (LocalUser) getValueAt(row, 0);
		} else {
			return null;
		}
	}

	public void doubleClick() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			window.showUser();
		} else {
			throw new Exception(
					"No user selected, please select a user!!!");
		}

	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}