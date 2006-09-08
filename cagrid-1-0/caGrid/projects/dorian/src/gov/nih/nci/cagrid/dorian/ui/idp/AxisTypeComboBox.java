package gov.nih.nci.cagrid.dorian.ui.idp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class AxisTypeComboBox extends JComboBox {

	private List list;
	private Class c;

	public AxisTypeComboBox(Class c) {
		this(c, false);
	}

	public AxisTypeComboBox(Class c,boolean any) {
		list = new ArrayList();
		this.c = c;

		if (any) {
			list.add("Any");
		}

		Field[] fields = c.getFields();

		for (int i = 0; i < fields.length; i++) {
			if (c.isAssignableFrom(fields[i].getType())) {
				try {
					Object o = fields[i].get(null);
					list.add(o);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			this.addItem(list.get(i));
		}
	}

	public Object getSelectedObject() {
		if (getSelectedItem().getClass().isAssignableFrom(c)) {
			return getSelectedItem();
		} else {
			return null;
		}
	}


}
