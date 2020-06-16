package me.highdk.api.v1.image;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.common.AppProperties;
import me.highdk.api.v1.common.OutmoonService;
import me.highdk.api.v1.common.exception.NotFoundException;
import me.highdk.api.v1.post.PostController;

@Slf4j
@Service
public class ImageService implements OutmoonService<Image, ImageResponse>{
	
	private final MimeType[] allowedTypes = { MimeTypeUtils.IMAGE_JPEG, MimeTypeUtils.IMAGE_PNG };
	
	private final AppProperties appProperties;
	
	private final ImageRepository imageRepository;
	
	@Autowired
	public ImageService(ImageRepository imageRepository,
						AppProperties appProperties) {
		this.imageRepository = imageRepository;
		this.appProperties = appProperties;
	}
	
	@Transactional
	public CollectionModel<ImageResponse> uploadByPostId(ImageRequest request){
		Tika tika = new Tika();
		Long postId = request.getPostId();
		File uploadPath = this.getFolder();
		
		List<Image> images = new ArrayList<>();
		List<ImageResponse> rejectedImages = new ArrayList<>();
		
		request.getImages().stream().forEach(image -> {
			try {
				String type = tika.detect(image.getBytes());
				MimeType mimeType = MimeType.valueOf(type);
				String name = FilenameUtils.getBaseName(image.getOriginalFilename());
				if(Arrays.asList(allowedTypes).contains(mimeType)) {
					
					String modifiedFilename = this.makeImageFileName(mimeType.getSubtype());
					File target = new File(uploadPath, modifiedFilename);
					FileUtils.copyInputStreamToFile(image.getInputStream(), target);
					
					Image newImage = Image.builder()
							 			 .name(name)
							 		     .filePath(target.getAbsolutePath())
							 		     .mimeType(type)
							 		     .build();
					
					Image savedImage = imageRepository.save(newImage);
					imageRepository.linkWithPost(postId, savedImage.getId());
					images.add(savedImage);
				} else {
					rejectedImages.add(ImageResponse.builder()
													 .name(name)
													 .message(type + "은 지원하지 않습니다.")
													 .build());
				}
			} catch (IOException e) {
				//TODO : SYJ, 에러처리 조금 더 고민.
				e.printStackTrace();
			}		
		});
		
		var response = this.toResponse(images);
		response.addAll(rejectedImages);
		
		var resource = this.toResource(response);
		resource.add(linkTo(methodOn(PostController.class).readOne(postId)).withRel("post"));
		
		return resource;
	}
	
	public ImageResponse display(Long id) {
		return imageRepository.findById(id).map(image -> {
												var response = this.toResponse(image);
												response.setSerializedImage(this.getSerializedImage(image.getFilePath()));
												return response;
										   })
										   .orElseThrow(() -> {
												throw new NotFoundException(id);
										   });
	}
	
	private String makeImageFileName(String ext) {
		return "om_" + Calendar.getInstance().getTimeInMillis() + "." + ext;
	}
	
	private File getFolder() {
		String root = appProperties.getFileUploadPath() + File.separator + "images";
		SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd");
		String hierarchicalNameBasedDate  = yyyy_mm_dd.format(new Date()).replace("-", File.separator);
		
		File folder = new File(root, hierarchicalNameBasedDate);
		if(folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}
	
	private byte[] getSerializedImage(String filePath) {
		File imageFile = new File(filePath);
		byte[] serializedImage = null;
		
		try {
			serializedImage = FileUtils.readFileToByteArray(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serializedImage;
	}
	
	@Override
	public ImageResponse toResponse(Image image) {
		String src = linkTo(ImageController.class).slash(image.getId()).toString();
		return ImageResponse.builder()
							.name(image.getName())
							.mimeType(image.getMimeType())
							.src(src)
							.build();
	}

	@Override
	public List<ImageResponse> toResponse(List<Image> images) {
		return Optional.ofNullable(images).map(imgs -> imgs.stream()
														   .map(this::toResponse).collect(Collectors.toList()))
										  .orElseGet(() -> null);
		
	}
	
	//TODO: SYJ, 나중에 한 번에 구현.
	public String deleteOne(Long id) {
		return imageRepository.findById(id).map(image -> {
											   int affectedRowCount = imageRepository.deleteById(id);											   
											   return "정상적으로 삭제하였습니다.";
										   })
										   .orElseThrow(() -> {
											   throw new NotFoundException(id);
										   }); 
		
	}

	

}
