package ad.rabbitmq.clients;

import com.rabbitmq.client.Channel;

@FunctionalInterface
public interface ClientMessageHandler {

	void handle(Long clientId, String message, Channel channel);
	
}
