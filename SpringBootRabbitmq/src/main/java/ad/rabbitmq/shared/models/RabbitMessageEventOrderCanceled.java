package ad.rabbitmq.shared.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMessageEventOrderCanceled extends RabbitMessage {

	private String reason;
	
	public RabbitMessageEventOrderCanceled(String type) {
		super(type);
	}
	
	public RabbitMessageEventOrderCanceled() {
		super("order_canceled");
	}
}
