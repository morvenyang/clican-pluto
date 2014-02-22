package com.ikidstv.quiz.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.ikidstv.quiz.bean.ContentTree;
import com.ikidstv.quiz.model.Image;
import com.ikidstv.quiz.util.StringUtils;

@Scope(ScopeType.PAGE)
@Name("imageAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class ImageAction extends SpringBeanAction {

	private final static Log log = LogFactory.getLog(ImageAction.class);

	private ContentTree contentTree;

	private ContentTree selectedContentTree;

	private List<Image> imageBySelectedContent;

	private Image image;

	public void listImages() {
		contentTree = this.getContentService().getContentTree();
	}

	public void selectContent(ContentTree contentTree) {
		this.selectedContentTree = contentTree;
		if (selectedContentTree.isEpisodeNode()) {
			imageBySelectedContent = this.getImageService().getImageByContent(
					selectedContentTree.getSeasonId(),
					selectedContentTree.getEpisonId());
			while(imageBySelectedContent.size()<5){
				imageBySelectedContent.add(null);
			}
		} else {
			imageBySelectedContent = new ArrayList<Image>();
		}
	}

	public List<Image> getImageBySelectedContent() {
		return imageBySelectedContent;
	}

	public ContentTree getContentTree() {
		return contentTree;
	}

	public ContentTree getSelectedContentTree() {
		return selectedContentTree;
	}

	public void uploadImage() {
		this.image = new Image();
		this.image.setEpisode(this.selectedContentTree.getEpisonId());
		this.image.setSeason(this.selectedContentTree.getSeasonId());
	}


	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void saveImage() {
		this.getImageService().saveImage(image);
		imageBySelectedContent = this.getImageService().getImageByContent(
				selectedContentTree.getSeasonId(),
				selectedContentTree.getEpisonId());
		while(imageBySelectedContent.size()<5){
			imageBySelectedContent.add(null);
		}
	}

	public synchronized void fileUploadListener(UploadEvent event) {
		List<UploadItem> itemList = event.getUploadItems();
		UploadItem item = itemList.get(0);
		File file = item.getFile();
		InputStream is = null;
		byte[] content = null;
		try {
			is = new FileInputStream(file);
			content = new byte[is.available()];
			is.read(content);
		} catch (FileNotFoundException e1) {
			log.error("", e1);
		} catch (IOException e2) {
			log.error("", e2);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				log.error("", e);
			}
		}
		String name = item.getFileName();
		int last = name.lastIndexOf(".");
		String suffix = name.substring(last + 1);
		String imagePath = StringUtils.generateFilePathByDate()+ "/" +UUID.randomUUID().toString() + "." + suffix;
		String path = this.getSpringProperty().getImagePath() + "/" + imagePath;
		File imageFile = new File(path);
		if (!imageFile.exists()) {
			imageFile.getParentFile().mkdirs();
		}
		try {
			FileUtils.writeByteArrayToFile(imageFile, content);
		} catch (IOException e2) {
			log.error("", e2);
		}
		image.setPath(imagePath);
	}

}
