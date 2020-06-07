package me.highdk.api.v1.image;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class ImageServiceTest {

//	@Autowired
//	ImageService imageService;
	
	@Test
	public void test() {
		Image image = Image.builder()
							.id(1L)
							.build();
//		imageService.toResponse(image);
	}
	
	@Test
	public void test2() {
		System.out.println(Calendar.getInstance().getTimeInMillis());
	}
}
