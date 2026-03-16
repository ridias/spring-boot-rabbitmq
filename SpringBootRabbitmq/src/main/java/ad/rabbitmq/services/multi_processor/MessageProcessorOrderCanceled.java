package ad.rabbitmq.services.multi_processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ad.rabbitmq.shared.models.RabbitEnvelope;
import ad.rabbitmq.shared.models.RabbitMessageEventOrderCanceled;
import ad.rabbitmq.shared.models.ResponseDataProcessed;

@Service
public class MessageProcessorOrderCanceled implements MultipleMessageProcessor<RabbitEnvelope<RabbitMessageEventOrderCanceled>> {

	@Override
	public List<ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventOrderCanceled>>> process(
			List<RabbitEnvelope<RabbitMessageEventOrderCanceled>> dataList, Long idClient) {
		
		var response = new ArrayList<ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventOrderCanceled>>>();
		if(dataList == null || dataList.isEmpty())
			return response;
		
		for(int i = 0; i < dataList.size(); i++) {
			var dataProcessed = new ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventOrderCanceled>>();
			dataProcessed.setData(dataList.get(i));
			response.add(dataProcessed);
		}
		
		return response;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return RabbitMessageEventOrderCanceled.class.equals(clazz);
	}

}
