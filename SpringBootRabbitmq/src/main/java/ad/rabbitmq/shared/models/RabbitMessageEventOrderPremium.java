package ad.rabbitmq.shared.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMessageEventOrderPremium extends RabbitMessage {

	private Double price;
	private Double discount;
	private Integer numElements;
	
	public RabbitMessageEventOrderPremium(String type) {
		super(type);
	}
	
	public RabbitMessageEventOrderPremium() {
		super("order_premium");
	}

}
