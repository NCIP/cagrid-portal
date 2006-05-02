package gov.nih.nci.cagrid.introduce.codegen.resource;

import javax.xml.namespace.QName;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.RemoveNotSupportedException;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceKey;

public class TestResourceHome implements ResourceHome {

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
