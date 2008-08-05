/*
 * Copyright (C) 2003 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 ****************************************************************
 * Source code information
 * -----------------------
 * Filename           $RCSfile: WSDLSOAPInvoker.java,v $
 * Revision           $Revision: 1.1 $
 * Release status     $State: Exp $
 * Last modified on   $Date: 2008-08-05 20:47:49 $
 *               by   $Author: tanw $
 * Created on 07-Apr-2006
 *****************************************************************/
package net.sf.taverna.wsdl.soap;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.wsdl.Definition;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import net.sf.taverna.wsdl.parser.UnknownOperationException;
import net.sf.taverna.wsdl.parser.WSDLParser;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;

import org.apache.axis.message.addressing.*;
//import org.apache.axis.types.URI.*;


import org.apache.log4j.Logger;
import org.apache.wsif.WSIFException;
import org.apache.wsif.WSIFOperation;
import org.apache.wsif.WSIFPort;
import org.apache.wsif.WSIFService;
import org.apache.wsif.WSIFServiceFactory;
import org.apache.wsif.providers.soap.apacheaxis.WSIFOperation_ApacheAxis;
import org.apache.wsif.providers.soap.apacheaxis.WSIFPort_ApacheAxis;
import org.w3c.dom.*;

/**
 * Invokes SOAP based webservices
 * 
 * @author sowen
 * 
 */
@SuppressWarnings("unchecked")
public class WSDLSOAPInvoker {
	private WSDLParser parser;
	private String operationName;
	private List<String> outputNames;
	
	private static Logger logger = Logger.getLogger(WSDLSOAPInvoker.class);

	public WSDLSOAPInvoker(WSDLParser parser, String operationName,
			List<String> outputNames) {
		this.parser = parser;
		this.operationName = operationName;
		this.outputNames = outputNames;
	}

	/**
	 * Invokes the webservice with the supplied input Map, and returns a Map
	 * containing the outputs, mapped against their output names.
	 * 
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> invoke(Map inputMap) throws Exception {
		return invoke(inputMap, null);
	}

	/**
	 * Invokes the webservice with the supplied input Map, and returns a Map
	 * containing the outputs, mapped against their output names.
	 * 
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> invoke(Map inputMap, EngineConfiguration config)
			throws Exception {

		Call call = getCall();
		//------------------------test for Resource Key------------------------------------------------
		if(operationName.equals("add")){
			//? classloader issue in Raven
			
			try{
				org.apache.axis.utils.ClassUtils.setDefaultClassLoader(org.apache.axis.types.URI.class.getClassLoader()) ;	
			}
			catch (Exception e) {
				 e.printStackTrace();
				 }
			//TODO manually create a EndpointReference -- should use the clause below instead
			//inputMap.get("EndpointReference"); 
			EndpointReferenceType epr = new EndpointReferenceType();
			/*
			try {
				epr.setAddress(new Address("http://192.168.1.65:8080/wsrf/services/CounterService"));
			} catch (MalformedURIException e) {
				
				e.printStackTrace();
			}
			*/
			 DocumentBuilder builder2;
			 String nameSpace = "http://counter.com";
		     String resourceKeyName = "CounterKey";
		     Document doc2;
		     Element resourceKeyElement = null;
		     ReferencePropertiesType properties = null;
		     Element referencePropertiesElement = null;
		     try {
				builder2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		        doc2 = builder2.newDocument();
		        resourceKeyElement = doc2.createElementNS(nameSpace,resourceKeyName);
		        resourceKeyElement.setTextContent("15703940");
		        
		        referencePropertiesElement = doc2.createElementNS("http://schemas.xmlsoap.org/ws/2004/03/addressing", "wsa");
		        referencePropertiesElement.appendChild(resourceKeyElement);			
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				properties = ReferencePropertiesType.fromElement(referencePropertiesElement);
				//properties.append(resourceKeyElement, "resourceKeyElement");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					/*
					 * <ns1:CounterResource xsi:type="ns2:EndpointReferenceType" xmlns:ns1="http://counter.com" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ns2="http://schemas.xmlsoap.org/ws/2004/03/addressing">
			 <ns2:Address xsi:type="ns2:AttributedURI">http://192.168.1.65:8080/wsrf/services/CounterService</ns2:Address>
			 <ns2:ReferenceProperties xsi:type="ns2:ReferencePropertiesType">
			  <ns1:CounterKey>3201085</ns1:CounterKey>
			 </ns2:ReferenceProperties>
			 <ns2:ReferenceParameters xsi:type="ns2:ReferenceParametersType"/>
			</ns1:CounterResource>
					 * */
			//<CounterKey  xmlns="http://counter.com">3201085</CounterKey>
			epr.setProperties(properties);
			//now we have an EPR
			//extract the resource key
			ReferencePropertiesType resourceKey =  epr.getProperties();
			//add the resource key to the soap header
			AddressingHeaders headers = new AddressingHeaders();
			headers.setReferenceProperties(resourceKey);
			System.out.println("resourcekey "+ resourceKey.toString()+ " is added.");
			call.setProperty(Constants.ENV_ADDRESSING_REQUEST_HEADERS, headers);			
		}
		//---------------------------------------------------------------------------------------------
		
