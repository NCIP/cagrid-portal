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

import org.globus.wsrf.Resource;

import org.globus.wsrf.impl.SimpleResourceKey;

import javax.xml.namespace.QName;

import org.globus.resolution.DataServiceInterface;

import org.globus.wsrf.utils.AddressingUtils;

import org.apache.axis.message.addressing.EndpointReferenceType;

import org.apache.axis.types.URI;

public class ResolutionResource implements Resource {

    DataServiceInterface registerInterface;

    private String address;
    private QName resolvedKeyQName;

    public ResolutionResource(String address_, QName keyQName_) {
        this.address = address_;
        this.resolvedKeyQName = keyQName_;
    }

    /**
     * Method to be invoked by data application to register an object
     * used to verify existence of object for the EPI
     */
    public void register(DataServiceInterface parameter) {
        this.registerInterface = parameter;
    }

    /**
     * Invokes exists method on the object regsitered to establish if
     * an object exists for said EPI. If no object has been regsitered,
     * false is returned.
     */
    public boolean epiExists(URI epi, String dn) {

        if (this.registerInterface != null) {
            return this.registerInterface.exists(epi, dn);
        }

        return false;
    }

    /**
     * Construct the EPR corresponding to the EPI. The configured
     * address and key QName is used to construct the EPR. 
     */
    public EndpointReferenceType constructEPR(URI epi) 
        throws Exception {

        SimpleResourceKey key = 
            new SimpleResourceKey(this.resolvedKeyQName, epi);

        EndpointReferenceType epr = 
            epr = AddressingUtils.createEndpointReference(this.address,
                                                          key);
        
        return epr;
    }
}
