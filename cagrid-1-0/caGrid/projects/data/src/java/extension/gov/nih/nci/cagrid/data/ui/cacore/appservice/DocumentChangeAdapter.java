package gov.nih.nci.cagrid.data.ui.cacore.appservice;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** 
 *  DocumentChangeAdapter
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Mar 23, 2007 3:15:24 PM
 * @version $Id: DocumentChangeAdapter.java,v 1.1 2007-03-28 17:33:11 dervin Exp $ 
 */
public abstract class DocumentChangeAdapter implements DocumentListener {

    public void changedUpdate(DocumentEvent e) {
        documentEdited(e);
    }


    public void insertUpdate(DocumentEvent e) {
        documentEdited(e);
    }


    public void removeUpdate(DocumentEvent e) {
        documentEdited(e);
    }

    
    public abstract void documentEdited(DocumentEvent e);
}
