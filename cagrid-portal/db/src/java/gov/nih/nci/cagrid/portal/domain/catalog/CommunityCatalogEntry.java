package gov.nih.nci.cagrid.portal.domain.catalog;

import java.net.URL;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("community")
public class CommunityCatalogEntry extends CatalogEntry {
	private URL communityUrl;

	public URL getCommunityUrl() {
		return communityUrl;
	}

	public void setCommunityUrl(URL communityUrl) {
		this.communityUrl = communityUrl;
	}
}