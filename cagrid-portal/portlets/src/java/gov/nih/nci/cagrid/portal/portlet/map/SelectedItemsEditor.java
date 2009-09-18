/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectedItemsEditor extends PropertyEditorSupport {

	private static final Log logger = LogFactory
			.getLog(SelectedItemsEditor.class);

	public SelectedItemsEditor() {
	}

	public SelectedItemsEditor(Object source) {
		super(source);
	}

	public void setAsText(String text) {
		logger.debug("Converting text '" + text + "' to List<Integer>");
		List<Integer> ids = new ArrayList<Integer>();
		if (text != null) {
			try {
				for (String idStr : text.split(",")) {
					ids.add(Integer.parseInt(idStr.trim()));
				}
			} catch (Exception ex) {
				String msg = "Error converting '" + text
						+ "' to List<Integer>: " + ex.getMessage();
				logger.error(msg, ex);
				throw new CaGridPortletApplicationException(msg, ex);
			}
			logger.debug("ids.size() = " + ids.size());
		}
		setValue(ids);
	}
}
