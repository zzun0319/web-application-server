package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
	
	public static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

	public static void addSession(String sessionId, HttpSession session) {
		sessions.put(sessionId, session);
	}
	
	public static HttpSession getSession(String sessionId){
		return sessions.get(sessionId);
	}
}
