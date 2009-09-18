/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TableScroller extends Scroller {

	private Table table;
	
	/**
	 * 
	 */
	public TableScroller() {
	}

	/**
	 * @param objects
	 * @param pageSize
	 */
	public TableScroller(Table table, int pageSize) {
		super(table.getRows(), pageSize);
		this.table = table;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

}
