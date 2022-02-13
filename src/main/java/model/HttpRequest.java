package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class HttpRequest {
	
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
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
			
			line = br.readLine();
			if(line != null) {
				String[] parts = line.split(" ");
				this.httpMethod = parts[0];
				this.url = parts[1];
				
				if(httpMethod.equals("GET") && url.contains("?")) {
					String[] urlParts = parts[1].split("\\?");
					url = urlParts[0];
					paramMap = HttpRequestUtils.parseQueryString(urlParts[1]);
				}
				
			}
			
			while((line = br.readLine()) != null) {
				
				if("".equals(line)) break;
				
				Pair pair = HttpRequestUtils.parseHeader(line);
				headers.put(pair.getKey(), pair.getValue());
				
			}
			
			if(httpMethod.equals("POST")) {
				body = br.readLine();
				paramMap = HttpRequestUtils.parseQueryString(body);
			}
			
			String cookieValue = getHeaderValue("Cookie");
			if(cookieValue != null) cookieMap = HttpRequestUtils.parseCookies(cookieValue);
			
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
