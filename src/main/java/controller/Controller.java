package controller;

import model.HttpRequest;
import model.HttpResponse;

public interface Controller {
	
	public void service(HttpRequest request, HttpResponse response);

}
