package ad.rabbitmq.shared.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMessageEventClientMsg extends RabbitMessage {

	private String message;
	private String createdAt;
	private Boolean processed;
	
	public RabbitMessageEventClientMsg(String type) {
		super(type);
		createdAt = LocalDateTime.now().toString();
		processed = false;
	}
	
	public RabbitMessageEventClientMsg() {
		super("message_client");
		createdAt = LocalDateTime.now().toString();
		processed = false;
	}

	@Override
	public String toString() {
		return super.toString() + 
				"&message=" + message + 
				"&createdAt=" + createdAt + 
				"&processed=" + processed;
	}
	
	

}
