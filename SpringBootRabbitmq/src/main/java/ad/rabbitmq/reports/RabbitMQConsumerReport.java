package ad.rabbitmq.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import ad.rabbitmq.data.RabbitMQConsumerData;
import ad.rabbitmq.services.single_processor.SingleMessageProcessorServiceReport;
import ad.rabbitmq.shared.models.RabbitMessage;

@Service
public class RabbitMQConsumerReport {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumerData.class);
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private SingleMessageProcessorServiceReport messageProcessor;
	
	@RabbitListener(queues = {"${rabbitmq.queue.reports.name}"}, ackMode = "MANUAL")
	public void consume(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
		this.processMessage(message, channel, tag);
	}
	
	@RabbitListener(queues = {"${rabbitmq.queue.reports.name}"}, ackMode = "MANUAL")
	public void consume2(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
		this.processMessage(message, channel, tag);
	}
	
	@RabbitListener(queues = {"${rabbitmq.queue.reports.name}"}, ackMode = "MANUAL")
	public void consume3(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
		this.processMessage(message, channel, tag);
	}
	
	@RabbitListener(queues = {"${rabbitmq.queue.reports.name}"}, ackMode = "MANUAL")
	public void consume4(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
		this.processMessage(message, channel, tag);
	}
	
	@RabbitListener(queues = {"${rabbitmq.queue.reports.name}"}, ackMode = "MANUAL")
	public void consume5(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
		this.processMessage(message, channel, tag);
	}
	
	private void processMessage(String message, Channel channel, long tag) {
		try {
			RabbitMessage data = objectMapper.readValue(message, RabbitMessage.class);
			this.messageProcessor.process(data);
			channel.basicAck(tag, false);
		}catch(Exception e) {
			log.error("Problems processing the message, retrying to requeue the message, more details: ", e);
			try {
                channel.basicNack(tag, false, true); 
            } catch (Exception ex) {
            	log.error("It wasn't possible to requeue the message, more details: ", ex);
            }
		}
	}
}
