package gov.nih.nci.cagrid.data.cql.ui;

import java.awt.Dimension;
import java.util.Properties;

import javax.swing.JPanel;

/** 
 *  CQLQueryProcessorConfigUI
 *  User interface for configuring a CQL Query Processor instance
 * 
 * @author David Ervin
 * 
 * @created Apr 6, 2007 12:45:01 PM
 * @version $Id: CQLQueryProcessorConfigUI.java,v 1.2 2007-04-09 14:19:14 dervin Exp $ 
 */
public abstract class CQLQueryProcessorConfigUI extends JPanel {

    public CQLQueryProcessorConfigUI() {
        super();
    }
    
    
    /**
     * Subclasses should implement this method to set up their GUI 
     * as needed based on the Properties supplied from the
     * CQL query processor's current configuration.
     * 
     * @param cqlProcessorProperties
     */
    public abstract void setUpUi(Properties cqlProcessorProperties);
    
    
    /**
     * Subclasses should implement this method to return a Properties
     * instance with appropriate keys and values configured for the
     * CQL query processor implementation.
     *  
     * @return
     *      CQL Query Processor configuration properties
     */
    public abstract Properties getConfiguredProperties();
    
    
    /**
     * Subclasses may override this method to specify the dimension at which
     * they wish the UI panel to be displayed.  The default is 400x350
     * @return
     *      The preferred display size
     */
    public Dimension getPreferredDimension() {
        return new Dimension(400, 350);
    }
}
