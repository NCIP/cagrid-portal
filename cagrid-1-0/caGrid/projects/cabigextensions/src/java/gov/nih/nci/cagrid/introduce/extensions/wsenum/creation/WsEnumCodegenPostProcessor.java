package gov.nih.nci.cagrid.introduce.extensions.wsenum.creation;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;

import java.io.File;
import java.io.FileWriter;

import org.projectmobius.common.XMLUtilities;


/**
 * WsEnumCreationPostProcessor Post-creation extension to Introduce to add
 * WS-Enumeration support to a Grid Service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 16, 2006
 * @version $Id: WsEnumCodegenPostProcessor.java,v 1.1 2007-05-02 14:22:54 hastings Exp $
 */
public class WsEnumCodegenPostProcessor implements CodegenExtensionPostProcessor {

    public WsEnumCodegenPostProcessor() {

    }


    public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
        throws CodegenExtensionException {
        try {
            editJNDI(desc, info);
        } catch (Exception e) {
            throw new CodegenExtensionException(e.getMessage(), e);
        }
    }


    private void editJNDI(ServiceExtensionDescriptionType desc, ServiceInformation info) throws Exception {
        String jndiContents = XMLUtilities.fileNameToString(info.getBaseDirectory() + File.separator
            + "jndi-config.xml");
        String serviceLocStr = "<service name=\"SERVICE-INSTANCE-PREFIX/" + info.getServices().getService(0).getName()
            + "Enumeration\">";
        String newString = "\n    <resource name=\"home\" type=\"org.globus.ws.enumeration.EnumResourceHome\">\n      <resourceParams>\n        <parameter>\n          <name>factory</name>\n          <value>org.globus.wsrf.jndi.BeanFactory</value>\n        </parameter>\n     </resourceParams>\n    </resource>";
        String testString = "<resource name=\"home\" type=\"org.globus.ws.enumeration.EnumResourceHome\">";
        //only add once as this will be called on every save
        if (jndiContents.indexOf(testString) < 0) {
            int location = jndiContents.indexOf(serviceLocStr);
            StringBuffer sb = new StringBuffer(jndiContents);
            sb.insert(location + serviceLocStr.length(), newString);
            FileWriter fw = new FileWriter(info.getBaseDirectory() + File.separator + "jndi-config.xml");
            fw.write(sb.toString());
            fw.close();
        }
    }
}
