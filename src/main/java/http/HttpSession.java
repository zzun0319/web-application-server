package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
	
	// 세션 아이디
	private String id;

	// 세션 속성
	private Map<String, Object> attributes;

	// 세션 생성시 id 부여
	public HttpSession(String id) {
		this.id = id;
		this.attributes = new HashMap<String, Object>();
	}
	
	public String getId() {
		return id;
	}
	
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}
	
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void removeAttibute(String name) {
		attributes.remove(name);
	}
	
	public void invalidate() {
	}
}
