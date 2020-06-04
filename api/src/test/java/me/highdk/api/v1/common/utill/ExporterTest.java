package me.highdk.api.v1.common.utill;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class ExporterTest {

	@Test
	void test() {
		
		String content = "안녕하세요 저는\n무엇일까요\n#인간 #일까요#음음 #하하하 @장난하나\n !@$!@ #김치 #만두국은 #사랑입니다";
		System.out.println(content);
		Pattern hashTagPattern = Pattern.compile("#([a-zA-Z0-9가-힣]*\\s)");
		Matcher hashTagMatcher = hashTagPattern.matcher(content + " ");
		
		while(hashTagMatcher.find()) {
			System.out.println(hashTagMatcher.group());
		}
		
	}
	
	
	
}
