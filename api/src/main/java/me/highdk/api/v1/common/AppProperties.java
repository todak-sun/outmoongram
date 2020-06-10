package me.highdk.api.v1.common;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "outmoon-app")
public class AppProperties {
	
	@NotEmpty
	private String fileUploadPath;
	
}
