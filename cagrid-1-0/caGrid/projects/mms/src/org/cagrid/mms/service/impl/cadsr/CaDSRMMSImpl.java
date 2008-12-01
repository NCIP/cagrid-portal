package org.cagrid.mms.service.impl.cadsr;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.cagrid.mms.domain.ModelSourceMetadata;
import org.cagrid.mms.domain.UMLAssociationExclude;
import org.cagrid.mms.domain.UMLProjectIdentifer;
import org.cagrid.mms.service.impl.MMS;
import org.cagrid.mms.service.impl.MMSGeneralException;


public class CaDSRMMSImpl implements MMS {

    private String caDSRApplicationServiceURL;


    public String getCaDSRApplicationServiceURL() {
        return caDSRApplicationServiceURL;
    }


    public void setCaDSRApplicationServiceURL(String caDSRApplicationServiceURL) {
        this.caDSRApplicationServiceURL = caDSRApplicationServiceURL;
    }


    public ModelSourceMetadata getModelSourceMetadata() {
        // TODO: needs to specify the caDSR Source
        return null;
    }


    public ServiceMetadata annotateServiceMetadata(ServiceMetadata serviceMetadata,
        Map<URI, UMLProjectIdentifer> namespaceToProjectMappings) throws MMSGeneralException {
        ApplicationService appService = null;
        try {
            appService = ApplicationServiceProvider.getApplicationServiceFromUrl(getCaDSRApplicationServiceURL());
        } catch (Exception e) {
            throw new MMSGeneralException("Problem loading caDSR ApplicationService", e);
        }
        //TODO: deal with namespaceToProjectMappings
        ServiceMetadataAnnotator annotator = new ServiceMetadataAnnotator(appService);
        try {
            annotator.annotateServiceMetadata(serviceMetadata);
        } catch (CaDSRGeneralException e) {
            throw new MMSGeneralException("Problem from remote caDSR (" + getCaDSRApplicationServiceURL() + "):"
                + e.getMessage(), e);
        }
        return serviceMetadata;
    }


    public DomainModel generateDomainModelForClasses(UMLProjectIdentifer umlProjectIdentifer,
        Collection<String> fullyQualifiedClassNames) {
        // TODO Auto-generated method stub
        return null;
    }


    public DomainModel generateDomainModelForClassesWithExcludes(UMLProjectIdentifer umlProjectIdentifer,
        Collection<String> fullyQualifiiedClassNames, Collection<UMLAssociationExclude> umlAssociationExclude) {
        // TODO Auto-generated method stub
        return null;
    }


    public DomainModel generateDomainModelForPackages(UMLProjectIdentifer umlProjectIdentifer,
        Collection<String> packageNames) {
        // TODO Auto-generated method stub
        return null;
    }


    public DomainModel generateDomainModelForProject(UMLProjectIdentifer umlProjectIdentifer) {
        // TODO Auto-generated method stub
        return null;
    }

}
