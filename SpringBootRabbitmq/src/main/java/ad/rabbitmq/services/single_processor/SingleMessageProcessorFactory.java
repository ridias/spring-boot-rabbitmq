package ad.rabbitmq.services.single_processor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ad.rabbitmq.shared.models.RabbitMessage;

@Component
public class SingleMessageProcessorFactory {

	private final Map<String, SingleMessageProcessor<RabbitMessage>> processors = new HashMap<>();
	
	@Autowired
	public SingleMessageProcessorFactory(Map<String, SingleMessageProcessor<RabbitMessage>> processorBeans) {
		processorBeans.values().forEach(p -> processors.put(p.getSupportedType(), p));
	}
	
	public SingleMessageProcessor<RabbitMessage> getProcessor(String type) {
		if(!processors.containsKey(type)) {
			return processors.get("undefined");
		}
		
		return processors.get(type);
	}
}
