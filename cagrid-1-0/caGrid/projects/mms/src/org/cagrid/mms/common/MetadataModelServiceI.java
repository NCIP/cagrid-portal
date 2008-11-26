package org.cagrid.mms.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public interface MetadataModelServiceI {

  /**
   * Generates a complete DomainModel for the specified Project
   *
   * @param umlProjectIdentifer
   *	The project to generate a DomainModel for.
   */
  public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForProject(org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer) throws RemoteException ;

  /**
   * Generates a DomainModel for the specified Project, including only the specified packages
   *
   * @param umlProjectIdentifer
   *	The project to generate a DomainModel for
   * @param packageNames
   *	The package names to include
   */
  public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForPackages(org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer,java.lang.String[] packageNames) throws RemoteException ;

  /**
   * Generates a complete DomainModel for the specified Project, including only the identified Classes
   *
   * @param umlProjectIdentifer
   *	The project to generate a DomainModel for
   * @param fullyQualifiedClassNames
   *	The fully qualified classnames to include in the model
   */
  public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClasses(org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer,java.lang.String[] fullyQualifiedClassNames) throws RemoteException ;

  /**
   * Generates a  DomainModel for the specified Project, including only the specified Classes, but excluding the specified Associations
   *
   * @param umlProjectIdentifer
   *	The project to generate a DomainModel for
   * @param fullyQualifiiedClassNames
   *	The fully qualified Class names to include in the model
   * @param uMLAssociationExclude
   *	A collection of UML Associations to exclude from the model
   */
  public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClassesWithExcludes(org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer,java.lang.String[] fullyQualifiiedClassNames,org.cagrid.mms.domain.UMLAssociationExclude[] uMLAssociationExclude) throws RemoteException ;

  /**
   * Annotates the specified ServiceMetadata model with semantic information from the specified model, by mapping QNames to UMLClasses
   *
   * @param serviceMetadata
   *	The unannotated ServiceMetadata model
   * @param namespaceToProjectMappings
   *	An optional collection of mappings from URI Namespace to UMLProjects
   */
  public gov.nih.nci.cagrid.metadata.common.SemanticMetadata annotateServiceMetadata(gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata,org.cagrid.mms.domain.NamespaceToProjectMapping[] namespaceToProjectMappings) throws RemoteException ;

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException ;

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException ;

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException ;

}

