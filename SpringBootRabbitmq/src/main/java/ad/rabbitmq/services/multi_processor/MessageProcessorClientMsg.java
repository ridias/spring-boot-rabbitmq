package ad.rabbitmq.services.multi_processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ad.rabbitmq.shared.models.RabbitEnvelope;
import ad.rabbitmq.shared.models.RabbitMessageEventClientMsg;
import ad.rabbitmq.shared.models.ResponseDataProcessed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageProcessorClientMsg implements MultipleMessageProcessor<RabbitEnvelope<RabbitMessageEventClientMsg>> {

	@Override
	public List<ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventClientMsg>>> process(
			List<RabbitEnvelope<RabbitMessageEventClientMsg>> dataList, Long idClient) {
		
		var response = new ArrayList<ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventClientMsg>>>();
		if(dataList == null || dataList.isEmpty())
			return response;
		
		for(int i = 0; i < dataList.size(); i++) {
			log.info(dataList.get(i).getPayload().toString());
			var dataProcessed = new ResponseDataProcessed<RabbitEnvelope<RabbitMessageEventClientMsg>>();
			dataProcessed.setData(dataList.get(i));
			dataProcessed.setProcessed(true);
			response.add(dataProcessed);
		}
		
		return response;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return RabbitMessageEventClientMsg.class.equals(clazz);
	}
}
