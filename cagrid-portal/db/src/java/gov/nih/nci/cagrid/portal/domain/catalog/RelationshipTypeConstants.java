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
package gov.nih.nci.cagrid.portal.domain.catalog;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class RelationshipTypeConstants {
	public static final String GRID_SERVICE_ENDPOINT_IMPLEMENTS_GRID_SERVICE_INTERFACE = "Grid Service Endpoint Implements Grid Service Interface";
	public static final String PERSON_POC_FOR_GRID_SERVICE_ENDPOINT = "Person Point-of-Contact for Grid Service Endpoint";
	public static final String HOSTING_INSTITUTION_OF_GRID_SERVICE_ENDPOINT = "Hosting Institution of Grid Service Endpoint";
	public static final String PERSON_POC_FOR_INSTITUTION = "Person is Point-of-Contact for Institution";
	public static final String GRID_SERVICE_ENDPOINT_SUPPORTS_INFORMATION_MODEL = "Grid Service Endpoint Supports Information Model";
	public static final String GRID_SERVICE_INTERFACE_SUPPORTS_INFORMATION_MODEL = "Grid Service Interface Supports Information Model";
}
