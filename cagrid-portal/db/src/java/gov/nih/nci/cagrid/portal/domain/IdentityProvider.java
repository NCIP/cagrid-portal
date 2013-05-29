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
