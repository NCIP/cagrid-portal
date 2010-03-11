/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "idp")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_idp")})
public class IdentityProvider extends AbstractDomainObject {
	
	private String label;
	private String url;
	private List<IdPAuthentication> authentications = new ArrayList<IdPAuthentication>();

	/**
	 * 
	 */
	public IdentityProvider() {

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@OneToMany(mappedBy = "identityProvider", cascade = CascadeType.ALL)
	public List<IdPAuthentication> getAuthentications() {
		return authentications;
	}

	public void setAuthentications(List<IdPAuthentication> authentications) {
		this.authentications = authentications;
	}

}
