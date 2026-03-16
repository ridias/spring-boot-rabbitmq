package ad.rabbitmq.dynamic;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class QueuePublisherFactory {

	private final List<QueuePublisher> publishers;
	
	public QueuePublisherFactory(List<QueuePublisher> publishers) {
		this.publishers = publishers;
	}
	
	public QueuePublisher getPublisher(String queueType) {
		return publishers.stream()
				.filter(p -> p.supports(queueType))
				.findFirst()
				.orElseThrow(() -> 
					new IllegalArgumentException("Unsupported queue type: " + queueType)
				);
	}
}
