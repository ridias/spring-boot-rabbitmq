package ad.rabbitmq.services.single_processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ad.rabbitmq.clients.ClientQueueProperties;
import ad.rabbitmq.clients.RabbitMQProducerClient;
import ad.rabbitmq.reports.RabbitMQProducerReport;
import ad.rabbitmq.shared.models.RabbitMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SingleMessageProcessorServiceData implements SingleMessageProcessor<RabbitMessage>{

	private final ClientQueueProperties clientQueueProperties;
	private final RabbitMQProducerClient producerClient;
	private final RabbitMQProducerReport producerReport;

	@Value("${rabbitmq.routing.key.reports.name}")
	private String routingKeyReports;
	
	@Autowired
	public SingleMessageProcessorServiceData(
			ClientQueueProperties clientQueueProperties,
			RabbitMQProducerClient producerClient,
			RabbitMQProducerReport producerReport) {
	
		this.clientQueueProperties = clientQueueProperties;
		this.producerClient = producerClient;
		this.producerReport = producerReport;
	}
	
	@Override
	public void process(RabbitMessage data) throws JsonMappingException, JsonProcessingException {
		Long idClient = data.getIdClient();
		
		if("message_client".equals(data.getType())) {
			String routingKey = this.clientQueueProperties.buildRoutingKey(idClient);
			this.producerClient.sendMessage(data, routingKey);
		}else if("message_report".equals(data.getType())) {
			this.producerReport.sendMessage(data, routingKeyReports);
		}
	}

	@Override
	public String getSupportedType() {
		return "message_data";
	}

}
