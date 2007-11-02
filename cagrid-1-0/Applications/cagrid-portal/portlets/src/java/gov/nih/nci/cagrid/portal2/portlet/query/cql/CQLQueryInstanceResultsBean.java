/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.portlet.util.TableScroller;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CQLQueryInstanceResultsBean {

	private TableScroller tableScroller;
	private CQLQueryInstance instance;
	private String error;
	private String prettyXml;

	/**
	 * 
	 */
	public CQLQueryInstanceResultsBean() {

	}

	public CQLQueryInstance getInstance() {
		return instance;
	}

	public void setInstance(CQLQueryInstance instance) {
		this.instance = instance;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void clear() {
		setInstance(null);
		setError(null);
		setTableScroller(null);
	}

	public TableScroller getTableScroller() {
		return tableScroller;
	}

	public void setTableScroller(TableScroller tableScroller) {
		this.tableScroller = tableScroller;
	}

	public String getPrettyXml() {
		return prettyXml;
	}

	public void setPrettyXml(String prettyXml) {
		this.prettyXml = prettyXml;
	}

}
