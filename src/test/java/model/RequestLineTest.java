package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RequestLineTest {
	
	@Test
	public void create_method() {
		RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
		assertEquals("GET", line.getMethod());
		assertEquals("/index.html", line.getPath());
		
		line = new RequestLine("POST /index.html HTTP/1.1");
		assertEquals("/index.html", line.getPath());
	}
	
	@Test
	public void create_path_and_params() {
		RequestLine line = new RequestLine("GET /user/create?userId=user1&password=abc1234 HTTP/1.1");
		assertEquals("GET", line.getMethod());
		assertEquals("/user/create", line.getPath());
		assertEquals(2, line.getParams().size());
	}

}
