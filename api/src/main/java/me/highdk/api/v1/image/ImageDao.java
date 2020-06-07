package me.highdk.api.v1.image;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImageDao {
	
	public int create(Image image);

	public Optional<Image> findById(Long id);
	
	public Image getById(Long id);
	
	public int createLinkWithPost(@Param(value = "postId") Long postId, 
								  @Param(value = "imageId") Long imageId);
}
