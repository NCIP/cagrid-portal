package org.cagrid.mms.service.impl;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.cagrid.mms.domain.ModelSourceMetadata;
import org.cagrid.mms.domain.UMLAssociationExclude;
import org.cagrid.mms.domain.UMLProjectIdentifer;


public interface MMS {

    public ModelSourceMetadata getModelSourceMetadata() throws MMSGeneralException;


    public DomainModel generateDomainModelForProject(UMLProjectIdentifer umlProjectIdentifer)
        throws MMSGeneralException;


    public DomainModel generateDomainModelForPackages(UMLProjectIdentifer umlProjectIdentifer,
        Collection<String> packageNames) throws MMSGeneralException;


    public DomainModel generateDomainModelForClasses(UMLProjectIdentifer umlProjectIdentifer,
        Collection<String> fullyQualifiedClassNames) throws MMSGeneralException;


    public DomainModel generateDomainModelForClassesWithExcludes(UMLProjectIdentifer umlProjectIdentifer,
        Collection<String> fullyQualifiiedClassNames, Collection<UMLAssociationExclude> umlAssociationExclude)
        throws MMSGeneralException;


    public ServiceMetadata annotateServiceMetadata(ServiceMetadata serviceMetadata,
        Map<URI, UMLProjectIdentifer> namespaceToProjectMappings) throws MMSGeneralException;

}