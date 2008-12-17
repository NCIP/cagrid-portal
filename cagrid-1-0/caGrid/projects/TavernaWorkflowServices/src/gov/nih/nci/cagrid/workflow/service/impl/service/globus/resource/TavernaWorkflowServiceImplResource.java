package gov.nih.nci.cagrid.workflow.service.impl.service.globus.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.factory.service.TavernaWorkflowServiceConfiguration;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;

import workflowmanagementfactoryservice.StartInputType;
import workflowmanagementfactoryservice.WMSInputType;
import workflowmanagementfactoryservice.WMSOutputType;
import workflowmanagementfactoryservice.WorkflowOutputType;
import workflowmanagementfactoryservice.WorkflowStatusType;


/** 
 * The implementation of this TavernaWorkflowServiceImplResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */


public class TavernaWorkflowServiceImplResource extends TavernaWorkflowServiceImplResourceBase {

	private String scuflDoc = null;
	private static String[] outputDoc = null;
	private String[] inputDoc = null;
	private String baseDir = null;

	private String tempDir = null;
	private String workflowName = null;

	private static WorkflowStatusType workflowStatus = WorkflowStatusType.Pending;
	private static TavernaWorkflowServiceConfiguration config = null;

	private class WorkflowExecutionThread extends Thread {
		
