package ad.rabbitmq.services.multi_processor;

import java.util.List;

import ad.rabbitmq.shared.models.ResponseDataProcessed;

public interface MultipleMessageProcessor<T> {

	List<ResponseDataProcessed<T>> process(List<T> dataList, Long idClient);
	boolean supports(Class<?> clazz);
}
