package ad.rabbitmq.dynamic;

public interface QueuePublisher {

	boolean supports(String queueType);
	void publish(Object payload, RabbitMQDynProperties config);
}
