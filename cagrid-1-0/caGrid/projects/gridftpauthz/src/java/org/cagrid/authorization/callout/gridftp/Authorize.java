package org.cagrid.authorization.callout.gridftp;

public interface Authorize {

    public boolean authorize(String identity, String operation, String target);

}
