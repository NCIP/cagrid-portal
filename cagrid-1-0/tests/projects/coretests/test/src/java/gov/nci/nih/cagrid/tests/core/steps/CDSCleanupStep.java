/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

/**
 * This step drops the dorian table from the configured database.
 * 
 * @author Patrick McConnell
 */
public class CDSCleanupStep extends AbstractDbCleanupStep {
    public CDSCleanupStep() {
        super();
    }


    public CDSCleanupStep(String dbUrl, String user, String password) {
        super(dbUrl, user, password);
    }


    @Override
    public void runStep() throws Throwable {
        super.dropDatabases(new String[]{"cds",});
    }


    public static void main(String[] args) throws Throwable {
        new CDSCleanupStep().runStep();
    }
}
