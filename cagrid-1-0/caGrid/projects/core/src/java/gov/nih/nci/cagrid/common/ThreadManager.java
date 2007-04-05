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

package gov.nih.nci.cagrid.common;

import EDU.oswego.cs.dl.util.concurrent.CountDown;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin </A>
 * @created Dec 18, 2003
 * @version $Id: ThreadManager.java,v 1.1 2007-04-05 16:07:58 langella Exp $
 */
public class ThreadManager {

	private static int POOL_SIZE = 20;
	private PooledExecutor workerPool;


	public ThreadManager() {
		workerPool = new PooledExecutor(new LinkedQueue(), POOL_SIZE);
		workerPool.setMinimumPoolSize(POOL_SIZE);
	}


	public void executeInBackground(Runner task) throws InterruptedException {
		workerPool.execute(task);
	}


	public void execute(Runner task) throws InterruptedException {
		CountDown barrier = new CountDown(1);
		task.setSync(barrier);
		workerPool.execute(task);
		barrier.acquire();
	}


	public void executeGroup(RunnerGroup group) throws InterruptedException {
		CountDown barrier = new CountDown(group.size());

		for (int i = 0; i < group.size(); i++) {
			Runner task = group.get(i);
			task.setSync(barrier);
			workerPool.execute(task);
		}
		barrier.acquire();

	}


	public void executeGroupInBackground(RunnerGroup group) throws InterruptedException {
		for (int i = 0; i < group.size(); i++) {
			Runner task = group.get(i);
			workerPool.execute(task);
		}
	}

}