package com.platform.apps.thirdpartymock.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
	
	public static Logger logger = LoggerFactory.getLogger(MyCommandLineRunner.class);
	
	@Autowired
	SocketServer socketServer;

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		socketServer.startSocketServer();
		logger.info("socket server is running..." );
	}

}
