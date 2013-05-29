/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class UMLClassSemanticMetadataBasedForeignTargetsProvider implements
		ForeignTargetsProvider {

	private UMLClassDao umlClassDao;
	
	/**
	 * 
	 */
	public UMLClassSemanticMetadataBasedForeignTargetsProvider() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.query.builder.ForeignTargetsProvider#getSemanticallyEquivalentClasses(gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass)
	 */
	public List<UMLClass> getSemanticallyEquivalentClasses(UMLClass example) {
		return getUmlClassDao().getClassesWithSameConceptCode(example);
	}

	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}

}
