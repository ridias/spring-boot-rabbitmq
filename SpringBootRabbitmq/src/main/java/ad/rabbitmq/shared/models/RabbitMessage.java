package ad.rabbitmq.shared.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,      
		include = JsonTypeInfo.As.PROPERTY, 
		property = "type"               
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = RabbitMessageEventReport.class, name = "message_report"),
	@JsonSubTypes.Type(value = RabbitMessageEventClientMsg.class, name = "message_client"),
})
@Getter
public class RabbitMessage {

	private final String type;
	private Long idClient;
	
	public RabbitMessage(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "type=" + type +
				"&idClient=" + idClient;
	}
}
