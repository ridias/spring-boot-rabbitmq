package ad.rabbitmq.clients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import ad.rabbitmq.services.multi_processor.MessageProcessorClientMsg;
import ad.rabbitmq.shared.BatchBuffer;
import ad.rabbitmq.shared.models.RabbitEnvelope;
import ad.rabbitmq.shared.models.RabbitMessage;
import ad.rabbitmq.shared.models.RabbitMessageEventClientMsg;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DynamicConsumerService {

	private final ConnectionFactory connectionFactory;

	private final Map<Long, SimpleMessageListenerContainer> activeContainersByIdClient =
            new ConcurrentHashMap<>();
	private final Map<Long, BatchBuffer<RabbitEnvelope<RabbitMessageEventClientMsg>>> activeBatchBuffersByIdClient =
			new ConcurrentHashMap<>();

	private static final Logger log = LoggerFactory.getLogger(DynamicConsumerService.class);
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private final MessageProcessorClientMsg processorService;
	
	@Value("${app.batch.max-size}")
	protected Integer batchMaxSize;
	
	public void startConsumer(Long clientId, 
			String queueName) {
		
		if(activeContainersByIdClient.containsKey(clientId)) {
			log.info("Already initialized consumer for clientId {}", clientId);
			return;
		}
		
		SimpleMessageListenerContainer container = buildContainer(queueName, clientId);
        container.start();

        activeContainersByIdClient.put(clientId, container);
        log.info("Consumer started for clientId={}, queue='{}'", clientId, queueName);
	}
	
	public void stopConsumer(Long clientId) {
        Optional.ofNullable(activeContainersByIdClient.remove(clientId)).ifPresentOrElse(
                container -> {
                    container.stop();
                    container.destroy();
                    log.info("Consumer stopped and destroyed by clientId={}", clientId);
                },
                () -> log.warn("Consumer active not found for clientId={}", clientId)
        );
    }
	
	public Set<Long> getActiveClientsIds(){
		return Collections.unmodifiableSet(activeContainersByIdClient.keySet());
	}
	
	public List<BatchBuffer<RabbitEnvelope<RabbitMessageEventClientMsg>>> getAllBatchBuffers(){
		var buffers = new ArrayList<BatchBuffer<RabbitEnvelope<RabbitMessageEventClientMsg>>>();
		
		for(Entry<Long, BatchBuffer<RabbitEnvelope<RabbitMessageEventClientMsg>>> entry : activeBatchBuffersByIdClient.entrySet()) {
			buffers.add(entry.getValue());
		}
		return buffers;
	}
	
	public boolean hasActiveConsumer(Long clientId) {
		return activeContainersByIdClient.containsKey(clientId);
	}
	
	private SimpleMessageListenerContainer buildContainer(
	        String queueName, 
	        Long clientId) {

	    SimpleMessageListenerContainer container =
	            new SimpleMessageListenerContainer(connectionFactory);

	    container.addQueueNames(queueName);
	    container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
	    container.setConcurrentConsumers(1);
	    container.setMaxConcurrentConsumers(1);
	    container.setAutoStartup(false);
	    
	    var batchBufferConsumer = this.initBufferMultipleMessageHikvision(clientId);
	    
	    activeBatchBuffersByIdClient.computeIfAbsent(clientId, k -> batchBufferConsumer);
	    
	    container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
	        long deliveryTag = message.getMessageProperties().getDeliveryTag();
	        try {
	            String body = new String(message.getBody());
	            
	            RabbitMessage data = objectMapper.readValue(body, RabbitMessage.class);
	            
	            if("message_client".equals(data.getType())) {
	            	RabbitMessageEventClientMsg msg = (RabbitMessageEventClientMsg) data;
	            	RabbitEnvelope<RabbitMessageEventClientMsg> env =
			                new RabbitEnvelope<>(msg, deliveryTag, channel);
	            	activeBatchBuffersByIdClient.get(clientId).add(env);
	            }else {
	            	log.error("No processor found for message type: {}", data.getClass().getName());
					channel.basicNack(deliveryTag, false, false);
	            }
	            
	        } catch (Exception e) {
	        	log.error("Problems processing the message, retrying to requeue the message, more details: ", e);
				try {
	                channel.basicNack(deliveryTag, false, true); 
	            } catch (Exception ex) {
	            	log.error("It wasn't possible to requeue the message, more details: ", ex);
	            }
	        }
	    });

	    return container;
	}
	
	
	protected BatchBuffer<RabbitEnvelope<RabbitMessageEventClientMsg>> initBufferMultipleMessageHikvision(
			Long idClient) {
		
		return new BatchBuffer<RabbitEnvelope<RabbitMessageEventClientMsg>>(
				batchMaxSize,
				idClient,
				processorService,
				result -> {
					try {
                		for(int i = 0; i < result.size(); i++) {
                			var dataProcessed = result.get(i);
                			long deliveryTag = dataProcessed.getData().getDeliveryTag();
                			Channel channel = dataProcessed.getData().getChannel();
                			if(dataProcessed.isProcessed()) {
                				channel.basicAck(deliveryTag, false);
                			}else {
                				channel.basicNack(deliveryTag, false, true);
                			}
                		}
                	}catch(Exception ex) {
                		log.error("There was a problem when processing results as ack or nack in consumer-2");
                	}
				}
		);
	}
}
