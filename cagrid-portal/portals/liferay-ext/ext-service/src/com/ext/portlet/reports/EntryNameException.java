/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package com.ext.portlet.reports;

import com.liferay.portal.PortalException;

public class EntryNameException extends PortalException {

	public EntryNameException() {
		super();
	}

	public EntryNameException(String msg) {
		super(msg);
	}

	public EntryNameException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public EntryNameException(Throwable cause) {
		super(cause);
	}

}