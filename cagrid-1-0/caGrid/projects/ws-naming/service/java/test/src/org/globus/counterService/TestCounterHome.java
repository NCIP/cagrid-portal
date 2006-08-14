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

import org.globus.resolution.DataServiceInterface;

import org.globus.resolution.SimpleDataService;

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.ResourceException;

import org.apache.axis.components.uuid.UUIDGen;
import org.apache.axis.components.uuid.UUIDGenFactory;

import org.apache.axis.types.URI;

public class TestCounterHome extends ResourceHomeImpl {


    SimpleDataService dataServiceObj;

    public static UUIDGen uuidGen = UUIDGenFactory.getUUIDGen();

    public ResourceKey create(int value) throws Exception {
 
        TestCounterResource resource = new TestCounterResource(value);
        ResourceKey key = new SimpleResourceKey(keyTypeName,
                                                resource.getID());
        add(key, resource);
        this.dataServiceObj.addEPI(new URI((String)resource.getID()));
        return key;
    }

    
    public void setDataServiceObject(SimpleDataService object) {
        this.dataServiceObj = object;
    }
}
