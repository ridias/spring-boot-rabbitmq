package ad.rabbitmq.shared.exceptions;

public class RabbitPublishException extends RuntimeException {

	private static final long serialVersionUID = 7700329641686881441L;
	
	public RabbitPublishException(String message) {
		super(message);
	}
	
	public RabbitPublishException(String message, Throwable cause) {
		super(message, cause);
	}
}
