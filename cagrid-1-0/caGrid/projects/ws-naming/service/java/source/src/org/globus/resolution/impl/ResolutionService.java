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
package org.globus.resolution.impl;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceContext;

import org.globus.naming.ResolveResponse;
import org.globus.naming.UnknownResourceFaultType;

import org.apache.axis.message.addressing.EndpointReferenceType;

import org.globus.wsrf.security.SecurityManager;

import org.apache.axis.types.URI;

public class ResolutionService {

    public static final String RESOLUTION_SERVICE_NAME = "ResolutionService";

    /**
     * This method is used to resolve the EPI to an EPR. 
     * See documentation in ResolutionResource
     */
    public ResolveResponse resolve(URI resolveRequest) 
        throws UnknownResourceFaultType, RemoteException {
        
        // try isExists on some registered listener
        ResolutionResource resource = 
            (ResolutionResource)ResourceContext.getResourceContext()
            .getResource();
        
        String caller = SecurityManager.getManager().getCaller();

        // if not exist, throw UnknownResourceFaultType
        if (!resource.epiExists(resolveRequest, caller)) {
            
            throw new UnknownResourceFaultType();
        }

        // if exists, construct EPR        
        EndpointReferenceType epr = null;
        try {
            epr = resource.constructEPR(resolveRequest);
        } catch (Exception exp) {
            // FIXME: more specific catch, chained exception
            throw new RemoteException(exp.getMessage());
        }
        
        // return resolveResponse
        return new ResolveResponse(epr);
    }
}
