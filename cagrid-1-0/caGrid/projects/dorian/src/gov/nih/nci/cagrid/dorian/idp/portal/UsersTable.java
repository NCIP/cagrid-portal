package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.portal.PortalBaseTable;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: UsersTable.java,v 1.4 2005-11-22 17:45:38 langella Exp $
 */
public class UsersTable extends PortalBaseTable {
	public static String USER = "user";

	public static String USERNAME = "Username";

	public static String FIRST_NAME = "First Name";

	public static String LAST_NAME = "Last Name";

	public static String ORGANIZATION = "Organization";

	public static String EMAIL = "Email";

	public static String STATUS = "Status";

	public static String ROLE = "Role";
	
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

	public void addUser(final IdPUser u) {
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

	public synchronized IdPUser getSelectedUser() {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (IdPUser) getValueAt(row, 0);
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