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


public class Model_1_1__1_3_Upgrader extends ModelUpgraderBase {

    public Model_1_1__1_3_Upgrader(IntroduceUpgradeStatus status, String servicePath) {
        super(status, servicePath, "1.1", "1.3");
    }


    protected void upgrade() throws Exception {
        getStatus().addDescriptionLine("Updating to the new Resource Framework Options");
        Document doc = XMLUtilities.fileNameToDocument(getServicePath() + File.separator + "introduce.xml");
        Element servicesEl = doc.getRootElement().getChild("Services",
            Namespace.getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));
        List serviceEls = servicesEl.getChildren();
        for (int i = 0; i < serviceEls.size(); i++) {
            Element service = (Element) serviceEls.get(i);
            String resourceFrameworkType = service.getAttributeValue("resourceFrameworkType");
            Element resourceFrameworkOptions = new Element("ResourceFrameworkOptions", Namespace
                .getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));

            Element main = new Element("Main", Namespace.getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));
            Element singleton = new Element("Singleton", Namespace
                .getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));
            Element identifiable = new Element("Identifiable", Namespace
                .getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));
            Element lifetime = new Element("Lifetime", Namespace
                .getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));
            Element custom = new Element("Custom", Namespace
                .getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));
            Element secure = new Element("Secure", Namespace
                .getNamespace("gme://gov.nih.nci.cagrid.introduce/1/Services"));

            if (resourceFrameworkType.equals(IntroduceConstants.INTRODUCE_MAIN_RESOURCE)) {
                resourceFrameworkOptions.addContent(main);
                resourceFrameworkOptions.addContent(singleton);
                resourceFrameworkOptions.addContent(identifiable);
                resourceFrameworkOptions.addContent(secure);
            } else if (resourceFrameworkType.equals(IntroduceConstants.INTRODUCE_CUSTOM_RESOURCE)) {
                resourceFrameworkOptions.addContent(custom);
            } else if (resourceFrameworkType.equals(IntroduceConstants.INTRODUCE_SINGLETON_RESOURCE)) {
                resourceFrameworkOptions.addContent(singleton);
                resourceFrameworkOptions.addContent(identifiable);
                resourceFrameworkOptions.addContent(secure);
            } else if (resourceFrameworkType.equals("base")) {
                resourceFrameworkOptions.addContent(identifiable);
                resourceFrameworkOptions.addContent(secure);
            } else if (resourceFrameworkType.equals(IntroduceConstants.INTRODUCE_LIFETIME_RESOURCE)) {
                resourceFrameworkOptions.addContent(lifetime);
                resourceFrameworkOptions.addContent(identifiable);
                resourceFrameworkOptions.addContent(secure);
            }

            service.addContent(resourceFrameworkOptions);
            service.removeAttribute("resourceFrameworkType");
        }

        FileWriter writer = new FileWriter(getServicePath() + File.separator + "introduce.xml");
        writer.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
        writer.close();
    }
}