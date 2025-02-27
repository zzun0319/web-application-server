package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
        	
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	
        		// 요청 정보 출력
	        	BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	    		String line = br.readLine();
	    		log.debug("###### request line : {} ######", line);
	    		
	    		if(line == null) return;
	    		
	    		String[] tokens = line.split(" ");
	    		String method = tokens[0];
	    		int contentLength = 0;
	    		boolean logined = false;
	    		String accept = "";
	    		
	    		while(!line.equals("")) {
	    			log.debug("header: {}", line);
	    			line = br.readLine();
	    			if(line.contains("Content-Length")) {
	    				contentLength = getContentLength(line);
	    			}
	    			if(line.contains("Cookie")) {
	    				logined = getLogined(line);
	    			}
	    			if(line.contains("Accept: ")) {
	    				accept = getAccept(line);
	    				log.debug("@@@@@@@@ Accept: {} @@@@@@@@", accept);
	    			}
	    		}
	    		// 여기까지 br이 ""까지 읽음. Header와 Body사이의 ""
	    		// 그럼 다음 readLine을 하게되면 "" 아래의 Request Body를 읽게 됨. 또는 Content-Length만큼 읽어들이면 Body를 읽을 수 있음.
	    		
	    		
	    		
	    		String url = tokens[1];
	    		if("/user/create".equals(url)){
	    			
	    			String requestBody = IOUtils.readData(br, contentLength);
	    			log.debug("Request Body: {}", requestBody);
	    			Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
	    			User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
	    			log.debug("User : {}", user);
	    			DataOutputStream dos = new DataOutputStream(out);
	    			DataBase.addUser(user);
	    			response302Header(dos, "/index.html");
	    			
	    		} else if("/user/login".equals(url)) {
	    			
	    			String body = IOUtils.readData(br, contentLength);
	    			Map<String, String> params = HttpRequestUtils.parseQueryString(body);
	    			User findUser = DataBase.findUserById(params.get("userId"));
	    			DataOutputStream dos = new DataOutputStream(out);
	    			if(findUser == null) loginResponse(dos, "false");
	    			if(findUser.getPassword().equals(params.get("password"))) {
	    				loginResponse(dos, "true");
	    			} else {
	    				loginResponse(dos, "false");
	    			}
	    		} else if("/user/list".equals(url)) {
	    			
	    			
	    			DataOutputStream dos = new DataOutputStream(out);
	    			
	    			if(logined) {
	    				
	    				String result = "";
	    				for (User u : DataBase.findAll()) {
	    					String tmpUser = "ID: " + u.getUserId() + ", name: " + u.getName() + "<br>";
	    					result += tmpUser;
					}
	    				byte[] body = result.getBytes();
	    				response200Header(dos, body.length, accept);
		    			responseBody(dos, body);
	    				
	    			} else {
	    				response302Header(dos, "/user/login.html");
	    			}
	    			
	    		} else {
	    			
	    			DataOutputStream dos = new DataOutputStream(out);
	    			byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
	    			response200Header(dos, body.length, accept);
	    			responseBody(dos, body);
	    		}
	    		
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

	private String getAccept(String line) {
		String[] headerTokens = line.split(":");
		return headerTokens[1].trim();
	}

	private void loginResponse(DataOutputStream dos, String isSuccess) {
		try {
		    dos.writeBytes("HTTP/1.1 302 Found \r\n");
		    dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
		    dos.writeBytes("Set-Cookie: logined=" + isSuccess + "\r\n");
		    if(isSuccess.equals("true")) dos.writeBytes("Location: /index.html");
		    else dos.writeBytes("Location: /user/login_failed.html");
		    dos.writeBytes("\r\n");
		} catch (IOException e) {
		    log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos, String path) {
		try {
		    dos.writeBytes("HTTP/1.1 302 Found \r\n");
		    dos.writeBytes("Location: " + path + "\r\n");
		    dos.writeBytes("\r\n");
		} catch (IOException e) {
		    log.error(e.getMessage());
		}
	}
	
	private boolean getLogined(String line) {
		String[] headerTokens = line.split(":");
		String cookies = headerTokens[1].trim();
		Map<String, String> cookieMap = HttpRequestUtils.parseCookies(cookies);
		String logined = cookieMap.get("logined");
		if(logined == null) return false;
		return Boolean.parseBoolean(logined);
	}

	private int getContentLength(String line) {
		String[] headerTokens = line.split(":");
		return Integer.parseInt(headerTokens[1].trim());
	}

	private String extractBodyFromReq(InputStream in, String requestUrl) throws IOException {
		String reqBody = "";
		if(requestUrl.startsWith("/user/create")) {
			String line = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			boolean flag = false;
			
			while((line = br.readLine()) != null) {
				log.info("a");
				if(!"".equals(line)) flag = true;
				if(line.contains("userId") && flag) {
					reqBody = line;
					break;
				}
			}
		}
		return reqBody;
	}

	private Map<String, String> paramSplitAndJoin(String reqBody) {
		Map<String, String> paramMap = HttpRequestUtils.parseQueryString(reqBody);
		User user = new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("name"), paramMap.get("email"));
		DataBase.addUser(user);
		return paramMap;
	}

	public String joinGetMethod(String requestUrl) {
		
		if(requestUrl.startsWith("/user/create")) {
			
			int index = requestUrl.indexOf("?");
			String param = requestUrl.substring(index + 1);
			Map<String, String> paramMap = paramSplitAndJoin(param);
			requestUrl = requestUrl.substring(0, index);
			if(paramMap != null) log.info("{}의 회원가입이 성공했습니다.", paramMap.get("userId"));
		}
		return requestUrl;
	}

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String accept) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            if(accept.contains("text/css")) dos.writeBytes("Content-Type: text/css;\r\n");
            else dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
