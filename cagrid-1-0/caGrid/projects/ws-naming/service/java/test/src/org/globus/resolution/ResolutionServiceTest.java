/*
 * Portions of this file Copyright 1999-2005 University of Chicago
 * Portions of this file Copyright 1999-2005 The University of Southern California.
 *
 * This file or a portion of this file is licensed under the
 * terms of the Globus Toolkit Public License, found at
 * http://www.globus.org/toolkit/download/license.html.
 * If you redistribute this file, with or without
 * modifications, you must include this notice in the file.
 */
package org.globus.resolution;

import javax.naming.InitialContext;

import java.net.URL;

import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;

import org.globus.wsrf.encoding.ObjectDeserializer;

import org.globus.counterService.TestCounterHome;

import org.globus.test.counter.TestCounter;
import org.globus.test.counter.TestCounterServiceLocator;
import org.globus.test.counter.TestCounterServiceAddressingLocator;

import org.globus.naming.ResolveResponse;
import org.globus.naming.UnknownResourceFaultType;

import org.globus.resolution.util.ResolutionUtil;

import org.globus.resolution.impl.ResolutionHome;
import org.globus.resolution.impl.ResolutionResource;
import org.globus.resolution.impl.ResolutionService;

import org.globus.wsrf.test.GridTestCase;
import org.globus.wsrf.utils.AddressingUtils;

import org.globus.naming.Naming;
import org.globus.naming.service.OGSANamingServiceLocator;

import org.globus.wsrf.encoding.ObjectSerializer;

import org.apache.axis.message.addressing.EndpointReferenceType;

import org.globus.test.counter.GetCounterRequest;

import javax.xml.rpc.Stub;

import org.globus.wsrf.impl.security.authentication.Constants;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;

import org.apache.axis.types.URI;

public class ResolutionServiceTest extends GridTestCase {

    String TEST_SERVICE_NAME= "TestCounterService";

    public ResolutionServiceTest(String name) {
        super(name);
    }

    public void test() throws Exception {

        assertTrue(TEST_CONTAINER != null);

        // resolution service name
	String serviceUrl = TEST_CONTAINER.getBaseURL() + 
            ResolutionService.RESOLUTION_SERVICE_NAME;

        // test counter service
        String testServiceUrl = TEST_CONTAINER.getBaseURL() 
            + TEST_SERVICE_NAME;

        // Test Counter Service home and key QName
        TestCounterHome counterHome = getCounterServiceHome();
        QName testServiceKeyName = counterHome.getKeyTypeName();

        // Resolution service home
        ResolutionHome resolutionHome = 
            ResolutionUtil.getResolutionHome();

        // testServiceUrl is address to be configured in resolution
        // service home. testServiceKeyName is the keyName that needs
        // to be set.
        resolutionHome.setResolvedKeyQName(testServiceKeyName.toString());
        resolutionHome.setAddress(testServiceUrl);
        
        // Generates some EPIs
        URI epi1 = new URI("bigid://1.2.3456/eduPerson?id=10");
        URI epi2 = new URI("bigid://1.2.3456/eduPerson?id=200");
        SimpleDataService simpleDataService = 
            new SimpleDataService(new URI[] { epi1,
                                              epi2});

        // register object that implements the data service interface 
        ResolutionUtil.registerDataService(simpleDataService);

        // Access resolution service
        OGSANamingServiceLocator locator = 
            new OGSANamingServiceLocator();
        Naming naming = locator.getNamingPort(new URL(serviceUrl));

        ((Stub)naming)._setProperty(Constants.GSI_SEC_MSG,
                                    Constants.SIGNATURE);
        ((Stub)naming)._setProperty(Constants.AUTHORIZATION,
                                    new NoAuthorization());

        // try to resolve exisiting EPI
        ResolveResponse response = naming.resolve(epi1);

        EndpointReferenceType eprReturned = response.getResolvedEpr();

        System.out.println(ObjectSerializer
                           .toString(eprReturned, new QName("foo", "bar")));

        // Try to reolve an EPI that does not exist
        boolean exceptionOccured = false;
        try {
            response = naming.resolve(new URI(epi2 + "**"));
        } catch (UnknownResourceFaultType exp) {
            exceptionOccured = true;
        } catch (Exception exp) {
            System.err.println("Unexpected exception " + exp.getMessage());
        }
        assertTrue(exceptionOccured);

        // Set up the Test Counter Service, so it registered ids with the
        // object that is registered with resolution service
        counterHome.setDataServiceObject(simpleDataService);

        // Create a TestCounterService resource 
        TestCounterServiceLocator testSerLoc = new TestCounterServiceLocator();
        TestCounter testDataService = testSerLoc
            .getTestCounterPort(new URL(testServiceUrl));

        // returned epr        
        EndpointReferenceType fromServiceEPR = testDataService.create(100);

        // access test counter service resource
        TestCounterServiceAddressingLocator addrLocator = 
            new TestCounterServiceAddressingLocator();
        testDataService = addrLocator.getTestCounterPort(fromServiceEPR);

        // get value 
        int value = testDataService.getCounterValue(new GetCounterRequest());
        
        // assert it is 100
        assertTrue(value == 100);

        // Following is to extract the resource id from EPR, which in
        // a real scenario would be the EPI 
        MessageElement reqElem = null;
        MessageElement[] elems = fromServiceEPR.getProperties().get_any();
        
        for (int i=0; i<elems.length; i++) {
            
            if (elems[i].getLocalName().equals(testServiceKeyName
                                               .getLocalPart()) &&
                elems[i].getNamespaceURI().equals(testServiceKeyName
                                                  .getNamespaceURI())) {
                reqElem = elems[i];
                break;
            }
        }
        String resourceId  = 
            (String)ObjectDeserializer.toObject(reqElem, 
                                                counterHome.getKeyTypeClass());
        
        // try to resolve extracted EPI with resolution service
        response = naming.resolve(new URI(resourceId));

        //  resolved EPR
        eprReturned = response.getResolvedEpr();
        
        // access resource with resolved EPR
        testDataService = addrLocator.getTestCounterPort(eprReturned);

        // get counter value
        value = testDataService.getCounterValue(new GetCounterRequest());
        
        // should be 100
        assertTrue(value == 100);
    }

    protected TestCounterHome getCounterServiceHome() throws Exception {

        InitialContext context = new InitialContext();
        return (TestCounterHome)context
            .lookup(org.globus.wsrf.Constants
                    .JNDI_SERVICES_BASE_NAME +
                    TEST_SERVICE_NAME +
                    org.globus.wsrf.Constants.HOME_NAME);

    }
}
