package controller;

import model.HttpRequest;
import model.HttpResponse;

public class UserUpdateController extends AbstractController {

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		
		if(request.getMethod().equals("GET")) doGet(request, response);
		
	}
	
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
	}

}
