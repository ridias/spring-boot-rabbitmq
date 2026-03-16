package ad.rabbitmq.dynamic;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ad.rabbitmq.shared.exceptions.RabbitPublishException;

@Service
public class DynamicRabbitPublisher implements QueuePublisher {

	private final DynamicRabbitTemplateFactory templateFactory;
	private final ObjectMapper objectMapper;
	
	@Autowired
	public DynamicRabbitPublisher(
			DynamicRabbitTemplateFactory templateFactory,
			ObjectMapper objectMapper) {
		this.templateFactory = templateFactory;
		this.objectMapper = objectMapper;
	}
	
	public void publish(Object payload, RabbitMQDynProperties config) {
		try {
			RabbitTemplate template = templateFactory.getTemplate(config);
			String json = objectMapper.writeValueAsString(payload);
			
			template.convertAndSend(
					config.getExchange(),
					config.getRoutingKey(),
					json);
			
		}catch(Exception ex) {
			throw new RabbitPublishException("Error puglishing to dynamic RabbitMQ", ex);
		}
	}

	@Override
	public boolean supports(String queueType) {
		return "RabbitMQ".equalsIgnoreCase(queueType);
	}
}
