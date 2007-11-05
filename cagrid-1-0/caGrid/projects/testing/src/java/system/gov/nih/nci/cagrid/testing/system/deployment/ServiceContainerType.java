package gov.nih.nci.cagrid.testing.system.deployment;

import java.util.Calendar;
import java.util.GregorianCalendar;

/** 
 *  ServiceContainerType
 *  Identifies the type of service container
 * 
 * @author David Ervin
 * 
 * @created Oct 16, 2007 12:01:50 PM
 * @version $Id: ServiceContainerType.java,v 1.3 2007-11-05 15:27:26 dervin Exp $ 
 */
public enum ServiceContainerType {

    GLOBUS_CONTAINER, TOMCAT_CONTAINER, JBOSS_CONTAINER;
    
    public String getZip() {
        switch (this) {
            case GLOBUS_CONTAINER:
                return "../testing/resources/containers/minimal-ws-core-enum-4.0.3.zip";
            case TOMCAT_CONTAINER:
                return "../testing/resources/containers/minimal-tomcat-5.0.28-with-globus-4.0.3.zip";
            case JBOSS_CONTAINER:
                throw new AssertionError("Container type " + this + " is not yet supported");
        }
        throw new AssertionError("Unknown service container type: " + this);
    }
    
    
    public String toString() {
        switch (this) {
            case GLOBUS_CONTAINER:
                return "Globus";
            case TOMCAT_CONTAINER:
                return "Tomcat";
            case JBOSS_CONTAINER:
                return "JBoss";
        }
        throw new AssertionError("Unknown service container type: " + this);
    }
    
    
    /**
     * Utility method for testing to get a service container
     * based on which day of the year it is.
     * @return
     *      A service container type
     */
    public static ServiceContainerType getTypeOfTheDay() {
        Calendar today = new GregorianCalendar();
        int dayOfYear = today.get(Calendar.DAY_OF_YEAR);
        int type = dayOfYear % 2; // TODO: when JBoss works, do % 3
        ServiceContainerType container = null;
        switch (type) {
            case 0:
                container = GLOBUS_CONTAINER;
            case 1:
                container = TOMCAT_CONTAINER;
        }
        return container;
    }
}
