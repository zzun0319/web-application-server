package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Test;

public class RequestHandlerTest {
	
	private static final Logger log = LoggerFactory.getLogger(RequestHandlerTest.class);
	
	@Test
	public void fileReadTest() {
		
		String resource = "/index.html";
		File file = new File("./webapp" + resource);
		try(FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
			String line = "";
			while((line = br.readLine()) != null) {
				log.info(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
