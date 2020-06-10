package me.highdk.api.v1.image;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import me.highdk.api.v1.common.exception.BadRequestException;
import me.highdk.api.v1.post.PostController;

@RestController
@RequestMapping(value = "/v1/api/images")
@Slf4j
public class ImageController {
	
	private final ImageService imageService;
	
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> readOne(@PathVariable Long id){
		ImageResponse response = imageService.display(id);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, response.getMimeType());
		
		return ResponseEntity.status(HttpStatus.OK)
							 .headers(headers)
							 .body(response.getSerializedImage());
	}
	
	@PostMapping
	public ResponseEntity<?> uploadByPostId(@ModelAttribute @Valid ImageRequest request,
											Errors errors){
		log.debug("/v1/api/images");
		if(errors.hasErrors()) {
			throw new BadRequestException(errors);
		}
		
		CollectionModel<ImageResponse> resource = imageService.uploadByPostId(request);
		URI postUri = linkTo(methodOn(PostController.class).readOne(request.getPostId())).toUri();
		return ResponseEntity.created(postUri)
							 .body(resource);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteOne(@PathVariable Long id){
		String message = imageService.deleteOne(id);
		return ResponseEntity.status(HttpStatus.OK)
							 .body(message);
	}
	
}
