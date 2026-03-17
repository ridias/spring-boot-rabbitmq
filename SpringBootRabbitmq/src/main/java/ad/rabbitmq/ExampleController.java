package ad.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ad.rabbitmq.data.RabbitMQProducerData;
import ad.rabbitmq.shared.models.RabbitMessageEventClientMsg;
import ad.rabbitmq.shared.models.RabbitMessageEventReport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/data")
public class ExampleController {
	
	@Autowired
	private RabbitMQProducerData producer;

	@PostMapping("/msg")
	public ResponseEntity<String> processMsgClient(
			@RequestBody RabbitMessageEventClientMsg message){
		
		this.producer.sendMessage(message);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
	
	@PostMapping("/report")
	public ResponseEntity<String> processReport(
			@RequestBody RabbitMessageEventReport message){
		
		this.producer.sendMessage(message);
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
}
