package controller;

import model.User;

import java.util.UUID;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSession;
import http.HttpSessions;

public class LoginController extends AbstractController {
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (user != null) {
            if (user.login(request.getParameter("password"))) {
            	
//                response.addHeader("Set-Cookie", "logined=true");
            	HttpSession session = new HttpSession(UUID.randomUUID().toString());
            	session.setAttribute("user", user);
            	HttpSessions.addSession(session.getId(), session);
                response.addHeader("Set-Cookie", "JSESSIONID=" + session.getId());
                
                response.sendRedirect("/index.html");
            } else {
                response.sendRedirect("/user/login_failed.html");
            }
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