		private String[] args = null;
		public WorkflowExecutionThread (String[] args)
		{
			this.args = args;
		}
		public void run()
		{			
			ProcessBuilder builder = new ProcessBuilder(this.args);
			builder.directory(new File("/Users/sulakhe/execution-example/target/classes"));

			Map<String, String> environment = builder.environment();
			environment.put("CLASSPATH", "/Users/sulakhe/execution-example/target/classes:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/workflowmodel-impl/0.3-SNAPSHOT/workflowmodel-impl-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/jdom/jdom/1.0/jdom-1.0.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/workflowmodel-api/0.3-SNAPSHOT/workflowmodel-api-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/t2reference-api/0.1-SNAPSHOT/t2reference-api-0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/springframework/spring-tx/2.5.4/spring-tx-2.5.4.jar:/Users/sulakhe/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:/Users/sulakhe/.m2/repository/org/springframework/spring-beans/2.5.4/spring-beans-2.5.4.jar:/Users/sulakhe/.m2/repository/org/springframework/spring-core/2.5.4/spring-core-2.5.4.jar:/Users/sulakhe/.m2/repository/org/springframework/spring-context/2.5.4/spring-context-2.5.4.jar:/Users/sulakhe/.m2/repository/aopalliance/aopalliance/1.0/aopalliance-1.0.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/spi-discovery-api/0.3-SNAPSHOT/spi-discovery-api-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/observer/0.3-SNAPSHOT/observer-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/log4j/log4j/1.2.15/log4j-1.2.15.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/raven/raven/1.7-SNAPSHOT/raven-1.7-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/raven/raven-log4j/1.7-SNAPSHOT/raven-log4j-1.7-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/raven/appconfig/1.7-SNAPSHOT/appconfig-1.7-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/raven/launcher-api/1.7-SNAPSHOT/launcher-api-1.7-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/raven/prelauncher/1.7-SNAPSHOT/prelauncher-1.7-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/security-api/0.3-SNAPSHOT/security-api-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/provenanceconnector-api/0.3-SNAPSHOT/provenanceconnector-api-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/provenanceconnector-impl/0.3-SNAPSHOT/provenanceconnector-impl-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/apache/derby/derby/10.2.2.0/derby-10.2.2.0.jar:/Users/sulakhe/.m2/repository/org/apache/derby/derbynet/10.2.2.0/derbynet-10.2.2.0.jar:/Users/sulakhe/.m2/repository/org/apache/derby/derbytools/10.2.2.0/derbytools-10.2.2.0.jar:/Users/sulakhe/.m2/repository/org/apache/derby/derbyclient/10.2.2.0/derbyclient-10.2.2.0.jar:/Users/sulakhe/.m2/repository/com/thoughtworks/xstream/xstream/1.2.1/xstream-1.2.1.jar:/Users/sulakhe/.m2/repository/xpp3/xpp3_min/1.1.3.4.O/xpp3_min-1.1.3.4.O.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/all-activities/0.3-SNAPSHOT/all-activities-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/apiconsumer-activity/0.3-SNAPSHOT/apiconsumer-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/t2reference-core-extensions/0.1-SNAPSHOT/t2reference-core-extensions-0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar:/Users/sulakhe/.m2/repository/commons-codec/commons-codec/1.2/commons-codec-1.2.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/beanshell-activity/0.3-SNAPSHOT/beanshell-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/beanshell/bsh/2.0b4/bsh-2.0b4.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/biomart-activity/0.3-SNAPSHOT/biomart-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/biomart/martservice/0.7.0-SNAPSHOT/martservice-0.7.0-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/biomart/martj/0.6/martj-0.6.jar:/Users/sulakhe/.m2/repository/commons-io/commons-io/1.3.1/commons-io-1.3.1.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/biomoby-activity/0.3-SNAPSHOT/biomoby-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/biomoby/taverna-biomoby/1.7.2-SNAPSHOT/taverna-biomoby-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/taverna-enactor/1.7.2-SNAPSHOT/taverna-enactor-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/baclava/baclava-core/1.7.2-SNAPSHOT/baclava-core-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/taverna-core/1.7.2-SNAPSHOT/taverna-core-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/taverna-bootstrap/1.7.2-SNAPSHOT/taverna-bootstrap-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/scufl/scufl-model/1.7.2-SNAPSHOT/scufl-model-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/resources/freefluo/NO-VERSION/freefluo-NO-VERSION.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/resources/freefluo-taverna-exts/1.7.2-SNAPSHOT/freefluo-taverna-exts-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/commons-discovery/commons-discovery/0.4/commons-discovery-0.4.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/taverna-scavenger/1.7.2-SNAPSHOT/taverna-scavenger-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/processors/taverna-java-processor/1.7.2-SNAPSHOT/taverna-java-processor-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/taverna/scufl/scufl-ui-api/1.7.2-SNAPSHOT/scufl-ui-api-1.7.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/javax/mail/mail/1.4/mail-1.4.jar:/Users/sulakhe/.m2/repository/javax/activation/activation/1.1/activation-1.1.jar:/Users/sulakhe/.m2/repository/org/biomoby/jmoby/1.1.1-taverna/jmoby-1.1.1-taverna.jar:/Users/sulakhe/.m2/repository/axis/axis/1.4/axis-1.4.jar:/Users/sulakhe/.m2/repository/axis/axis-jaxrpc/1.3/axis-jaxrpc-1.3.jar:/Users/sulakhe/.m2/repository/axis/axis-saaj/1.3/axis-saaj-1.3.jar:/Users/sulakhe/.m2/repository/axis/axis-wsdl4j/1.5.1/axis-wsdl4j-1.5.1.jar:/Users/sulakhe/.m2/repository/axis/axis-ant/1.4/axis-ant-1.4.jar:/Users/sulakhe/.m2/repository/commons-lang/commons-lang/2.3/commons-lang-2.3.jar:/Users/sulakhe/.m2/repository/javax/sql/jdbc-stdext/2.0/jdbc-stdext-2.0.jar:/Users/sulakhe/.m2/repository/commons-dbcp/commons-dbcp/1.1/commons-dbcp-1.1.jar:/Users/sulakhe/.m2/repository/commons-pool/commons-pool/1.1/commons-pool-1.1.jar:/Users/sulakhe/.m2/repository/xml-apis/xml-apis/1.3.03/xml-apis-1.3.03.jar:/Users/sulakhe/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar:/Users/sulakhe/.m2/repository/commons-configuration/commons-configuration/1.5/commons-configuration-1.5.jar:/Users/sulakhe/.m2/repository/commons-collections/commons-collections/2.1.1/commons-collections-2.1.1.jar:/Users/sulakhe/.m2/repository/commons-digester/commons-digester/1.8/commons-digester-1.8.jar:/Users/sulakhe/.m2/repository/commons-beanutils/commons-beanutils/1.7.0/commons-beanutils-1.7.0.jar:/Users/sulakhe/.m2/repository/commons-beanutils/commons-beanutils-core/1.7.0/commons-beanutils-core-1.7.0.jar:/Users/sulakhe/.m2/repository/castor/castor/0.9.5/castor-0.9.5.jar:/Users/sulakhe/.m2/repository/edu/mit/lcs/haystack/adenine/2003/adenine-2003.jar:/Users/sulakhe/.m2/repository/com/hp/hpl/jena/jena/2.5.4/jena-2.5.4.jar:/Users/sulakhe/.m2/repository/com/hp/hpl/jena/arq/2.1/arq-2.1.jar:/Users/sulakhe/.m2/repository/org/apache/lucene/lucene-core/2.0.0/lucene-core-2.0.0.jar:/Users/sulakhe/.m2/repository/com/hp/hpl/jena/arq-extra/2.1/arq-extra-2.1.jar:/Users/sulakhe/.m2/repository/com/hp/hpl/jena/jenatest/2.5.4/jenatest-2.5.4.jar:/Users/sulakhe/.m2/repository/com/hp/hpl/jena/iri/0.5/iri-0.5.jar:/Users/sulakhe/.m2/repository/com/ibm/icu/icu4j/3.8/icu4j-3.8.jar:/Users/sulakhe/.m2/repository/antlr/antlr/2.7.5/antlr-2.7.5.jar:/Users/sulakhe/.m2/repository/commons-logging/commons-logging-api/1.1/commons-logging-api-1.1.jar:/Users/sulakhe/.m2/repository/com/hp/hpl/jena/concurrent-jena/1.3.2/concurrent-jena-1.3.2.jar:/Users/sulakhe/.m2/repository/com/hp/hpl/jena/json-jena/1.0/json-jena-1.0.jar:/Users/sulakhe/.m2/repository/stax/stax-api/1.0/stax-api-1.0.jar:/Users/sulakhe/.m2/repository/org/codehaus/woodstox/wstx-asl/3.0.0/wstx-asl-3.0.0.jar:/Users/sulakhe/.m2/repository/xerces/xmlParserAPIs/2.2.1/xmlParserAPIs-2.2.1.jar:/Users/sulakhe/.m2/repository/xerces/xercesImpl/2.7.1/xercesImpl-2.7.1.jar:/Users/sulakhe/.m2/repository/com/ibm/lsid/lsid-server/1.1.2/lsid-server-1.1.2.jar:/Users/sulakhe/.m2/repository/com/ibm/lsid/lsid-client/1.1.2/lsid-client-1.1.2.jar:/Users/sulakhe/.m2/repository/org/xbill/dnsjava/2003/dnsjava-2003.jar:/Users/sulakhe/.m2/repository/com/sun/xml/ws/jaxws-tools/2.1.3/jaxws-tools-2.1.3.jar:/Users/sulakhe/.m2/repository/com/sun/xml/ws/jaxws-rt/2.1.3/jaxws-rt-2.1.3.jar:/Users/sulakhe/.m2/repository/javax/xml/ws/jaxws-api/2.1/jaxws-api-2.1.jar:/Users/sulakhe/.m2/repository/javax/xml/bind/jaxb-api/2.1/jaxb-api-2.1.jar:/Users/sulakhe/.m2/repository/javax/xml/stream/stax-api/1.0-2/stax-api-1.0-2.jar:/Users/sulakhe/.m2/repository/com/sun/xml/bind/jaxb-impl/2.1.6/jaxb-impl-2.1.6.jar:/Users/sulakhe/.m2/repository/com/sun/xml/messaging/saaj/saaj-impl/1.3/saaj-impl-1.3.jar:/Users/sulakhe/.m2/repository/javax/xml/soap/saaj-api/1.3/saaj-api-1.3.jar:/Users/sulakhe/.m2/repository/com/sun/xml/stream/buffer/streambuffer/0.7/streambuffer-0.7.jar:/Users/sulakhe/.m2/repository/org/jvnet/staxex/stax-ex/1.2/stax-ex-1.2.jar:/Users/sulakhe/.m2/repository/com/sun/xml/stream/sjsxp/1.0/sjsxp-1.0.jar:/Users/sulakhe/.m2/repository/com/sun/org/apache/xml/internal/resolver/20050927/resolver-20050927.jar:/Users/sulakhe/.m2/repository/org/jvnet/mimepull/1.1/mimepull-1.1.jar:/Users/sulakhe/.m2/repository/com/sun/xml/bind/jaxb-xjc/2.1.6/jaxb-xjc-2.1.6.jar:/Users/sulakhe/.m2/repository/javax/xml/jaxb-impl/2.0EA3/jaxb-impl-2.0EA3.jar:/Users/sulakhe/.m2/repository/javax/xml/jaxb-xjc/2.0EA3/jaxb-xjc-2.0EA3.jar:/Users/sulakhe/.m2/repository/jaxen/jaxen/1.1.1/jaxen-1.1.1.jar:/Users/sulakhe/.m2/repository/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:/Users/sulakhe/.m2/repository/xom/xom/1.0/xom-1.0.jar:/Users/sulakhe/.m2/repository/xalan/xalan/2.6.0/xalan-2.6.0.jar:/Users/sulakhe/.m2/repository/javax/xml/jaxrpc-api/1.1/jaxrpc-api-1.1.jar:/Users/sulakhe/.m2/repository/com/toedter/jcalendar/2004/jcalendar-2004.jar:/Users/sulakhe/.m2/repository/com/artofsolving/jodconverter/2.2.1/jodconverter-2.2.1.jar:/Users/sulakhe/.m2/repository/org/slf4j/slf4j-api/1.4.3/slf4j-api-1.4.3.jar:/Users/sulakhe/.m2/repository/org/openoffice/juh/2.3.1/juh-2.3.1.jar:/Users/sulakhe/.m2/repository/org/openoffice/jurt/2.3.1/jurt-2.3.1.jar:/Users/sulakhe/.m2/repository/org/openoffice/ridl/2.3.0/ridl-2.3.0.jar:/Users/sulakhe/.m2/repository/org/openoffice/unoil/2.3.0/unoil-2.3.0.jar:/Users/sulakhe/.m2/repository/com/sun/net/httpserver/http/20070405/http-20070405.jar:/Users/sulakhe/.m2/repository/org/cgiar/icis/icis-pedigree/2006/icis-pedigree-2006.jar:/Users/sulakhe/.m2/repository/javax/xml/jsr173/1.0/jsr173-1.0.jar:/Users/sulakhe/.m2/repository/javax/jws/jsr181-api/1.0-MR1/jsr181-api-1.0-MR1.jar:/Users/sulakhe/.m2/repository/javax/annotation/jsr250-api/1.0/jsr250-api-1.0.jar:/Users/sulakhe/.m2/repository/mysql/mysql-connector-java/3.1.14/mysql-connector-java-3.1.14.jar:/Users/sulakhe/.m2/repository/org/tulsoft/alltools/2.1.1-taverna/alltools-2.1.1-taverna.jar:/Users/sulakhe/.m2/repository/org/apache/ant/ant/1.7.0/ant-1.7.0.jar:/Users/sulakhe/.m2/repository/org/apache/ant/ant-launcher/1.7.0/ant-launcher-1.7.0.jar:/Users/sulakhe/.m2/repository/org/apache/ant/ant-nodeps/1.7.0/ant-nodeps-1.7.0.jar:/Users/sulakhe/.m2/repository/ant-contrib/ant-contrib/1.0b2/ant-contrib-1.0b2.jar:/Users/sulakhe/.m2/repository/org/biomoby/jmoby-dashboard/1.1.0-taverna/jmoby-dashboard-1.1.0-taverna.jar:/Users/sulakhe/.m2/repository/com/sun/org/apache/jaxp-ri/1.4/jaxp-ri-1.4.jar:/Users/sulakhe/.m2/repository/javax/xml/parsers/jaxp-api/1.4/jaxp-api-1.4.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/dataflow-activity/0.3-SNAPSHOT/dataflow-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/localworker-activity/0.3-SNAPSHOT/localworker-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/soaplab-activity/0.3-SNAPSHOT/soaplab-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/resources/axis/1.4-taverna/axis-1.4-taverna.jar:/Users/sulakhe/.m2/repository/wsdl4j/wsdl4j/1.5.1/wsdl4j-1.5.1.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/workbench/activity-palette-api/0.1-SNAPSHOT/activity-palette-api-0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/partition/0.3-SNAPSHOT/partition-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/workbench/configuration-api/0.1-SNAPSHOT/configuration-api-0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/opencsv/opencsv/1.8/opencsv-1.8.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/rshell-activity/0.3-SNAPSHOT/rshell-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/uk/org/mygrid/resources/JRclient/RE817/JRclient-RE817.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/stringconstant-activity/0.3-SNAPSHOT/stringconstant-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/wsdl-activity/0.3-SNAPSHOT/wsdl-activity-0.3-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/wsdl-generic/1.2-SNAPSHOT/wsdl-generic-1.2-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/credential-manager/credential-manager/0.0.1-SNAPSHOT/credential-manager-0.0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/t2security-agents/t2security-agents/0.0.1-SNAPSHOT/t2security-agents-0.0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/apache/ws/security/wss4j/1.5.4/wss4j-1.5.4.jar:/Users/sulakhe/.m2/repository/org/apache/santuario/xmlsec/1.4.0/xmlsec-1.4.0.jar:/Users/sulakhe/.m2/repository/opensaml/opensaml/1.1/opensaml-1.1.jar:/Users/sulakhe/.m2/repository/bouncycastle/bcprov-jdk15/136/bcprov-jdk15-136.jar:/Users/sulakhe/.m2/repository/security-profiles/security-profiles/0.0.1-SNAPSHOT/security-profiles-0.0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/xml-security/xmlsec/1.4.0/xmlsec-1.4.0.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/workbench/reference-config/0.1-SNAPSHOT/reference-config-0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/platform/0.1-SNAPSHOT/platform-0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/springframework/spring-orm/2.5.4/spring-orm-2.5.4.jar:/Users/sulakhe/.m2/repository/org/springframework/spring-jdbc/2.5.4/spring-jdbc-2.5.4.jar:/Users/sulakhe/.m2/repository/geronimo-spec/geronimo-spec-jta/1.0-M1/geronimo-spec-jta-1.0-M1.jar:/Users/sulakhe/.m2/repository/org/hibernate/hibernate/3.2.5.ga.raven/hibernate-3.2.5.ga.raven.jar:/Users/sulakhe/.m2/repository/cglib/cglib/2.1_3/cglib-2.1_3.jar:/Users/sulakhe/.m2/repository/asm/asm/1.5.3/asm-1.5.3.jar:/Users/sulakhe/.m2/repository/net/sf/taverna/t2/t2reference-impl/0.1-SNAPSHOT/t2reference-impl-0.1-SNAPSHOT.jar:/Users/sulakhe/.m2/repository/org/aspectj/aspectjrt/1.6.0/aspectjrt-1.6.0.jar:/Users/sulakhe/.m2/repository/org/aspectj/aspectjweaver/1.6.0/aspectjweaver-1.6.0.jar");
			Iterator it = environment.entrySet().iterator();
		//	while(it.hasNext())
		//	{
		//		Map.Entry pairs = (Map.Entry) it.next();
		//		System.out.println(pairs.getKey() + " = " + pairs.getValue());
		//	}			
			try {
				Process process;
				process = builder.start();
				InputStream is = process.getInputStream();

				InputStreamReader isr = new InputStreamReader(is);

				BufferedReader br = new BufferedReader(isr);
				String line;
				System.out.printf("Output of running %s is: ", 
						Arrays.toString(args));
				boolean finished = false;
				String output = "";
				while ((line = br.readLine()) != null) {
					if(finished == true)
					{
						System.out.println(line);
						//this.setOutputDoc(new String[] {line});
						output = output + line;
						workflowStatus = WorkflowStatusType.Done;
					}
					if(line.equals("Finished!"))		
						finished = true;
				}
				String[] temp = output.split(":::");
				System.out.println("\nOUTPUT:\n" + temp[1]);
				setOutputDoc(new String[] {temp[1]});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}

	public TavernaWorkflowServiceImplResource() {
		try {
			config = TavernaWorkflowServiceConfiguration.getConfiguration();
			this.setBaseDir(config.getBaseRepositoryDir());

			if(config.getTavernaDir() == null)
			{
				throw new RemoteException("tavernaDir is not set the services.properties file"); 
			}


			System.out.println("basedir:" + baseDir);

			if (this.getBaseDir().equals("null"))
			{
				if(System.getProperty("os.type").startsWith("Windows"))
				{
					this.setBaseDir(System.getProperty("user.home")+ "\\Application Data" + "\\Taverna-1.7.1\\");
				}
				else
				{
					this.setBaseDir(System.getProperty("user.home")+ "\\Application Data" + "\\Taverna-1.7.1\\");
				}
			}

			//			if (this.getBaseDir().equals("$home/Library/Application Support/Taverna-1.7.1/repository/"))
			//			{
			//				this.setBaseDir(this.getBaseDir().replaceAll("\\$home", System.getenv("HOME")));
			//				//baseDir = baseDir.replaceAll("\\$home", System.getenv("HOME"));
			//			}
			//System.out.println("New Resource Created: " + this.getResourceKey().toString());

			System.out.println("\n\nThe Taverna Basedir is set to: " + baseDir);
			System.out.println("NOTE: Please set the Taverna base directly correctly. This can be set in the service.properties file of service code.\n\n");

			tempDir = System.getProperty("java.io.tmpdir");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String[] getInputDoc() {
		return inputDoc;
	}

	public void setInputDoc(String[] inputDoc) {
		this.inputDoc = inputDoc;
	}

	public static String[] getOutputDoc() {
		return outputDoc;
	}

	public static void setOutputDoc(String[] workflowOuput) {
		outputDoc = workflowOuput;
	}

	public String getScuflDoc() {
		return scuflDoc;
	}

	public void setScuflDoc(String scuflDoc) {
		this.scuflDoc = scuflDoc;
	}



	public void createWorkflow(WMSInputType wMSInputElement)
	{
		try {

			String [] keys = this.getResourceKey().toString().trim().split("TavernaWorkflowServiceImplResultsKey=");
			System.out.println("Workflow NAME :" + wMSInputElement.getWorkflowName());
			this.setWorkflowName(wMSInputElement.getWorkflowName());

			String scuflDocTemp = this.getTempDir() + "/" + keys[1] + "--workflow.xml";
			Utils.stringBufferToFile(new StringBuffer(wMSInputElement.getScuflDoc()), scuflDocTemp);
			this.setScuflDoc(scuflDocTemp);

			//String output = this.getTempDir() + keys[1] + "--output.xml";
			//this.setOutputDoc(output);



		} catch (Exception e){
			this.workflowStatus = WorkflowStatusType.Failed;
			e.printStackTrace();
			System.exit(1);
		}

	}

	public WorkflowStatusType start (StartInputType startInput) {

		String [] keys = this.getResourceKey().toString().trim().split("TavernaWorkflowServiceImplResultsKey=");
		try {
			
			int inputPorts = 0;
			
			if(startInput != null)
			{
				System.out.println("InputFile provided for the workflow.");
				String[] inputs = startInput.getInputArgs();
				for (int i=0; i < inputs.length; i++)
				{
					String inputFile = this.getTempDir() + "/" + keys[1] + "-input-" + i + ".xml";					
					Utils.stringBufferToFile(new StringBuffer(inputs[i]), inputFile);
					System.out.println("Input file " + i + " : " + inputFile);
				}
				this.setInputDoc(inputs);
				inputPorts = this.getInputDoc().length;
			}

			/*			String [] args = { "-workflow", this.getScuflDoc(), 
					"-outputdoc", this.getOutputDoc(),
					"-basedir", this.getBaseDir(),
					"-inputdoc", this.getInputDoc()	};*/


			//The first argument is the scuflDoc string, and remaining are the input strings.
			//String [] args = { this.getScuflDoc(), "blah", "and blah"	};
			String [] args = new String[inputPorts + 3];

			args[0] = "java";
			args[1] = "net.sf.taverna.t2.examples.execution.ExecuteWorkflow";
			args[2] = this.getScuflDoc();
			for(int i = 0; i < inputPorts; i++)
			{
				args[i+3] = this.getInputDoc()[i];
			}

			workflowStatus = WorkflowStatusType.Active;
			WorkflowExecutionThread executor = new WorkflowExecutionThread(args);
			executor.start();

			/*	
			System.out.println("In Service resource: \n");
			System.out.println("Launching WorkflowLauncher: \n");

			String [] results = new ExecuteWorkflow().run(args);
			this.setOutputDoc(results);

			// Need to return the WorkflowStatusType with status as "Done" */

			//this.workflowStatus = WorkflowStatusType.Done;

		} catch (Exception e) {
			e.printStackTrace();
			this.workflowStatus = WorkflowStatusType.Failed;
		}
		return this.workflowStatus;
	}

	public WorkflowOutputType getWorkflowOutput()
	{
		WorkflowOutputType workflowOuputElement = new WorkflowOutputType();
		try {
			//if( (new File(this.getOutputDoc()).exists()) && (this.workflowStatus.equals(WorkflowStatusType.Done)))
			if( this.workflowStatus.equals(WorkflowStatusType.Done) )
			{
				//workflowOuputElement.setOutputFile(Utils.fileToStringBuffer( new File(this.getOutputDoc())).toString());
				// Currently, the results are stored as array of string holding the outputs.
				// Ideally, the output files should be created for each output in the "start" operation,
				//	 and then access those files here, converte them into string buffers and then store them below.

				workflowOuputElement.setOutputFile(this.getOutputDoc());
			}
			else
			{
				throw new RemoteException("Either the Workflow execution is not Completed or Failed.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return workflowOuputElement;
	}


	public WorkflowStatusType getStatus()
	{
		return this.workflowStatus;
	}

}
