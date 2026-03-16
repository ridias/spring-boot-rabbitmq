package ad.rabbitmq.services.multi_processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ad.rabbitmq.shared.models.RabbitEnvelope;
import ad.rabbitmq.shared.models.RabbitMessageEventOrderProcessed;
import ad.rabbitmq.shared.models.ResponseDataProcessed;

@Service
public class MessageProcessorOrderProcessed implements MultipleMessageProcessor<RabbitEnvelope<RabbitMessageEventOrderProcessed>> {

	@Override
	public List<ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventOrderProcessed>>> process(
			List<RabbitEnvelope<RabbitMessageEventOrderProcessed>> dataList, Long idClient) {
		
		var response = new ArrayList<ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventOrderProcessed>>>();
		if(dataList == null || dataList.isEmpty())
			return response;
		
		for(int i = 0; i < dataList.size(); i++) {
			var dataProcessed = new ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventOrderProcessed>>();
			dataProcessed.setData(dataList.get(i));
			response.add(dataProcessed);
		}
		
		return response;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return RabbitMessageEventOrderProcessed.class.equals(clazz);
	}
}
