/**
 *
 */
package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.ForceDiscriminator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
@Entity
@Table(name = "grid_services")
@GenericGenerator(name = "id-generator", strategy = "native",
        parameters = {
                @Parameter(name = "sequence", value = "seq_grid_services")
        }
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "service_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("GridService")
@ForceDiscriminator
public class GridService extends AbstractDomainObject {

    private List<IndexService> indexServices = new ArrayList<IndexService>();
    private String url;
    private ServiceMetadata serviceMetadata;
    private List<ServiceAnnotation> annotations = new ArrayList<ServiceAnnotation>();
    private String metadataHash;
    private List<StatusChange> statusHistory = new ArrayList<StatusChange>();
    private List<SemanticMetadataMapping> semanticMetadataMappings = new ArrayList<SemanticMetadataMapping>();
    private String conceptIndexHash;
    private List<NotificationSubscription> subscriptions = new ArrayList<NotificationSubscription>();
    private GridServiceEndPointCatalogEntry catalog;
    private List<GridServiceUmlClass> gridServiceUmlClasses = new ArrayList<GridServiceUmlClass>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    public List<ServiceAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<ServiceAnnotation> annotations) {
        this.annotations = annotations;
    }


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "service")
    public ServiceMetadata getServiceMetadata() {
        return serviceMetadata;
    }

    public void setServiceMetadata(ServiceMetadata serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ManyToMany(
            mappedBy = "services"
    )
    public List<IndexService> getIndexServices() {
        return indexServices;
    }

    public void setIndexServices(List<IndexService> indexServices) {
        this.indexServices = indexServices;
    }

    public String getMetadataHash() {
        return metadataHash;
    }

    public void setMetadataHash(String metadataHash) {
        this.metadataHash = metadataHash;
    }

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @Filter(name = "archivedStatus", condition = "archived = :archivedFlag")
    @OrderBy("time")
    public List<StatusChange> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<StatusChange> statusHistory) {
        this.statusHistory = statusHistory;
    }

    @Transient
    public ServiceStatus getCurrentStatus() {
        List<StatusChange> history = getStatusHistory();
        if (history.size() < 1)
            return ServiceStatus.UNKNOWN;
        return history.get(history.size() - 1).getStatus();
    }

    @OneToMany(mappedBy = "gridService", cascade = CascadeType.ALL)
    public List<SemanticMetadataMapping> getSemanticMetadataMappings() {
        return semanticMetadataMappings;
    }

    public void setSemanticMetadataMappings(
            List<SemanticMetadataMapping> semanticMetadataMappings) {
        this.semanticMetadataMappings = semanticMetadataMappings;
    }

    public String getConceptIndexHash() {
        return conceptIndexHash;
    }

    public void setConceptIndexHash(String conceptIndexHash) {
        this.conceptIndexHash = conceptIndexHash;
    }

    @Transient
    public ServiceInfo getServiceInfo() {
        return new ServiceInfo(this);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service")
    public List<NotificationSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<NotificationSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @OneToOne(mappedBy = "about", cascade = CascadeType.REMOVE)
    public GridServiceEndPointCatalogEntry getCatalog() {
        return catalog;
    }

    public void setCatalog(GridServiceEndPointCatalogEntry catalog) {
        this.catalog = catalog;
    }
    
    
    @OneToMany(mappedBy = "gridService" , cascade = CascadeType.ALL)
    public List<GridServiceUmlClass> getGridServiceUmlClasses() {
        return gridServiceUmlClasses;
    }

	public void setGridServiceUmlClasses(
			List<GridServiceUmlClass> gridServiceUmlClasses) {
		this.gridServiceUmlClasses = gridServiceUmlClasses;
	}
}
