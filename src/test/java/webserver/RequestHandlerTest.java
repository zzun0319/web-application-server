package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class RequestHandlerTest {
	
	private static final Logger log = LoggerFactory.getLogger(RequestHandlerTest.class);
	
	@Test
	public void 파일읽기Test() {
		
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
	
	@Test
	public void 회원가입Get메서드test() {
		String requestUrl = "/user/create?userId=user1&password=abc1234&name=lee&email=lee@naver.com";
		RequestHandler rh = new RequestHandler(null);
		String requestPath = rh.joinGetMethod(requestUrl);
		Assert.assertEquals(requestPath, "/user/create");
		Assert.assertTrue(DataBase.findUserById("user1").equals(new User("user1","abc1234","lee","lee@naver.com")));

	}

}
