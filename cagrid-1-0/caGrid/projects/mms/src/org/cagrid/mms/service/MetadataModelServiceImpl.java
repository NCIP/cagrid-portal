package org.cagrid.mms.service;

import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class MetadataModelServiceImpl extends MetadataModelServiceImplBase {

	
	public MetadataModelServiceImpl() throws RemoteException {
		super();
	}
	
  public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForProject(org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer) throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForPackages(org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer,java.lang.String[] packageNames) throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClasses(org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer,java.lang.String[] fullyQualifiedClassNames) throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClassesWithExcludes(org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer,java.lang.String[] fullyQualifiiedClassNames,org.cagrid.mms.domain.UMLAssociationExclude[] uMLAssociationExclude) throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public gov.nih.nci.cagrid.metadata.common.SemanticMetadata annotateServiceMetadata(gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata,org.cagrid.mms.domain.NamespaceToProjectMapping[] namespaceToProjectMappings) throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

}

