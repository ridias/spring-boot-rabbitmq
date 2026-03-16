package ad.rabbitmq.shared.models;

import com.rabbitmq.client.Channel;

import lombok.Getter;

@Getter
public class RabbitEnvelope<T> {

	private final T payload;
	private final long deliveryTag;
	private final Channel channel;
	
	public RabbitEnvelope(
			T payload, 
			long deliveryTag,
			Channel channel) {
		this.payload = payload;
		this.deliveryTag = deliveryTag;
		this.channel = channel;
	}
}
