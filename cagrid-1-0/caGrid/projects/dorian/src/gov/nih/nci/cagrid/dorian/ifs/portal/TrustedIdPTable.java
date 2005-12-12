package gov.nih.nci.cagrid.gums.ifs.portal;

import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.portal.PortalBaseTable;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedIdPTable.java,v 1.1 2005-12-12 21:00:41 langella Exp $
 */
public class TrustedIdPTable extends PortalBaseTable {
	public static String IDP = "idp";

	public static String IDP_ID = "IdP Id";
	
	public static String NAME = "Identity Provider Name";

	
	TrustedIdPsWindow window;

	public TrustedIdPTable(TrustedIdPsWindow window) {
		super(createTableModel());
		this.window = window;
		TableColumn c = this.getColumn(IDP);
		c.setMaxWidth(0);
		c.setMinWidth(0);
		c.setPreferredWidth(0);
		c.setResizable(false);
		
		c = this.getColumn(IDP_ID);
		c.setMaxWidth(35);
		c.setMinWidth(35);
		c.setPreferredWidth(0);

		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(IDP);
		model.addColumn(IDP_ID);
		model.addColumn(NAME);
		return model;

	}

	public void addTrustedIdP(final TrustedIdP idp) {
		Vector v = new Vector();
		v.add(idp);
		v.add(String.valueOf(idp.getId()));
		v.add(idp.getName());
		addRow(v);
	}

	public synchronized TrustedIdP getSelectedTrustedIdP() throws Exception{
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (TrustedIdP) getValueAt(row, 0);
		} else {
			throw new Exception("Please select an Identity Provider!!!");
		}
	}
	
	public synchronized void removeSelectedTrustedIdP() throws Exception{
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			removeRow(row);
		} else {
			throw new Exception("Please select an Identity Provider!!!");
		}
	}

	public void doubleClick() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			window.showTrustedIdP();
		} else {
			throw new Exception(
				"Please select an Identity Provider!!!");
		}

	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}