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
package org.globus.transfer.impl;

import org.w3c.dom.Element;

import org.globus.transfer.DataServiceInterface;

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceKey;

import org.apache.axis.types.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransferResource implements Resource {

    private static Log logger =
        LogFactory.getLog(TransferResource.class.getName());

    DataServiceInterface registerInterface;

    /**
     * Method to be invoked by data application to register an object
     * used to retrieve the object for the EPI
     */
    public void register(DataServiceInterface parameter) {
        logger.debug("Registering data service object");
        this.registerInterface = parameter;
    }

    /**
     * Uses the regsitered data service interface to retrieve the data
     * corresponding to EPI. The DN of the caller is passed as
     * parameter, so any authorization policy can be evaluated at the
     * application level.
     */
    public Element getData(ResourceKey key, String caller) {
        
        logger.debug("Get Data ");
        Object value = key.getValue();
        if (this.registerInterface != null) {
            // type casting to URI, since the key here should be a URI
            Element element = 
                this.registerInterface.getData((URI)value, caller);
            logger.debug("Element in resource is is " + org.apache.axis.utils
                         .XMLUtils.ElementToString(element));
            return element;
        } else {
            logger.warn("No data service object registered");
            return null;
        }
    }
}
