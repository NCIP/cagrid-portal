/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package com.ext.portlet.reports;

import com.liferay.portal.PortalException;

public class NoSuchEntryException extends PortalException {

	public NoSuchEntryException() {
		super();
	}

	public NoSuchEntryException(String msg) {
		super(msg);
	}

	public NoSuchEntryException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NoSuchEntryException(Throwable cause) {
		super(cause);
	}

}