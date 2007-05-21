package gov.nih.nci.cagrid.data.ui.auditors;

import gov.nih.nci.cagrid.data.auditing.MonitoredEvents;

import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.Dimension;
import java.awt.GridLayout;

/** 
 *  MonitoredEventsPanel
 *  Panel to configure / display which events an auditor will respond to
 * 
 * @author David Ervin
 * 
 * @created May 21, 2007 10:50:26 AM
 * @version $Id: MonitoredEventsPanel.java,v 1.1 2007-05-21 19:07:56 dervin Exp $ 
 */
public class MonitoredEventsPanel extends JPanel {

    private JCheckBox queryBeginsCheckBox = null;
    private JCheckBox validationFailureCheckBox = null;
    private JCheckBox queryProcessingFailureCheckBox = null;
    private JCheckBox queryResultsCheckBox = null;


    public MonitoredEventsPanel() {
        initialize();
    }
    
    
    private void initialize() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(2);
        gridLayout.setHgap(2);
        gridLayout.setVgap(2);
        gridLayout.setColumns(2);
        this.setLayout(gridLayout);
        this.setSize(new Dimension(572, 55));
        this.add(getQueryBeginsCheckBox(), null);
        this.add(getValidationFailureCheckBox(), null);
        this.add(getQueryProcessingFailureCheckBox(), null);
        this.add(getQueryResultsCheckBox(), null);
        
    }
    
    
    public void setMonitoredEvents(MonitoredEvents events) {
        getQueryBeginsCheckBox().setSelected(events.isQueryBegin());
        getValidationFailureCheckBox().setSelected(events.isValidationFailure());
        getQueryProcessingFailureCheckBox().setSelected(events.isQueryProcessingFailure());
        getQueryResultsCheckBox().setSelected(events.isQueryResults());
    }
    
    
    public MonitoredEvents getMonitoredEvents() {
        MonitoredEvents events = new MonitoredEvents();
        events.setQueryBegin(getQueryBeginsCheckBox().isSelected());
        events.setValidationFailure(getValidationFailureCheckBox().isSelected());
        events.setQueryProcessingFailure(getQueryProcessingFailureCheckBox().isSelected());
        events.setQueryResults(getQueryResultsCheckBox().isSelected());
        return events;
    }


    /**
     * This method initializes queryBeginsCheckBox	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getQueryBeginsCheckBox() {
        if (queryBeginsCheckBox == null) {
            queryBeginsCheckBox = new JCheckBox();
            queryBeginsCheckBox.setText("Query Begins");
        }
        return queryBeginsCheckBox;
    }


    /**
     * This method initializes validationFailureCheckBox	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getValidationFailureCheckBox() {
        if (validationFailureCheckBox == null) {
            validationFailureCheckBox = new JCheckBox();
            validationFailureCheckBox.setText("Validation Failure");
        }
        return validationFailureCheckBox;
    }


    /**
     * This method initializes queryProcessingFailureCheckBox	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getQueryProcessingFailureCheckBox() {
        if (queryProcessingFailureCheckBox == null) {
            queryProcessingFailureCheckBox = new JCheckBox();
            queryProcessingFailureCheckBox.setText("Query Processing Failure");
        }
        return queryProcessingFailureCheckBox;
    }


    /**
     * This method initializes queryResultsCheckBox	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getQueryResultsCheckBox() {
        if (queryResultsCheckBox == null) {
            queryResultsCheckBox = new JCheckBox();
            queryResultsCheckBox.setText("Query Results");
        }
        return queryResultsCheckBox;
    }
}
