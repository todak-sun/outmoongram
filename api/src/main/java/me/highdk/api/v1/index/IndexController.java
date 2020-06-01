package me.highdk.api.v1.index;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class IndexController {
	
	@GetMapping
	public ResponseEntity<?> index(){
		return null;
	}
	
}
