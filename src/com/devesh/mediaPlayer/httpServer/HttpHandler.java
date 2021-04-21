package com.devesh.mediaPlayer.httpServer;

import com.devesh.mediaPlayer.Application;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHandler implements com.sun.net.httpserver.HttpHandler {
	
	Logger logger;
	
	public HttpHandler(){
		logger = LoggerFactory.getLogger(HttpHandler.class);
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException
	{
		if ("PUT".equals(exchange.getRequestMethod()))
		{
			String path = exchange.getRequestHeaders().getFirst("Path");
			Application.open(path);
			
			exchange.sendResponseHeaders(200, 0);
			exchange.getResponseBody().close();
		}
	}

}
