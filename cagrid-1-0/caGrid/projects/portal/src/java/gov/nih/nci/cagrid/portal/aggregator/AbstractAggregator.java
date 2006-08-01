package gov.nih.nci.cagrid.portal.aggregator;

import org.apache.log4j.Category;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 25, 2006
 * Time: 2:56:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAggregator extends TimerTask {

    protected Category _logger = Category.getInstance(getClass().getName());
    ;


}
