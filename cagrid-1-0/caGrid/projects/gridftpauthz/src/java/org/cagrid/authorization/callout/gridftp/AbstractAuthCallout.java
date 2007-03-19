package org.cagrid.authorization.callout.gridftp;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.authorization.AuthLogger;


/*-----------------------------------------------------------------------------
 * Copyright (c) 2003-2004, The Ohio State University,
 * Department of Biomedical Informatics, Multiscale Computing Laboratory
 * All rights reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3  All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement: This product includes
 *    material developed by the Mobius Project (http://www.projectmobius.org/).
 * 
 * 4. Neither the name of the Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * 5. Products derived from this Software may not be called "Mobius"
 *    nor may "Mobius" appear in their names without prior written
 *    permission of Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *---------------------------------------------------------------------------*/

public abstract class AbstractAuthCallout implements Authorize {

	static String _logFilePattern = "%t/AuthCallout.%u.log";
	protected Logger _logger = Logger.getLogger("AuthCallout");
	//TODO change logger
	//private Log _logger = LogFactory.getLog(AuthLogger.class.getName());
	

	
	public AbstractAuthCallout() {
		try {
			Handler handler = new FileHandler(_logFilePattern);
			handler.setFormatter(new SimpleFormatter());
			_logger.addHandler(handler);
			_logger.setLevel(Level.INFO);
		} catch (Exception e) {
			System.out.println("Could not configure logging due to reason: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public boolean authorize(String identity, String operation, String target) {
		boolean authorized = false;
		
		_logger.info("identity: " + identity);
		_logger.info("operation: " + operation);
		_logger.info("target URL: " + target);

		//there are only a few valid operation strings
		//convert to the appropriate typesafe enum
		GridFTPOperation.Operation op;
		try {
			op = GridFTPOperation.toAuthOperation(operation);
			authorized = authorizeOperation(identity, op, target);
		} catch (UnknownOperationException e) {
			//need to make an authorization decision. Clearly this implementation version
			//does not match the GridFTP supported operations, so basically barf.
			String msg = "Detected unsupported operation: " + operation;
			_logger.severe(msg);
		}
		
		return authorized;
	}

	public abstract boolean authorizeOperation(String identity, GridFTPOperation.Operation operation, String target);
	
}
