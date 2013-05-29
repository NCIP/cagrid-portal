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
package gov.nih.nci.cagrid.portal.portlet.browse.img;

import gov.nih.nci.cagrid.portal.dao.catalog.ImageDao;
import gov.nih.nci.cagrid.portal.domain.catalog.Image;
import gov.nih.nci.cagrid.portal.domain.catalog.ImageFormat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author joshua
 * 
 */
public class DownloadImageController implements Controller {

	private ImageDao imageDao;

	/**
	 * 
	 */
	public DownloadImageController() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			Image i = getImageDao().getById(
					Integer.parseInt(request.getParameter("id")));
			if (i.getFormat().equals(ImageFormat.PNG)) {
				response.setContentType("image/png");
			} else {
				response.setContentType("image/jpeg");
			}
			InputStream in = new ByteArrayInputStream(i.getData());
			OutputStream out = response.getOutputStream();
			ImageIO.write(ImageIO.read(in), i.getFormat().toString(), out);
			out.flush();
			in.close();

		} catch (Exception ex) {
			throw new Exception("Error downloading file: " + ex.getMessage(),
					ex);
		}
		return null;
	}

	public ImageDao getImageDao() {
		return imageDao;
	}

	public void setImageDao(ImageDao imageDao) {
		this.imageDao = imageDao;
	}

}
