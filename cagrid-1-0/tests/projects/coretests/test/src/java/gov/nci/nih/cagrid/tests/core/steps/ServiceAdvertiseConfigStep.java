/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.axis.message.addressing.EndpointReferenceType;

import com.atomicobject.haste.framework.Step;


/**
 * This step modifies the etc/registration.xml file to register with a specific
 * index service (likely
 * http://localhost:8080/wsrf/services/DefaultIndexService).
 * 
 * @author Patrick McConnell
 */
public class ServiceAdvertiseConfigStep extends Step {
    private File serviceDir;
    private EndpointReferenceType indexServiceEPR;


    public ServiceAdvertiseConfigStep(EndpointReferenceType indexServiceEPR, File serviceDir) {
        super();

        this.serviceDir = serviceDir;
        this.indexServiceEPR = indexServiceEPR;
    }


    @Override
    public void runStep() throws Throwable {
        File tmpFile = File.createTempFile("AdvertiseServiceStep", ".xml");
        tmpFile.deleteOnExit();
        File registrationFile = new File(this.serviceDir, "etc" + File.separator + "registration.xml");

        // TODO: clean this up
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tmpFile)));
        BufferedReader br = new BufferedReader(new FileReader(registrationFile));
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("<wsa:Address>")) {
                    int index1 = line.indexOf(">");
                    int index2 = line.indexOf("<", index1 + 1);
                    line = line.substring(0, index1 + 1) + this.indexServiceEPR.getAddress().toString()
                        + line.substring(index2);
                }
                out.println(line);
            }
        } finally {
            out.close();
            out.flush();
            br.close();
        }

        FileUtils.copy(tmpFile, registrationFile);
        tmpFile.delete();
    }
}
