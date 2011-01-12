/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class BrowseCommand {
	
	private List<String> browseTypes = new ArrayList<String>();
	
	private String browseType;
	
	public BrowseCommand(){
		browseTypes.add(BrowseTypeEnum.ALL.toString());
		browseTypes.add(BrowseTypeEnum.DATASET.toString());
		browseTypes.add(BrowseTypeEnum.PERSON.toString());
		browseTypes.add(BrowseTypeEnum.TOOL.toString());
		browseTypes.add(BrowseTypeEnum.INSTITUTION.toString());
		browseTypes.add(BrowseTypeEnum.COMMUNITY.toString());		
	}

	public List<String> getBrowseTypes() {
		return browseTypes;
	}

	public void setBrowseTypes(List<String> browseTypes) {
		this.browseTypes = browseTypes;
	}

	public String getBrowseType() {
		return browseType;
	}

	public void setBrowseType(String browseType) {
		this.browseType = browseType;
	}
	
	

}
