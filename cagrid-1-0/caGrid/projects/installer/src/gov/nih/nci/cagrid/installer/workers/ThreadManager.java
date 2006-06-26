package gov.nih.nci.cagrid.installer.workers;


import java.util.ArrayList;

public class ThreadManager {
	private ArrayList	tasks = new ArrayList();
	
	private int swcounter=0;
	public ThreadManager(ArrayList tasks){
		this.tasks= tasks;
	}
    
	public void executeTasks(){
		if(tasks.size()!=0){
			firstFirstTask();
		}
	}
	
	
	
	private void firstFirstTask(){
		
		SwingWorker sw = (SwingWorker)tasks.get(swcounter);
		fireTask(sw);
	}
	
	private void fireNextTask(){
		if(swcounter<tasks.size()){
		SwingWorker sw = (SwingWorker)tasks.get(swcounter);
		fireTask(sw);
		}
	}
	
	private void fireTask(SwingWorker sw){
		
		sw.start();
		swcounter++;
	}
	public void notifyFinished(){
		
		fireNextTask();
	}
}

