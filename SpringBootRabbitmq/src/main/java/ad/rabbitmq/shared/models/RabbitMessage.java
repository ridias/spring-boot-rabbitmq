package ad.rabbitmq.shared.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,      
		include = JsonTypeInfo.As.PROPERTY, 
		property = "type"               
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = RabbitMessageEventOrderCanceled.class, name = "order_canceled"),
	@JsonSubTypes.Type(value = RabbitMessageEventOrderProcessed.class, name = "order_processed"),
	@JsonSubTypes.Type(value = RabbitMessageEventOrderPremium.class, name = "order_premium")
})
public class RabbitMessage {

	private final String type;
	private Long idClient;
	private Long idOrder;
	
	public RabbitMessage(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "type=" + type +
				"&idOrder=" + idOrder +
				"&idClient=" + idClient;
	}
}
