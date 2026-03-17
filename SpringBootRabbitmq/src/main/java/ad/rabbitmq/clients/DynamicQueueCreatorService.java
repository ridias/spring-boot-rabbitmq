package ad.rabbitmq.clients;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicQueueCreatorService {

	private final AmqpAdmin amqpAdmin;
	
	@Autowired
	private DynamicQueueCreatorService(RabbitAdmin rabbitAdmin) {
		this.amqpAdmin = rabbitAdmin;
	}
	
    public void createQueueAndBinding(String queueName, String exchangeName, String routingKey) {
    	Queue queue = new Queue(queueName, true, false, false);
        amqpAdmin.declareQueue(queue);

        DirectExchange exchange = new DirectExchange(exchangeName);
        amqpAdmin.declareExchange(exchange);

        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        amqpAdmin.declareBinding(binding);
    }
}
