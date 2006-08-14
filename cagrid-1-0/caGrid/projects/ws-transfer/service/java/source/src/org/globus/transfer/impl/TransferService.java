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

import org.globus.transfer.AnyXmlType;
import org.globus.transfer.EmptyType;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.ResourceContext;

import org.globus.wsrf.security.SecurityManager;

import org.apache.axis.message.MessageElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransferService {

    private static Log logger =
        LogFactory.getLog(TransferService.class.getName());

    public static final String TRANSFER_SERVICE_NAME = "TransferService";

    /**
     * Retrieves the obejct corresponding to the resource key used to
     * make the invocation (which is the EPI of the object). the
     * object is returned as xsd:any
     */
    public AnyXmlType get(EmptyType body) throws RemoteException {

        logger.debug("In get method ");

        // get resource context
        ResourceContext context = ResourceContext.getResourceContext();
            
        // get resource key
        ResourceKey key = context.getResourceKey();

        if (key == null) {
            logger.debug("Key is null, returning null");
            return null;
        }

        logger.debug("Key is " + key);

        // get resource
        TransferResource resource = (TransferResource)context.getResource();

        // get caller D        
        String caller = SecurityManager.getManager().getCaller();
        logger.debug("Caller is " + caller);

        // get object from resource
        Element element = resource.getData(key, caller);

        if (element == null) {
            logger.debug("Element is null");
            return null;
        }
        
        logger.debug("Element is " + org.apache.axis.utils.XMLUtils
                     .ElementToString(element));

        // return as AnyXmlType
        MessageElement msgElem = new MessageElement(element);

        return new AnyXmlType(new MessageElement[] { msgElem });
    }
}
