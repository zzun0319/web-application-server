package controller;

import enums.HttpMethod;
import model.HttpRequest;
import model.HttpResponse;

public class AbstractController implements Controller {
	
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		if(request.getMethod() == HttpMethod.GET) doGet(request, response);
		else doPost(request, response);
	}
	
	public void doGet(HttpRequest request, HttpResponse response) {}
	public void doPost(HttpRequest request, HttpResponse response) {}

}
