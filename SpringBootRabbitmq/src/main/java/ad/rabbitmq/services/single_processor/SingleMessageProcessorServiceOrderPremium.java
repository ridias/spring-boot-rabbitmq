package ad.rabbitmq.services.single_processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ad.rabbitmq.shared.models.RabbitMessage;
import ad.rabbitmq.shared.models.RabbitMessageEventOrderPremium;

@Service
public class SingleMessageProcessorServiceOrderPremium implements SingleMessageProcessor<RabbitMessage> {

	private static final Logger log = LoggerFactory.getLogger(SingleMessageProcessorServiceOrderPremium.class);
	
	@Override
	public void process(RabbitMessage data) throws JsonMappingException, JsonProcessingException {
		if((data instanceof RabbitMessageEventOrderPremium) == false) {
			log.info("It is not Event order premium!");
			return;
		}
		
		log.info("Processing order premium");
	}

	@Override
	public String getSupportedType() {
		return "order_premium";
	}

}
