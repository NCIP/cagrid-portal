package org.cagrid.fqp.test.remote.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.util.Iterator;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.Filter;

public class ChangeJndiSweeperDelayStep extends Step {
    
    public static final String RESOURCE_HOME_TYPE_NAME = 
        "gov.nih.nci.cagrid.fqp.results.service.globus.resource.FQPResultResourceHome";
    
    private File fqpServiceDir;
    private long delay;
    
    public ChangeJndiSweeperDelayStep(File fqpServiceDir, long delay) {
        this.fqpServiceDir = fqpServiceDir;
        this.delay = delay;
    }
    

    public void runStep() throws Throwable {
        // find the JNDI file
        File jndiFile = new File(fqpServiceDir, "jndi-config.xml");
        assertTrue("JNDI config file (" + jndiFile.getAbsolutePath() + ") not found.", jndiFile.exists());
        Document jndiDocument = null;
        try {
            jndiDocument = XMLUtilities.fileNameToDocument(jndiFile.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error reading JNDI file as JDOM document: " + ex.getMessage());
        }
        // walk the JNDI to find the right resource home
        Element jndiRootElement = jndiDocument.getRootElement();
        Iterator fqpResultResourceElements = jndiRootElement.getDescendants(new Filter() {
            public boolean matches(Object obj) {
                if (obj instanceof Element) {
                    Element elem = (Element) obj;
                    if (elem.getName().equals("resource")) {
                        Attribute nameAttr = elem.getAttribute("name");
                        if (nameAttr != null && nameAttr.getValue().equals("home")) {
                            Attribute typeAttr = elem.getAttribute("type");
                            if (typeAttr != null && typeAttr.getValue().equals(RESOURCE_HOME_TYPE_NAME)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
        assertTrue("No resource home for " + RESOURCE_HOME_TYPE_NAME + " found in JNDI!", fqpResultResourceElements.hasNext());
        Element resourceHomeElement = (Element) fqpResultResourceElements.next();
        assertFalse("More than one resource home for " + RESOURCE_HOME_TYPE_NAME + " foind in JNDI!", fqpResultResourceElements.hasNext());
        // find and change the sweeper delay value
        Element resourceParamsElement = resourceHomeElement.getChild("resourceParams", resourceHomeElement.getNamespace());
        Iterator paramElementIter = resourceParamsElement.getChildren("parameter", resourceParamsElement.getNamespace()).iterator();
        boolean sweeperDelaySet = false;
        while (paramElementIter.hasNext() && !sweeperDelaySet) {
            Element paramElement = (Element) paramElementIter.next();
            Element nameElement = paramElement.getChild("name", paramElement.getNamespace());
            if (nameElement != null && nameElement.getText().equals("sweeperDelay")) {
                Element valueElement = paramElement.getChild("value", paramElement.getNamespace());
                valueElement.setText(String.valueOf(delay));
                sweeperDelaySet = true;
            }
        }
        assertTrue("Sweeper delay parameter not found in JNDI!", sweeperDelaySet);
        // write the JNDI back to disk
        try {
            String jndiText = XMLUtilities.documentToString(jndiDocument);
            Utils.stringBufferToFile(new StringBuffer(jndiText), jndiFile.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error writing edited JNDI to disk: " + ex.getMessage());
        }
    }
}
