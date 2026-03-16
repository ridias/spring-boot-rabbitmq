package ad.rabbitmq.dynamic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMQDynProperties {

	private String hostname;
	private Integer port;
	private String virtualHost;
	private String exchange;
	private String routingKey;
	private String username;
	private String password;
}
