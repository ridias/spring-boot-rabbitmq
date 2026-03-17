package ad.rabbitmq.shared.models;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ad.rabbitmq.services.multi_processor.MultipleMessageProcessor;

public class BatchBuffer<T> {

	private final Long idClient;
    private final int maxSize;
    private final MultipleMessageProcessor<T> processor;
    
    protected final List<T> buffer = new ArrayList<>();
    
    protected final Consumer<List<ResponseDataProcessed<T>>> callback;
    
    public BatchBuffer(
    		int maxSize,
    		Long idClient,
    		MultipleMessageProcessor<T> processor,
    		Consumer<List<ResponseDataProcessed<T>>> callback) {
    	
    	this.maxSize = maxSize;
        this.processor = processor;
        this.idClient = idClient;
        this.callback = callback;
    }
    
    public synchronized void add(T item) {
        buffer.add(item);
        if (buffer.size() >= maxSize) {
            flush();
        }
    }

    public synchronized void flushIfNotEmpty() {
        if (!buffer.isEmpty()) {
            flush();
        }
    }

    protected synchronized void flush() {
        List<T> batch = new ArrayList<>(buffer);
        buffer.clear();
        var dataProcessed = processor.process(batch, idClient);
    	callback.accept(dataProcessed);
    }

    /*public void shutdown() {
        scheduler.shutdown();
    }*/
}