		if (config != null) {
			call.setClientHandlers(config.getGlobalRequest(), config
					.getGlobalResponse());
		}
		call.setTimeout(getTimeout());

		BodyBuilder builder = BodyBuilderFactory.instance().create(parser,
				operationName,
				parser.getOperationInputParameters(operationName));
		SOAPBodyElement body = builder.build(inputMap);

		SOAPEnvelope requestEnv = new SOAPEnvelope();
		
		requestEnv.addBodyElement(body);
		logger.info("Invoking service with SOAP envelope:\n"+requestEnv);
		
		SOAPEnvelope responseEnv = call.invoke(requestEnv);
		
		logger.info("Recieved SOAP response:\n"+responseEnv);

		Map<String, Object> result;
		if (responseEnv == null) {
			if (outputNames.size() == 1
					&& outputNames.get(0).equals("attachmentList")) {
				// Could be axis 2 service with no output (TAV-617)
				result = new HashMap<String, Object>();
			} else {
				throw new IllegalStateException(
						"Missing expected outputs from service");
			}
		} else {
			List response = responseEnv.getBodyElements();
			logger.info("SOAP response was:" + response);
			SOAPResponseParser parser = SOAPResponseParserFactory
					.instance()
					.create(
							response,
							getUse(),
							getStyle(),
							this.parser
									.getOperationOutputParameters(operationName));
			result = parser.parse(response);
		}

		result.put("attachmentList", extractAttachmentsDataThing(call));

		return result;
	}

	/**
	 * Reads the property taverna.wsdl.timeout, default to 5 minutes if missing.
	 * 
	 * @return
	 */
	private Integer getTimeout() {
		int result = 300000;
		String minutesStr = System.getProperty("taverna.wsdl.timeout");

		if (minutesStr == null) {
			// using default of 5 minutes
			return result;
		}
		try {
			int minutes = Integer.parseInt(minutesStr.trim());
			result = minutes * 1000 * 60;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	private String getStyle() {
		return parser.getStyle();
	}

	private String getUse() throws UnknownOperationException {
		return parser.getUse(operationName);
	}

	/**
	 * Returns an axis based Call, initialised for the operation that needs to
	 * be invoked
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws UnknownOperationException
	 * @throws WSDLException
	 * @throws WSIFException
	 */
	private Call getCall() throws ServiceException, UnknownOperationException,
			WSDLException, WSIFException {

		String use = parser.getUse(operationName);
		Call result = (((WSIFPort_ApacheAxis) ((WSIFOperation_ApacheAxis) getWSIFOperation())
				.getWSIFPort()).getCall());
		result.setUseSOAPAction(true);
		result.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
				Boolean.FALSE);
		result.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
				Boolean.FALSE);
		result
				.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		
		if (parser.getSOAPActionURI(operationName)!=null) {
			result.setSOAPActionURI(parser.getSOAPActionURI(operationName));
		}
		
		if (use.equalsIgnoreCase("literal")) {
			result.setEncodingStyle(null);
		}

		return result;
	}

	private WSIFOperation getWSIFOperation() throws WSIFException {
		Definition def = parser.getDefinition();
		Service s = (Service) def.getServices().values().toArray()[0];
		WSIFServiceFactory factory = WSIFServiceFactory.newInstance();
		PortType portType = parser.getPortType(operationName);
		WSIFService service = factory.getService(def, s, portType);
		WSIFPort port = service.getPort();
		WSIFOperation op = port.createOperation(operationName);
		return op;
	}

	/**
	 * Exctracts any attachments that result from invoking the service, and
	 * returns them as a List wrapped within a DataThing
	 * 
	 * @param axisCall
	 * @return
	 * @throws SOAPException
	 * @throws IOException
	 */
	private List extractAttachmentsDataThing(Call axisCall)
			throws SOAPException, IOException {
		List attachmentList = new ArrayList();
		if (axisCall.getResponseMessage() != null
				&& axisCall.getResponseMessage().getAttachments() != null) {
			for (Iterator i = axisCall.getResponseMessage().getAttachments(); i
					.hasNext();) {
				AttachmentPart ap = (AttachmentPart) i.next();
				DataHandler dh = ap.getDataHandler();
				BufferedInputStream bis = new BufferedInputStream(dh
						.getInputStream());
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int c;
				while ((c = bis.read()) != -1) {
					bos.write(c);
				}
				bis.close();
				bos.close();
				String mimeType = dh.getContentType();
				if (mimeType.matches(".*image.*")
						|| mimeType.matches(".*octet.*")
						|| mimeType.matches(".*audio.*")
						|| mimeType.matches(".*application/zip.*")) {
					attachmentList.add(bos.toByteArray());
				} else {
					attachmentList.add(new String(bos.toByteArray()));
				}
			}
		}

		return attachmentList;
	}
}
