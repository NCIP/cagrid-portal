package org.cagrid.data.sdkquery41.style.wizard.config;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DomainModelConfigurationStep
 * Provides a central point for configuring the domain model.
 * 
 * This bean will actually store up information from all three means of
 * getting a domain model into a service (file system provided, cadsr, and SDK's XMI)
 * and on applyConfiguration() implement the method defined by setModelSource() 
 * 
 * @author David
 */
public class DomainModelConfigurationStep extends AbstractStyleConfigurationStep {
    
    private DomainModelConfigurationSource configurationSource = null;
    
    // from local file system
    private File domainModelFile = null;
    
    // from SDK's config
    private String projectShortName = null;
    private String projectVersion = null;
    
    // from cadsr
    // TODO: from mms??
    private Project selectedProject = null;
    private List<UMLPackageMetadata> selectedPackages = null;

    public DomainModelConfigurationStep(ServiceInformation serviceInfo) {
        super(serviceInfo);
        selectedPackages = new ArrayList<UMLPackageMetadata>();
    }


    public void applyConfiguration() throws Exception {
        if (configurationSource == null) {
            throw new IllegalStateException(
                "The domain model configuration source has not been defined!!!");
        }
        switch (configurationSource) {
            case CADSR:
                applyCadsrModelConfiguration();
                break;
            case SDK_CONFIG_XMI:
                applySdkModelConfiguration();
                break;
            case FILE_SYSTEM:
                applyFileSystemModelConfiguration();
                break;
            default:
                throw new IllegalStateException("Unknown model configuration source");
        }
    }
    
    
    private void applyCadsrModelConfiguration() throws Exception {
        
    }
    
    
    private void applySdkModelConfiguration() throws Exception {
        
    }
    
    
    private void applyFileSystemModelConfiguration() throws Exception {
        
    }
    
    
    public void setModelSource(DomainModelConfigurationSource source) {
        this.configurationSource = source;
    }
    
    
    public DomainModelConfigurationSource getCurrentModelSource() {
        return configurationSource;
    }
    
    
    public void setDomainModelLocalFile(File file) {
        this.domainModelFile = file;
    }
    
    
    public void setModelFromConfigInformation(String shortName, String version) {
        this.projectShortName = shortName;
        this.projectVersion = version;
    }
    
    
    public void setCadsrProject(Project proj) {
        this.selectedProject = proj;
    }
    
    
    public void addCadsrPackage(UMLPackageMetadata pack) {
        for (UMLPackageMetadata p : selectedPackages) {
            if (p.getName().equals(pack)) {
                throw new IllegalStateException("Package " + p.getName() + " is already selected");
            }
        }
        this.selectedPackages.add(pack);
    }
    
    
    public boolean removeCadsrPackage(String name) {
        Iterator<UMLPackageMetadata> iter = selectedPackages.iterator();
        while (iter.hasNext()) {
            UMLPackageMetadata pack = iter.next();
            if (name.equals(pack.getName())) {
                iter.remove();
                return true;
            }
        }
        return false;
    }
    
    
    public List<UMLPackageMetadata> getCadsrPacakges() {
        return this.selectedPackages;
    }
    
    
    public enum DomainModelConfigurationSource {
        CADSR, SDK_CONFIG_XMI, FILE_SYSTEM
    }
}
