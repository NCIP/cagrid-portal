/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ExportQueryResultTableToXMLController extends AbstractController {

	private QueryResultTableDao queryResultTableDao;

	/**
	 * 
	 */
	public ExportQueryResultTableToXMLController() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		
		res.setContentType("text/xml");
		res.addHeader("Content-Disposition",
				"attachment;filename=\"query_results.xml\"");

		String instanceId = req.getParameter("instanceId");
		QueryResultTable table = getQueryResultTableDao().getByQueryInstanceId(
				Integer.valueOf(instanceId));
		byte[] buffer = new byte[8192];
		GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(table
				.getData().getData()));
		OutputStream out = res.getOutputStream();
		int read = 0;
		do {
			read = in.read(buffer, 0, buffer.length);
			if (read > 0) {
				 out.write(buffer, 0, read);
			}
		} while (read >= 0);
		out.flush();

		return null;
	}

	public QueryResultTableDao getQueryResultTableDao() {
		return queryResultTableDao;
	}

	public void setQueryResultTableDao(QueryResultTableDao queryResultTableDao) {
		this.queryResultTableDao = queryResultTableDao;
	}

}
