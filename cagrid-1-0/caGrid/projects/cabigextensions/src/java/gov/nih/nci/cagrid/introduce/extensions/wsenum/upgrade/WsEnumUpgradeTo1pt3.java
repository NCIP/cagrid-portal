package gov.nih.nci.cagrid.introduce.extensions.wsenum.upgrade;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.wsenum.common.WsEnumConstants;

public class WsEnumUpgradeTo1pt3 extends WsEnumUpgradeTo1pt2 {
    
    private static final String DATA_SERVICE_ENUM_PROPERTY = "dataService_enumIteratorType";

    public WsEnumUpgradeTo1pt3(ExtensionType extensionType, ServiceInformation serviceInfo, String servicePath,
        String fromVersion, String toVersion) {
        super(extensionType, serviceInfo, servicePath, fromVersion, toVersion);
    }

    
    protected void upgrade() throws Exception {
        super.upgrade();
        System.out.println("UPGRADE TO 1.2 COMPLETE, PROCEDING TO " + UpgraderConstants.ENUMERATION_CURRENT_VERSION);
        System.out.println("UPGRADE TO 1.2 COMPLETE, PROCEDING TO " + UpgraderConstants.ENUMERATION_CURRENT_VERSION);
        System.out.println("UPGRADE TO 1.2 COMPLETE, PROCEDING TO " + UpgraderConstants.ENUMERATION_CURRENT_VERSION);
        System.out.println("UPGRADE TO 1.2 COMPLETE, PROCEDING TO " + UpgraderConstants.ENUMERATION_CURRENT_VERSION);
        if (getStatus().getStatus().equals(StatusBase.UPGRADE_OK)) {
            try {
                String line = "Adding service property " + WsEnumConstants.ITER_IMPL_TYPE_PROPERTY;
                System.out.println("ADDING DESCRIPTION LINE:");
                System.out.println(line);
                getStatus().addDescriptionLine(line);
                // only procede if the base upgrade worked
                String iterTypeName = WsEnumConstants.DEFAULT_ITER_IMPL_TYPE;
                // if the data service extension has set the enum iterator type, use it
                ServiceDescription desc = getServiceInformation().getServiceDescriptor();
                if (CommonTools.servicePropertyExists(desc, DATA_SERVICE_ENUM_PROPERTY)) {
                    iterTypeName = CommonTools.getServicePropertyValue(desc, DATA_SERVICE_ENUM_PROPERTY);
                    line = "Using data service ws-enum iter implementation property value of " + iterTypeName;
                    System.out.println("ADDING DESCRIPTION LINE:");
                    System.out.println(line);
                    getStatus().addDescriptionLine(line);
                }
                setIterImplTypeServiceProperty(iterTypeName);
                System.out.println("ADDING ISSUE:");
                String issue = "New WS-Enumeration IterImplType service property added";
                String resolution = "If it exists, the data service property for controling the WS-Enumeration IterImplType (" 
                    + DATA_SERVICE_ENUM_PROPERTY + ") is deprecated.  Use " + WsEnumConstants.ITER_IMPL_TYPE_PROPERTY; 
                getStatus().addIssue(issue, resolution);
            } catch (Exception ex) {
                getStatus().setStatus(StatusBase.UPGRADE_FAIL);
                throw ex;
            }
        }
    }
    
    
    private void setIterImplTypeServiceProperty(String iterTypeName) {
        ServiceDescription desc = getServiceInformation().getServiceDescriptor();
        if (!CommonTools.servicePropertyExists(desc, WsEnumConstants.ITER_IMPL_TYPE_PROPERTY)) {
            CommonTools.setServiceProperty(desc, WsEnumConstants.ITER_IMPL_TYPE_PROPERTY, 
                iterTypeName, false);
        }
    }
}
