package model;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
	
	String id;
	Map<String, String> attributes = new HashMap<String, String>();
	
	public HttpSession(String id) {
		this.id = id;
	}
	
	public void setAttribute(String name, Object value) {
		attributes.put(name, name);
	}
	
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	
	public void invalidate() {
		HttpSessions.removeSession(id);
	}
	
}
