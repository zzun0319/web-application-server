package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class UserListController extends AbstractController {

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		String cookieLoginedStr = request.getCookie("logined");
		boolean logined = (cookieLoginedStr != null) ? Boolean.parseBoolean(cookieLoginedStr) : false;
		
		if(logined) {
			
			StringBuilder sb = new StringBuilder();
			sb.append("<body><table boarder='1'>");
			for (User u : DataBase.findAll()) {
				sb.append("<tr>");
				sb.append("<td>" + u.getUserId() + "</td>");
				sb.append("<td>" + u.getName() + "</td>");
				sb.append("<td>" + u.getEmail() + "</td>");
				sb.append("</tr>");
			}
			sb.append("</table></body>");
			response.forwardBody(sb.toString());
			
		} else {
			response.sendRedirect("/user/login.html");
		}
	}

}
