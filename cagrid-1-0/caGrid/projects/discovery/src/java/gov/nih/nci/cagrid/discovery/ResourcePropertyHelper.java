/*
 * Portions of this file Copyright 1999-2005 University of Chicago Portions of
 * this file Copyright 1999-2005 The University of Southern California. This
 * file or a portion of this file is licensed under the terms of the Globus
 * Toolkit Public License, found at
 * http://www.globus.org/toolkit/download/license.html. If you redistribute this
 * file, with or without modifications, you must include this notice in the
 * file.
 */

package gov.nih.nci.cagrid.discovery;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.globus.axis.gsi.GSIConstants;
import org.globus.axis.util.Util;
import org.globus.wsrf.WSRFConstants;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.utils.AnyHelper;
import org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse;
import org.oasis.wsrf.properties.GetMultipleResourceProperties_Element;
import org.oasis.wsrf.properties.GetMultipleResourceProperties_PortType;
import org.oasis.wsrf.properties.GetResourceProperty;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import org.oasis.wsrf.properties.QueryExpressionType;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;
import org.oasis.wsrf.properties.QueryResourceProperties_Element;
import org.oasis.wsrf.properties.QueryResourceProperties_PortType;
import org.oasis.wsrf.properties.WSResourcePropertiesServiceAddressingLocator;
import org.w3c.dom.Element;


public class ResourcePropertyHelper {

	static {
		Util.registerTransport();
	}


	public static MessageElement[] queryResourceProperties(EndpointReferenceType endpoint, String queryExpression)
		throws Exception {
		String dialect = WSRFConstants.XPATH_1_DIALECT;

		WSResourcePropertiesServiceAddressingLocator locator = new WSResourcePropertiesServiceAddressingLocator();
		QueryExpressionType query = new QueryExpressionType();

		try {
			query.setDialect(dialect);
		} catch (MalformedURIException e) {
			// TODO: change to internal error
			throw new Exception(e);
		}

		query.setValue(queryExpression);

		QueryResourceProperties_PortType port;
		try {
			port = locator.getQueryResourcePropertiesPort(endpoint);
		} catch (ServiceException e) {
			// TODO: change to user error
			throw new Exception(e);
		}

		setAnonymous((Stub) port);

		QueryResourceProperties_Element request = new QueryResourceProperties_Element();
		request.setQueryExpression(query);

		QueryResourcePropertiesResponse response;
		try {
			response = port.queryResourceProperties(request);
		} catch (RemoteException e) {
			// TODO: change to user error
			throw new Exception(e);
		}

		return response.get_any();

	}


	public static Element getResourceProperties(EndpointReferenceType endpoint) throws Exception {
		String dialect = WSRFConstants.XPATH_1_DIALECT;
		String queryExpression = "/";

		WSResourcePropertiesServiceAddressingLocator locator = new WSResourcePropertiesServiceAddressingLocator();
		QueryExpressionType query = new QueryExpressionType();

		try {
			query.setDialect(dialect);
		} catch (MalformedURIException e) {
			throw new Exception(e);
		}

		query.setValue(queryExpression);

		QueryResourceProperties_PortType port;
		try {
			port = locator.getQueryResourcePropertiesPort(endpoint);
		} catch (ServiceException e) {
			throw new Exception(e);
		}

		setAnonymous((Stub) port);

		QueryResourceProperties_Element request = new QueryResourceProperties_Element();
		request.setQueryExpression(query);

		QueryResourcePropertiesResponse response;
		try {
			response = port.queryResourceProperties(request);
		} catch (RemoteException e) {
			throw new Exception(e);
		}

		MessageElement messageElements[] = response.get_any();
		if (messageElements == null) {
			return (null);
		}

		if (messageElements.length > 1) {
			throw new Exception("Resource property query returned " + Integer.toString(messageElements.length)
				+ " elements; I only know how to deal with one");
		}
		Element element;
		try {
			element = messageElements[0].getAsDOM();
		} catch (Exception e) {
			throw new Exception("Error parsing message element", e);
		}
		return element;

	}


	public static Element getResourceProperty(EndpointReferenceType endpoint, QName rpName) throws Exception {
		GetResourceProperty port;
		WSResourcePropertiesServiceAddressingLocator locator = new WSResourcePropertiesServiceAddressingLocator();
		port = locator.getGetResourcePropertyPort(endpoint);

		setAnonymous((Stub) port);

		GetResourcePropertyResponse response;

		try {
			response = port.getResourceProperty(rpName);
		} catch (RemoteException e) {
			throw new Exception("Error getting resource property; " + "endpoint was '" + endpoint + "', name was '"
				+ rpName.toString(), e);
		}
		MessageElement[] messageElements = response.get_any();
		if (messageElements == null) {
			return (null);
		}
		if (messageElements.length > 1) {
			throw new Exception("Get resource property returned " + Integer.toString(messageElements.length)
				+ " elements; I only know how to deal with one");
		}
		Element element;
		try {
			element = messageElements[0].getAsDOM();
		} catch (Exception e) {
			throw new Exception("Error parsing message element", e);
		}
		return element;
	}


	public static Element[] getResourceProperties(EndpointReferenceType endpoint, QName[] rpNames) throws Exception {

		WSResourcePropertiesServiceAddressingLocator locator = new WSResourcePropertiesServiceAddressingLocator();
		GetMultipleResourceProperties_PortType port = locator.getGetMultipleResourcePropertiesPort(endpoint);

		setAnonymous((Stub) port);

		GetMultipleResourceProperties_Element request = new GetMultipleResourceProperties_Element();
		request.setResourceProperty(rpNames);

		GetMultipleResourcePropertiesResponse response = port.getMultipleResourceProperties(request);

		System.out.println(AnyHelper.toSingleString(response));
		return AnyHelper.toElement(response.get_any());

	}


	private static void setAnonymous(Stub stub) {
		//stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
		//	org.globus.wsrf.security.Constants.ENCRYPTION);
		stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS, Boolean.TRUE);
		stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, NoAuthorization.getInstance());
		stub._setProperty(GSIConstants.GSI_AUTHORIZATION, org.globus.gsi.gssapi.auth.NoAuthorization.getInstance());
	}
}
