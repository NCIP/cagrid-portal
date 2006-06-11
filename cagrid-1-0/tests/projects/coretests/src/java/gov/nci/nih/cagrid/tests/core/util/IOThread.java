/*
 * Created on Jun 10, 2006
 */
package gov.nci.nih.cagrid.tests.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class IOThread
	extends Thread
{
	private InputStream is;
	private PrintStream out;
	private Exception exception;
	
	public IOThread(InputStream is, PrintStream out)
	{
		super();
		
		this.is = is;
		this.out = out;
	}
	
	public void run()
	{
		this.exception = null;
		try {
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) out.write(buf, 0, len);
			
			out.flush();
			out.close();
			is.close();
		} catch (IOException e) {
			this.exception = e;
			e.printStackTrace();
		}
	}
	
	public Exception getException()
	{
		return exception;
	}
}
