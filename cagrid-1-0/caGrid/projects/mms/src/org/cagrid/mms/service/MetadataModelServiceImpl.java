package org.cagrid.mms.service;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.mms.domain.NamespaceToProjectMapping;
import org.cagrid.mms.domain.UMLAssociationExclude;
import org.cagrid.mms.domain.UMLProjectIdentifer;
import org.cagrid.mms.service.globus.resource.MetadataModelServiceResource;
import org.cagrid.mms.service.impl.MMSGeneralException;
import org.cagrid.mms.stubs.types.InvalidUMLProjectIndentifier;


/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.3
 */
public class MetadataModelServiceImpl extends MetadataModelServiceImplBase {

    protected static Log LOG = LogFactory.getLog(MetadataModelServiceImpl.class.getName());


    public MetadataModelServiceImpl() throws RemoteException {
        super();
    }


    public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForProject(
        org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer) throws RemoteException,
        org.cagrid.mms.stubs.types.InvalidUMLProjectIndentifier {
        MetadataModelServiceResource addressedResource;
        try {
            addressedResource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            String message = "Problem locating main resource of service:" + e.getMessage();
            LOG.error(message, e);
            throw new RemoteException(message);
        }

        if (umlProjectIdentifer == null) {
            InvalidUMLProjectIndentifier fault = new InvalidUMLProjectIndentifier();
            FaultHelper helper = new FaultHelper(fault);
            helper.setDescription("A null UMLProjectIdentifier cannot be used!");
            throw (InvalidUMLProjectIndentifier) helper.getFault();
        }

        try {
            return addressedResource.getMms().generateDomainModelForProject(umlProjectIdentifer);
        } catch (MMSGeneralException e) {
            // TODO: replace with typed exception?
            throw new RemoteException(e.getMessage(), e);
        }
    }


    public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForPackages(
        org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer, java.lang.String[] packageNames)
        throws RemoteException, org.cagrid.mms.stubs.types.InvalidUMLProjectIndentifier {
        MetadataModelServiceResource addressedResource;
        try {
            addressedResource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            String message = "Problem locating main resource of service:" + e.getMessage();
            LOG.error(message, e);
            throw new RemoteException(message);
        }

        if (umlProjectIdentifer == null) {
            InvalidUMLProjectIndentifier fault = new InvalidUMLProjectIndentifier();
            FaultHelper helper = new FaultHelper(fault);
            helper.setDescription("A null UMLProjectIdentifier cannot be used!");
            throw (InvalidUMLProjectIndentifier) helper.getFault();
        }

        Collection<String> packages = new ArrayList<String>();
        if (packageNames != null) {
            for (String pkg : packages) {
                packages.add(pkg);
            }
        }

        try {
            return addressedResource.getMms().generateDomainModelForPackages(umlProjectIdentifer, packages);
        } catch (MMSGeneralException e) {
            // TODO: replace with typed exception?
            throw new RemoteException(e.getMessage(), e);
        }
    }


    public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClasses(
        org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer, java.lang.String[] fullyQualifiedClassNames)
        throws RemoteException, org.cagrid.mms.stubs.types.InvalidUMLProjectIndentifier {
        MetadataModelServiceResource addressedResource;
        try {
            addressedResource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            String message = "Problem locating main resource of service:" + e.getMessage();
            LOG.error(message, e);
            throw new RemoteException(message);
        }

        if (umlProjectIdentifer == null) {
            InvalidUMLProjectIndentifier fault = new InvalidUMLProjectIndentifier();
            FaultHelper helper = new FaultHelper(fault);
            helper.setDescription("A null UMLProjectIdentifier cannot be used!");
            throw (InvalidUMLProjectIndentifier) helper.getFault();
        }

        Collection<String> classes = new ArrayList<String>();
        if (fullyQualifiedClassNames != null) {
            for (String className : fullyQualifiedClassNames) {
                classes.add(className);
            }
        }

        try {
            return addressedResource.getMms().generateDomainModelForClasses(umlProjectIdentifer, classes);
        } catch (MMSGeneralException e) {
            // TODO: replace with typed exception?
            throw new RemoteException(e.getMessage(), e);
        }
    }


