package ad.rabbitmq.shared.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMessageEventOrderProcessed extends RabbitMessage {

	private Double price;
	private Integer numElements; 
	
	public RabbitMessageEventOrderProcessed(String type) {
		super(type);
	}
	
	public RabbitMessageEventOrderProcessed() {
		super("order_processed");
	}

}
