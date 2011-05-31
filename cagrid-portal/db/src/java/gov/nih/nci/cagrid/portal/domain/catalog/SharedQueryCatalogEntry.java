package gov.nih.nci.cagrid.portal.domain.catalog;

import gov.nih.nci.cagrid.portal.domain.dataservice.Query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("tool_shared_query")
public class SharedQueryCatalogEntry extends ToolCatalogEntry {

	public List<CriterionDescriptor> criteria = new ArrayList<CriterionDescriptor>();
	public List<QueryResultColumnDescriptor> columns = new ArrayList<QueryResultColumnDescriptor>();
	public List<SharedQueryToolsRelationship> toolRelationships = new ArrayList<SharedQueryToolsRelationship>();
	public List<Term> typesOfCancer = new ArrayList<Term>();
	private Query about;

	// private GridServiceEndPointCatalogEntry defaultService;
	// private boolean defaultServiceEditable;

	@OneToMany(mappedBy = "query")
	public List<CriterionDescriptor> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<CriterionDescriptor> criteria) {
		this.criteria = criteria;
	}

	@OneToMany(mappedBy = "query")
	public List<QueryResultColumnDescriptor> getColumns() {
		return columns;
	}

	public void setColumns(List<QueryResultColumnDescriptor> columns) {
		this.columns = columns;
	}

	@OneToMany(mappedBy = "sharedQuery")
	public List<SharedQueryToolsRelationship> getToolRelationships() {
		return toolRelationships;
	}

	public void setToolRelationships(
			List<SharedQueryToolsRelationship> toolRelationships) {
		this.toolRelationships = toolRelationships;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<Term> getTypesOfCancer() {
		return typesOfCancer;
	}

	public void setTypesOfCancer(List<Term> typesOfCancer) {
		this.typesOfCancer = typesOfCancer;
	}

	@OneToOne
	@JoinColumn(name = "query_id")
	public Query getAbout() {
		return about;
	}

	public void setAbout(Query about) {
		this.about = about;
	}

	// @ManyToOne
	// @JoinColumn(name = "default_service_id")
	// public GridServiceEndPointCatalogEntry getDefaultService() {
	// return defaultService;
	// }
	//
	// public void setDefaultService(GridServiceEndPointCatalogEntry
	// defaultService) {
	// this.defaultService = defaultService;
	// }
	//
	// public boolean isDefaultServiceEditable() {
	// return defaultServiceEditable;
	// }
	//
	// public void setDefaultServiceEditable(boolean defaultServiceEditable) {
	// this.defaultServiceEditable = defaultServiceEditable;
	// }
}