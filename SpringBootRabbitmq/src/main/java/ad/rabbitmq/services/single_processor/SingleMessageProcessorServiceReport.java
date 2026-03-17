package ad.rabbitmq.services.single_processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ad.rabbitmq.shared.models.RabbitMessage;
import ad.rabbitmq.shared.models.RabbitMessageEventReport;

@Service
public class SingleMessageProcessorServiceReport implements SingleMessageProcessor<RabbitMessage> {

	private static final Logger log = LoggerFactory.getLogger(SingleMessageProcessorServiceReport.class);
	
	@Override
	public void process(RabbitMessage data) throws JsonMappingException, JsonProcessingException {
		if((data instanceof RabbitMessageEventReport) == false) {
			log.info("It is not a report client!");
			return;
		}
		
		log.info(data.toString());
	}

	@Override
	public String getSupportedType() {
		return "message_report";
	}
}
