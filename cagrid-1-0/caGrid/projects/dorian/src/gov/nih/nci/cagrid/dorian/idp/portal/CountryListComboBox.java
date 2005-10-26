package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.gums.idp.bean.CountryCode;

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
public class CountryListComboBox extends JComboBox {

	private List countries = null;

	public CountryListComboBox() {
		this(false);
	}

	public CountryListComboBox(boolean anyCountry) {

		countries = new ArrayList();

		if (anyCountry) {
			countries.add("Any");
		}
		Field[] fields = CountryCode.class.getFields();

		for (int i = 0; i < fields.length; i++) {
			if (CountryCode.class.isAssignableFrom(fields[i].getType())) {
				try {
					CountryCode code = (CountryCode) fields[i].get(null);
					countries.add(code);
				} catch (Exception e) {
					e.getMessage();
				}
			}
		}

		for (int i = 0; i < countries.size(); i++) {
			this.addItem(countries.get(i));
		}
		if (!anyCountry) {
			setSelectedItem(CountryCode.US);
		}
	}

	public CountryCode getSelectedCountry() {
		if (getSelectedItem() instanceof CountryCode) {
			return (CountryCode) getSelectedItem();
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		new CountryListComboBox();
	}

}
