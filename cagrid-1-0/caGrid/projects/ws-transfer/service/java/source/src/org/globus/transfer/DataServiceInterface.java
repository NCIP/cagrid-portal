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

import org.w3c.dom.Element;

import org.apache.axis.types.URI;

/**
 * Interface that needs to be implemented by object that is registered
 * by data application. This registered object is used to retrieve the
 * data 
 */
public interface DataServiceInterface {

    /**
     * This method should return the object corresponding to the EPI
     * as a DOM Element. If any authorization policy exists, the DN can
     * be used to evaluate the policy
     *
     * @param epi
     *        Endpoint Identifier of the object
     * @param dn
     *        DN of the entity requesting to retrieve the data
     */
    public Element getData(URI epi, String dn);
}
