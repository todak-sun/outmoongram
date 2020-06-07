package me.highdk.api.v1.image;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.common.OutmoonService;
import me.highdk.api.v1.common.exception.NotFoundException;

@Slf4j
@Service
public class ImageService implements OutmoonService<Image, ImageResponse>{
	
	private final String[] allowedTypes = {"image/jpeg", "image/png"};
	
	private final ImageRepository imageRepository;
	
	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}
	
	public CollectionModel<ImageResponse> uploadByPostId(ImageRequest request){
		Tika tika = new Tika();
		Long postId = request.getPostId();
		File uploadPath = new File("/Users/yongjoo_sun/Documents/upload-outmoon");
		
		List<Image> images = new ArrayList<>();
		
		request.getImages().stream().forEach(image -> {
			try {
				String type = tika.detect(image.getBytes());
				MimeType mimeType = MimeType.valueOf(type);
				String modifiedFilename = this.makeImageFileName(mimeType.getSubtype());
				String name = FilenameUtils.getBaseName(image.getOriginalFilename());
				String filePath = uploadPath.getAbsolutePath() + File.separator + modifiedFilename; 
				
				FileUtils.copyInputStreamToFile(image.getInputStream(), new File(filePath));
				
				Image newImage = Image.builder()
						 			 .name(name)
						 		     .filePath(filePath)
						 		     .build();
				
				Image savedImage = imageRepository.save(newImage);
				imageRepository.linkWithPost(postId, savedImage.getId());
				images.add(savedImage);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		});
		
		var resource = this.toResource(this.toResponse(images));
		
		return resource;
	}
	
	private String makeImageFileName(String type) {
		return "om_" + Calendar.getInstance().getTimeInMillis() + "." + type;
	}
	
	@Override
	public ImageResponse toResponse(Image image) {
		String src = linkTo(ImageController.class).slash(image.getId()).toString();
		return ImageResponse.builder()
							.name(image.getName())
							.src(src)
							.build();
	}

	@Override
	public List<ImageResponse> toResponse(List<Image> images) {
		return images.stream().map(this::toResponse).collect(Collectors.toList());
	}

	public byte[] readOne(Long id) {
		return imageRepository.findById(id).map(image -> {
												File file = new File(image.getFilePath());
												byte[] serializedImage = null;
												try {
													serializedImage = FileUtils.readFileToByteArray(file);
												} catch (IOException e) {
													e.printStackTrace();
												}
												return serializedImage;
											})
											.orElseThrow(() -> {
												throw new NotFoundException(id);
											});
	}

}
