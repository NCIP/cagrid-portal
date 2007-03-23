package org.cagrid.gridftp.authorization.plugin;

import junit.framework.TestCase;

public class GridFTPOperationTestCase extends TestCase {

	/**
	 * test known valid GridFTP actions
	 */
	public void testEnumValueOf() {
		GridFTPOperation operation = GridFTPOperation.valueOf("read");
		operation = GridFTPOperation.valueOf("create");
		operation = GridFTPOperation.valueOf("write");
		operation = GridFTPOperation.valueOf("lookup");
		operation = GridFTPOperation.valueOf("delete");
		operation = GridFTPOperation.valueOf("chdir");
	}
}
