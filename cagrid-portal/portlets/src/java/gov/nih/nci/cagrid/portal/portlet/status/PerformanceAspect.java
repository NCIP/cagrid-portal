/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;

import java.util.Calendar;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Aspect
public class PerformanceAspect {
    private static Log logger = LogFactory.getLog(PerformanceAspect.class);

    public Object measure(ProceedingJoinPoint pjp) throws Throwable {

        long init = Calendar.getInstance().getTimeInMillis();
        Object result = pjp.proceed();
        Signature sig = pjp.getSignature();

        long timeTaken = Calendar.getInstance().getTimeInMillis() - init;
        logger.info("Time taken  for " + sig.getDeclaringType().getName() + "." + sig.getName() + " " + timeTaken + " milliseconds.");

        return result;
    }
}
