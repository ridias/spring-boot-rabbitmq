package ad.rabbitmq.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ad.rabbitmq.clients.RabbitMQProducerClient;
import ad.rabbitmq.shared.models.RabbitMessage;

@Service
public class RabbitMQProducerReport {

	@Value("${rabbitmq.exchange.gen.name}")
	private String exchange;
	
	private RabbitTemplate rabbitTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(RabbitMQProducerClient.class);
	
	public RabbitMQProducerReport(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void sendMessage(RabbitMessage dataToSend, String routingKey) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonMessage = objectMapper.writeValueAsString(dataToSend);
			rabbitTemplate.convertAndSend(exchange, routingKey, jsonMessage);
		}catch(JsonProcessingException e) {
			log.error("It wasn't possible to send the message data to the queue client, more details: ", e);
		}
	}
}
