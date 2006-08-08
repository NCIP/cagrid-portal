package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalBaseTable;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: StemPrivilegesTable.java,v 1.2 2006-08-08 17:11:44 langella Exp $
 */
public class StemPrivilegesTable extends PortalBaseTable {
	public final static String CADDY = "Caddy";

	public final static String IDENTITY = "Identity";

	public final static String CREATOR = "Create";

	public final static String STEMMER = "Stem";

	public StemPrivilegesTable() {
		super(createTableModel());
		TableColumn c = this.getColumn(CADDY);
		c.setMaxWidth(0);
		c.setMinWidth(0);
		c.setPreferredWidth(0);
		c.setResizable(false);
		
		c = this.getColumn(CREATOR);
		c.setMinWidth(60);
		c.setMaxWidth(60);
		c.setPreferredWidth(0);
		
		c = this.getColumn(STEMMER);
		c.setMinWidth(60);
		c.setMaxWidth(60);
		c.setPreferredWidth(0);

		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(CADDY);
		model.addColumn(IDENTITY);
		model.addColumn(STEMMER);
		model.addColumn(CREATOR);
		return model;

	}

	public void addPrivilege(final StemPrivilegeCaddy priv) {
		Vector v = new Vector();
		v.add(priv);
		v.add(priv.getIdentity());
		v.add(Boolean.valueOf(priv.hasStem()));
		v.add(Boolean.valueOf(priv.hasCreate()));
		addRow(v);
	}

	public synchronized StemPrivilegeCaddy getSelectedPrivilege()
			throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (StemPrivilegeCaddy) getValueAt(row, 0);
		} else {
			throw new Exception("Please select a privilege!!!");
		}
	}

	public synchronized void removeSelectedPrivilege() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			removeRow(row);
		} else {
			throw new Exception("Please select a user!!!");
		}
	}

	public void doubleClick() throws Exception {

	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub

	}

}