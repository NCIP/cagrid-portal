/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.builder;

import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryCommand;

import javax.portlet.RenderRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewCqlXmlController extends AbstractQueryRenderController {

	/**
	 * 
	 */
	public ViewCqlXmlController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		CQLQueryCommand command = getQueryModel().getWorkingQuery();
		if(command == null){
			command = new CQLQueryCommand();
		}
		return command;
	}

}
