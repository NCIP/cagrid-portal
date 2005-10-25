package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.gums.idp.bean.User;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.projectmobius.portal.PortalTable;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: UsersTable.java,v 1.1 2005-10-25 20:21:49 langella Exp $
 */
public class UsersTable extends PortalTable {
	public static String USER = "user";

	public static String USERNAME = "Username";

	public static String FIRST_NAME = "First Name";

	public static String LAST_NAME = "Last Name";

	public static String ORGANIZATION = "Organization";

	public static String EMAIL = "Email";

	public static String STATUS = "Status";

	public static String ROLE = "Role";

	public UsersTable() {
		super(createTableModel());
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

	public void addUser(final User u) {
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

}