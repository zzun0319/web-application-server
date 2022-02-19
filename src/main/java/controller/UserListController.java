package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class UserListController extends AbstractController {

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		
		String cookieLoginedStr = request.getCookieValue("logined");
		boolean logined = (cookieLoginedStr != null) ? Boolean.parseBoolean(cookieLoginedStr) : false;
		
		if(logined) {
			
			String result = "";
			for (User u : DataBase.findAll()) {
				String tmpUser = "ID: " + u.getUserId() + ", name: " + u.getName() + "\r\n";
				result += tmpUser;
		}
			byte[] body = result.getBytes();
			response.forward(body);
			
		} else {
			response.sendRedirect("/user/login.html");
		}
		
	}

}
