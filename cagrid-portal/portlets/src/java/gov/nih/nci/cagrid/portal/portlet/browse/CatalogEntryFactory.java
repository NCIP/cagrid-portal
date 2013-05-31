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
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class CatalogEntryFactory {

	private Map<String,String> entryTypeMap = new HashMap<String,String>();
	
	/**
	 * 
	 */
	public CatalogEntryFactory() {

	}
	
	public CatalogEntry newCatalogEntry(String logicalTypeName){
		CatalogEntry ce = null;
		String classname = getEntryTypeMap().get(logicalTypeName);
		if(classname == null){
			throw new RuntimeException("Unknown catalog entry type: " + logicalTypeName);
		}
		try{
			ce = (CatalogEntry)Class.forName(classname).newInstance();
		}catch(Exception ex){
			throw new RuntimeException("Error creating catalog entry: " + ex.getMessage(), ex);
		}
		
		return ce;
	}

	public Map<String, String> getEntryTypeMap() {
		return entryTypeMap;
	}

	public void setEntryTypeMap(Map<String, String> entryTypeMap) {
		this.entryTypeMap = entryTypeMap;
	}

}
