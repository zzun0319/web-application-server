package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
	
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	
	private String method;
	private String path;
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, String> params = new HashMap<String, String>();
	private Map<String, String> cookies;
	
	public HttpRequest(InputStream in) {
		
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			// 이 부분이 try(이 위치) 에 있으면 Socket이 닫히는 듯..
			
			String line = br.readLine();
			if(line == null) return;
			
			// method, path + GET이면 params까지 분리
			processRequestLine(line);
			
			line = br.readLine();
			while(!"".equals(line) && line != null) {
				log.debug("header : {}", line);
				String[] tokens = line.split(":");
				headers.put(tokens[0].trim(), tokens[1].trim());
				line = br.readLine();
			}
			
			String cookieValue = getHeader("Cookie");
			if(cookieValue != null) cookies = HttpRequestUtils.parseCookies(cookieValue);
			
			if(method.equals("POST")) { 
				String body = IOUtils.readData(br, Integer.valueOf(headers.get("Content-Length")));
				params = HttpRequestUtils.parseQueryString(body);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void processRequestLine(String requestLine) {
		log.debug("request line: {}", requestLine);
		String[] parts = requestLine.split(" ");
		method = parts[0];
		if(method.equals("POST")) {
			path = parts[1];
			return;
		} 

		int index = parts[1].indexOf("?");
		if(index == -1) {
			path = parts[1];
		} else {
			path = parts[1].substring(0, index);
			params = HttpRequestUtils.parseQueryString(parts[1].substring(index + 1));
		}
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getHeader(String headerKey) {
		return headers.get(headerKey);
	}

	public String getParameter(String paramKey) {
		return params.get(paramKey);
	}

	public String getCookie(String cookieKey) {
		return cookies.get(cookieKey);
	}

}
