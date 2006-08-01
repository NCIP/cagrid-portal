package gov.nih.nci.cagrid.gridgrouper.stem.service.globus.resource;

import javax.xml.namespace.QName;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.RemoveNotSupportedException;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceKey;

/**
 * This class implements a resource home
 */

public class BaseResourceHome implements ResourceHome {

	public Class getKeyTypeClass() {
		// TODO Auto-generated method stub
		return null;
	}


	public QName getKeyTypeName() {
		// TODO Auto-generated method stub
		return null;
	}


	public Resource find(ResourceKey arg0) throws ResourceException, NoSuchResourceException,
		InvalidResourceKeyException {
		// TODO Auto-generated method stub
		return null;
	}


	public void remove(ResourceKey arg0) throws ResourceException, NoSuchResourceException,
		InvalidResourceKeyException, RemoveNotSupportedException {
		// TODO Auto-generated method stub

	}

}