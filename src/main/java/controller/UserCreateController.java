package controller;

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
		
		User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
		log.debug("User : {}", user);
		
		DataBase.addUser(user);
		
		response.sendRedirect("/index.html");
		
	}

}
