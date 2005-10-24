package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.gums.idp.bean.StateCode;

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
public class StateListComboBox extends JComboBox {

	private static List states;


	public StateListComboBox() {
		if(states == null){
			states = new ArrayList();
			Field[] fields = StateCode.class.getFields();

			for (int i = 0; i < fields.length; i++) {
				if (StateCode.class.isAssignableFrom(fields[i].getType())) {
						try {
							StateCode code = (StateCode) fields[i].get(null);
							states.add(code);
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}
		}
		for(int i=0; i<states.size(); i++){
			this.addItem(states.get(i));
		}
	}

	public StateCode getSelectedState() {
		return (StateCode) getSelectedItem();
	}

	public static void main(String[] args) {
		new StateListComboBox();
	}

}
