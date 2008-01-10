package org.cagrid.transfer.servlet;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cagrid.transfer.context.stubs.TransferServiceContextResourceProperties;
import org.cagrid.transfer.descriptor.DataDescriptor;
import org.globus.axis.gsi.GSIConstants;


public class TransferServlet extends HttpServlet {

    private Properties props = null;
    String persistenceDir = null;
    int blockSize = 1024;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        props = new Properties();
        System.out.println("Calling Transfer Servlet: " + getServletContext().getServerInfo() + getServletInfo());

        // reload everytime now so that it ca be changed.....
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("server.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
        persistenceDir = props.getProperty("transfer.service.persistence.location");
        String configBlockSizeS = props.getProperty("transfer.service.block.size");
        if (configBlockSizeS != null) {

            try {
                int configBlockSize = Integer.parseInt(configBlockSizeS);
                blockSize = configBlockSize;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Storage data is stored in: " + persistenceDir);

        // 1 get the GSI attributes
        String userDN = (String) req.getAttribute(GSIConstants.GSI_USER_DN);

        // 2 get the requested ID
        String requestedID = (String) req.getParameter("id");
        if (requestedID == null || requestedID.length() <= 0) {
            System.out.println("Not ID");
            throw new IOException("No ID provided");
        }

        // 3 authorize
        TransferServiceContextResourceProperties props = null;
        try {
            props = (TransferServiceContextResourceProperties) Utils.deserializeObject(new FileReader(persistenceDir
                + File.separator + requestedID + ".xml"), TransferServiceContextResourceProperties.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Cannot locate resource for id: " + requestedID);
        }

        DataDescriptor desc = props.getDataDescriptor();
        // verify that the user calling is the owner or there is no owner
        if (desc.getUserDN() == null || userDN.equals(desc.getUserDN())) {

            // 4 write data to the response
            FileInputStream fis = new FileInputStream(desc.getLocation());
            byte[] bytes = new byte[blockSize];
            int length = fis.read(bytes);
            while (length == blockSize) {
                resp.getOutputStream().write(bytes);
                length = fis.read(bytes);
            }
            resp.getOutputStream().write(bytes,0,length);
        }

    }

}
