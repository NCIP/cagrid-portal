/**
 * 
 */
package org.cagrid.installer.tasks;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DownloadFileTask extends BasicTask {

	private static final Log logger = LogFactory.getLog(DownloadFileTask.class);

	private static final int BUFFER_SIZE = 1024;

	private String fromUrlProp;

	private String toFileProp;

	// Max time to wait for connection
	private long connectTimeout;

	/**
	 * @param name
	 * @param description
	 */
	public DownloadFileTask(String name, String description,
			String fromUrlProp, String toFileProp, long timeout) {
		super(name, description);
		this.fromUrlProp = fromUrlProp;
		this.toFileProp = toFileProp;
		this.connectTimeout = timeout;
	}

	protected Object internalExecute(Map state) throws Exception {

		String fromUrl = (String) state.get(this.fromUrlProp);

		URL url = null;
		try {
			url = new URL(fromUrl);
		} catch (MalformedURLException ex) {
			throw new RuntimeException("Bad URL.", ex);
		}

		ConnectThread t = new ConnectThread(url);
		t.start();
		try {
			t.join(this.connectTimeout);
		} catch (InterruptedException ex) {
			throw new RuntimeException("Thread interrupted", ex);
		}

		if (t.getEx() != null) {
			throw new RuntimeException("Error connecting to " + fromUrl + ": "
					+ t.getEx().getMessage(), t.getEx());
		}
		if (!t.isFinished()) {
			throw new RuntimeException("Connection to " + fromUrl
					+ " timed out.");
		}
		InputStream inputStream = t.getIn();
//		updateStepCount(inputStream);

		String toFile = state.get(Constants.TEMP_DIR_PATH) + "/"
				+ state.get(this.toFileProp);
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(toFile));
		byte[] buffer = new byte[BUFFER_SIZE];
		int len = -1;
		int stepNum = 0;
		while ((len = inputStream.read(buffer)) > 0) {
			out.write(buffer, 0, len);
			stepNum += 1;
//			if (stepNum >= getStepCount()) {
//				updateStepCount(inputStream);
//
//			}
//			setLastStep(stepNum);
		}
		out.flush();
		out.close();
		inputStream.close();

		return null;
	}

	private void updateStepCount(InputStream inputStream) {
		int bytesRead = getLastStep() * BUFFER_SIZE;
		int available = 0;
		try {
			available = inputStream.available();
		} catch (IOException ex) {
			throw new RuntimeException("Error getting available bytes: "
					+ ex.getMessage(), ex);
		}
		int totalBytes = available + bytesRead;
		int count = totalBytes / BUFFER_SIZE;
		if (totalBytes % BUFFER_SIZE > 0) {
			count += 1;
		}
		setStepCount(count);
	}

	private class ConnectThread extends Thread {
		private InputStream in;

		private Exception ex;

		private boolean finished;

		private URL url;

		ConnectThread(URL url) {
			this.url = url;
		}

		public void run() {
			try {
				this.in = this.url.openStream();
				this.finished = true;
			} catch (Exception ex) {
				this.ex = ex;
			}
		}

		Exception getEx() {
			return this.ex;
		}

		boolean isFinished() {
			return this.finished;
		}

		InputStream getIn() {
			return this.in;
		}
	}

}
