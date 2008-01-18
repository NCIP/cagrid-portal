package org.cagrid.transfer.servlet;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cagrid.transfer.context.common.TransferServiceContextConstants;
import org.cagrid.transfer.context.stubs.TransferServiceContextResourceProperties;
import org.cagrid.transfer.descriptor.DataStorageDescriptor;
import org.globus.axis.gsi.GSIConstants;


public class TransferServlet extends HttpServlet {

    private Properties props = null;
    String persistenceDir = null;
    int blockSize = 1024;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        props = new Properties();
        System.out.println("Calling Transfer Servlet PUT: " + getServletContext().getServerInfo() + getServletInfo());
        System.out.println("Calling Transfer Servlet at: " + getServletContext().getRealPath("/"));
        
        
        // reload everytime now so that it can be changed while container is running.....
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("server.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            resp.sendError(500);
            return;
        }
        
        String myLocation =  getServletContext().getRealPath("/").replace("\\", "/");
        String rootWebappLocation = myLocation.substring(0,myLocation.lastIndexOf("/"));
        rootWebappLocation = rootWebappLocation.substring(0,rootWebappLocation.lastIndexOf("/") + 1);
        String persistenceDir = rootWebappLocation + props.getProperty("transfer.service.persistence.relative.location");
        System.out.println("Storage data is stored in: " + persistenceDir);
        
        String configBlockSizeS = props.getProperty("transfer.service.block.size");
        if (configBlockSizeS != null) {

            try {
                int configBlockSize = Integer.parseInt(configBlockSizeS);
                blockSize = configBlockSize;
            } catch (NumberFormatException e) {
                System.out.println("Service attribute block size not configured properly");
                resp.sendError(500);
                return;
            }
        }

        // 1 get the GSI attributes
        String userDN = (String) req.getAttribute(GSIConstants.GSI_USER_DN);

        // 2 get the requested ID
        String requestedID = (String) req.getParameter("id");
        if (requestedID == null || requestedID.length() <= 0) {
            System.out.println("Not ID");
            resp.sendError(400);
            return;
        }

        // 3 authorize
        TransferServiceContextResourceProperties props = null;
        try {
            props = (TransferServiceContextResourceProperties) Utils.deserializeObject(new FileReader(persistenceDir
                + File.separator + requestedID + ".xml"), TransferServiceContextResourceProperties.class);
        } catch (Exception e) {
            System.out.println("Cannot find or deserialize the resource properties describing this transfer object: " + requestedID);
            e.printStackTrace();
            resp.sendError(500);
            return;
        }

        DataStorageDescriptor desc = props.getDataStorageDescriptor();
        // verify that the user calling is the owner or there is no owner
        if (desc.getUserDN() == null || desc.getUserDN().equals(userDN)) {
            System.out.println("Storing data using block size of: " + blockSize );
            // 4 write data to the response
            File outFile = new File(desc.getLocation());
            if(outFile.exists()){
                System.out.println("File is already staged for resource: " + requestedID + " at file: " + desc.getLocation());
                resp.sendError(500);
                return;
            }
            
            FileOutputStream fos = new FileOutputStream(desc.getLocation());
            byte[] data = new byte[blockSize];
            int length = req.getInputStream().read(data);
            while(length==blockSize){
                fos.write(data);
                length = req.getInputStream().read(data);
            }
           fos.write(data,0,length);
           fos.close();
        } else {
            System.out.println("Trouble storing data for requested object: " + requestedID + " at file: " + desc.getLocation());
            resp.sendError(403);
            return;
        }
        props.getDataStorageDescriptor().setStaged(true);
        try {
            Utils.serializeDocument(persistenceDir
                    + File.separator + requestedID + ".xml", props, TransferServiceContextConstants.RESOURCE_PROPERTY_SET);
        } catch (Exception e) {
            resp.sendError(500);
            return;
        }
        
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        props = new Properties();
        System.out.println("Calling Transfer Servlet GET: " + getServletContext().getServerInfo() + getServletInfo());
        System.out.println("Calling Transfer Servlet at: " + getServletContext().getRealPath("/"));
        
        
        // reload everytime now so that it can be changed while container is running.....
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("server.properties"));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
            return;
            
        }
        
        String myLocation =  getServletContext().getRealPath("/").replace("\\", "/");
        String rootWebappLocation = myLocation.substring(0,myLocation.lastIndexOf("/"));
        rootWebappLocation = rootWebappLocation.substring(0,rootWebappLocation.lastIndexOf("/") + 1);
        String persistenceDir = rootWebappLocation + props.getProperty("transfer.service.persistence.relative.location");
        System.out.println("Storage data is stored in: " + persistenceDir);
        
        String configBlockSizeS = props.getProperty("transfer.service.block.size");
        if (configBlockSizeS != null) {

            try {
                int configBlockSize = Integer.parseInt(configBlockSizeS);
                blockSize = configBlockSize;
            } catch (NumberFormatException e) {
                System.out.println("Service attribute block size not configured properly");
                resp.sendError(500);
                return;
            }
        }

        // 1 get the GSI attributes
        String userDN = (String) req.getAttribute(GSIConstants.GSI_USER_DN);

        // 2 get the requested ID
        String requestedID = (String) req.getParameter("id");
        if (requestedID == null || requestedID.length() <= 0) {
            System.out.println("Not ID");
            resp.sendError(400);
            return;
        }

        // 3 authorize
        TransferServiceContextResourceProperties props = null;
        try {
            props = (TransferServiceContextResourceProperties) Utils.deserializeObject(new FileReader(persistenceDir
                + File.separator + requestedID + ".xml"), TransferServiceContextResourceProperties.class);
        } catch (Exception e) {
            System.out.println("Cannot find or deserialize the resource properties describing this transfer object: " + requestedID);
            e.printStackTrace();
            resp.sendError(500);
            return;
        }

        DataStorageDescriptor desc = props.getDataStorageDescriptor();
        // verify that the user calling is the owner or there is no owner
        if (desc.getUserDN() == null || desc.getUserDN().equals(userDN)) {
            System.out.println("Transfering data using block size of: " + blockSize );
            // 4 write data to the response
            FileInputStream fis = new FileInputStream(desc.getLocation());
            byte[] bytes = new byte[blockSize];
            int length = fis.read(bytes);
            while (length == blockSize) {
                resp.getOutputStream().write(bytes);
                length = fis.read(bytes);
            }
            resp.getOutputStream().write(bytes,0,length);
        } else {
            System.out.println("Trouble retrieving data for requested object: " + requestedID + " at file: " + desc.getLocation());
            resp.sendError(403);
            return;
        }

    }

}
