package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.modification.cadsr.CADSRParameterConfigurationComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.gme.GMEConfigurationPanel;
import gov.nih.nci.cagrid.introduce.portal.modification.gme.GMEParameterConfigurationComponent;

import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import org.projectmobius.portal.JComponentTable;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: OutputTypeTable.java,v 1.15 2006-02-24 20:19:05 hastings Exp $
 */
public class OutputTypeTable extends JComponentTable {

	public static String PACKAGENAME = "Package Name";

	public static String CLASSNAME = "Classname";

	public static String ISARRAY = "Is Array";

	public static String NAMESPACE = "Namespace";

	public static String TYPE = "Type";

	public static String LOCATION = "Location";
	
	public static String DATA1 = "DATA1";

	private MethodType method;

	private File schemaDir;
	
	private GMEParameterConfigurationComponent gmePanel;


	public OutputTypeTable(GMEParameterConfigurationComponent gmePanel, MethodType method, File schemaDir) {
		super(createTableModel());
		this.method = method;
		this.schemaDir = schemaDir;
		this.gmePanel = gmePanel;
		initialize();
	}


	public boolean isCellEditable(int row, int column) {
		return true;
		// if (column == 4) {
		// return true;
		// } else {
		// return false;
		// }
	}


	private void initialize() {
		this.getColumn(DATA1).setMaxWidth(0);
		this.getColumn(DATA1).setMinWidth(0);
		this.getColumn(DATA1).setPreferredWidth(0);
		MethodTypeOutput output = method.getOutput();
		final Vector v = new Vector();
		v.add(output.getPackageName());
		v.add(output.getClassName());
		if (output.getIsArray() != null) {
			v.add(String.valueOf(output.getIsArray().booleanValue()));
		} else {
			v.add("");
		}
		v.add(output.getNamespace());
		v.add(output.getType());
		v.add(output.getLocation());
		v.add(v);
		((DefaultTableModel) this.getModel()).addRow(v);
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(PACKAGENAME);
		model.addColumn(CLASSNAME);
		model.addColumn(ISARRAY);
		model.addColumn(NAMESPACE);
		model.addColumn(TYPE);
		model.addColumn(LOCATION);
		model.addColumn("DATA1");

		return model;
	}
}