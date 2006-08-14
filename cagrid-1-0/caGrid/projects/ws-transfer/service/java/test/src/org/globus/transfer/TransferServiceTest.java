/*
 * Copyright 1999-2006 University of Chicago
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.globus.transfer;

import org.apache.axis.message.MessageElement;

import javax.xml.namespace.QName;

import org.globus.transfer.EmptyType;
import org.globus.transfer.AnyXmlType;
import org.globus.transfer.Resource;

import org.globus.wsrf.test.GridTestCase;

import org.globus.transfer.impl.TransferHome;
import org.globus.transfer.impl.TransferResource;
import org.globus.transfer.impl.TransferService;

import org.globus.transfer.util.TransferUtil;

import org.globus.transfer.EmptyType;

import org.globus.transfer.service.TransferServiceLocator;
import org.globus.transfer.service.TransferServiceAddressingLocator;

import java.net.URL;

import org.globus.wsrf.utils.AddressingUtils;

import org.globus.wsrf.impl.security.authentication.Constants;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;

import javax.xml.rpc.Stub;

import javax.naming.InitialContext;

import org.apache.axis.types.URI;

import org.apache.axis.message.addressing.EndpointReferenceType;

import org.globus.wsrf.impl.SimpleResourceKey;

public class TransferServiceTest extends GridTestCase {

    public TransferServiceTest(String name) {
        super(name);
    }

    public void test() throws Exception {

        assertTrue(TEST_CONTAINER != null);

        // transfer service name
	String serviceUrl = TEST_CONTAINER.getBaseURL() + 
            TransferService.TRANSFER_SERVICE_NAME;
        
        TransferServiceLocator locator = new TransferServiceLocator();
        Resource resource = locator.getResourcePort(new URL(serviceUrl));

        ((Stub)resource)._setProperty(Constants.GSI_SEC_MSG,
                                    Constants.SIGNATURE);
        ((Stub)resource)._setProperty(Constants.AUTHORIZATION,
                                    new NoAuthorization());

        //  Try to get data. This should return a null. No resource key
        AnyXmlType returnVal = resource.get(new EmptyType());

        assertTrue(returnVal == null);

        // create an instance of test data service
        TestDataService testDataService = new TestDataService();
        
        // register with Transfer Service
        TransferUtil.registerDataService(testDataService);

        // create new data objects
        URI epi1 = testDataService.create(10);
        URI epi2 = testDataService.create(1111);

        EndpointReferenceType transferEPR1 = getEPR(epi1, serviceUrl);

        System.out.println("Transfer EPR is " + transferEPR1);

        // try to get data object
        TransferServiceAddressingLocator addrLoc = 
            new TransferServiceAddressingLocator();
        resource = addrLoc.getResourcePort(transferEPR1);

        ((Stub)resource)._setProperty(Constants.GSI_SEC_MSG,
                                      Constants.SIGNATURE);
        ((Stub)resource)._setProperty(Constants.AUTHORIZATION,
                                      new NoAuthorization());

        returnVal = resource.get(new EmptyType());
        
        assertTrue(returnVal != null);

        MessageElement[] elems = returnVal.get_any();
        
        String elemStr = org.apache.axis.utils.XMLUtils
            .ElementToString(elems[0]);
        assertTrue(elemStr.indexOf("10") != -1);

        EndpointReferenceType transferEPR2 = getEPR(epi2, serviceUrl);

        resource = addrLoc.getResourcePort(transferEPR2);

        ((Stub)resource)._setProperty(Constants.GSI_SEC_MSG,
                                      Constants.SIGNATURE);
        ((Stub)resource)._setProperty(Constants.AUTHORIZATION,
                                      new NoAuthorization());

        returnVal = resource.get(new EmptyType());
        
        assertTrue(returnVal != null);

        elems = returnVal.get_any();
        
        elemStr = org.apache.axis.utils.XMLUtils.ElementToString(elems[0]);
        assertTrue(elemStr.indexOf("1111") != -1);
    }


    // construct transfer service EPR, this is typically returned
    // from resolution service. Note that the QName should match
    // transfer service qname
    protected EndpointReferenceType getEPR(URI epi, String address) 
        throws Exception {

        TransferHome home = TransferUtil.getTransferHome();
        SimpleResourceKey key =
            new SimpleResourceKey(home.getKeyTypeName(), epi);
        return AddressingUtils.createEndpointReference(address, key);
    }
    

}
