package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.client.ClientConfigTemplate;
import gov.nih.nci.cagrid.introduce.templates.client.ServiceClientTemplate;
import gov.nih.nci.cagrid.introduce.templates.common.ServiceITemplate;
import gov.nih.nci.cagrid.introduce.templates.service.ServiceImplBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.ServiceImplTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceAuthorizationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.ServiceProviderImplTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.ResourceConstantsTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.base.BaseResourceBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.base.BaseResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.base.BaseResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.lifetime.LifetimeResourceBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.lifetime.LifetimeResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.lifetime.LifetimeResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainConfigurationTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.main.MainResourceTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonResourceBaseTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonResourceHomeTemplate;
import gov.nih.nci.cagrid.introduce.templates.service.globus.resource.singleton.SingletonResourceTemplate;

import java.io.File;
import java.io.FileWriter;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class SkeletonSourceCreator {

    public SkeletonSourceCreator() {
    }


    public void createSkeleton(File baseDirectory, ServiceInformation info, ServiceType service) throws Exception {
        System.out.println("Creating new source code in : " + baseDirectory.getAbsolutePath() + File.separator + "src");

        File srcDir = new File(baseDirectory.getAbsolutePath() + File.separator + "src");
        srcDir.mkdir();

        new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)).mkdirs();
        new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service) + File.separator
            + "client").mkdirs();
        new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service) + File.separator
            + "common").mkdirs();
        new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service) + File.separator
            + "service").mkdirs();
        new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service) + File.separator
            + "service" + File.separator + "globus").mkdirs();
        new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service) + File.separator
            + "service" + File.separator + "globus" + File.separator + "resource").mkdirs();
        new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service) + File.separator
            + "service" + File.separator + "globus" + File.separator + "resource").mkdirs();

        ServiceClientTemplate clientT = new ServiceClientTemplate();
        String clientS = clientT.generate(new SpecificServiceInformation(info, service));
        File clientF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
            + File.separator + "client" + File.separator + service.getName() + "Client.java");

        FileWriter clientFW = new FileWriter(clientF);
        clientFW.write(clientS);
        clientFW.close();

        ClientConfigTemplate clientConfigT = new ClientConfigTemplate();
        String clientConfigS = clientConfigT.generate(new SpecificServiceInformation(info, service));
        File clientConfigF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
            + File.separator + "client" + File.separator + "client-config.wsdd");
        FileWriter clientConfigFW = new FileWriter(clientConfigF);
        clientConfigFW.write(clientConfigS);
        clientConfigFW.close();

        ServiceITemplate iT = new ServiceITemplate();
        String iS = iT.generate(new SpecificServiceInformation(info, service));
        File iF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
            + File.separator + "common" + File.separator + service.getName() + "I.java");

        FileWriter iFW = new FileWriter(iF);
        iFW.write(iS);
        iFW.close();

        ServiceImplBaseTemplate implBaseT = new ServiceImplBaseTemplate();
        String implBaseS = implBaseT.generate(new SpecificServiceInformation(info, service));
        File implBaseF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
            + File.separator + "service" + File.separator + service.getName() + "ImplBase.java");

        FileWriter implBaseFW = new FileWriter(implBaseF);
        implBaseFW.write(implBaseS);
        implBaseFW.close();

        ServiceImplTemplate implT = new ServiceImplTemplate();
        String implS = implT.generate(new SpecificServiceInformation(info, service));
        File implF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
            + File.separator + "service" + File.separator + service.getName() + "Impl.java");

        FileWriter implFW = new FileWriter(implF);
        implFW.write(implS);
        implFW.close();

        ServiceProviderImplTemplate providerImplT = new ServiceProviderImplTemplate();
        String providerImplS = providerImplT.generate(new SpecificServiceInformation(info, service));
        File providerImplF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
            + File.separator + "service" + File.separator + "globus" + File.separator + service.getName()
            + "ProviderImpl.java");

        FileWriter providerImplFW = new FileWriter(providerImplF);
        providerImplFW.write(providerImplS);
        providerImplFW.close();

        ServiceAuthorizationTemplate authorizationT = new ServiceAuthorizationTemplate();
        String authorizationS = authorizationT.generate(new SpecificServiceInformation(info, service));
        File authorizationF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
            + File.separator + "service" + File.separator + "globus" + File.separator + service.getName()
            + "Authorization.java");

        FileWriter authorizationFW = new FileWriter(authorizationF);
        authorizationFW.write(authorizationS);
        authorizationFW.close();
        
        MainConfigurationTemplate metadataConfigurationT = new MainConfigurationTemplate();
        String metadataConfigurationS = metadataConfigurationT.generate(new SpecificServiceInformation(info,
            service));
        File metadataConfigurationF = new File(srcDir.getAbsolutePath() + File.separator
            + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
            + File.separator + "resource" + File.separator + "ResourceConfiguration.java");

        FileWriter metadataConfigurationFW = new FileWriter(metadataConfigurationF);
        metadataConfigurationFW.write(metadataConfigurationS);
        metadataConfigurationFW.close();

        if (service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_BASE_RESOURCE)) {
            ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
            String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(info, service));
            File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "ResourceConstants.java");

            FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
            resourceContanstsFW.write(resourceContanstsS);
            resourceContanstsFW.close();

            BaseResourceBaseTemplate baseResourceBaseT = new BaseResourceBaseTemplate();
            String baseResourceBaseS = baseResourceBaseT.generate(new SpecificServiceInformation(info, service));
            File baseResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "BaseResourceBase.java");

            FileWriter baseResourceBaseFW = new FileWriter(baseResourceBaseF);
            baseResourceBaseFW.write(baseResourceBaseS);
            baseResourceBaseFW.close();

            BaseResourceTemplate baseResourceT = new BaseResourceTemplate();
            String baseResourceS = baseResourceT.generate(new SpecificServiceInformation(info, service));
            File baseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

            FileWriter baseResourceFW = new FileWriter(baseResourceF);
            baseResourceFW.write(baseResourceS);
            baseResourceFW.close();

            BaseResourceHomeTemplate baseResourceHomeT = new BaseResourceHomeTemplate();
            String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(info, service));
            File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "BaseResourceHome.java");

            FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
            baseResourceHomeFW.write(baseResourceHomeS);
            baseResourceHomeFW.close();

        } else if (service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_LIFETIME_RESOURCE)) {
            ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
            String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(info, service));
            File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "ResourceConstants.java");

            FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
            resourceContanstsFW.write(resourceContanstsS);
            resourceContanstsFW.close();

            LifetimeResourceBaseTemplate lifetimeResourceBaseT = new LifetimeResourceBaseTemplate();
            String lifetimeResourceBaseS = lifetimeResourceBaseT
                .generate(new SpecificServiceInformation(info, service));
            File lifetimeResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "BaseResourceBase.java");

            FileWriter lifetimeResourceBaseFW = new FileWriter(lifetimeResourceBaseF);
            lifetimeResourceBaseFW.write(lifetimeResourceBaseS);
            lifetimeResourceBaseFW.close();

            LifetimeResourceTemplate lifetimeResourceT = new LifetimeResourceTemplate();
            String lifetimeResourceS = lifetimeResourceT.generate(new SpecificServiceInformation(info, service));
            File lifetimeResourceF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

            FileWriter lifetimeResourceFW = new FileWriter(lifetimeResourceF);
            lifetimeResourceFW.write(lifetimeResourceS);
            lifetimeResourceFW.close();

            LifetimeResourceHomeTemplate lifetimeResourceHomeT = new LifetimeResourceHomeTemplate();
            String lifetimeResourceHomeS = lifetimeResourceHomeT
                .generate(new SpecificServiceInformation(info, service));
            File lifetimeResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "BaseResourceHome.java");

            FileWriter lifetimeResourceHomeFW = new FileWriter(lifetimeResourceHomeF);
            lifetimeResourceHomeFW.write(lifetimeResourceHomeS);
            lifetimeResourceHomeFW.close();

        } else if (service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_SINGLETON_RESOURCE)) {
            ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
            String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(info, service));
            File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "ResourceConstants.java");

            FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
            resourceContanstsFW.write(resourceContanstsS);
            resourceContanstsFW.close();

            SingletonResourceTemplate baseResourceT = new SingletonResourceTemplate();
            String baseResourceS = baseResourceT.generate(new SpecificServiceInformation(info, service));
            File baseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

            FileWriter baseResourceFW = new FileWriter(baseResourceF);
            baseResourceFW.write(baseResourceS);
            baseResourceFW.close();

            SingletonResourceBaseTemplate baseResourceBaseT = new SingletonResourceBaseTemplate();
            String baseResourceBaseS = baseResourceBaseT.generate(new SpecificServiceInformation(info, service));
            File baseResourceBaseF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "BaseResourceBase.java");

            FileWriter baseResourceBaseFW = new FileWriter(baseResourceBaseF);
            baseResourceBaseFW.write(baseResourceBaseS);
            baseResourceBaseFW.close();

            SingletonResourceHomeTemplate baseResourceHomeT = new SingletonResourceHomeTemplate();
            String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(info, service));
            File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "BaseResourceHome.java");

            FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
            baseResourceHomeFW.write(baseResourceHomeS);
            baseResourceHomeFW.close();

        } else if (service.getResourceFrameworkType().equals(IntroduceConstants.INTRODUCE_MAIN_RESOURCE)) {
            ResourceConstantsTemplate resourceContanstsT = new ResourceConstantsTemplate();
            String resourceContanstsS = resourceContanstsT.generate(new SpecificServiceInformation(info, service));
            File resourceContanstsF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "ResourceConstants.java");

            FileWriter resourceContanstsFW = new FileWriter(resourceContanstsF);
            resourceContanstsFW.write(resourceContanstsS);
            resourceContanstsFW.close();

            ServiceConfigurationTemplate serviceConfT = new ServiceConfigurationTemplate();
            String serviceConfS = serviceConfT.generate(new SpecificServiceInformation(info, service));
            File serviceConfF = new File(srcDir.getAbsolutePath() + File.separator + CommonTools.getPackageDir(service)
                + File.separator + "service" + File.separator + "ServiceConfiguration.java");

            FileWriter serviceConfFW = new FileWriter(serviceConfF);
            serviceConfFW.write(serviceConfS);
            serviceConfFW.close();

            MainResourceTemplate baseResourceT = new MainResourceTemplate();
            String baseResourceS = baseResourceT.generate(new SpecificServiceInformation(info, service));
            File baseResourceF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + service.getName() + "Resource.java");

            FileWriter baseResourceFW = new FileWriter(baseResourceF);
            baseResourceFW.write(baseResourceS);
            baseResourceFW.close();

            MainResourceHomeTemplate baseResourceHomeT = new MainResourceHomeTemplate();
            String baseResourceHomeS = baseResourceHomeT.generate(new SpecificServiceInformation(info, service));
            File baseResourceHomeF = new File(srcDir.getAbsolutePath() + File.separator
                + CommonTools.getPackageDir(service) + File.separator + "service" + File.separator + "globus"
                + File.separator + "resource" + File.separator + "BaseResourceHome.java");

            FileWriter baseResourceHomeFW = new FileWriter(baseResourceHomeF);
            baseResourceHomeFW.write(baseResourceHomeS);
            baseResourceHomeFW.close();

        }

    }

}