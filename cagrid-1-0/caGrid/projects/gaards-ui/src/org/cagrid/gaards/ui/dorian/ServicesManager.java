package org.cagrid.gaards.ui.dorian;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.RunnerGroup;
import gov.nih.nci.cagrid.common.ThreadManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.configuration.ServiceConfiguration;
import org.cagrid.grape.configuration.ServiceDescriptor;
import org.cagrid.grape.configuration.Services;


public class ServicesManager extends Runner {

    private static ServicesManager instance;

    private Logger log;

    private List<DorianHandle> dorianServices;

    private ThreadManager threadManager;

    private Object mutex;
    private boolean firstRun;


    private ServicesManager() {
        this.log = Logger.getLogger(getClass());
        this.dorianServices = new ArrayList<DorianHandle>();
        this.threadManager = new ThreadManager();
        this.mutex = new Object();
        this.firstRun = true;
    }
    
    private void startup(){
        try {
            threadManager.executeInBackground(this);
        } catch (Exception e) {
            log.error(e);
        }
    }


    public synchronized static ServicesManager getInstance() {
        if (instance == null) {
            instance = new ServicesManager();
            instance.startup();
        }
        return instance;
    }


    public void execute() {
        while (true) {
            if (firstRun) {
                synchronized (mutex) {
                    syncServices();
                    firstRun = false;
                }
            } else {
                syncServices();
            }
            try {
                Thread.sleep(120000);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
    
    public void syncWithUpdatedConfiguration(){
        synchronized (mutex) {
            if(!firstRun){
               syncServices(); 
            }
        }
    }


    public synchronized void syncServices() {
        List<DorianHandle> dorians = new ArrayList<DorianHandle>();
        try {
            ServiceConfiguration conf = (ServiceConfiguration) GridApplication.getContext().getConfigurationManager()
                .getConfigurationObject(DorianUIConstants.DORIAN_UI_CONF);
            Services s = conf.getServices();
            if (s != null) {
                ServiceDescriptor[] list = s.getServiceDescriptor();
                if (list != null) {
                    RunnerGroup group = new RunnerGroup();
                    for (int i = 0; i < list.length; i++) {
                        DorianHandle handle = new DorianHandle(list[i]);
                        dorians.add(handle);
                        group.add(new AuthenticationLookupThread(handle));
                    }
                    threadManager.executeGroup(group);
                }
            }
        } catch (Throwable e) {
            log.error(e);
        }
        synchronized (mutex) {
            this.dorianServices = dorians;
        }
    }


    public List<DorianHandle> getDorianServices() {
        synchronized (mutex) {
            return dorianServices;
        }
    }
}
