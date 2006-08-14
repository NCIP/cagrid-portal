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
package org.globus.resolution.util;

import javax.naming.InitialContext;

import org.globus.resolution.DataServiceInterface;

import org.globus.resolution.impl.ResolutionHome;
import org.globus.resolution.impl.ResolutionResource;
import org.globus.resolution.impl.ResolutionService;

public class ResolutionUtil {

    /**
     * Retrieves the resolution home object. The call will work only
     * if made from with in the same JVM as in which the service is running.
     */
    public static ResolutionHome getResolutionHome() throws Exception {
        
        InitialContext context = new InitialContext();
        ResolutionHome home = (ResolutionHome)context
            .lookup(org.globus.wsrf.Constants
                    .JNDI_SERVICES_BASE_NAME +
                    ResolutionService.RESOLUTION_SERVICE_NAME +
                    org.globus.wsrf.Constants.HOME_NAME);
        return home;
    }

    /**
     * Retrieves resolution resource. since it is a singleton
     * resource, there is only one instance of this resource. This call
     * will work only if made from with in the same JVM as in which the
     * service is running.
     */
    public static ResolutionResource getResolutionResource() throws Exception {
        
        ResolutionHome home = getResolutionHome();
        return (ResolutionResource)home.find(null);
    }

    /**
     * Utily method for data application to regsiter an object which
     * implements the DataServiceInterface.  This call will work only
     * if made from with in the same JVM as in which the serivce is running.
     */
    public static void registerDataService(DataServiceInterface object) 
        throws Exception {
        
        ResolutionResource resource = getResolutionResource();
        resource.register(object);
    }
}

