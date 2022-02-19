package model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

	private DataOutputStream dos;
	private Map<String, String> headers = new HashMap<>();
	
	public HttpResponse(DataOutputStream dos) {
		this.dos = dos;
	}
	
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

	public void forward(String path) {

		try {
			
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			
			byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			
			for (String key : headers.keySet()) {
				dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
			}
			dos.writeBytes("\r\n");

			dos.write(body, 0, body.length);
			dos.flush();

		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}
	
	public void forward(byte[] body) {

		try {
			
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			
			for (String key : headers.keySet()) {
				dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
			}
			dos.writeBytes("\r\n");

			dos.write(body, 0, body.length);
			dos.flush();

		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}
	
	public void sendRedirect(String location) {
		try {
		    dos.writeBytes("HTTP/1.1 302 Found \r\n");
		    
		    dos.writeBytes("Location: " + location + "\r\n");
		    for (String key : headers.keySet()) {
				dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
			}
			dos.writeBytes("\r\n");
			
		} catch (IOException e) {
		    log.error(e.getMessage());
		}
	}

}
