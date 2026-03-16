package ad.rabbitmq.shared.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDataProcessed<T> {

	private T data;
	private LocalDateTime processedAt;
	
	public ResponseDataProcessed() {
		this.processedAt = LocalDateTime.now();
	}
}
