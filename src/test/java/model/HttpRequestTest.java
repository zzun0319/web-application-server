package model;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

public class HttpRequestTest {
	
	private String resourcePath = "./src/test/resources/";
	
	@Test
	public void 분리테스트GET() {
		
		HttpRequest httpRequest = null;
		
		try {
			FileInputStream fis = new FileInputStream(resourcePath + "Http_GET.txt");
			
			httpRequest = new HttpRequest(fis);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		
		assertEquals(httpRequest.getHttpMethod(), "GET");
		assertEquals(httpRequest.getUrl(), "/user/create");
		assertEquals(httpRequest.getBody(), null);
		assertEquals(httpRequest.getHeaderValue("Host"), "localhost:8080");
		assertEquals(httpRequest.getHeaderValue("Connection"), "keep-alive");
		assertEquals(httpRequest.getHeaderValue("Accept"), "*/*");
		assertEquals(httpRequest.getParamValue("userId"), "user1");
		assertEquals(httpRequest.getParamValue("password"), "abc1234");
		assertEquals(httpRequest.getParamValue("name"), "kim");
	}
	
	@Test
	public void 분리테스트POST() {
		
HttpRequest httpRequest = null;
		
		try {
			FileInputStream fis = new FileInputStream(resourcePath + "Http_POST.txt");
			
			httpRequest = new HttpRequest(fis);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		assertEquals(httpRequest.getHttpMethod(), "POST");
		assertEquals(httpRequest.getUrl(), "/user/create");
		assertEquals(httpRequest.getBody(), "userId=user1&password=abc1234&name=kim");
		assertEquals(httpRequest.getHeaderValue("Host"), "localhost:8080");
		assertEquals(httpRequest.getHeaderValue("Connection"), "keep-alive");
		assertEquals(httpRequest.getHeaderValue("Content-Length"), "38");
		assertEquals(httpRequest.getHeaderValue("Content-Type"), "application/x-www-form-urlencoded");
		assertEquals(httpRequest.getHeaderValue("Accept"), "*/*");
		assertEquals(httpRequest.getParamValue("userId"), "user1");
		assertEquals(httpRequest.getParamValue("password"), "abc1234");
		assertEquals(httpRequest.getParamValue("name"), "kim");
		
	}

}
