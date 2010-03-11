/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.service.PortalFileService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class ExportQueryResultTableToXMLController extends AbstractController {

	private QueryResultTableDao queryResultTableDao;
	private PortalFileService portalFileService;

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

		String instanceId = req.getParameter("instanceId");
		doExport(Integer.valueOf(instanceId), res);

		return null;
	}

	protected void doExport(Integer instanceId, HttpServletResponse res)
			throws Exception {
		res.setContentType("text/xml");
		res.addHeader("Content-Disposition",
				"attachment;filename=\"query_results.xml\"");
		QueryResultTable table = getQueryResultTableDao().getByQueryInstanceId(
				instanceId);
		byte[] in = null;
		try {
			in = getPortalFileService().read(table.getData().getFileName());
			OutputStream out = res.getOutputStream();
			out.write(in);
			out.flush();
		} catch (IOException e) {
			logger.warn("Could not read File using the file service", e);
		}

	}

	public PortalFileService getPortalFileService() {
		return portalFileService;
	}

	public void setPortalFileService(PortalFileService portalFileService) {
		this.portalFileService = portalFileService;
	}

	public QueryResultTableDao getQueryResultTableDao() {
		return queryResultTableDao;
	}

	public void setQueryResultTableDao(QueryResultTableDao queryResultTableDao) {
		this.queryResultTableDao = queryResultTableDao;
	}

}
