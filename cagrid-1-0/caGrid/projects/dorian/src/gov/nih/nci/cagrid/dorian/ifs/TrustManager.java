package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TrustManager extends GUMSObject {

	private Database db;

	private String table;

	private boolean dbBuilt = false;

	public TrustManager(Database db, String table){
		this.db = db;
		this.table = table;
	}

	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(table)) {
				String trust = "CREATE TABLE " + table + " ("
						+ "NAME VARCHAR(255) NOT NULL,"
						+ "IDP_SUBJECT VARCHAR(255) NOT NULL,"
						+ "IDP_CERTIFICATE TEXT NOT NULL,"
						+ "INDEX document_index (NAMESPACE,NAME));";
				db.update(trust);
				dbBuilt = true;
			}
			
		}
	}

	public boolean attributeExists(AttributeDescriptor des)
			throws GUMSInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + table
					+ " where NAMESPACE='" + des.getNamespace()
					+ "' AND NAME='" + des.getName() + "'");
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return exists;
	}

	public AttributeDescriptor[] getRequiredAttributes()
			throws GUMSInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + table);
			List atts = new ArrayList();
			while (rs.next()) {
				AttributeDescriptor des = new AttributeDescriptor();
				des.setNamespace(rs.getString("NAMESPACE"));
				des.setName(rs.getString("NAME"));
				atts.add(des);
			}
			rs.close();
			s.close();

			AttributeDescriptor[] attributes = new AttributeDescriptor[atts
					.size()];
			for (int i = 0; i < atts.size(); i++) {
				attributes[i] = (AttributeDescriptor) atts.get(i);
			}
			return attributes;

		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
	}

	public void insertRequiredAttribute(AttributeDescriptor des)
			throws GUMSInternalFault {
		buildDatabase();
		if (!attributeExists(des)) {
			db.update("INSERT INTO " + table + " SET NAMESPACE='"
					+ des.getNamespace() + "',NAME='" + des.getName() + "'");
		}
	}

	public void removeRequiredAttribute(AttributeDescriptor des)
			throws GUMSInternalFault {
		buildDatabase();
		if (attributeExists(des)) {
			db.update("delete from " + table + " WHERE NAMESPACE='"
					+ des.getNamespace() + "' AND NAME='" + des.getName() + "'");
		}
	}

	public void removeAllAttributes() throws GUMSInternalFault {
		buildDatabase();
		db.update("delete from " + table);
	}

	public void destroyTable() throws GUMSInternalFault {
		db.update("DROP TABLE IF EXISTS " + table);
		dbBuilt = false;
	}

}