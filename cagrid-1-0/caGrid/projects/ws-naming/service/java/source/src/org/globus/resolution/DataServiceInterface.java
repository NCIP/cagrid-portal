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
package org.globus.resolution;

import org.apache.axis.types.URI;

/**
 * Interface that needs to be implemented by object that is registered
 * by data application. This registered object is used during the
 * resolution process to determine if the EPI exists.
 */
public interface DataServiceInterface {

    /**
     * This method should return true if the EPI exists and any
     * authorization policy that exists, allows for the said DN to
     * resolve that EPI
     *
     * @param epi
     *        Endpoint Identifier of the object
     * @param dn
     *        DN of the entity requesting to resolve the EPI
     */
    public boolean exists(URI epi, String dn);
}
