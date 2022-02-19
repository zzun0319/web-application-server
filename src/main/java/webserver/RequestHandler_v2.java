package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.UserCreateController;
import controller.UserListController;
import controller.UserLoginController;
import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class RequestHandler_v2 extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler_v2.class);

    private Socket connection;

    public RequestHandler_v2(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
    	
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
        	
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	
        		HttpRequest httpRequest = new HttpRequest(in);
	    		String cookieLoginedStr = httpRequest.getCookieValue("logined");
	    		boolean logined = (cookieLoginedStr != null) ? Boolean.parseBoolean(cookieLoginedStr) : false;
	    		String accept = httpRequest.getHeaderValue("Accept");
	    		String url = httpRequest.getUrl();
	    		Map<String, String> params = httpRequest.getParamMap();
	    		
	    		DataOutputStream dos = new DataOutputStream(out);
	    		
	    		log.debug("Request URL: {}", url);
	    		HttpResponse response = new HttpResponse(dos);
	    		
	    		Map<String, Controller> contMap = new HashMap<String, Controller>();
	    		contMap.put("/user/create", new UserCreateController());
	    		contMap.put("/user/login", new UserLoginController());
	    		contMap.put("/user/list", new UserListController());
	    		
	    		if("/user/create".equals(url)){
	    			
	    			if(params.size() == 0) return;
	    			
	    			User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
	    			log.debug("User : {}", user);
	    			
	    			DataBase.addUser(user);
	    			
	    			response.sendRedirect("/index.html");
	    			
	    		} else if("/user/login".equals(url)) {
	    			
	    			if(params.size() == 0) return;
	    			
	    			User findUser = DataBase.findUserById(params.get("userId"));
	    			
	    			if(findUser == null || !findUser.getPassword().equals(params.get("password"))) {
	    				response.addHeader("Set-Cookie", "logined=false");
	    				response.sendRedirect("/user/login_failed.html");
	    			} else {
	    				response.addHeader("Set-Cookie", "logined=true");
	    				response.sendRedirect("/index.html");
	    			}
	    			
	    		} else if("/user/list".equals(url)) {
	    			
	    			if(logined) {
	    				
	    				String result = "";
	    				for (User u : DataBase.findAll()) {
	    					String tmpUser = "ID: " + u.getUserId() + ", name: " + u.getName() + "\r\n";
	    					result += tmpUser;
					}
	    				byte[] body = result.getBytes();
	    				response.forward(body);
	    				
	    			} else {
	    				response.sendRedirect("/user/login.html");
	    			}
	    			
	    		} else { // 그 외 요청
	    			if(accept.contains("text/html")) {
	    				response.addHeader("Content-Type", "text/html;charset=utf-8");
	    			} else if (accept.contains("text/css")) {
	    				response.addHeader("Content-Type", "text/css;charset=utf-8");
	    			}
	    			response.forward(url);
	    		}
	    		
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
