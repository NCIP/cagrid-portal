package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.utilities.DomainModelUtils;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/** 
 *  RoleNameResolver
 *  Utility for determining role names of associations
 * 
 * @author David Ervin
 * 
 * @created Dec 20, 2007 11:40:22 AM
 * @version $Id: RoleNameResolver.java,v 1.1 2008-01-18 15:13:29 dervin Exp $ 
 */
public class RoleNameResolver {
    private static Logger LOG = Logger.getLogger(RoleNameResolver.class);
    
    private DomainModel domainModel = null;
    private Map<String, String> roleNames = null;
    
    public RoleNameResolver(DomainModel domainModel) {
        this.domainModel = domainModel;
        this.roleNames = new HashMap<String, String>();
    }
    

    /**
     * Gets the role name of an association from the perspective of the parent
     * 
     * @param parentName
     *      The name of the parent class of the association
     * @param assoc
     *      The CQL association for which a role name is to be determined
     * @return
     *      The determined role name
     * @throws QueryProcessingException
     */
    public String getRoleName(String parentName, Association assoc) throws QueryProcessingException {
        String roleKey = generateRoleNameKey(parentName, assoc);
        String roleName = roleNames.get(roleKey);
        if (roleName == null) {
            LOG.debug("Role name for " + roleKey + " not found... trying to locate");
            if (assoc.getRoleName() != null) { // role name supplied
                LOG.debug("Role name for " + roleKey + " supplied by association");
                roleName = assoc.getRoleName();
                roleNames.put(roleKey, roleName);
            } else { // look up role name
                LOG.debug("Role name for " + roleKey + " not supplied, checking domain model");
                // get the reference to the parent class
                UMLClassReference ref = DomainModelUtils.getClassReference(domainModel, parentName);
                // use the ref to find associations with the parent ref as the source
                if (ref == null) {
                    throw new QueryProcessingException("Error looking up role name. " +
                            "Could not locate class " + parentName + " in domain model");
                }
                List<String> associationRoleNames = getAssociationRoleNames(ref, assoc.getName());
                if (associationRoleNames.size() > 1) {
                    throw new QueryProcessingException("Association from " + parentName 
                        + " to " + assoc.getName() + " is ambiguous without role name specified (" 
                        + associationRoleNames.size() + " associations found)");
                } else if (associationRoleNames.size() == 0) {
                    throw new QueryProcessingException("Association from " + parentName 
                        + " to " + assoc.getName() + " was not found in the domain model");
                }
                roleName = associationRoleNames.get(0);
                roleNames.put(roleKey, roleName);
            }
        }
        
        return roleName;
    }
    
    
    private List<String> getAssociationRoleNames(
        UMLClassReference source, String associationClassname) {
        List<String> refs = new ArrayList<String>();
        if (domainModel.getExposedUMLAssociationCollection() != null &&
            domainModel.getExposedUMLAssociationCollection().getUMLAssociation() != null) {
            UMLAssociation[] associations = 
                domainModel.getExposedUMLAssociationCollection().getUMLAssociation();
            for (UMLAssociation assoc : associations)  {
                UMLClassReference sourceRef = assoc.getSourceUMLAssociationEdge()
                    .getUMLAssociationEdge().getUMLClassReference();
                if (sourceRef.getRefid().equals(source.getRefid())) {
                    UMLClassReference targetRef = assoc.getTargetUMLAssociationEdge()
                        .getUMLAssociationEdge().getUMLClassReference();
                    // resolve the target ref
                    UMLClass associatedClass = DomainModelUtils.getReferencedUMLClass(domainModel, targetRef);
                    String name = DomainModelUtils.getQualifiedClassname(associatedClass);
                    if (name.equals(associationClassname)) {
                        refs.add(assoc.getSourceUMLAssociationEdge().getUMLAssociationEdge().getRoleName());
                    }
                }
            }
        }
        return refs;
    }
    
    
    private String generateRoleNameKey(String parentName, Association assoc) {
        return parentName + "->" + assoc.getName() + " [" + assoc.getRoleName() + "]";
    }
}