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

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.ImageDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.Image;
import gov.nih.nci.cagrid.portal.domain.catalog.ImageFormat;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author joshua
 * 
 */
public class UploadImageController implements Controller {

	private static final Log logger = LogFactory
			.getLog(UploadImageController.class);

	private ImageDao imageDao;
	private CatalogEntryDao catalogEntryDao;
	private UserModel userModel;

	private int imageMaxHeight = 180;
	private int imageMaxWidth = 180;
	private int thumbnailMaxHeight = 50;
	private int thumbnailMaxWidth = 50;

	/**
	 * 
	 */
	public UploadImageController() {

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

		JSONObject json = new JSONObject();
		try {

			MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
			MultipartFile uploadedFile = mpReq.getFile("image");
			long fileSize = uploadedFile.getSize();
			if (fileSize == 0) {
				json.append("error", "No data was uploaded.");
			} else {

				CatalogEntry entry = getUserModel().getCurrentCatalogEntry();

				String origFilename = uploadedFile.getOriginalFilename()
						.toLowerCase();
				ImageFormat imageFormat = null;
				if (origFilename.endsWith("png")) {
					imageFormat = ImageFormat.PNG;
				} else if (origFilename.endsWith("jpg")
						|| origFilename.endsWith("jpeg")) {
					imageFormat = ImageFormat.JPEG;
				} else {
					json.append("error", "Only PNG or JPEG formats are supported.");
				}
				if (imageFormat != null) {
					InputStream in = uploadedFile.getInputStream();
					BufferedImage bImage = ImageIO.read(in);
					in.close();

					BufferedImage regBImage = ImageUtils.scaleImage(bImage,
							getImageMaxHeight(), getImageMaxWidth());
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(regBImage, imageFormat.toString(), bos);
					Image regImage = new Image();
					regImage.setData(bos.toByteArray());
					bos.close();
					regImage.setHeight(regBImage.getHeight());
					regImage.setWidth(regBImage.getWidth());
					regImage.setType(regBImage.getType());
					regImage.setFormat(imageFormat);
					getImageDao().save(regImage);

					BufferedImage thumbBImage = ImageUtils.scaleImage(bImage,
							getThumbnailMaxHeight(), getThumbnailMaxWidth());
					bos = new ByteArrayOutputStream();
					ImageIO.write(thumbBImage, imageFormat.toString(), bos);
					bos.close();
					Image thumbImage = new Image();
					thumbImage.setData(bos.toByteArray());
					thumbImage.setHeight(thumbBImage.getHeight());
					thumbImage.setWidth(thumbBImage.getWidth());
					thumbImage.setType(thumbBImage.getType());
					thumbImage.setFormat(imageFormat);
					getImageDao().save(thumbImage);
					
					entry.setImage(regImage);
					entry.setThumbnail(thumbImage);
					getCatalogEntryDao().save(entry);
					
					json.append("result", regImage.getId());
				}
			}
		} catch (Exception ex) {
			String msg = "Error handling uploaded file: " + ex.getMessage();
			logger.error(msg, ex);
			String errMsg = "An error was encountered: "
				+ ex.getMessage();
			json.append("error", errMsg);
		}
		
		response.getWriter().write(json.toString());
		return null;
	}

	public ImageDao getImageDao() {
		return imageDao;
	}

	public void setImageDao(ImageDao imageDao) {
		this.imageDao = imageDao;
	}

	public int getImageMaxHeight() {
		return imageMaxHeight;
	}

	public void setImageMaxHeight(int imageMaxHeight) {
		this.imageMaxHeight = imageMaxHeight;
	}

	public int getImageMaxWidth() {
		return imageMaxWidth;
	}

	public void setImageMaxWidth(int imageMaxWidth) {
		this.imageMaxWidth = imageMaxWidth;
	}

	public int getThumbnailMaxHeight() {
		return thumbnailMaxHeight;
	}

	public void setThumbnailMaxHeight(int thumbnailMaxHeight) {
		this.thumbnailMaxHeight = thumbnailMaxHeight;
	}

	public int getThumbnailMaxWidth() {
		return thumbnailMaxWidth;
	}

	public void setThumbnailMaxWidth(int thumbnailMaxWidth) {
		this.thumbnailMaxWidth = thumbnailMaxWidth;
	}

	public CatalogEntryDao getCatalogEntryDao() {
		return catalogEntryDao;
	}

	public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
		this.catalogEntryDao = catalogEntryDao;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

}
