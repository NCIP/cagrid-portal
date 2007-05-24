/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("GridPortalUser")
public class GridPortalUser extends PortalUser {
	private String idpUrl;

	public String getIdpUrl() {
		return idpUrl;
	}

	public void setIdpUrl(String idpUrl) {
		this.idpUrl = idpUrl;
	}
}
