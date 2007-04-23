package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.data.extension.ClassMapping;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;

import java.util.EventListener;

/** 
 *  DomainModelClassSelectionListener
 *  Listens for changes to the class selection in the domain model
 * 
 * @author David Ervin
 * 
 * @created Apr 10, 2007 3:16:54 PM
 * @version $Id: DomainModelClassSelectionListener.java,v 1.1 2007-04-23 17:05:49 dervin Exp $ 
 */
public interface DomainModelClassSelectionListener extends EventListener {

    public void classAdded(String packageName, ClassMapping mapping, NamespaceType packageNamespace);
    
    
    public void classRemoved(String packageName, String className);
    
    
    public void classesCleared();
}
