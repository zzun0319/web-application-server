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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        		String requestUrl = "";
        		requestInfoPrint(in, requestUrl);
        		
        		// 요청 url 분리
        		requestUrl = HttpRequestUtils.getRequestUrl(requestUrl);
        		
        		// 분리한 리소스 위치의 파일 읽기
        		byte[] body = Files.readAllBytes(Paths.get("./webapp" + requestUrl));
        		
            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello World~~~!!!".getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

	private void requestInfoPrint(InputStream in, String requestUrl) throws IOException {
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		String requestLine = "";
		int cnt = 0;
		while((requestLine = br.readLine()) != null) {
			if(requestLine.equals("")) break;
			log.info("{}\n", requestLine);
			if(cnt == 0) {
				requestUrl = requestLine;
			}
		}
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
