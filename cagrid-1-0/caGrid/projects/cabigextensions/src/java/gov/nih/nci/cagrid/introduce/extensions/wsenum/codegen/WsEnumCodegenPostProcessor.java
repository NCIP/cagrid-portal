package gov.nih.nci.cagrid.introduce.extensions.wsenum.codegen;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;

import java.io.File;
import java.io.FileWriter;

import org.projectmobius.common.XMLUtilities;


/**
 * WsEnumCreationPostProcessor Post-creation extension to Introduce to add
 * WS-Enumeration support to a Grid Service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 16, 2006
 * @version $Id: WsEnumCodegenPostProcessor.java,v 1.1 2007-05-03 20:19:44 dervin Exp $
 */
public class WsEnumCodegenPostProcessor implements CodegenExtensionPostProcessor {

    public WsEnumCodegenPostProcessor() {

    }


    public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
        throws CodegenExtensionException {
        try {
            editJNDI(info);
        } catch (Exception e) {
            throw new CodegenExtensionException(e.getMessage(), e);
        }
    }


    private void editJNDI(ServiceInformation info) throws Exception {
        String jndiContents = XMLUtilities.fileNameToString(
            info.getBaseDirectory() + File.separator + "jndi-config.xml");
        String serviceLocStr = "<service name=\"SERVICE-INSTANCE-PREFIX/" + info.getServices().getService(0).getName()
            + "Enumeration\">";
        // String newString = "\n    <resource name=\"home\" type=\"org.globus.ws.enumeration.EnumResourceHome\">\n      <resourceParams>\n        <parameter>\n          <name>factory</name>\n          <value>org.globus.wsrf.jndi.BeanFactory</value>\n        </parameter>\n     </resourceParams>\n    </resource>";
        String newString = "\n    <resourceLink name=\"home\" target=\"java:comp/env/enumeration/EnumerationHome\" />";
        String goldString = "<resourceLink name=\"home\" target=\"java:comp/env/enumeration/EnumerationHome\" />";
        // only add once as this will be called on every save
        if (jndiContents.indexOf(goldString) == -1) {
            int location = jndiContents.indexOf(serviceLocStr);
            StringBuffer sb = new StringBuffer(jndiContents);
            sb.insert(location + serviceLocStr.length(), newString);
            FileWriter fw = new FileWriter(info.getBaseDirectory() + File.separator + "jndi-config.xml");
            fw.write(sb.toString());
            fw.close();
        }
    }
}
