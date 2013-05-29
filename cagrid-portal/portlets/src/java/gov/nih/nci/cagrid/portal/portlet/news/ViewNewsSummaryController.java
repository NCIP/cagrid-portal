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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import gov.nih.nci.cagrid.portal.dao.news.NewsItemDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsItem;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewNewsSummaryController extends AbstractController {

    private String successView;
    private NewsItemDao newsItemDao;
    private int newsItemsLimit = 10;
    private String rssUrl;

    private static final Log logger = LogFactory.getLog(ViewNewsSummaryController.class);
    /**
     *
     */
    public ViewNewsSummaryController() {

    }

    protected ModelAndView handleRenderRequestInternal(RenderRequest request,
                                                       RenderResponse response)
            throws Exception{
        ModelAndView mav = new ModelAndView(getSuccessView());
        try {

            List<NewsItem> items = getNewsItemDao().getLatestNewsItems(getNewsItemsLimit());
            mav.addObject("items", items);
        } catch (Exception e) {
            logger.error(e);
        }

        mav.addObject("rssUrl", getRssUrl());
        return mav;
    }

    public String getSuccessView() {
        return successView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    public NewsItemDao getNewsItemDao() {
        return newsItemDao;
    }

    public void setNewsItemDao(NewsItemDao newsItemDao) {
        this.newsItemDao = newsItemDao;
    }

    public int getNewsItemsLimit() {
        return newsItemsLimit;
    }

    public void setNewsItemsLimit(int newsItemsLimit) {
        this.newsItemsLimit = newsItemsLimit;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

}
