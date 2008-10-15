package org.cagrid.gme.test.system.steps;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gme.client.GlobalModelExchangeClient;
import org.cagrid.gme.domain.XMLSchemaBundle;
import org.cagrid.gme.domain.XMLSchemaImportInformation;


public class CheckGetSchemaBundlesStep extends Step {

    private final EndpointReferenceType gmeEPR;
    private final Collection<XMLSchemaImportInformation> iis;


    public CheckGetSchemaBundlesStep(EndpointReferenceType gmeEPR,
        Collection<XMLSchemaImportInformation> importInformations) {
        this.gmeEPR = gmeEPR;
        this.iis = importInformations;
    }


    @Override
    public void runStep() throws RemoteException, MalformedURIException {
        assertNotNull("A non-null EPR must be passed in.", this.gmeEPR);
        assertNotNull("A non-null XMLSchemaInformation must be passed in.", this.iis);

        GlobalModelExchangeClient gme = new GlobalModelExchangeClient(this.gmeEPR);

        for (XMLSchemaImportInformation ii : this.iis) {
            XMLSchemaBundle retrievedSchemaBundle = gme.getXMLSchemaAndDependencies(ii.getTargetNamespace());

            assertEquals("The retrieved schema bundle did contain the expected schema import information.", ii,
                retrievedSchemaBundle.getImportInformationForTargetNamespace(ii.getTargetNamespace()));
        }
    }
}
