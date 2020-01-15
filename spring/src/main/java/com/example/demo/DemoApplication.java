package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.demo.websocket.WebSocketServerHandler;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private WebSocketServerHandler webSocketServerHandler;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Configuration
	@EnableWebSocket
	public class WebSocketConfig implements WebSocketConfigurer {
		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
			registry.addHandler(webSocketServerHandler, WebSocketServerHandler.PATH);
		}
	}

	@Configuration
	public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
		@Override
		protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
			messages.simpDestMatchers("/ws/*").authenticated();
		}
	}

}
