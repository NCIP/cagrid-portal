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

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.RemoveNotSupportedException;

import javax.xml.namespace.QName;

/**
 * This is like a singleton resource home, but the keys are not
 * null. Refer to org.globus.wsrf.impl.SingletonResource
 */
public class TransferHome implements ResourceHome {

    Resource res = null;
    protected QName keyTypeName;
    protected Class keyTypeClass;

    public TransferHome() {
        res = new TransferResource();
    }
    
    public Resource find(ResourceKey key) throws ResourceException,
                                                 NoSuchResourceException,
                                                 InvalidResourceKeyException {
        return res;
    }
    
    public void remove(ResourceKey key) throws ResourceException, 
                                               NoSuchResourceException,
                                               InvalidResourceKeyException,
                                               RemoveNotSupportedException {
        throw new RemoveNotSupportedException();
    }

    public void setResourceKeyType(String clazz)
        throws ClassNotFoundException {
        this.keyTypeClass = Class.forName(clazz);
    }

    public void setResourceKeyName(String name) {
        this.keyTypeName = QName.valueOf(name);
    }

    public Class getKeyTypeClass() {
        return this.keyTypeClass;
    }

    public QName getKeyTypeName() {
        return this.keyTypeName;
    }
}
