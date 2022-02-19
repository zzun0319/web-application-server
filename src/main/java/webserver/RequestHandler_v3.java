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
import controller.OtherController;
import controller.UserCreateController;
import controller.UserListController;
import controller.UserLoginController;
import model.HttpRequest;
import model.HttpResponse;

public class RequestHandler_v3 extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler_v3.class);

    private Socket connection;

    public RequestHandler_v3(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
    	
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
        	
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	
        		HttpRequest httpRequest = new HttpRequest(in);
	    		
	    		String url = httpRequest.getUrl();
	    		log.debug("Request URL: {}", url);
	    		
	    		DataOutputStream dos = new DataOutputStream(out);
	    		HttpResponse response = new HttpResponse(dos);
	    		
	    		Map<String, Controller> contMap = new HashMap<String, Controller>();
    			contMap.put("/user/create", new UserCreateController());
    			contMap.put("/user/login", new UserLoginController());
    			contMap.put("/user/list", new UserListController());
    			contMap.put("other", new OtherController());
	    		
	    		if(contMap.containsKey(url)) contMap.get(url).service(httpRequest, response);
	    		else contMap.get("other").service(httpRequest, response);
	    		
	    		
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
