package gov.nih.nci.cagrid.dorian.ifs.portal;

import gov.nih.nci.cagrid.dorian.portal.PortalBaseTable;

import java.security.cert.X509Certificate;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: CertificateTable.java,v 1.4 2006-01-11 19:50:28 langella Exp $
 */
public class CertificateTable extends PortalBaseTable {
	public static String CERTIFICATE = "certificate";

	public static String SUBJECT = "Subject";

	public static String EXPIRES = "Expires";


	public CertificateTable() {
		super(createTableModel());
		TableColumn c = this.getColumn(CERTIFICATE);
		c.setMaxWidth(0);
		c.setMinWidth(0);
		c.setPreferredWidth(0);
		c.setResizable(false);
		c = this.getColumn(EXPIRES);
		c.setMaxWidth(175);
		c.setMinWidth(175);
		c.setPreferredWidth(0);
		this.clearTable();

	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(CERTIFICATE);
		model.addColumn(SUBJECT);
		model.addColumn(EXPIRES);
		return model;

	}

	public void addCertificate(final X509Certificate cert) {
		Vector v = new Vector();
		v.add(cert);
		v.add(cert.getSubjectDN().getName());
		v.add(cert.getNotAfter());
		addRow(v);
	}

	public synchronized X509Certificate getSelectedCertificate() {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			return (X509Certificate) getValueAt(row, 0);
		} else {
			return null;
		}
	}

	public void doubleClick() throws Exception {
		int row = getSelectedRow();
		if ((row >= 0) && (row < getRowCount())) {
			PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new CertificateInformationComponent(getSelectedCertificate()),600,425);	
		} else {
			throw new Exception(
					"No certificate selected, please select a certificate!!!");
		}

	}

	public void singleClick() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}