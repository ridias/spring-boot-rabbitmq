package ad.rabbitmq.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ad.rabbitmq.shared.models.RabbitMessage;

@Service
public class RabbitMQProducerData {

	@Value("${rabbitmq.exchange.gen.name}")
	private String exchange;
	@Value("${rabbitmq.routing.key.data.name}")
	private String routingKey;
	
	private RabbitTemplate rabbitTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(RabbitMQProducerData.class);
	
	public RabbitMQProducerData(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void sendMessage(RabbitMessage dataToSend) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonMessage = objectMapper.writeValueAsString(dataToSend);
			rabbitTemplate.convertAndSend(exchange, routingKey, jsonMessage);
		}catch(JsonProcessingException e) {
			log.error("It wasn't possible to send the message data to the queue data, more details: ", e);
		}
	}
}
