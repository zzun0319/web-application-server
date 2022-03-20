package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import model.HttpRequest;
import model.HttpResponse;
import util.RequestMapping;

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
        	
        		HttpRequest request = new HttpRequest(in);
	    		HttpResponse response = new HttpResponse(out);
	    		
	    		String path = request.getPath();
	    		log.debug("Request URL: {}", path);
	    		
	    		String sessionId = request.getCookie("JSESSIONID");
				if(sessionId == null) {
	    			response.sendRedirect("/index.html");
	    			return;
	    		}
				
				log.debug("sessionId: {}", sessionId);
	    		
	    		Controller controller = RequestMapping.getController(path);
	    		if(controller == null){
	    			path = getDefaultPath(request.getPath());
	    			response.forward(path);
	    		} else {
	    			controller.service(request, response);
	    		}
	    		
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private String getDefaultPath(String path) {
	    	if(path.equals("/")) return "/index.html";
	    	return path;
    }

}
