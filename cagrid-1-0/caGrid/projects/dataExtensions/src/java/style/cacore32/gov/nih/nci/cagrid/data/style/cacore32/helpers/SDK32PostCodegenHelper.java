package gov.nih.nci.cagrid.data.style.cacore32.helpers;

import java.io.File;

import gov.nih.nci.cagrid.data.common.CastorMappingUtil;
import gov.nih.nci.cagrid.data.style.cacore32.encoding.SDK32EncodingUtils;
import gov.nih.nci.cagrid.data.style.sdkstyle.helpers.PostCodegenHelper;
import gov.nih.nci.cagrid.data.utilities.WsddUtil;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;

/** 
 *  SDK32PostCodegenHelper
 *  Fixes stuff in the castor mapping
 * 
 * @author David Ervin
 * 
 * @created Aug 30, 2007 3:56:50 PM
 * @version $Id: SDK32PostCodegenHelper.java,v 1.1 2007-08-31 15:50:47 dervin Exp $ 
 */
public class SDK32PostCodegenHelper extends PostCodegenHelper {

    public void codegenPostProcessStyle(ServiceExtensionDescriptionType desc, ServiceInformation info) throws Exception {
        super.codegenPostProcessStyle(desc, info);
        editWsddForCastorMappings(info);
    }
    
    
    private void editWsddForCastorMappings(ServiceInformation info) throws Exception {
        String mainServiceName = info.getIntroduceServiceProperties().getProperty(
            IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
        ServiceType mainService = CommonTools.getService(info.getServices(), mainServiceName);
        String servicePackageName = mainService.getPackageName();
        String packageDir = servicePackageName.replace('.', File.separatorChar);
        // find the client source directory, where the client-config will be located
        File clientConfigFile = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "src"
            + File.separator + packageDir + File.separator + "client" + File.separator + "client-config.wsdd");
        if (!clientConfigFile.exists()) {
            throw new CodegenExtensionException("Client config file " + clientConfigFile.getAbsolutePath()
                + " not found!");
        }
        // fine the server-config.wsdd, located in the service's root directory
        File serverConfigFile = new File(info.getBaseDirectory().getAbsolutePath() + File.separator
            + "server-config.wsdd");
        if (!serverConfigFile.exists()) {
            throw new CodegenExtensionException("Server config file " + serverConfigFile.getAbsolutePath()
                + " not found!");
        }
        
        // set properties in the client
        WsddUtil.setGlobalClientParameter(clientConfigFile.getAbsolutePath(),
            SDK32EncodingUtils.CASTOR_MARSHALLER_PROPERTY, 
            CastorMappingUtil.getMarshallingCastorMappingName(info));
        WsddUtil.setGlobalClientParameter(clientConfigFile.getAbsolutePath(),
            SDK32EncodingUtils.CASTOR_UNMARSHALLER_PROPERTY, 
            CastorMappingUtil.getUnmarshallingCastorMappingName(info));
        
        // set properties in the server
        WsddUtil.setServiceParameter(serverConfigFile.getAbsolutePath(),
            info.getServices().getService(0).getName(),
            SDK32EncodingUtils.CASTOR_MARSHALLER_PROPERTY,
            CastorMappingUtil.getMarshallingCastorMappingName(info));
        WsddUtil.setServiceParameter(serverConfigFile.getAbsolutePath(),
            info.getServices().getService(0).getName(),
            SDK32EncodingUtils.CASTOR_UNMARSHALLER_PROPERTY,
            CastorMappingUtil.getUnmarshallingCastorMappingName(info));
    }
}
