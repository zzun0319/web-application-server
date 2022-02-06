package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;

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
        		String requestUrl = requestInfoPrint(in);
        		
        		// 요청 url 분리
    			requestUrl = HttpRequestUtils.getRequestUrl(requestUrl);
    			log.info("########################{}###########################", requestUrl);
    			
    			// 회원 가입(GET)
//    			requestUrl = joinGetMethod(requestUrl);
    			
    			// 회원가입(POST)
    			String reqBody = extractBodyFromReq(in, requestUrl);
    			Map<String, String> paramMap = paramSplitAndJoin(reqBody);
    			log.info("{}의 회원가입 완료", paramMap.get("userId"));
        		
        		// 분리한 리소스 위치의 파일 읽기
        		byte[] body = Files.readAllBytes(Paths.get("./webapp" + requestUrl));
//        		byte[] body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath()); 이렇게도 되네
        		
            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello World~~~!!!".getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

	private String requestInfoPrint(InputStream in) throws IOException {
//		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String requestLine = "";
		String firstLine = "";
		int cnt = 0;
		while((requestLine = br.readLine()) != null) {
			if(requestLine.equals("")) break;
			log.info("{}\n", requestLine);
			if(cnt == 0 && requestLine.contains("HTTP")) {
				firstLine = requestLine;
			}
		}
		return firstLine;
	}

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
