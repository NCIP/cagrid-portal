package org.cagrid.cadsr.client;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlresultset.CQLObjectResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.client.DataServiceClient;

import java.rmi.RemoteException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.cadsr.UMLModelService;


public class CaDSRUMLModelService implements UMLModelService {

    private DataServiceClient client;


    public CaDSRUMLModelService(String address) throws MalformedURIException, RemoteException {
        this(new EndpointReferenceType(new URI(address)));
    }


    public CaDSRUMLModelService(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
        client = new DataServiceClient(epr);
    }


    public Project[] findAllProjects() throws RemoteException {
        CQLQuery query = new CQLQuery();
        Object target = new Object();
        query.setTarget(target);
        target.setName(Project.class.getName());

        CQLQueryResults results = this.client.query(query);
        CQLObjectResult[] objectResult = results.getObjectResult();
        if (objectResult == null) {
            return null;
        }

        // TODO implement
        return null;

    }


    public UMLClassMetadata[] findClassesInPackage(Project project, String packageName) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }


    public UMLPackageMetadata[] findPackagesInProject(Project project) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

}
