package ad.rabbitmq.services.single_processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ad.rabbitmq.shared.models.RabbitMessage;

@Service
public class SingleMessageProcessorServiceUndefined implements SingleMessageProcessor<RabbitMessage> {

	private static final Logger log = LoggerFactory.getLogger(SingleMessageProcessorServiceUndefined.class);
	
	@Override
	public void process(RabbitMessage data) throws JsonMappingException, JsonProcessingException {
		log.warn("Processor is undefined for this message");
	}

	@Override
	public String getSupportedType() {
		return "undefined";
	}
}
