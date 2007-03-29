/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

/**
 * This step cleans up a GME deployment by dropping the databases
 * globusgme_gme_registry, globusgme_gme_schema_cache, and
 * globusgme_gme_schema_store.
 * 
 * @author Patrick McConnell
 */
public class GMECleanupStep extends AbstractDbCleanupStep {
    public GMECleanupStep() {
        super();
    }


    public GMECleanupStep(String dbUrl, String user, String password) {
        super(dbUrl, user, password);
    }


    @Override
    public void runStep() throws Throwable {
        super.dropDatabases(new String[]{"GlobusGME_GME_REGISTRY", "GlobusGME_GME_SCHEMA_CACHE",
                "GlobusGME_GME_SCHEMA_STORE"});
    }


    public static void main(String[] args) throws Throwable {
        new GMECleanupStep().runStep();
    }
}
