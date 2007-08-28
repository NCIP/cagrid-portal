package gov.nci.nih.cagrid.validator.builder;

import gov.nih.nci.cagrid.tests.core.beans.validation.ServiceDescription;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** 
 *  ServiceTable
 *  Table to display / edit services for a deployment validation configuration
 * 
 * @author David Ervin
 * 
 * @created Aug 28, 2007 12:43:19 PM
 * @version $Id: ServiceTable.java,v 1.1 2007-08-28 20:38:55 dervin Exp $ 
 */
public class ServiceTable extends JTable {
    private DefaultTableModel model;

    public ServiceTable() {
        super();
        model = new DefaultTableModel();
        model.addColumn("Service Name");
        model.addColumn("Type");
        model.addColumn("URL");
        setModel(model);
        getColumnModel().getColumn(0).setPreferredWidth(40);
        getColumnModel().getColumn(1).setPreferredWidth(20);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    
    
    public void addService(final ServiceDescription service) {
        Vector row = new Vector(3);
        row.add(service.getServiceName());
        row.add(service.getServiceType().toString());
        row.add(service.getServiceUrl().toString());
    }
}
