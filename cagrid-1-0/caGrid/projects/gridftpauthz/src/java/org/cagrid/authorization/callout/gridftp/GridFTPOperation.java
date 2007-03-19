package org.cagrid.authorization.callout.gridftp;
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

/**
 * This class uses an Enum as a type-safe version of an operation.
 * Use {@link #toAuthOperation(String op) toAuthOperation(String op)} to get
 * an Operation for a given String.
 * Valid operation strings can be found at the following URL:
 * {@link http://www.globus.org/toolkit/docs/4.0/security/cas/WS_AA_CAS_HOWTO_Setup_GridFTP.html WS_AA_CAS_HOWTO_Setup_GridFTP}
 */
public class GridFTPOperation {

	private static String OP_READ_STRING = "read";
	private static String OP_LOOKUP_STRING = "lookup";
	private static String OP_WRITE_STRING = "write";
	private static String OP_CREATE_STRING = "create";
	private static String OP_DELETE_STRING = "delete";
	private static String OP_CHDIR_STRING = "chdir";

	public enum Operation { 
		
		READ(OP_READ_STRING), 
		LOOKUP(OP_LOOKUP_STRING),
		WRITE(OP_WRITE_STRING),
		CREATE(OP_CREATE_STRING),
		DELETE(OP_DELETE_STRING),
		CHDIR(OP_CHDIR_STRING);
		
		private String _operation;
		
		private Operation(String op) {
			_operation = op;
		}
		
		public String toString() {
			return _operation;
		}
		
	}
	
	public static Operation toAuthOperation(String op) throws UnknownOperationException {
		if (op.equals(OP_READ_STRING)) {
			return Operation.READ;
		} else if (op.equals(OP_LOOKUP_STRING)) {
			return Operation.LOOKUP;
		} else if (op.equals(OP_WRITE_STRING)) {
			return Operation.WRITE;
		} else if (op.equals(OP_CREATE_STRING)) {
			return Operation.CREATE;
		} else if (op.equals(OP_DELETE_STRING)) {
			return Operation.DELETE;
		} else if (op.equals(OP_CHDIR_STRING)) {
			return Operation.CHDIR;
		} else {
			throw new UnknownOperationException("Unknown operation: " + op);
		}
	}

}
