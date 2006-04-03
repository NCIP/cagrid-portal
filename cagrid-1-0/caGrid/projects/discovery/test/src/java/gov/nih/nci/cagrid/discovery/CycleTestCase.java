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

package gov.nih.nci.cagrid.discovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


public class CycleTestCase extends TestCase {

	private JDepend jdepend;


	public CycleTestCase(String name) {
		super(name);
	}


	protected void setUp() {
		jdepend = new JDepend();
		try {
			String dir = System.getProperty("build.dir", ".");
			jdepend.addDirectory(dir);
			jdepend.analyzeTestClasses(false);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail(ioe.getMessage());
		}
	}


	protected void tearDown() {
		jdepend = null;
	}


	/**
	 * Tests that a package dependency cycle does not exist for any of the
	 * analyzed packages.
	 */
	public void testAllPackagesCycle() {
		int numCycles = 0;
		Collection packages = jdepend.analyze();
		if (jdepend.containsCycles()) {
			Iterator iter = packages.iterator();
			while (iter.hasNext()) {
				JavaPackage p = (JavaPackage) iter.next();
				if (p.containsCycle()) {
					System.out.println("\nPackage: " + p.getName() + " contains a cycle with:");
					numCycles++;
					List list = new ArrayList();
					p.collectAllCycles(list);
					for (Iterator iterator = list.iterator(); iterator.hasNext();) {
						JavaPackage dependsP = (JavaPackage) iterator.next();
						System.out.println("->" + dependsP.getName());

					}
				}
			}
		}
		System.out.println("\n===== Found " + numCycles + " cyclic packages. =====\n\n");

		assertEquals("Cycles exist", false, jdepend.containsCycles());
	}


	public static void main(String args[]) {

		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(CycleTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}