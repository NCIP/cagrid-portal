package gov.nih.nci.cagrid.introduce.portal.modification;

import gov.nih.nci.cagrid.introduce.portal.AnalyticalLookAndFeel;

import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import org.jdom.Element;
import org.projectmobius.portal.JComponentTable;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: OutputTypeTable.java,v 1.4 2005-11-18 19:47:54 hastings Exp $
 */
public class OutputTypeTable extends JComponentTable {

	public static String CLASSNAME = "Classname";
	public static String NAMESPACE = "Namespace";
	public static String TYPE = "Type";
	public static String LOCATION = "Location";
	public static String GME = "Get Type From GME";

	private Element method;
	private File schemaDir;

	public OutputTypeTable(Element method, File schemaDir) {
		super(createTableModel());
		this.method = method;
		this.schemaDir = schemaDir;
		initialize();
	}

	public boolean isCellEditable(int row, int column) {
		if (column == 4) {
			return true;
		} else {
			return false;
		}
	}

	private void initialize() {
		Element output = method.getChild("output", method.getNamespace());
		final Vector v = new Vector();
		v.add(output.getAttributeValue("className"));
		v.add(output.getAttributeValue("namespace"));
		v.add(output.getAttributeValue("type"));
		v.add(output.getAttributeValue("location"));
		JButton gme = new JButton("GME");
		// gme.setIcon(AnalyticalLookAndFeel.getMobiusIcon());
		gme.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				PortalResourceManager.getInstance().getGridPortal()
						.addGridPortalComponent(
								new GMEParameterConfigurationComponent(v,
										schemaDir, false));
				editCellAt(0, Integer.MAX_VALUE);
			}
		});
		v.add(gme);
		((DefaultTableModel) this.getModel()).addRow(v);
	}

	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(CLASSNAME);
		model.addColumn(NAMESPACE);
		model.addColumn(TYPE);
		model.addColumn(LOCATION);
		model.addColumn(GME);

		return model;
	}
}