package ad.rabbitmq.services.single_processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface SingleMessageProcessor<T> {

	void process(T data) throws JsonMappingException, JsonProcessingException;
	String getSupportedType();
}
