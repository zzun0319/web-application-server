package model;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import enums.HttpMethod;

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
		
		
		assertEquals(httpRequest.getMethod(), HttpMethod.GET);
		assertEquals(httpRequest.getPath(), "/user/create");
		assertEquals(httpRequest.getHeader("Host"), "localhost:8080");
		assertEquals(httpRequest.getHeader("Connection"), "keep-alive");
		assertEquals(httpRequest.getHeader("Accept"), "*/*");
		assertEquals(httpRequest.getParameter("userId"), "user1");
		assertEquals(httpRequest.getParameter("password"), "abc1234");
		assertEquals(httpRequest.getParameter("name"), "kim");
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
		
		assertEquals(httpRequest.getMethod(), HttpMethod.POST);
		assertEquals(httpRequest.getPath(), "/user/create");
		assertEquals(httpRequest.getHeader("Host"), "localhost:8080"); // ":"로 자르고 trim() 해서 그래.. ": " 이걸로 자르고 trim()하는 게 맞는 거 같음.
		assertEquals(httpRequest.getHeader("Connection"), "keep-alive");
		assertEquals(httpRequest.getHeader("Content-Length"), "38");
		assertEquals(httpRequest.getHeader("Content-Type"), "application/x-www-form-urlencoded");
		assertEquals(httpRequest.getHeader("Accept"), "*/*");
		assertEquals(httpRequest.getParameter("userId"), "user1");
		assertEquals(httpRequest.getParameter("password"), "abc1234");
		assertEquals(httpRequest.getParameter("name"), "kim");
		
	}

}
