package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class UserLoginController extends AbstractController {

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		
		User findUser = DataBase.findUserById(request.getParameter("userId"));
		
		if(findUser == null || !findUser.getPassword().equals(request.getParameter("password"))) {
			response.addHeader("Set-Cookie", "logined=false");
			response.sendRedirect("/user/login_failed.html");
		} else {
			response.addHeader("Set-Cookie", "logined=true");
			response.sendRedirect("/index.html");
		}
		
	}

}
