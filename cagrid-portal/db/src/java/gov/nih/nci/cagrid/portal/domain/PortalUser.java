/**
 *
 */
package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.Comment;
import gov.nih.nci.cagrid.portal.domain.catalog.Commentable;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Entity
@Table(name = "portal_users")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_portal_users")})
public class PortalUser extends AbstractDomainObject implements Commentable {

    private Person person;

    private String portalId;

    private String gridCredential;

    private String gridIdentity;

    private String delegatedEPR;

    private List<QueryInstance> queryInstances = new ArrayList<QueryInstance>();

    private List<SharedCQLQuery> sharedQueries = new ArrayList<SharedCQLQuery>();

    private NotificationSubscriber subscriber;


    private PersonCatalogEntry catalog;

    private List<Comment> comments = new ArrayList<Comment>();

    private List<CatalogEntry> catalogEntries = new ArrayList<CatalogEntry>();

    private List<IdPAuthentication> authentications = new ArrayList<IdPAuthentication>();
    
    private AuthnTicket authnTicket;


    @OneToOne(mappedBy = "portalUser", cascade = CascadeType.ALL)
    public AuthnTicket getAuthnTicket() {
		return authnTicket;
	}

	public void setAuthnTicket(AuthnTicket authnTicket) {
		this.authnTicket = authnTicket;
	}

	@ManyToOne
    @JoinColumn(name = "person_id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Lob
    public String getGridCredential() {
        return gridCredential;
    }

    public void setGridCredential(String gridCredential) {
        this.gridCredential = gridCredential;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "portalUser")
    @OrderBy("startTime desc")
    public List<QueryInstance> getQueryInstances() {
        return queryInstances;
    }

    public void setQueryInstances(List<QueryInstance> queryInstances) {
        this.queryInstances = queryInstances;
    }

    public String getGridIdentity() {
        return gridIdentity;
    }

    public void setGridIdentity(String gridIdentity) {
        this.gridIdentity = gridIdentity;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    @OrderBy("shareDate desc")
    public List<SharedCQLQuery> getSharedQueries() {
        return sharedQueries;
    }

    public void setSharedQueries(List<SharedCQLQuery> sharedQueries) {
        this.sharedQueries = sharedQueries;
    }

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "portalUser")
    public NotificationSubscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(NotificationSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Lob
    public String getDelegatedEPR() {
        return delegatedEPR;
    }

    public void setDelegatedEPR(String delegatedEPR) {
        this.delegatedEPR = delegatedEPR;
    }

    //delete all catalogs for the user
    @OneToOne(mappedBy = "about", cascade = CascadeType.ALL)
    public PersonCatalogEntry getCatalog() {
        return catalog;
    }

    public void setCatalog(PersonCatalogEntry catalog) {
        this.catalog = catalog;
    }

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<CatalogEntry> getCatalogEntries() {
        return catalogEntries;
    }

    public void setCatalogEntries(List<CatalogEntry> catalogEntries) {
        this.catalogEntries = catalogEntries;
    }

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.ALL)
    public List<IdPAuthentication> getAuthentications() {
        return authentications;
    }

    public void setAuthentications(List<IdPAuthentication> authentications) {
        this.authentications = authentications;
    }
}
