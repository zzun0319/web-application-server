package controller;

import java.util.Map;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class UserLoginController extends AbstractController {

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		
		Map<String, String> params = request.getParamMap();
		
		if(params.size() == 0) return;
		
		User findUser = DataBase.findUserById(params.get("userId"));
		
		if(findUser == null || !findUser.getPassword().equals(params.get("password"))) {
			response.addHeader("Set-Cookie", "logined=false");
			response.sendRedirect("/user/login_failed.html");
		} else {
			response.addHeader("Set-Cookie", "logined=true");
			response.sendRedirect("/index.html");
		}
		
	}

}
