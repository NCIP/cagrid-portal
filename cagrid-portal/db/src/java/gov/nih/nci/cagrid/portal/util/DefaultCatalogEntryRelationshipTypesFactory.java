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
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipTypeDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRoleTypeDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class DefaultCatalogEntryRelationshipTypesFactory implements
		InitializingBean {

	private boolean enabled;
	private CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao;
	private CatalogEntryRoleTypeDao catalogEntryRoleTypeDao;
	private List<RelationshipTypeConfig> relationshipTypes = new ArrayList<RelationshipTypeConfig>();

	/**
	 * 
	 */
	public DefaultCatalogEntryRelationshipTypesFactory() {

	}

	public List<RelationshipTypeConfig> getRelationshipTypes() {
		return relationshipTypes;
	}

	public void setRelationshipTypes(
			List<RelationshipTypeConfig> relationshipTypes) {
		this.relationshipTypes = relationshipTypes;
	}
	
	public void init(){
		for(RelationshipTypeConfig config : getRelationshipTypes()){
			CatalogEntryRelationshipType relType = getCatalogEntryRelationshipTypeDao().getByName(config.getName());
			if(relType != null){
				continue;
			}
			
			CatalogEntryRelationshipType parentType = null;
			if(config.getParentName() != null){
				parentType = getCatalogEntryRelationshipTypeDao().getByName(config.getParentName());
				if(parentType == null){
					throw new RuntimeException("Parent '" + config.getParentName() + "' not found for '" + config.getName() + "'.");
				}
			}
			
			relType = new CatalogEntryRelationshipType();
			CatalogEntryRelationshipType ceRelType1 = new CatalogEntryRelationshipType();
			ceRelType1.setParent(parentType);
			ceRelType1.setName(config.getName());
			ceRelType1.setDescription(config.getRoleADescription());
			ceRelType1.setTemporal(true);
			getCatalogEntryRelationshipTypeDao().save(ceRelType1);

			CatalogEntryRoleType ceRoleType1 = new CatalogEntryRoleType();
			ceRoleType1.setType(config.getRoleAType());
			ceRoleType1.setName(config.getRoleAName());
			ceRoleType1.setDescription(config.getRoleADescription());
			ceRoleType1.setRelationshipType(ceRelType1);
			getCatalogEntryRoleTypeDao().save(ceRoleType1);

			CatalogEntryRoleType ceRoleType2 = new CatalogEntryRoleType();
			ceRoleType2.setType(config.getRoleBType());
			ceRoleType2.setName(config.getRoleBName());
			ceRoleType2.setDescription(config.getRoleBDescription());
			ceRoleType2.setRelationshipType(ceRelType1);
			getCatalogEntryRoleTypeDao().save(ceRoleType2);

			ceRelType1.setRoleTypeA(ceRoleType1);
			ceRelType1.setRoleTypeB(ceRoleType2);
			getCatalogEntryRelationshipTypeDao().save(ceRelType1);
		}
	}

	public void afterPropertiesSet() throws Exception {
		if(isEnabled()){
			init();
		}
	}

	public static class RelationshipTypeConfig {
		
		private String name;
		private String parentName;
		private String description;
		private String roleAType;
		private String roleAName;
		private String roleADescription;
		private String roleBType;
		private String roleBName;
		private String roleBDescription;
		
		public RelationshipTypeConfig(){
			
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getRoleAType() {
			return roleAType;
		}

		public void setRoleAType(String roleAType) {
			this.roleAType = roleAType;
		}

		public String getRoleAName() {
			return roleAName;
		}

		public void setRoleAName(String roleAName) {
			this.roleAName = roleAName;
		}

		public String getRoleADescription() {
			return roleADescription;
		}

		public void setRoleADescription(String roleADescription) {
			this.roleADescription = roleADescription;
		}

		public String getRoleBType() {
			return roleBType;
		}

		public void setRoleBType(String roleBType) {
			this.roleBType = roleBType;
		}

		public String getRoleBName() {
			return roleBName;
		}

		public void setRoleBName(String roleBName) {
			this.roleBName = roleBName;
		}

		public String getRoleBDescription() {
			return roleBDescription;
		}

		public void setRoleBDescription(String roleBDescription) {
			this.roleBDescription = roleBDescription;
		}

		public String getParentName() {
			return parentName;
		}

		public void setParentName(String parentName) {
			this.parentName = parentName;
		}
	}

	public CatalogEntryRelationshipTypeDao getCatalogEntryRelationshipTypeDao() {
		return catalogEntryRelationshipTypeDao;
	}

	public void setCatalogEntryRelationshipTypeDao(
			CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao) {
		this.catalogEntryRelationshipTypeDao = catalogEntryRelationshipTypeDao;
	}

	public CatalogEntryRoleTypeDao getCatalogEntryRoleTypeDao() {
		return catalogEntryRoleTypeDao;
	}

	public void setCatalogEntryRoleTypeDao(
			CatalogEntryRoleTypeDao catalogEntryRoleTypeDao) {
		this.catalogEntryRoleTypeDao = catalogEntryRoleTypeDao;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
