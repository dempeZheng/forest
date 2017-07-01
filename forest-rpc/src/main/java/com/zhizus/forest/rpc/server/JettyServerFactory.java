package com.zhizus.forest.rpc.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="server")
@ConditionalOnWebApplication 
public class JettyServerFactory{  
	private int maxThreads = 128  ;
	
	private int queenSize = 0  ;
 
	@Bean
	public EmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {
		JettyServletContainerFactory factory = new JettyServletContainerFactory() ;
		factory.setMaxThreads(maxThreads);
		factory.setQueenSize(queenSize);
		return factory ;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getQueenSize() {
		return queenSize;
	}

	public void setQueenSize(int queenSize) {
		this.queenSize = queenSize;
	}   
}
