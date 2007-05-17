package gov.nih.nci.cagrid.data.service.auditing;

/** 
 *  DataServiceAuditor
 *  Base class for all data service auditors
 * 
 * @author David Ervin
 * 
 * @created May 17, 2007 10:50:11 AM
 * @version $Id: DataServiceAuditor.java,v 1.1 2007-05-17 18:39:15 dervin Exp $ 
 */
public abstract class DataServiceAuditor {

    private String instanceName;
    
    public DataServiceAuditor(/*Some configuration nugget,*/ String instanceName) {
        this.instanceName = instanceName;
    }
    
    
    /**
     * Returns the instance name of this auditor
     * @return
     *      The name of this auditor instance
     */
    public String getInstanceName() {
        return instanceName;
    }
    
    
    
}
