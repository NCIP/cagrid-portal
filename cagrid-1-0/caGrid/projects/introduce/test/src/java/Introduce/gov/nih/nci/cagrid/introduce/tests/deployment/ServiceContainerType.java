package gov.nih.nci.cagrid.introduce.tests.deployment;

/** 
 *  ServiceContainerType
 *  Identifies the type of service container
 * 
 * @author David Ervin
 * 
 * @created Oct 16, 2007 12:01:50 PM
 * @version $Id: ServiceContainerType.java,v 1.1 2007-10-17 17:00:44 dervin Exp $ 
 */
public enum ServiceContainerType {

    GLOBUS_CONTAINER, TOMCAT_CONTAINER, JBOSS_CONTAINER;
    
    public String getZip() {
        switch (this) {
            case GLOBUS_CONTAINER:
                return "test/resources/containers/minimal-ws-core-enum-4.0.3.zip";
            case TOMCAT_CONTAINER:
                
                break;
            case JBOSS_CONTAINER:
                
                break;
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
}
