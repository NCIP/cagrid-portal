package gov.nih.nci.cagrid.introduce.upgrade.model;

import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.ModelUpgraderBase;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;


public class Model_1_2__1_3_Upgrader extends ModelUpgraderBase {

    public Model_1_2__1_3_Upgrader(IntroduceUpgradeStatus status, String servicePath) {
        super(status, servicePath, "1.2", "1.3");
    }


    protected void upgrade() throws Exception {
        getStatus().addDescriptionLine("Updating to the new Resource Framework Options");
        Document doc = XMLUtilities.fileNameToDocument(getServicePath() + File.separator + "introduce.xml");
       

        FileWriter writer = new FileWriter(getServicePath() + File.separator + "introduce.xml");
        writer.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
        writer.close();
    }
}
