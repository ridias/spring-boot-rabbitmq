package ad.rabbitmq.dynamic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class DynamicRabbitTemplateFactory {

	private final Map<String, RabbitTemplate> cacheConnections = new ConcurrentHashMap<>();
	private static final Logger log = LoggerFactory.getLogger(DynamicRabbitTemplateFactory.class);
	
	public RabbitTemplate getTemplate(RabbitMQDynProperties config) {
		if(config == null ||
				config.getHostname() == null ||
				config.getPort() == null ||
				config.getUsername() == null ||
				config.getPassword() == null) {
			
			throw new IllegalArgumentException("Invalid RabbitMQ dynamic config");
		}
		
		String key = buildKey(config);
		
		return cacheConnections.computeIfAbsent(key, k -> {
			
			log.info("Creating connection {} with port {} and virtual host {}", config.getHostname(), config.getPort(), config.getVirtualHost());

            CachingConnectionFactory factory = new CachingConnectionFactory();
            factory.setHost(config.getHostname());
            factory.setPort(config.getPort());
            factory.setUsername(config.getUsername());
            factory.setPassword(config.getPassword());
            factory.setVirtualHost(config.getVirtualHost());
            factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
            factory.setPublisherReturns(true);

            return new RabbitTemplate(factory);
        });
	}
	
	 private String buildKey(RabbitMQDynProperties c) {
	        return c.getHostname() + "|" + 
	        		c.getPort() + "|" + 
	        		c.getUsername() + "|" + 
	        		c.getVirtualHost() + "|";
	    }
}
