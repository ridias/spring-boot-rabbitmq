package ad.rabbitmq.shared.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMessageEventReport extends RabbitMessage {

	private String reason;
	
	public RabbitMessageEventReport(String type) {
		super(type);
	}
	
	public RabbitMessageEventReport() {
		super("message_report");
	}

	@Override
	public String toString() {
		return super.toString() + "&reason=" + reason;
	}
	
	
}
