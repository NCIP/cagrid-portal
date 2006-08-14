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
package org.globus.counterService;

import java.util.Vector;

import javax.naming.InitialContext;

import org.apache.axis.message.addressing.EndpointReferenceType;

import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;

import org.globus.wsrf.utils.AddressingUtils;

import java.rmi.RemoteException;

import org.globus.test.counter.GetCounterRequest;

public class TestCounterService {

    String SERVICE_NAME= "TestCounterService";

    public EndpointReferenceType create(int createRequest) 
        throws RemoteException {

        ResourceContext ctx = null;
        TestCounterHome home = null;
        ResourceKey key = null;
        
        try {
            ctx = ResourceContext.getResourceContext();
            home = (TestCounterHome) ctx.getResourceHome();
            key = home.create(createRequest);
        } catch(RemoteException e) {
            throw e;
        } catch(Exception e) {
            throw new RemoteException("", e);
        }

        EndpointReferenceType epr = null;
        try {
            epr = AddressingUtils.createEndpointReference(ctx, key);
        } catch(Exception e) {
            throw new RemoteException("", e);
        }
        return epr;
    }

    public int getCounterValue(GetCounterRequest getCounterRequest) 
        throws RemoteException {

        ResourceContext ctx = null;
        try {
            ctx = ResourceContext.getResourceContext();
        } catch(RemoteException e) {
            throw e;
        } catch(Exception e) {
            throw new RemoteException("", e);
        }
        
        TestCounterResource resource = (TestCounterResource)ctx.getResource();
        return resource.getValue();
        
    }

}
