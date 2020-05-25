package me.highdk.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//최상위 package에 있는 Class에 Annotation 적용해서, AOP를 찾을 수 있도록...Proxy 생성
@SpringBootApplication
public class OutmoongramApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OutmoongramApiApplication.class, args);
	}
	
}
