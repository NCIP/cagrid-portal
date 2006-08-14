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

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.ResourceIdentifier;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;

public class TestCounterResource implements Resource, ResourceIdentifier {

    private int value;
    private Object key;
    String prefix = "local://ResolutionService:8080";
    
    public TestCounterResource(int i) {
        this.value = i;
        key = prefix + TestCounterHome.uuidGen.nextUUID();
    }
    
    public Object getID() {
        return this.key;
    }

    public int getValue() {
        return value;
    }
}
