package gov.nih.nci.cagrid.common.portal;

import javax.swing.JFrame;

public abstract class BusyDialogRunnable implements Runnable {
	
	final private BusyDialog dialog;
	
	public BusyDialogRunnable(JFrame owner, String title){
		dialog = new BusyDialog(owner, "Progress (" + title + ")", this);
	}
	
	public void setProgressText(String text){
		dialog.getProgress().setString(text);
	}
	

	public void run() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				dialog.show();
			}
		});
		thread.start();
		dialog.getProgress().setIndeterminate(true);
		process();
		dialog.getProgress().setIndeterminate(false);
		dialog.dispose();
	}
	
    public abstract void process(); 

}
