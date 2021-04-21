package com.devesh.mediaPlayer.httpServer;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

	private HttpServer server;
	Logger logger;

	public Server(int port) {
		logger = LoggerFactory.getLogger(Server.class);
		try
		{
			server = HttpServer.create(new InetSocketAddress("localhost", port),
					0);
			server.createContext("/open", new HttpHandler());
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
			logger.info("http server started on port: " + Integer.toString(
					port));
		} catch (IOException ex)
		{
			logger.error("Exception while creating http server on port: "
					+ Integer.toString(port), ex);
		}
	}

}
