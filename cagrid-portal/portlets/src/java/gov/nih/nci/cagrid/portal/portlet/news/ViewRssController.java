/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import java.text.SimpleDateFormat;
import java.util.Locale;

import gov.nih.nci.cagrid.portal.dao.news.NewsChannelDao;
import gov.nih.nci.cagrid.portal.domain.news.NewsChannel;
import gov.nih.nci.cagrid.portal.domain.news.NewsItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewRssController extends AbstractController {

	private NewsChannelDao newsChannelDao;
	private static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", new Locale("en"));
	
	/**
	 * 
	 */
	public ViewRssController() {

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element rssEl = doc.createElement("rss");
		doc.appendChild(rssEl);
		rssEl.setAttribute("version", "2.0");
		for(NewsChannel channel : getNewsChannelDao().getAll()){
			
			Element channelEl = addChild(doc, rssEl, "channel");
			addChild(doc, channelEl, "title", channel.getTitle());
			addChild(doc, channelEl, "link", channel.getLink());
			addChild(doc, channelEl, "description", channel.getDescription());
			addChild(doc, channelEl, "pubDate", format.format(channel.getPubDate()));
			addChild(doc, channelEl, "lastBuildDate", format.format(channel.getLastBuildDate()));
			
			for(NewsItem item : channel.getItems()){
				
				Element itemEl = addChild(doc, channelEl, "item");
				addChild(doc, itemEl, "title", item.getTitle());
				addChild(doc, itemEl, "link", item.getLink());
				addChild(doc, itemEl, "description", item.getDescription());
				Element guidEl = addChild(doc, itemEl, "guid", item.getLink());
				guidEl.setAttribute("isPermaLink", "false");
				addChild(doc, itemEl, "pubDate", format.format(item.getPubDate()));
			}
		}
		
		response.setContentType("application/rss+xml");
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));

		return null;
	}
	private Element addChild(Document doc, Element parentEl, String childElName){
		return addChild(doc, parentEl, childElName, null);
	}
	private Element addChild(Document doc, Element parentEl, String childElName, String childElContent){
		Element childEl = doc.createElement(childElName);
		parentEl.appendChild(childEl);
		if(childElContent != null){
			childEl.setTextContent(childElContent);
		}
		return childEl;
	}


	public NewsChannelDao getNewsChannelDao() {
		return newsChannelDao;
	}

	public void setNewsChannelDao(NewsChannelDao newsChannelDao) {
		this.newsChannelDao = newsChannelDao;
	}

}
