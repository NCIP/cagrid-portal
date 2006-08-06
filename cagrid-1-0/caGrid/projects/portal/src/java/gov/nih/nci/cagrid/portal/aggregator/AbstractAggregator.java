package gov.nih.nci.cagrid.portal.aggregator;

import org.apache.log4j.Category;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ApplicationEvent;

import java.util.TimerTask;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 25, 2006
 * Time: 2:56:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAggregator extends TimerTask implements ApplicationContextAware, ApplicationListener  {

    ApplicationContext ctx;

    protected Category _logger = Category.getInstance(getClass().getName());


     public void setApplicationContext(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //dummy implementation. Does nothing
    }
}
