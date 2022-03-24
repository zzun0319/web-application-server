package controller;

import java.util.UUID;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.HttpSession;
import model.HttpSessions;
import model.User;

public class UserLoginController extends AbstractController {

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		
		User findUser = DataBase.findUserById(request.getParameter("userId"));
		
		if(findUser == null || !findUser.getPassword().equals(request.getParameter("password"))) {
//			response.addHeader("Set-Cookie", "logined=false");
			response.sendRedirect("/user/login_failed.html");
		} else {
			String sessionId = UUID.randomUUID().toString();
			HttpSession session = new HttpSession(sessionId);
			session.setAttribute("user", findUser);
			HttpSessions.addSession(sessionId, session);
//			response.addHeader("Set-Cookie", "logined=true");
			response.addHeader("Set-Cookie", "JSESSIONID=" + sessionId);
			response.sendRedirect("/index.html");
		}
		
	}

}
