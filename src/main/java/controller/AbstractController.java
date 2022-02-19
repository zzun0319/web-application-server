package controller;

import model.HttpRequest;
import model.HttpResponse;

public abstract class AbstractController implements Controller {
	
	@Override
	public abstract void service(HttpRequest request, HttpResponse response);
	
	public void doGet() {}
	public void doPost() {}

}
