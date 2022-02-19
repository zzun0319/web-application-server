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
import util.HttpRequestUtils.Pair;
import util.IOUtils;

public class HttpRequest {
	
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	
	private String httpMethod;
	private String url;
	private Map<String, String> headers;
	private String body;
	private Map<String, String> paramMap;
	private Map<String, String> cookieMap;
	
	public HttpRequest(InputStream in) {
		
		headers = new HashMap<>();
		paramMap = new HashMap<>();
		cookieMap = new HashMap<>();
		
		String line = "";
		
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			// 이 부분이 try(이 위치) 에 있으면 Socket이 닫히는 듯..
			
			line = br.readLine();
			if(line == null) return;
			log.debug("######first line: {}", line);
			
			String[] parts = line.split(" ");
			this.httpMethod = parts[0];
			this.url = parts[1];
			log.debug("######method: {}", httpMethod);
			log.debug("######url: {}", url);
			
			line = br.readLine();
			while(!"".equals(line) && line != null) {
//				log.debug("line to process: {}", line);
				Pair pair = HttpRequestUtils.parseHeader(line);
				headers.put(pair.getKey(), pair.getValue());
				line = br.readLine();
//				log.debug("next line: {}", line);
			}
			
			String cookieValue = getHeaderValue("Cookie");
			if(cookieValue != null) cookieMap = HttpRequestUtils.parseCookies(cookieValue);
			
			if(httpMethod.equals("GET") && url.contains("?")) {
				String[] urlParts = parts[1].split("\\?");
				url = urlParts[0];
				paramMap = HttpRequestUtils.parseQueryString(urlParts[1]);
				return;
			}
			
			if(httpMethod.equals("POST")) { 
//				log.debug("여기 진입");
//				body = br.readLine(); // 여기서 멈춘다
				body = IOUtils.readData(br, Integer.valueOf(getHeaderValue("Content-Length")));
				paramMap = HttpRequestUtils.parseQueryString(body);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String getUrl() {
		return url;
	}
	
	public String getHeaderValue(String headerKey) {
		return headers.get(headerKey);
	}
	
	public String getParamValue(String paramKey) {
		return paramMap.get(paramKey);
	}
	
	public String getCookieValue(String cookieKey) {
		return cookieMap.get(cookieKey);
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}
	
}
