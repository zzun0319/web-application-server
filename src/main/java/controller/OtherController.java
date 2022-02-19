package controller;

import model.HttpRequest;
import model.HttpResponse;

public class OtherController extends AbstractController {

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		
		String accept = request.getHeader("Accept");
		String url = request.getPath();

		if(accept.contains("text/html")) {
			response.addHeader("Content-Type", "text/html;charset=utf-8");
		} else if (accept.contains("text/css")) {
			response.addHeader("Content-Type", "text/css;charset=utf-8");
		}
		response.forward(url);
		
	}

}
