package com.platform.apps.thirdpartymock.component.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
        /*TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory(){
        	 @Override
             protected void postProcessContext(Context context) {
                 SecurityConstraint securityConstraint=new SecurityConstraint();
                 securityConstraint.setUserConstraint("CONFIDENTIAL");//confidential
                 SecurityCollection collection=new SecurityCollection();
                 collection.addPattern("/*");
                 securityConstraint.addCollection(collection);
                 context.addConstraint(securityConstraint);
             }
        };*/
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;

    }
	
	@Bean
	  public Connector httpConnector(){
	      Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
//	      connector.setScheme("http");
	      connector.setPort(9600);
//	      connector.setSecure(false);
//	      connector.setRedirectPort(9800);
	      return connector;
	  }
}
