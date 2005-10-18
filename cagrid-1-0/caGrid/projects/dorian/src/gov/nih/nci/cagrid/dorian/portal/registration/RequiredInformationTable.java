
package gov.nih.nci.cagrid.gums.portal.registration;

import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.portal.AttributeViewer;
import gov.nih.nci.cagrid.gums.portal.GumsPortalConf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.projectmobius.portal.PortalResourceManager;
import org.projectmobius.portal.PortalTable;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: RequiredInformationTable.java,v 1.2 2005-10-18 23:23:48 langella Exp $
 */
public class RequiredInformationTable extends PortalTable{
	
	public static String NAMESPACE = "Namespace";
	public static String NAME = "Name";
	
	public List viewers;
	public Map viewersHash;
	

	public RequiredInformationTable() {
		super(createTableModel());
		viewers = new ArrayList();
		viewersHash = new HashMap();
	}


	public static DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(NAMESPACE);
		model.addColumn(NAME);
		return model;
	}


	public List getAttributeViewers(){
		return viewers;
	}
	
	public AttributeViewer getAttributeViewer(AttributeDescriptor des){
		return (AttributeViewer)viewersHash.get(des);
	}
	
	public void addRequiredInformation(AttributeDescriptor des) throws Exception {
		Vector v = new Vector();
		v.add(des.getNamespace());
		v.add(des.getName());
		GumsPortalConf conf = (GumsPortalConf)PortalResourceManager.getInstance().getResource(GumsPortalConf.RESOURCE);
		AttributeViewer viewer = conf.getAttributeViewer(des);
		viewers.add(viewer);
		viewersHash.put(des,viewer);
		addRow(v);
	}
}
