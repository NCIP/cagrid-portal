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
import org.globus.wsrf.ResourceException;

import javax.xml.namespace.QName;

import org.globus.wsrf.impl.SingletonResourceHome;

/**
 * A singleton resource object is used. The home can be configured
 * with the resolved key qname and the address of the service for
 * which the EPR is being constructed.
 */
public class ResolutionHome extends SingletonResourceHome {

    private String address;
    private QName resolvedKeyQName;
    
    /**
     * Set sddress of the service to which EPI is being resolved.
     */
    public void setAddress(String address_) {

        this.address = address_;
    }

    /**
     * Get address of the service to which EPI is being resolved.
     */
    public String getAddress() {

        return this.address;
    }
    
    /**
     * Set key QName of the service to which the EPI is being resolved
     */
    public void setResolvedKeyQName(String qName) 
        throws Exception {
        this.resolvedKeyQName = QName.valueOf(qName);
    }

    /**
     * Get key QName of the service to which the EPI is being resolved
     */
    public String getResolvedKeyQName() {
        return this.resolvedKeyQName.toString();
    }

    public QName getResolvedKeyName() {

        return this.resolvedKeyQName;
    }

    /**
     * Return an instance of ResolutionResource
     */
    protected Resource findSingleton() throws ResourceException {
        
        return new ResolutionResource(this.address, this.resolvedKeyQName);
    }
}
