package me.highdk.api.v1.image;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepository {
	
	private final ImageDao imageDao;
	
	@Autowired
	public ImageRepository(ImageDao imageDao) {
		this.imageDao = imageDao;
	}
	
	public Image save(Image image) {
		int affectedRowCount = imageDao.create(image);
		return imageDao.getById(image.getId());
	}
	
	public int linkWithPost(Long postId, Long imageId) {
		return imageDao.createLinkWithPost(postId, imageId);
	}
	
	public Optional<Image> findById(Long id) {
		return imageDao.findById(id);
	}
	
}
