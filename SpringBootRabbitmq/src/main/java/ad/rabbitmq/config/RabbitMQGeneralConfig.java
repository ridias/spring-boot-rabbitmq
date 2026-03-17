package ad.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQGeneralConfig {

	@Value("${rabbitmq.queue.reports.name}")
	private String queueReports;
	@Value("${rabbitmq.queue.data.name}")
	private String queueData;
	@Value("${rabbitmq.exchange.gen.name}")
	private String exchangeGen;
	@Value("${rabbitmq.routing.key.reports.name}")
	private String routingKeyReports;
	@Value("${rabbitmq.routing.key.data.name}")
	private String routingKeyData;
	
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
	
	@Bean
	public Queue queueReports() {
		return new Queue(queueReports);
	}
	
	@Bean
	public Queue queueData() {
		return new Queue(queueData);
	}
	
	@Bean
	public TopicExchange exchangeGen() {
		return new TopicExchange(exchangeGen);
	}
	
	@Bean
	public Binding bindingImgs() {
		return BindingBuilder.bind(queueReports())
				.to(exchangeGen())
				.with(routingKeyReports);
	}
	
	@Bean
	public Binding bindingData() {
		return BindingBuilder.bind(queueData())
				.to(exchangeGen())
				.with(routingKeyData);
	}
}
