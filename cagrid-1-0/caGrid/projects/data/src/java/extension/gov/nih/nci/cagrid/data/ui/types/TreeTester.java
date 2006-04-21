package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/** 
 *  TreeTester
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 21, 2006 
 * @version $Id$ 
 */
public class TreeTester extends JFrame {
	private TargetTypesTree tree = null;
	private JScrollPane treeScrollPane = null;
	
	public TreeTester() {
		super();
		initialize();
	}
	
	
	private void initialize() {
		this.setContentPane(getJScrollPane());
		pack();
		this.setSize(new java.awt.Dimension(332,283));
		setVisible(true);
	}
	
	
	private TargetTypesTree getTree() {
		if (tree == null) {
			tree = new TargetTypesTree();
			tree.setNamespace(getNamespaces());
		}
		return tree;
	}
	
	
	private NamespaceType getNamespaces() {
		NamespaceType ns = new NamespaceType();
		ns.setNamespace("projectmobius.org/1/BookStore");
		ns.setPackageName("org.projectmobius");
		ns.setLocation(".");
		SchemaElementType[] types = new SchemaElementType[4];
		for (int i = 0; i < types.length; i++) {
			SchemaElementType type = new SchemaElementType();
			type.setClassName("Book" + i);
			type.setDeserializer("FakeDeserializer");
			type.setSerializer("FakeSerializer");
			type.setType("Book" + i);
			types[i] = type;
		}
		ns.setSchemaElement(types);
		return ns;
	}
	

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (treeScrollPane == null) {
			treeScrollPane = new JScrollPane();
			treeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tree!", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			treeScrollPane.setViewportView(getTree());
		}
		return treeScrollPane;
	}


	public static void main(String[] args) {
		JFrame tester = new TreeTester();
		tester.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
