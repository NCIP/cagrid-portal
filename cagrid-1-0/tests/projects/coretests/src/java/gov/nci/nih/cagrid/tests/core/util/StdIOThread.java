/*
 * Created on Apr 22, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class StdIOThread extends Thread {
    private BufferedReader br;


    public StdIOThread(InputStream is) {
        super();
        this.br = new BufferedReader(new InputStreamReader(is));
        super.setDaemon(true);
    }


    @Override
    public void run() {
        try {
            String line = null;
            while ((line = this.br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.br != null) {
                    this.br.close();
                }
            } catch (IOException ex) {

            }
        }
    }

}
