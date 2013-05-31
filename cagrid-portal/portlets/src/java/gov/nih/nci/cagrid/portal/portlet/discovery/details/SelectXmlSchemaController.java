/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.dao.XMLSchemaDao;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectXmlSchemaController extends
		AbstractDiscoverySelectDetailsController {

	private XMLSchemaDao xmlSchemaDao;
	
	/**
	 * 
	 */
	public SelectXmlSchemaController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectXmlSchemaController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectXmlSchemaController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.discovery.details.AbstractDiscoverySelectDetailsController#doSelect(gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel, java.lang.Integer)
	 */
	@Override
	protected void doSelect(DiscoveryModel model, Integer selectedId) {
		XMLSchema xmlSchema = getXmlSchemaDao().getById(selectedId);
		model.setSelectedXmlSchema(xmlSchema);
	}

	public XMLSchemaDao getXmlSchemaDao() {
		return xmlSchemaDao;
	}

	public void setXmlSchemaDao(XMLSchemaDao xmlSchemaDao) {
		this.xmlSchemaDao = xmlSchemaDao;
	}

}
