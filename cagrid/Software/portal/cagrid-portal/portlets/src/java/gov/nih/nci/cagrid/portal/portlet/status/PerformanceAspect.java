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
