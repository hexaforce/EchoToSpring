package com.example.demo.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.demo.service.DemoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketServerHandler extends AbstractWebSocketHandler {

	@Autowired
	private DemoService demoService;

	// wscat -c localhost:8080/ws/aaa
	public static final String PATH = "/ws/{channel}";

	private final Map<String, List<WebSocketSession>> channelsSession = new ConcurrentHashMap<String, List<WebSocketSession>>();

	String channel(WebSocketSession session) {
		String[] uri_list = session.getUri().toString().split("/");
		String channel = uri_list[uri_list.length - 1];
		log.info("channel: {} id: {}", channel, session.getId());
		return channel;
	}

	@Override
	protected void openSession(WebSocketSession session) {
		String channel = channel(session);

		if (!channelsSession.containsKey(channel)) {
			channelsSession.put(channel, new CopyOnWriteArrayList<WebSocketSession>());
		}
		channelsSession.get(channel).add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message, String text) throws Exception {
		String channel = channel(session);
		String result = demoService.demo(text);
		channelsSession.get(channel).parallelStream().forEach(s -> {
			try {
				s.sendMessage(new TextMessage(result, true));
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		});
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message, byte[] binary) throws Exception {
		try {
			session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Binary messages not supported"));
		} catch (IOException ex) {
			// ignore
		}
	}

	@Override
	protected void closeSession(WebSocketSession session) {
		String channel = channel(session);

		if (channelsSession.containsKey(channel)) {
			channelsSession.get(channel).remove(session);
			if (channelsSession.get(channel).isEmpty()) {
				channelsSession.remove(channel);
			}
		}
	}

}