    public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClassesWithExcludes(
        org.cagrid.mms.domain.UMLProjectIdentifer umlProjectIdentifer, java.lang.String[] fullyQualifiedClassNames,
        org.cagrid.mms.domain.UMLAssociationExclude[] umlAssociationExclude) throws RemoteException,
        org.cagrid.mms.stubs.types.InvalidUMLProjectIndentifier {
        MetadataModelServiceResource addressedResource;
        try {
            addressedResource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            String message = "Problem locating main resource of service:" + e.getMessage();
            LOG.error(message, e);
            throw new RemoteException(message);
        }

        if (umlProjectIdentifer == null) {
            InvalidUMLProjectIndentifier fault = new InvalidUMLProjectIndentifier();
            FaultHelper helper = new FaultHelper(fault);
            helper.setDescription("A null UMLProjectIdentifier cannot be used!");
            throw (InvalidUMLProjectIndentifier) helper.getFault();
        }

        Collection<String> classes = new ArrayList<String>();
        if (fullyQualifiedClassNames != null) {
            for (String className : fullyQualifiedClassNames) {
                classes.add(className);
            }
        }

        Collection<UMLAssociationExclude> excludes = new ArrayList<UMLAssociationExclude>();
        if (umlAssociationExclude != null) {
            for (UMLAssociationExclude exclude : umlAssociationExclude) {
                excludes.add(exclude);
            }
        }

        try {
            return addressedResource.getMms().generateDomainModelForClassesWithExcludes(umlProjectIdentifer, classes,
                excludes);
        } catch (MMSGeneralException e) {
            // TODO: replace with typed exception?
            throw new RemoteException(e.getMessage(), e);
        }
    }


    public gov.nih.nci.cagrid.metadata.ServiceMetadata annotateServiceMetadata(
        gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata,
        org.cagrid.mms.domain.NamespaceToProjectMapping[] namespaceToProjectMappings) throws RemoteException,
        org.cagrid.mms.stubs.types.InvalidUMLProjectIndentifier {
        MetadataModelServiceResource addressedResource;
        try {
            addressedResource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            String message = "Problem locating main resource of service:" + e.getMessage();
            LOG.error(message, e);
            throw new RemoteException(message);
        }

        Map<URI, UMLProjectIdentifer> mappings = new HashMap<URI, UMLProjectIdentifer>();
        if (namespaceToProjectMappings != null) {
            for (NamespaceToProjectMapping mapping : namespaceToProjectMappings) {
                try {
                    if (mapping.getUMLProjectIdentifer() == null) {
                        InvalidUMLProjectIndentifier fault = new InvalidUMLProjectIndentifier();
                        FaultHelper helper = new FaultHelper(fault);
                        helper
                            .setDescription("A null UMLProjectIdentifier cannot be used for the mapping from namespace ("
                                + mapping.getNamespaceURI() + ").");
                        throw (InvalidUMLProjectIndentifier) helper.getFault();

                    }
                    mappings.put(new URI(mapping.getNamespaceURI().toString()), mapping.getUMLProjectIdentifer());
                } catch (URISyntaxException e) {
                    String message = "Problem parsing specified URI:" + e.getMessage();
                    LOG.error(message, e);
                    throw new RemoteException(message);
                }
            }
        }

        try {
            return addressedResource.getMms().annotateServiceMetadata(serviceMetadata, mappings);
        } catch (MMSGeneralException e) {
            // TODO: replace with typed exception?
            throw new RemoteException(e.getMessage(), e);
        }
    }


    public org.cagrid.mms.domain.ModelSourceMetadata getModelSourceMetadata() throws RemoteException {
        MetadataModelServiceResource addressedResource;
        try {
            addressedResource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            String message = "Problem locating main resource of service:" + e.getMessage();
            LOG.error(message, e);
            throw new RemoteException(message);
        }

        try {
            return addressedResource.getMms().getModelSourceMetadata();
        } catch (MMSGeneralException e) {
            // TODO: replace with typed exception?
            throw new RemoteException(e.getMessage(), e);
        }
    }

}
