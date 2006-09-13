package gov.nih.nci.cagrid.data.utilities.query.cqltree;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;

import javax.swing.Icon;

/** 
 *  QueryTreeNode
 *  CQL Query Node
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 11, 2006 
 * @version $Id$ 
 */
public class QueryTreeNode extends IconTreeNode {

	private CQLQuery query;
	
	public QueryTreeNode(CQLQuery query) {
		this.query = query;
		setUserObject("CQL Query");
		rebuild();
	}
	
	
	public CQLQuery getQuery() {
		return this.query;
	}
	
	
	public Icon getIcon() {
		return PortalLookAndFeel.getQueryIcon();
	}
	
	
	public void rebuild() throws IllegalStateException {
		TargetTreeNode targetNode = new TargetTreeNode(query.getTarget());
		add(targetNode);
		if (query.getQueryModifier() != null) {
			//
		}
	}
}
