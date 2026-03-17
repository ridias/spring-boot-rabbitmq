package ad.rabbitmq.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientQueueProperties {

	@Value("${rabbitmq.exchange.clients.name}")
	private String clientExchangeName;
	
	public String getExchangeName() {
		return clientExchangeName;
	}
	
	public String buildQueueName(Long clientId) {
		return "queue_client_" + clientId;
	}
	
	public String buildRoutingKey(Long clientId) {
		return "client_" + clientId + "_routing_key";
	}
}
