/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

/**
 * @author joshua
 *
 */
public class CatalogEntryAnnotationType {
	
	private String name;
	private String label;
	private String description;
	private String terminologyUri;
	private String catalogEntryType;

	/**
	 * 
	 */
	public CatalogEntryAnnotationType() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTerminologyUri() {
		return terminologyUri;
	}

	public void setTerminologyUri(String terminologyUri) {
		this.terminologyUri = terminologyUri;
	}

	public String getCatalogEntryType() {
		return catalogEntryType;
	}

	public void setCatalogEntryType(String catalogEntryType) {
		this.catalogEntryType = catalogEntryType;
	}

}
