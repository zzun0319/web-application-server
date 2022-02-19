package controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class UserCreateController extends AbstractController {
	
	private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		
		Map<String, String> params = request.getParamMap();
		
		if(params.size() == 0) return;
		
		User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
		log.debug("User : {}", user);
		
		DataBase.addUser(user);
		
		response.sendRedirect("/index.html");
		
	}

}
